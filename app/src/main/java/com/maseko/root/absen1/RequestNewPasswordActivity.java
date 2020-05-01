package com.maseko.root.absen1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestNewPasswordActivity extends AppCompatActivity {

    @BindView(R.id.txtNip)
    TextView txtNip;

    @BindView(R.id.txtNama)
    TextView txtNama;

    @BindView(R.id.txtInfo)
    TextView txtInfo;

    @BindView(R.id.edPassword)
    EditText edPassword;

    TelephonyManager telephonyManager;
    String subscriberId, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_new_password);
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(RequestNewPasswordActivity.this);

        txtNip.setText(SaveSharedPreference.getNipUser(RequestNewPasswordActivity.this));
        txtNama.setText(SaveSharedPreference.getNamaUser(RequestNewPasswordActivity.this));

        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        subscriberId = telephonyManager.getSubscriberId();

        if(getIntent().getStringExtra("info").equals("true")){
            txtInfo.setText("Hallo, Silahkan masukkan password baru anda!");
        }else {
            txtInfo.setText("Anda terditeksi menggunakan device baru, Silahkan perbaharui password anda");
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("tes", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                         token = task.getResult().getToken();
                    }
                });
    }
    public void runtimeEnableAutoInit() {
        // [START fcm_runtime_enable_auto_init]
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        // [END fcm_runtime_enable_auto_init]
    }

    @OnClick(R.id.button)
    public void requestDevice(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading ...");
        dialog.setCancelable(false);
        dialog.show();

        final String id_user = SaveSharedPreference.getIdUser(RequestNewPasswordActivity.this);
        final String password = edPassword.getText().toString();

        UserClient getResponse = ApiConfig.getRetrofit().create(UserClient.class);
        Call<Presensi> call = getResponse.requestPassword(id_user, password, subscriberId,token);

        call.enqueue(new Callback<Presensi>() {
            @Override
            public void onResponse(Call<Presensi> call, Response<Presensi> response) {
                if (response.body().getValue().equals(1)) {
                    Toast.makeText(RequestNewPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.hide();
                    finish();

                } else {
                    dialog.hide();
                    Log.d("asd", "onResponse: " + id_user + "\n" + password+ "\n" + subscriberId +"\n" + token);
                    Toast.makeText(RequestNewPasswordActivity.this, "Try it", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Presensi> call, Throwable t) {
                dialog.hide();
                Toast.makeText(RequestNewPasswordActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
