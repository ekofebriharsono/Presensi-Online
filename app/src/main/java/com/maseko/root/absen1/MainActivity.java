package com.maseko.root.absen1;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.maseko.root.absen1.Api.UserClient;
import com.maseko.root.absen1.Config.ApiConfig;
import com.maseko.root.absen1.Respone.CekPresensiHariIni;
import com.maseko.root.absen1.Respone.Login;
import com.maseko.root.absen1.SharePreference.SaveSharedPreference;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.maseko.root.absen1.ScanQrCode.getTanggal;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        txtNip.setText(SaveSharedPreference.getNipUser(MainActivity.this));
        txtNama.setText(SaveSharedPreference.getNamaUser(MainActivity.this));

     //   isAdmin();

      //  cekHadirHariIni();

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
                    Geocoder geocoder = new Geocoder(MainActivity.this);

                    try {
                        addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        kodepos = addressList.get(0).getAddressLine(0);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    SaveSharedPreference.setLokasiUser(MainActivity.this, kodepos);
                    SaveSharedPreference.setLatUser(MainActivity.this, Double.toString(location.getLatitude()));
                    SaveSharedPreference.setLongUser(MainActivity.this, Double.toString(location.getLongitude()));
//                    Toast.makeText(MainActivity.this,kodepos,
//                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*public void isAdmin() {
        if (SaveSharedPreference.getAdmin(MainActivity.this).equals("1")) {
           // btnPresensiPegawai.setVisibility(View.VISIBLE);
            btnNotif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, NotifActivity.class));
                }
            });
        } else {
            btnNotif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, NotifPegawaiActivity.class));
                }
            });
        }
    }*/

    public void cekHadirHariIni() {
        UserClient getResponse = ApiConfig.getRetrofit().create(UserClient.class);
        Call<CekPresensiHariIni> call = getResponse.cekPresensiHariIni(SaveSharedPreference.getIdUser(MainActivity.this), getTanggal());

        call.enqueue(new Callback<CekPresensiHariIni>() {
            @Override
            public void onResponse(Call<CekPresensiHariIni> call, Response<CekPresensiHariIni> response) {
                if (response.body().getValue().equals(1)) {


                    if (!response.body().getWaktu().equals("-") && !response.body().getWaktuPulang().equals("-")) {
                        SaveSharedPreference.setPresensiUser(MainActivity.this, "2");
                    } else {
                        SaveSharedPreference.setPresensiUser(MainActivity.this, "1");
                    }


                } else {

                    SaveSharedPreference.setPresensiUser(MainActivity.this, "0");

                }
            }

            @Override
            public void onFailure(Call<CekPresensiHariIni> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(MainActivity.this, ScanQrCode.class);
        intent.putExtra("presensi", "pegawai");
        if (rShift1.isChecked()) {
            intent.putExtra("shift", rShift1.getText().toString());
        } else if (rShift2.isChecked()) {
            intent.putExtra("shift", rShift2.getText().toString());
        } else if (rShift3.isChecked()) {
            intent.putExtra("shift", rShift3.getText().toString());
        } else if (rShift4.isChecked()) {
            intent.putExtra("shift", rShift4.getText().toString());
        }

        if (SaveSharedPreference.getPresensiUser(MainActivity.this).equals("0") && rbPulang.isChecked()) {
            Toast.makeText(MainActivity.this, "Maaf, Anda Belum Melakukan Presensi HADIR!", Toast.LENGTH_SHORT).show();
        } else if (SaveSharedPreference.getPresensiUser(MainActivity.this).equals("1") && rbHadir.isChecked()) {
            Toast.makeText(MainActivity.this, "Maaf, Anda Telah Presensi HADIR!", Toast.LENGTH_SHORT).show();
        } else if (SaveSharedPreference.getPresensiUser(MainActivity.this).equals("2")) {
            Toast.makeText(MainActivity.this, "Maaf, Telah Melakukan Presensi Hari Ini", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //cekHadirHariIni();

    }
/*
    @OnClick(R.id.btnPresensiPegawai)
    public void cariNip() {
        final Dialog dialog = new Dialog(MainActivity.this);
        final ProgressDialog dialogLoading = new ProgressDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cari_pegawai);

        final EditText edCari = dialog.findViewById(R.id.EDcari);
        Button btnCari = dialog.findViewById(R.id.btnCari);

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogLoading.setMessage("Loading ...");
                dialogLoading.setCancelable(false);
                dialogLoading.show();

                final String nip = edCari.getText().toString();

                UserClient getResponse = ApiConfig.getRetrofit().create(UserClient.class);
                Call<Login> call = getResponse.presensiPegawai(nip);

                call.enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> call, Response<Login> response) {
                        if (response.body().getValue().equals(1)) {
                            SaveSharedPreference.setIdUserLain(MainActivity.this, response.body().getIdUser());
                            SaveSharedPreference.setNipUserLain(MainActivity.this, response.body().getNip());
                            SaveSharedPreference.setNamaUserLain(MainActivity.this, response.body().getNama());
                            SaveSharedPreference.setPresensiUserLain(MainActivity.this, response.body().getPresensi());
                            txtNip.setText(SaveSharedPreference.getNipUserLain(MainActivity.this));
                            txtNama.setText(SaveSharedPreference.getNamaUserLain(MainActivity.this));

                            Intent intent = new Intent(MainActivity.this, OptionPresensiActivity.class);
                            intent.putExtra("optionPresensi", "pegawaiLain");
                            startActivity(intent);
                            finish();

                            dialogLoading.hide();

                        } else {

                            dialogLoading.hide();
                        }
                    }

                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {
                        dialogLoading.hide();
                        Toast.makeText(MainActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        dialog.show();
    }*/
}
