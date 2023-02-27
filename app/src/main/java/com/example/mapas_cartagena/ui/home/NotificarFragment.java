package com.example.mapas_cartagena.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.bumptech.glide.Glide;
import com.example.mapas_cartagena.R;
import com.example.mapas_cartagena.databinding.FragmentNotificationsBinding;
import com.example.mapas_cartagena.databinding.FragmentNotificationsBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class NotificarFragment extends Fragment {

private FragmentNotificationsBinding binding;
private ActivityResultLauncher<String[]> locationPermissionRequest;
        String mCurrentPhotoPath;
private Uri photoURI;
private ImageView foto;
static final int REQUEST_TAKE_PHOTO = 1;

public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        HomeViewModel homeViewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(HomeViewModel.class);


        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        return root;
        }

public void onClick(View view){
        ImageView Foto = view.findViewById(R.id.foto);
        Button buttonFoto = view.findViewById(R.id.button_foto);
        buttonFoto.setOnClickListener(button -> {
        dispatchTakePictureIntent();
        });
        getLocation();
        }


private void getLocation(){
        if (ContextCompat.checkSelfPermission(getContext(),
        Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {

        Toast.makeText(requireContext(), "Request permisssions", Toast.LENGTH_SHORT).show();

        locationPermissionRequest.launch(new String[]{
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
        });
        } else {
        Toast.makeText(requireContext(), "getLocation: permissions granted", Toast.LENGTH_SHORT).show();
        }
        }
private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
        imageFileName,
        ".jpg",
        storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
        }
private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(
        getContext().getPackageManager()) != null) {

        File photoFile = null;
        try {
        photoFile = createImageFile();
        } catch (IOException ex) {


        }

        if (photoFile != null) {
        photoURI = FileProvider.getUriForFile(getContext(),
        "com.example.android.fileprovider",
        photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
        }
        }
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {

        if (resultCode == Activity.RESULT_OK) {

        Glide.with(this).load(photoURI).into(foto);

        } else {

        Toast.makeText(getContext(),
        "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        }
        }
        }


        }