package com.example.mapas_cartagena.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mapas_cartagena.databinding.FragmentHomeBinding;
import com.example.mapas_cartagena.ui.Incidencia;
import com.example.mapas_cartagena.ui.SharedViewModel;
import com.example.mapas_cartagena.databinding.FragmentHomeBinding;
import com.example.mapas_cartagena.ui.Incidencia;
import com.example.mapas_cartagena.ui.SharedViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private boolean mTrackingLocation = false;
    private FirebaseUser authUser;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        SharedViewModel.getCurrentAddress().observe(getViewLifecycleOwner(), address -> {
            binding.txtDireccio.setText(String.format(
                    "Direcció: %1$s \n Hora: %2$tr",
                    address, System.currentTimeMillis()));
        });

        sharedViewModel.getCurrentLatLng().observe(getViewLifecycleOwner(), latlng -> {
            binding.txtLatitud.setText(String.valueOf(latlng.latitude));
            binding.txtLongitud.setText(String.valueOf(latlng.longitude));
        });

        sharedViewModel.getProgressBar().observe(getViewLifecycleOwner(), visible -> {
            if (visible)
                binding.loading.setVisibility(ProgressBar.VISIBLE);
            else
                binding.loading.setVisibility(ProgressBar.INVISIBLE);
        });

        sharedViewModel.switchTrackingLocation();

        sharedViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            authUser = user;
        });

        //insertar dades
        binding.buttonNotificar.setOnClickListener(button -> {
            Incidencia incidencia = new Incidencia();
            //solucion al carregant
            String x = binding.txtLatitud.getText().toString() + binding.txtLongitud.getText().toString();

            incidencia.setDireccio(String.format("Direcció: %1$s \n Hora: %2$tr",x,System.currentTimeMillis()));
            incidencia.setLatitud(binding.txtLatitud.getText().toString());
            incidencia.setLongitud(binding.txtLongitud.getText().toString());
            incidencia.setProblema(binding.txtDescripcio.getText().toString());


            DatabaseReference base = FirebaseDatabase.getInstance(
            ).getReference();

            DatabaseReference users = base.child("users");
            DatabaseReference uid = users.child(authUser.getUid());
            DatabaseReference incidencies = uid.child("incidencies");

            DatabaseReference reference = incidencies.push();
            reference.setValue(incidencia);
        });


        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}