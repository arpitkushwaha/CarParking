package com.e.carparking.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.carparking.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.mapviewlite.MapScene;
import com.here.sdk.mapviewlite.MapStyle;
import com.here.sdk.mapviewlite.MapViewLite;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MapActivity extends AppCompatActivity {

    FusedLocationProviderClient mFusedLocationClient;
    private MapViewLite mapView;
    MapStyle style;
    private String latitude,longitude;
    private static final String TAG = "MapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Get a MapViewLite instance from the layout.
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        style = MapStyle.NORMAL_DAY;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(style==MapStyle.NORMAL_DAY)
                {
                    style=MapStyle.SATELLITE;
                    fab.setImageResource(R.drawable.normal_view);
                    loadMapScene(style);
                }
                else
                {
                    style=MapStyle.NORMAL_DAY;
                    fab.setImageResource(R.drawable.satellite_view);
                    loadMapScene(style);
                }
            }
        });


        final FloatingActionButton fab_current_location = findViewById(R.id.fab_current_location);
        fab_current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               loadMapScene(style);
            }
        });


        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                getLastLocation();
                loadMapScene(style);
            }
            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Log.e(TAG, "onError: DexterPermissionError occured: "+error.toString() );
            }
        }).onSameThread()
                .check();

    }

    private void loadMapScene(MapStyle map_style)
    {

        mapView.getMapScene().loadScene(map_style, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable MapScene.ErrorCode errorCode) {
                if(errorCode == null) {
                    mapView.getCamera().setTarget(new GeoCoordinates(Double.parseDouble(latitude), Double.parseDouble(longitude)));
                    mapView.getCamera().setZoomLevel(18);
                } else {
                    Log.d(TAG, "onLoadScene failed: "+errorCode.toString());
                }
            }
        });
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
            {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                   latitude = location.getLatitude()+"";
                                   longitude = location.getLongitude()+"";
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude()+"";
            longitude = mLastLocation.getLongitude()+"";
        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}

