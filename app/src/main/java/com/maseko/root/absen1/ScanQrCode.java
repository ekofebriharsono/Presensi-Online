package com.maseko.root.absen1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;
import com.maseko.root.absen1.Api.UserClient;
import com.maseko.root.absen1.Config.ApiConfig;
import com.maseko.root.absen1.Respone.Presensi;
import com.maseko.root.absen1.SharePreference.SaveSharedPreference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanQrCode extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    public static String getWaktu() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getTanggal() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.v("TAG", rawResult.getText()); // Prints scan results
        Log.v("TAG", rawResult.getBarcodeFormat().toString());

        if (rawResult.getText().isEmpty()){
            Toast.makeText(ScanQrCode.this,"Gagal",Toast.LENGTH_SHORT).show();
        } else {
           /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Scan Result");
            builder.setMessage(rawResult.getText());
            AlertDialog alert1 = builder.create();
            alert1.show();
            mScannerView.stopCamera();*/
            mScannerView.stopCamera();
            finish();

            Intent intent = new Intent(ScanQrCode.this, FotoActivity.class);
            intent.putExtra("waktu", getWaktu());
            intent.putExtra("tanggal", getTanggal());
            intent.putExtra("barcode", rawResult.getText());
            intent.putExtra("shift", getIntent().getStringExtra("shift"));
            intent.putExtra("presensi", getIntent().getStringExtra("presensi"));
            startActivity(intent);



         //  cekLokasiBarcode(rawResult.getText());

        }

        mScannerView.resumeCameraPreview(this);
    }

    public void cekLokasiBarcode(final String id_barcode){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading ...");
        dialog.setCancelable(false);
        dialog.show();


        final String longitude = SaveSharedPreference.getLongUser(ScanQrCode.this);
        final String lat = SaveSharedPreference.getLatUser(ScanQrCode.this);

        UserClient getResponse = ApiConfig.getRetrofit().create(UserClient.class);
        Call<Presensi> call = getResponse.cekLokasiBarcode(id_barcode, lat, longitude);

        call.enqueue(new Callback<Presensi>() {
            @Override
            public void onResponse(Call<Presensi> call, Response<Presensi> response) {
                if (response.body().getValue().equals(1)) {
                    Toast.makeText(ScanQrCode.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();

                    Intent intent = new Intent(ScanQrCode.this, FotoActivity.class);
                    intent.putExtra("waktu", getWaktu());
                    intent.putExtra("tanggal", getTanggal());
                    intent.putExtra("barcode", id_barcode);
                    intent.putExtra("shift", getIntent().getStringExtra("shift"));
                    intent.putExtra("presensi", getIntent().getStringExtra("presensi"));
                    startActivity(intent);

                } else {
                    dialog.hide();
                    Toast.makeText(ScanQrCode.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Presensi> call, Throwable t) {
                dialog.hide();
                Toast.makeText(ScanQrCode.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }



}