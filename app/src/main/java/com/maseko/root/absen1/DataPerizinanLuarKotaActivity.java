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

import com.maseko.root.absen1.Adapter.DataPerizinanLuarKotaAdapter;
import com.maseko.root.absen1.Api.UserClient;
import com.maseko.root.absen1.Config.ApiConfig;
import com.maseko.root.absen1.Respone.DataPrizinanLuarKota;
import com.maseko.root.absen1.Result.ResultDataPerizinanLuarKota;

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

public class DataPerizinanLuarKotaActivity extends AppCompatActivity {

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

    private List<ResultDataPerizinanLuarKota> results = new ArrayList<>();
    private DataPerizinanLuarKotaAdapter dataPerizinanLuarKotaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_perizinan_luar_kota);
        ButterKnife.bind(this);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);


        dataPerizinanLuarKotaAdapter = new DataPerizinanLuarKotaAdapter(DataPerizinanLuarKotaActivity.this, results);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DataPerizinanLuarKotaActivity.this);
        rcycDataPresensi.setLayoutManager(mLayoutManager);
        rcycDataPresensi.setItemAnimator(new DefaultItemAnimator());
        rcycDataPresensi.setAdapter(dataPerizinanLuarKotaAdapter);

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
        if (tanggalAwal != null && tanggalAkhir != null) filterDataPerizinanLuarKota();
        else
            Toast.makeText(DataPerizinanLuarKotaActivity.this, "Tanggal awal dan akhir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
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

    public void filterDataPerizinanLuarKota() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading ...");
        dialog.setCancelable(false);
        dialog.show();

        UserClient getResponse = ApiConfig.getRetrofit().create(UserClient.class);
        Call<DataPrizinanLuarKota> call = getResponse.filterDataPresensiLuarKota(tanggalAwal, tanggalAkhir);
        call.enqueue(new Callback<DataPrizinanLuarKota>() {
            @Override
            public void onResponse(Call<DataPrizinanLuarKota> call, Response<DataPrizinanLuarKota> response) {
                if (response.body().getValue() == 1) {
                    results = response.body().getResult();
                    dialog.hide();

                    dataPerizinanLuarKotaAdapter = new DataPerizinanLuarKotaAdapter(DataPerizinanLuarKotaActivity.this, results);
                    rcycDataPresensi.setAdapter(dataPerizinanLuarKotaAdapter);

                } else {
                    dialog.hide();
                }

            }

            @Override
            public void onFailure(Call<DataPrizinanLuarKota> call, Throwable t) {
                dialog.hide();
                Toast.makeText(DataPerizinanLuarKotaActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
