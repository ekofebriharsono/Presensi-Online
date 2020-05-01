package com.maseko.root.absen1;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.maseko.root.absen1.Adapter.DataPresensiAdapter;
import com.maseko.root.absen1.Api.UserClient;
import com.maseko.root.absen1.Config.ApiConfig;
import com.maseko.root.absen1.Respone.DataPresensi;
import com.maseko.root.absen1.Result.ResultDataPresensi;
import com.maseko.root.absen1.SharePreference.SaveSharedPreference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataPresensiActivity extends AppCompatActivity {

    @BindView(R.id.btnTanggalAwal)
    Button btnTanggalAwal;

    @BindView(R.id.btnTanggalAkhir)
    Button btnTanggalAkhir;

    @BindView(R.id.rcycDataPresensi)
    RecyclerView rcycDataPresensi;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    String tanggalAwal = null;
    String tanggalAkhir = null;

    private DataPresensiAdapter dataPresensiAdapter;
    private List<ResultDataPresensi> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_presensi);
        ButterKnife.bind(this);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        dataPresensiAdapter = new DataPresensiAdapter(DataPresensiActivity.this, results);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DataPresensiActivity.this);
        rcycDataPresensi.setLayoutManager(mLayoutManager);
        rcycDataPresensi.setItemAnimator(new DefaultItemAnimator());
        rcycDataPresensi.setAdapter(dataPresensiAdapter);
    }

    @OnClick(R.id.btnTanggalAwal)
    public void pilihTanggalAwal() {
        showDateDialogAwal();
    }

    @OnClick(R.id.btnTanggalAkhir)
    public void pilihTanggalAkhir() {
        showDateDialogAkhir();
    }

    @OnClick(R.id.btnFIlterTanggal)
    public void btnFilterDataPresensi() {
        if (tanggalAwal != null && tanggalAkhir != null) filterDataPresensi();
        else
            Toast.makeText(DataPresensiActivity.this, "Tanggal awal dan akhir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
    }

    private void showDateDialogAwal() {

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                String resultTanggal = dateFormatter.format(newDate.getTime());
                btnTanggalAwal.setText(resultTanggal);
                tanggalAwal = resultTanggal;

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void showDateDialogAkhir() {

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                String resultTanggal = dateFormatter.format(newDate.getTime());
                btnTanggalAkhir.setText(resultTanggal);
                tanggalAkhir = resultTanggal;

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public void filterDataPresensi() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading ...");
        dialog.setCancelable(false);
        dialog.show();

        UserClient getResponse = ApiConfig.getRetrofit().create(UserClient.class);
        Call<DataPresensi> call = getResponse.filterDataPresensi(SaveSharedPreference.getIdUser(DataPresensiActivity.this), tanggalAwal, tanggalAkhir);

        call.enqueue(new Callback<DataPresensi>() {
            @Override
            public void onResponse(Call<DataPresensi> call, Response<DataPresensi> response) {
                if (response.body().getValue() == 1) {
                    results = response.body().getResult();
                    dialog.hide();

                    dataPresensiAdapter = new DataPresensiAdapter(DataPresensiActivity.this, results);
                    rcycDataPresensi.setAdapter(dataPresensiAdapter);

                } else {
                    dialog.hide();
                }

            }

            @Override
            public void onFailure(Call<DataPresensi> call, Throwable t) {
                dialog.hide();
                Toast.makeText(DataPresensiActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
