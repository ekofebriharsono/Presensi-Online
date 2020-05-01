package com.maseko.root.absen1;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.maseko.root.absen1.Adapter.AccForAdminAdapter;
import com.maseko.root.absen1.Api.UserClient;
import com.maseko.root.absen1.Config.ApiConfig;
import com.maseko.root.absen1.Respone.Login;
import com.maseko.root.absen1.Respone.NotifAdmin;
import com.maseko.root.absen1.Result.ResultNotifAdmin;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifActivity extends AppCompatActivity {

    @BindView(R.id.rcycNotifAdmin)
    RecyclerView rcycNotifAdmin;

    private AccForAdminAdapter accForAdminAdapter;
    private List<ResultNotifAdmin> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);
        ButterKnife.bind(this);

        accForAdminAdapter = new AccForAdminAdapter(NotifActivity.this, results);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NotifActivity.this);
        rcycNotifAdmin.setLayoutManager(mLayoutManager);
        rcycNotifAdmin.setItemAnimator(new DefaultItemAnimator());
        rcycNotifAdmin.setAdapter(accForAdminAdapter);

        loadNotifAdmin();

    }

    public void loadNotifAdmin() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading ...");
        dialog.setCancelable(false);
        dialog.show();

        UserClient getResponse = ApiConfig.getRetrofit().create(UserClient.class);
        Call<NotifAdmin> call = getResponse.getListNotifAdmin();

        call.enqueue(new Callback<NotifAdmin>() {
            @Override
            public void onResponse(Call<NotifAdmin> call, Response<NotifAdmin> response) {
                if (response.body().getValue().equals("1")) {
                    results = response.body().getResult();
                    dialog.hide();

                    accForAdminAdapter = new AccForAdminAdapter(NotifActivity.this, results);
                    rcycNotifAdmin.setAdapter(accForAdminAdapter);

                } else {
                    dialog.hide();
                }


            }

            @Override
            public void onFailure(Call<NotifAdmin> call, Throwable t) {
                dialog.hide();
                Toast.makeText(NotifActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
