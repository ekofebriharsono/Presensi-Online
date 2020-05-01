package com.maseko.root.absen1;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.maseko.root.absen1.Adapter.AccForAdminAdapter;
import com.maseko.root.absen1.Adapter.NotifForPegawaiAdapter;
import com.maseko.root.absen1.Api.UserClient;
import com.maseko.root.absen1.Config.ApiConfig;
import com.maseko.root.absen1.Respone.NotifAdmin;
import com.maseko.root.absen1.Respone.NotifPegawai;
import com.maseko.root.absen1.Result.ResultNotifAdmin;
import com.maseko.root.absen1.Result.ResultNotifPegawai;
import com.maseko.root.absen1.SharePreference.SaveSharedPreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifPegawaiActivity extends AppCompatActivity {

    @BindView(R.id.rcycNotifPegawai)
    RecyclerView rcycNotifPegawai;


    private NotifForPegawaiAdapter notifForPegawaiAdapter;
    private List<ResultNotifPegawai> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_pegawai);
        ButterKnife.bind(this);

        notifForPegawaiAdapter = new NotifForPegawaiAdapter(NotifPegawaiActivity.this, results);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NotifPegawaiActivity.this);
        rcycNotifPegawai.setLayoutManager(mLayoutManager);
        rcycNotifPegawai.setItemAnimator(new DefaultItemAnimator());
        rcycNotifPegawai.setAdapter(notifForPegawaiAdapter);

        loadNotifPegawai();

    }

    public void loadNotifPegawai() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading ...");
        dialog.setCancelable(false);
        dialog.show();

        UserClient getResponse = ApiConfig.getRetrofit().create(UserClient.class);
        Call<NotifPegawai> call = getResponse.notifPegawai(SaveSharedPreference.getNipUser(NotifPegawaiActivity.this));

        call.enqueue(new Callback<NotifPegawai>() {
            @Override
            public void onResponse(Call<NotifPegawai> call, Response<NotifPegawai> response) {
                if (response.body().getValue().equals("1")) {
                    results = response.body().getResult();
                    dialog.hide();

                    notifForPegawaiAdapter = new NotifForPegawaiAdapter(NotifPegawaiActivity.this, results);
                    rcycNotifPegawai.setAdapter(notifForPegawaiAdapter);

                } else {
                    dialog.hide();
                }

            }

            @Override
            public void onFailure(Call<NotifPegawai> call, Throwable t) {
                dialog.hide();
                Toast.makeText(NotifPegawaiActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
