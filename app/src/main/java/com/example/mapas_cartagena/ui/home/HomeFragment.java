package com.example.mapas_cartagena.ui.home;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION.SDK_INT;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mapas_cartagena.R;
import com.example.mapas_cartagena.databinding.FragmentHomeBinding;
import com.example.mapas_cartagena.ui.Incidencia;
import com.example.mapas_cartagena.ui.SharedViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private FirebaseUser authUser;
    public String hora;

    String mCurrentPhotoPath;
    private Uri photoURI;
    private ImageView foto;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static int RESULT_LOAD_IMG = 2;
    private boolean DARDEALTA = false;

    //
    StorageReference storageRef;
    String randomName;
    StorageReference ref;
    String imgDecodableString;






    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        SharedViewModel.getCurrentAddress().observe(getViewLifecycleOwner(), address -> {
            hora = String.format(
                    "Direcció: %1$s \nHora: %2$tr",
                    address, System.currentTimeMillis());
            binding.txtDireccio.setText(String.format("Direcció: %1$s \nHora: %2$tr",address, System.currentTimeMillis()));
        });


        binding.buttonFoto.setOnClickListener(button ->{
            loadImagefromGallery();
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

        ImageView Foto = binding.foto.findViewById(R.id.foto);
        Button buttonFoto = binding.buttonFoto.findViewById(R.id.button_foto);

        sharedViewModel.switchTrackingLocation();

        sharedViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            authUser = user;
        });
        sharedViewModel.getCurrentLatLng().observe(getViewLifecycleOwner(), latlng -> {
            binding.txtLatitud.setText(String.valueOf(latlng.latitude));
            binding.txtLongitud.setText(String.valueOf(latlng.longitude));

        });

        binding.buttonNotificar.setOnClickListener(button -> {
            if(DARDEALTA == false){
                Toast.makeText(getContext(), "Añade una nueva imagen", Toast.LENGTH_SHORT).show();
            }else{
                Incidencia incidencia = new Incidencia();
                String x = binding.txtLatitud.getText().toString() + " " + binding.txtLongitud.getText().toString();
                incidencia.setDireccio(String.format("Direcció: %1$s\nHora: %2$tr", x, System.currentTimeMillis())+ "\n");
                incidencia.setLatitud(binding.txtLatitud.getText().toString());
                incidencia.setLongitud(binding.txtLongitud.getText().toString());
                incidencia.setProblema(binding.txtDescripcio.getText().toString());
                incidencia.setUrl(randomName);

                DatabaseReference base = FirebaseDatabase.getInstance(
                ).getReference();

                DatabaseReference users = base.child("users");
                DatabaseReference uid = users.child(authUser.getUid());
                DatabaseReference incidencies = uid.child("incidencies");

                DatabaseReference reference = incidencies.push();
                reference.setValue(incidencia);
                uploadImage();
                DARDEALTA = false;
                foto.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_black_24dp));
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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

    //GALERIA
    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        DARDEALTA = true;
    }

    private void uploadImage(){

        ref.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                double progress
                        = (100.0
                        * snapshot.getBytesTransferred()
                        / snapshot.getTotalByteCount());
                progressDialog.setMessage(
                        "Uploaded "
                                + (int)progress + "%");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            randomName = UUID.randomUUID().toString();
            ref = storageRef.child("Fotos/"+randomName);

            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                imgDecodableString = cursor.getString(columnIndex);

                mCurrentPhotoPath = cursor.getString(columnIndex);

                File uriFile = new File(mCurrentPhotoPath);
                photoURI = Uri.fromFile(uriFile);
                storageRef.putFile(photoURI);

                Log.e("PATH" , cursor.getString(columnIndex));

                cursor.close();

                foto = getView().findViewById(R.id.foto);
                // Set the Image in ImageView after decoding the String
                foto.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
            Log.e("ERROR URI", e+"");
        }
    }

    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getActivity().getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
}