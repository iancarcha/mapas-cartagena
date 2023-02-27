package com.example.mapas_cartagena.ui.notifications;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mapas_cartagena.R;
import com.example.mapas_cartagena.databinding.FragmentNotificationsBinding;
import com.example.mapas_cartagena.ui.API.ValenBisiAPI;
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
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class NotificationsFragment extends Fragment {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private FragmentNotificationsBinding binding;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final DatabaseReference base = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference users = base.child("users");
    private final DatabaseReference uid = users.child(auth.getUid());
    private final DatabaseReference incidencies = uid.child("incidencies");



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

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

        ValenBisiAPI valenBisiAPI = retrofit.create(ValenBisiAPI.class);

        binding.map.setTileSource(TileSourceFactory.MAPNIK);
        binding.map.setMultiTouchControls(true);
        IMapController mapController = binding.map.getController();
        mapController.setZoom(14.5);
        GeoPoint startPoint = new GeoPoint(37.4219983, -122.084);
        mapController.setCenter(startPoint);

        MyLocationNewOverlay myLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(requireContext()), binding.map);
        myLocationNewOverlay.enableMyLocation();
        myLocationNewOverlay.enableFollowLocation();

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
                m.setIcon(getResources().getDrawable(R.drawable.ic_baseline_house_24));
                m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_TOP);
                m.setTitle(incidencia.getDireccio());
                m.setSnippet(incidencia.getProblema());
                m.setOnMarkerClickListener(new Marker.OnMarkerClickListener(){
                    @Override
                    public boolean onMarkerClick(Marker marker, MapView mapView) {
                        String imagen = "https://firebasestorage.googleapis.com/v0/b/incivismogym.appspot.com/o/Fotos%2F";
                        Glide.with(getContext()).load(imagen + incidencia.getUrl() + "?alt=media&token=" + incidencia.getUrl()).into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                // Aquí se carga la imagen en un ImageView
                                ImageView imageView = new ImageView(getContext());
                                imageView.setImageDrawable(resource);


                                // Aquí se muestra la imagen en un diálogo
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle(incidencia.getDireccio());
                                builder.setView(imageView);
                                builder.create().show();
                            }
                        });
                        return true;
                    }
                });
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
    }



    @Override
    public void onResume() {
        super.onResume();
        binding.map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        binding.map.onPause();
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