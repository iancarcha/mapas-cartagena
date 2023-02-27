package com.example.mapas_cartagena.ui;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;

public class SharedViewModel extends AndroidViewModel {

    private final Application app;
    private static final MutableLiveData<String> currentAddress = new MutableLiveData<>();
    private final MutableLiveData<String> checkPermission = new MutableLiveData<>();
    private final MutableLiveData<String> buttonText = new MutableLiveData<>();
    private final MutableLiveData<Boolean> progressBar = new MutableLiveData<>();
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean mTrackingLocation;
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    private final MutableLiveData<LatLng> currentLatLng = new MutableLiveData<>();


    public SharedViewModel(@NonNull Application application) {
        super(application);
        this.app = application;
    }

    public MutableLiveData<LatLng> getCurrentLatLng() {
        return currentLatLng;
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public void setUser(FirebaseUser passedUser) {
        user.postValue(passedUser);
    }

    public void setFusedLocationClient(FusedLocationProviderClient mFusedLocationClient) {
        this.mFusedLocationClient = mFusedLocationClient;
    }

    public static LiveData<String> getCurrentAddress() {
        return currentAddress;
    }

    public MutableLiveData<String> getButtonText() {
        return buttonText;
    }

    public MutableLiveData<Boolean> getProgressBar() {
        return progressBar;
    }

    public LiveData<String> getCheckPermission() {
        return checkPermission;
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null) {
                fetchAddress(locationResult.getLastLocation());
            }
        }
    };

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    public void switchTrackingLocation() {
        if (!mTrackingLocation) {
            startTrackingLocation(true);
        } else {
            stopTrackingLocation();
        }

    }

    @SuppressLint("MissingPermission")
    public void startTrackingLocation(boolean needsChecking) {
        if (needsChecking) {
            checkPermission.postValue("check");
        } else {
            mFusedLocationClient.requestLocationUpdates(
                    getLocationRequest(),
                    mLocationCallback, null
            );

            currentAddress.postValue("Carregant...");

            progressBar.postValue(true);
            mTrackingLocation = true;
            buttonText.setValue("Aturar el seguiment de la ubicació");
        }
    }


    private void stopTrackingLocation() {
        if (mTrackingLocation) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            mTrackingLocation = false;
            progressBar.postValue(false);
            buttonText.setValue("Comença a seguir la ubicació");
        }
    }

    private void fetchAddress(Location location) {
        try {
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
            currentLatLng.postValue(latlng);

            } catch(Exception ex){
            ex.printStackTrace();
        }

    }

}
