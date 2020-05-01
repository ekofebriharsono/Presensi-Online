package com.maseko.root.absen1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.maseko.root.absen1.Api.UserClient;
import com.maseko.root.absen1.Config.ApiConfig;
import com.maseko.root.absen1.Respone.Presensi;
import com.maseko.root.absen1.SharePreference.SaveSharedPreference;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.effortlesspermissions.AfterPermissionDenied;
import me.zhanghai.android.effortlesspermissions.EffortlessPermissions;
import pub.devrel.easypermissions.AfterPermissionGranted;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Marker marker;
    private Location locationLast;

    private static final int REQUEST_PERMISSION_CODE = 168;

    private static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @BindView(R.id.txtinfo)
    TextView txtInfo;

    @BindView(R.id.txtVersi)
    TextView txtVersi;

    @BindView(R.id.btnRetri)
    Button btnRetri;

    public Intent OpenBrowser;
    public Intent UrlBrowser;

    String version = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        try {
            PackageInfo pInfo = MapsActivity.this.getPackageManager().getPackageInfo(MapsActivity.this.getPackageName(), 0);
            version = pInfo.versionName;
            Log.d("tes", "onLocationChangedLONG: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        txtVersi.setText("V" + version);


        requestPermission();

/*        final PackageManager pm = getPackageManager();
//get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            Log.d("tes", "Installed package :" + packageInfo.packageName);
            Log.d("tes", "Source dir : " + packageInfo.);
            Log.d("tes", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));

        }*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EffortlessPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (googleApiClient == null) {
                    buildGoolgeApiClient();
                }
                mMap.setMyLocationEnabled(true);
            }
        } else {
            Toast.makeText(MapsActivity.this, "Permission Denait", Toast.LENGTH_SHORT).show();
        }
        return;
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_CODE)
    private void requestPermission() {
        if (!EffortlessPermissions.hasPermissions(this, PERMISSIONS)) {
            EffortlessPermissions.requestPermissions(this,
                    "Beri izin untuk aplikasi ini!",
                    REQUEST_PERMISSION_CODE, PERMISSIONS);
        }


    }

    @AfterPermissionDenied(REQUEST_PERMISSION_CODE)
    private void onAccessLocationPermissionDenied() {
        Toast.makeText(MapsActivity.this, "tidak di izinkan!", Toast.LENGTH_SHORT).show();
    }


    protected synchronized void buildGoolgeApiClient() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoolgeApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();

        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {

        locationLast = location;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        List<Address> addressList = null;
        String kodepos = "";
        Geocoder geocoder = new Geocoder(MapsActivity.this);

        try {
            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            kodepos = addressList.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        SaveSharedPreference.setLokasiUser(MapsActivity.this, kodepos);
        SaveSharedPreference.setLatUser(MapsActivity.this, Double.toString(location.getLatitude()));
        SaveSharedPreference.setLongUser(MapsActivity.this, Double.toString(location.getLongitude()));

        txtInfo.setText("Memerika pembaharuan...");


       checkForUpdate();



        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    public void checkForUpdate(){
        btnRetri.setVisibility(View.GONE);
        UserClient getResponse = ApiConfig.getRetrofit().create(UserClient.class);
        Call<Presensi> call = getResponse.cekVersi(version);

        call.enqueue(new Callback<Presensi>() {
            @Override
            public void onResponse(Call<Presensi> call, final Response<Presensi> response) {
                if (response.body().getValue().equals(1)) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginStepOne.class));

                } else {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            MapsActivity.this);
                    alertDialogBuilder.setTitle("Peringatan");
                    alertDialogBuilder
                            .setMessage("Perbaharui aplikasi anda!")
                            .setCancelable(false)
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    OpenBrowser = new Intent(Intent.ACTION_VIEW);
                                    OpenBrowser.setData(Uri.parse(response.body().getMessage()));
                                    UrlBrowser = Intent.createChooser(OpenBrowser, "Choose a Damartana App");
                                    startActivity(OpenBrowser);

                                }
                            })
                            .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    MapsActivity.this.finish();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<Presensi> call, Throwable t) {
                btnRetri.setVisibility(View.VISIBLE);
                Toast.makeText(MapsActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @OnClick(R.id.btnRetri)
    public void retriCheckForUpdate(){
        checkForUpdate();
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
