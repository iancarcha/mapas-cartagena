package com.example.mapas_cartagena.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mapas_cartagena.R;
import com.example.mapas_cartagena.databinding.FragmentNotificationsBinding;
import com.example.mapas_cartagena.ui.API.ValenBisiApi;
import com.example.mapas_cartagena.ui.Incidencia;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    String mCurrentPhotoPath;
    private Uri photoURI;
    private ImageView foto;
    static final int REQUEST_TAKE_PHOTO = 1;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final DatabaseReference base = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference users = base.child("users");
    private final DatabaseReference uid = users.child(auth.getUid());
    private final DatabaseReference incidencies = uid.child("incidencies");


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        Context ctx = requireActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String BASE_URL = "http://api.citybik.es/v2/networks/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        ValenBisiApi valenBisiApi = retrofit.create(ValenBisiApi.class);

        binding.map.setTileSource(TileSourceFactory.MAPNIK);
        binding.map.setMultiTouchControls(true);
        IMapController mapController = binding.map.getController();
        mapController.setZoom(14.5);
        GeoPoint startPoint = new GeoPoint(39.4715612, -0.3930977);
        mapController.setCenter(startPoint);

        MyLocationNewOverlay myLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(requireContext()), binding.map);
        myLocationNewOverlay.enableMyLocation();
        binding.map.getOverlays().add(myLocationNewOverlay);

        CompassOverlay compassOverlay = new CompassOverlay(requireContext(), new InternalCompassOrientationProvider(requireContext()), binding.map);
        compassOverlay.enableCompass();
        binding.map.getOverlays().add(compassOverlay);

        requestPermissionsIfNecessary(new String[]{
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                }
        );

        incidencies.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Incidencia incidencia = snapshot.getValue(Incidencia.class);

                Marker m = new Marker(binding.map);
                m.setPosition(new GeoPoint(Double.parseDouble(incidencia.getLatitud()), Double.parseDouble(incidencia.getLongitud())));
                m.setTextLabelFontSize(40);
                m.setIcon(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));
                m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_TOP);
                m.setTitle(incidencia.getDireccio());
                m.setSnippet(incidencia.getProblema());
                binding.map.getOverlays().add(m);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }//oncreateviwed

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
        binding.map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Configuration.getInstance().save(getContext(), prefs);
        binding.map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(requireActivity(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}