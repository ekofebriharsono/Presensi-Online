package com.maseko.root.absen1;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.maseko.root.absen1.Adapter.AppAdapter;
import com.maseko.root.absen1.Api.UserClient;
import com.maseko.root.absen1.Config.ApiConfig;
import com.maseko.root.absen1.Respone.Login;
import com.maseko.root.absen1.Result.AppList;
import com.maseko.root.absen1.SharePreference.SaveSharedPreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginStepOne extends AppCompatActivity {

    @BindView(R.id.edNip)
    EditText edNip;

    @BindView(R.id.edPassword)
    TextInputEditText edPassword;

    @BindView(R.id.checkBox)
    CheckBox checkBox;

    TelephonyManager telephonyManager;
    String subscriberId;

    List<AppList> res = new ArrayList<AppList>();
    List<AppList> installedApps = new ArrayList<AppList>();
    AppAdapter installedAppAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_step_one);
        ButterKnife.bind(this);


        cekFakeGps();


        if (SaveSharedPreference.getIngat(LoginStepOne.this)) {
            checkBox.setChecked(true);
            edNip.setText(SaveSharedPreference.getNipUser(LoginStepOne.this));
            edPassword.setText(SaveSharedPreference.getPassUser(LoginStepOne.this));
        } else {
            checkBox.setChecked(false);
        }

        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        subscriberId = telephonyManager.getSubscriberId();
    }

    Dialog dialog;

    public void cekFakeGps() {

        installedApps = getAplicationList();
        if (!res.isEmpty()) {
            dialog = new Dialog(LoginStepOne.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_fake_gps);
            dialog.setCancelable(false);
            ListView userInstalledApps = dialog.findViewById(R.id.installed_app_list);
            Button btnUninstall = dialog.findViewById(R.id.btnUninstall);

            installedAppAdapter = new AppAdapter(LoginStepOne.this, installedApps);
            userInstalledApps.setAdapter(installedAppAdapter);

            btnUninstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS));
                    dialog.cancel();

                }
            });

            dialog.show();
        }
    }

    public List<AppList> getAplicationList() {

        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((isSystemPackage(p) == false)) {
                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
                if (appName.contains("fake gps") || appName.contains("Fake GPS") || appName.contains("fake location") || appName.contains("Lokasi Palsu") || appName.contains("Palsu") || appName.contains("Lokasi")) {
                    Log.d("tes", "Installed package :" + appName);
                    res.add(new AppList(appName, icon));

                }


            }
        }
        return res;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }


    public void loginUser() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading ...");
        dialog.setCancelable(false);
        dialog.show();

        final String Nip = edNip.getText().toString();
        final String Password = edPassword.getText().toString();

        UserClient getResponse = ApiConfig.getRetrofit().create(UserClient.class);
        Call<Login> call = getResponse.login(Nip, Password);

        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.body().getValue().equals(1)) {
                    SaveSharedPreference.setIdUser(LoginStepOne.this, response.body().getIdUser());
                    SaveSharedPreference.setNipUser(LoginStepOne.this, Nip);
                    SaveSharedPreference.setPassUser(LoginStepOne.this, Password);
                    SaveSharedPreference.setNamaUser(LoginStepOne.this, response.body().getNama());
                    SaveSharedPreference.setAdmin(LoginStepOne.this, response.body().getAcp());

                    if (checkBox.isChecked()) {
                        // SaveSharedPreference.setPresensiUser(LoginStepOne.this, response.body().getPresensi());
                        SaveSharedPreference.setIngat(LoginStepOne.this, true);

                    } else {
                        SaveSharedPreference.setIngat(LoginStepOne.this, false);
                    }

                    if (response.body().getIdDevice().equals("-") && response.body().getStatusDevice().equals("0")) {
                        dialog.hide();
                        Intent intent = new Intent(LoginStepOne.this, RequestNewPasswordActivity.class);
                        intent.putExtra("info", "true");
                        startActivity(intent);
                    } /*else if (response.body().getIdDevice().equals("-")) {
                        dialog.hide();
                        Intent intent = new Intent(LoginStepOne.this, RequestNewDeviceActivity.class);
                        startActivity(intent);
                    } else if (response.body().getStatusDevice().equals("0")) {
                        dialog.hide();
                        Toast.makeText(LoginStepOne.this, "Silahkan tunggu admin untuk ACC Device anda!", Toast.LENGTH_SHORT).show();
                    } */ else if (!response.body().getIdDevice().equals(subscriberId)) {
                        dialog.hide();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                LoginStepOne.this);
                        alertDialogBuilder.setTitle("Peringatan");
                        alertDialogBuilder
                                .setMessage("Anda terdeteksi menggunakan device lain!")
                                .setCancelable(false)
                                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        LoginStepOne.this.finish();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    } else {
                        dialog.hide();
                        Intent intent = new Intent(LoginStepOne.this, OptionPresensiActivity.class);
                        intent.putExtra("optionPresensi", "pegawai");
                        startActivity(intent);
                        finish();
                    }

                } else {
                    dialog.hide();
                    Toast.makeText(LoginStepOne.this, "Try it", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                dialog.hide();
                Toast.makeText(LoginStepOne.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.button)
    public void nextToLogin() {
        loginUser();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.cancel();
            res.clear();
            installedAppAdapter.notifyDataSetChanged();
        }
        // cekFakeGps();

    }

    @Override
    public void onResume() {
        super.onResume();

        if (dialog != null) {
            dialog.cancel();
            res.clear();
            installedAppAdapter.notifyDataSetChanged();
        }

        cekFakeGps();

    }

    public boolean doubleTapParam = false;

    @Override
    public void onBackPressed() {
        if (doubleTapParam) {
            super.onBackPressed();
            return;
        }

        this.doubleTapParam = true;
        Toast.makeText(this, "Tap sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleTapParam = false;

            }
        }, 2000);
    }
}
