package com.maseko.root.absen1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.maseko.root.absen1.SharePreference.SaveSharedPreference;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainPegawaiLainActivity extends AppCompatActivity {

    @BindView(R.id.radioGroup2)
    RadioGroup radioGroup2;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.txtNip)
    TextView txtNip;

    @BindView(R.id.txtNama)
    TextView txtNama;

    @BindView(R.id.rbPulang)
    RadioButton rbPulang;

    @BindView(R.id.rbHadir)
    RadioButton rbHadir;

    @BindView(R.id.rShift1)
    RadioButton rShift1;

    @BindView(R.id.rShift2)
    RadioButton rShift2;

    @BindView(R.id.rShift3)
    RadioButton rShift3;

    @BindView(R.id.rShift4)
    RadioButton rShift4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pegawai_lain);
        ButterKnife.bind(this);

        txtNama.setText(SaveSharedPreference.getNamaUserLain(MainPegawaiLainActivity.this));
        txtNip.setText(SaveSharedPreference.getNipUserLain(MainPegawaiLainActivity.this));

    }

    public void getCurrentLocation() {
        // GET CURRENT LOCATION
        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Do it all with location
                    Log.d("My Current location", "Lat : " + location.getLatitude() + " Long : " + location.getLongitude());
                    // Display in Toast

                    List<Address> addressList = null;
                    String kodepos = "";
                    Geocoder geocoder = new Geocoder(MainPegawaiLainActivity.this);

                    try {
                        addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        kodepos = addressList.get(0).getAddressLine(0);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    SaveSharedPreference.setLokasiUser(MainPegawaiLainActivity.this, kodepos);
                    SaveSharedPreference.setLatUser(MainPegawaiLainActivity.this, Double.toString(location.getLatitude()));
                    SaveSharedPreference.setLongUser(MainPegawaiLainActivity.this, Double.toString(location.getLongitude()));
                    Toast.makeText(MainPegawaiLainActivity.this,kodepos,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @OnClick(R.id.rbHadir)
    public void setHadir() {
        radioGroup2.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.rbPulang)
    public void setPulang() {
        radioGroup2.setVisibility(View.GONE);
    }

    @OnClick(R.id.button)
    public void scanQrCode() {
        getCurrentLocation();
        Intent intent = new Intent(MainPegawaiLainActivity.this, ScanQrCode.class);
        intent.putExtra("presensi", "pegawaiLain");
        if (rShift1.isChecked()) {
            intent.putExtra("shift", rShift1.getText().toString());
        } else if (rShift2.isChecked()) {
            intent.putExtra("shift", rShift2.getText().toString());
        } else if (rShift3.isChecked()) {
            intent.putExtra("shift", rShift3.getText().toString());
        } else if (rShift4.isChecked()) {
            intent.putExtra("shift", rShift4.getText().toString());
        }

        if (SaveSharedPreference.getPresensiUserLain(MainPegawaiLainActivity.this).equals("0") && rbPulang.isChecked()) {
            Toast.makeText(MainPegawaiLainActivity.this, "Maaf, Anda Belum Melakukan Presensi HADIR!", Toast.LENGTH_SHORT).show();
        } else if (SaveSharedPreference.getPresensiUserLain(MainPegawaiLainActivity.this).equals("1") && rbHadir.isChecked()) {
            Toast.makeText(MainPegawaiLainActivity.this, "Maaf, Anda Belum Telah Presensi HADIR!", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(intent);
        }
    }

}
