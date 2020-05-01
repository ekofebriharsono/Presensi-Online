package com.maseko.root.absen1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.maseko.root.absen1.Adapter.AccForAdminAdapter;
import com.maseko.root.absen1.Api.UserClient;
import com.maseko.root.absen1.Config.ApiConfig;
import com.maseko.root.absen1.Notification.Client;
import com.maseko.root.absen1.Notification.Data;
import com.maseko.root.absen1.Notification.MyResponse;
import com.maseko.root.absen1.Notification.Sender;
import com.maseko.root.absen1.Respone.ListAdmin;
import com.maseko.root.absen1.Respone.NotifAdmin;
import com.maseko.root.absen1.Respone.Presensi;
import com.maseko.root.absen1.Respone.ServerResponse;
import com.maseko.root.absen1.Result.ResultNotifAdmin;
import com.maseko.root.absen1.Result.ResultTokenAdmin;
import com.maseko.root.absen1.SharePreference.SaveSharedPreference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FotoActivity extends AppCompatActivity {

    TelephonyManager telephonyManager;

    @BindView(R.id.imageView3)
    ImageButton btnCamera;

    @BindView(R.id.imgFoto)
    ImageView previewImg;

    @BindView(R.id.close)
    ImageButton close;

    @BindView(R.id.checkBox3)
    CheckBox save_to_gallery;

    @BindView(R.id.txtNip)
    TextView txtNip;

    @BindView(R.id.txtNama)
    TextView txtNama;

    @BindView(R.id.txtWaktu)
    TextView txtWaktu;

    @BindView(R.id.txtTanggal)
    TextView txtTanggal;

    @BindView(R.id.txtLokasi)
    TextView txtLokasi;

    private static final String IMAGE_DIRECTORY = "/PresensiHistory";
    private String mImageFileLocation = "";
    private String postPath;
    private static final int CAMERA_PIC_REQUEST = 1111;

    UserClient apiService;


    Bitmap thumbnail;
    String id;

    private List<ResultTokenAdmin> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);
        ButterKnife.bind(this);


        switch (getIntent().getStringExtra("presensi")) {
            case "pegawai":
                txtNama.setText(SaveSharedPreference.getNamaUser(FotoActivity.this));
                txtNip.setText(SaveSharedPreference.getNipUser(FotoActivity.this));
                id = SaveSharedPreference.getIdUser(FotoActivity.this);
                Log.d("tes", "onCreate: " + "lokal / luar kota (diri sendiri)");
                break;
            case "luarKota":
                txtNama.setText(SaveSharedPreference.getNamaUser(FotoActivity.this));
                txtNip.setText(SaveSharedPreference.getNipUser(FotoActivity.this));
                id = SaveSharedPreference.getIdUser(FotoActivity.this);
                Log.d("tes", "onCreate: " + "lokal / luar kota (diri sendiri)");
                break;
            case "luarKotaLain":
                txtNama.setText(SaveSharedPreference.getNamaUserLain(FotoActivity.this));
                txtNip.setText(SaveSharedPreference.getNipUserLain(FotoActivity.this));
                id = SaveSharedPreference.getIdUserLain(FotoActivity.this);
                Log.d("tes", "onCreate: " + "lokal / luar kota (orang lain)");
                break;
            case "pegawaiLain":
                txtNama.setText(SaveSharedPreference.getNamaUserLain(FotoActivity.this));
                txtNip.setText(SaveSharedPreference.getNipUserLain(FotoActivity.this));
                id = SaveSharedPreference.getIdUserLain(FotoActivity.this);
                Log.d("tes", "onCreate: " + "lokal / luar kota (diri sendiri)");
                break;
            default:

                break;
        }

        txtWaktu.setText(getIntent().getStringExtra("waktu"));
        txtTanggal.setText(getIntent().getStringExtra("tanggal"));
        txtLokasi.setText(SaveSharedPreference.getLokasiUser(FotoActivity.this));

    }

    private void takePhotoFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, CAMERA_PIC_REQUEST);

    }

    @OnClick(R.id.imageView3)
    public void openCamera() {
        takePhotoFromCamera();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
       /* if (requestCode == CAMERA) {
            thumbnail = (Bitmap) data.getExtras().get("data");
            previewImg.setImageBitmap(thumbnail);
            //saveImage(thumbnail);
            previewImg.setVisibility(View.VISIBLE);
            close.setVisibility(View.VISIBLE);
            save_to_gallery.setVisibility(View.VISIBLE);
            btnCamera.setVisibility(View.GONE);

        }*/
        if (requestCode == CAMERA_PIC_REQUEST) {
            thumbnail = (Bitmap) data.getExtras().get("data");
            previewImg.setImageBitmap(thumbnail);
            postPath = mImageFileLocation;
            previewImg.setVisibility(View.VISIBLE);
            close.setVisibility(View.VISIBLE);
            save_to_gallery.setVisibility(View.VISIBLE);
            btnCamera.setVisibility(View.GONE);
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {

            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("filesave", "File Saved::--->" + f.getAbsolutePath());
            postPath = f.getAbsolutePath();
            //  Toast.makeText(FotoActivity.this, postPath, Toast.LENGTH_SHORT).show();

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    @OnClick(R.id.close)
    public void deleteImg() {
        previewImg.setVisibility(View.GONE);
        close.setVisibility(View.GONE);
        save_to_gallery.setVisibility(View.GONE);
        btnCamera.setVisibility(View.VISIBLE);
        thumbnail = null;
    }

    @OnClick(R.id.button)
    public void submitPresensi() {

        if (thumbnail != null) {
            saveImage(thumbnail);
            inputPresensi();
        } else {
            Toast.makeText(FotoActivity.this, "Anda harus memberikan bukti foto terlebih dahulu!", Toast.LENGTH_SHORT).show();
        }

    }

    public void inputPresensi() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Save data to server");
        dialog.setMessage("Loading ...");
        dialog.setCancelable(false);
        dialog.show();

        final String tanggal = getIntent().getStringExtra("tanggal");
        final String waktu = getIntent().getStringExtra("waktu");
        final String lokasi = txtLokasi.getText().toString();
        final String barcode = getIntent().getStringExtra("barcode");
        final String link_foto = "http://maseko.000webhostapp.com/smile_sisi.png";
        final String shift = getIntent().getStringExtra("shift");
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        final String imei = telephonyManager.getDeviceId();

        UserClient getResponse = ApiConfig.getRetrofit().create(UserClient.class);
        Call<Presensi> call;

        switch (getIntent().getStringExtra("presensi")) {
            case "pegawai":
                if (SaveSharedPreference.getPresensiUser(FotoActivity.this).equals("0")) {
                    call = getResponse.inputPresensi(id, tanggal, waktu, lokasi, barcode, link_foto, shift, "1", "0", imei, "-", "-");
                } else {
                    call = getResponse.inputPresensi(id, tanggal, waktu, lokasi, barcode, link_foto, shift, "1", "1", imei, "-", "-");
                }
                break;
            case "luarKota":
                if (SaveSharedPreference.getPresensiUser(FotoActivity.this).equals("0")) {
                    call = getResponse.inputPresensi(id, tanggal, waktu, lokasi, "-", link_foto, shift, "1", "0", imei, "0", "-");
                } else {
                    call = getResponse.inputPresensi(id, tanggal, waktu, lokasi, "-", link_foto, shift, "1", "1", imei, "0", "1");
                }
                break;
            case "luarKotaLain":
                if (SaveSharedPreference.getPresensiUserLain(FotoActivity.this).equals("0")) {
                    call = getResponse.inputPresensi(id, tanggal, waktu, lokasi, "-", link_foto, shift, "1", "0", imei, "0", "-");
                } else {
                    call = getResponse.inputPresensi(id, tanggal, waktu, lokasi, "-", link_foto, shift, "1", "1", imei, "0", "1");
                }
                break;
            case "pegawaiLain":
                if (SaveSharedPreference.getPresensiUserLain(FotoActivity.this).equals("0")) {
                    call = getResponse.inputPresensi(id, tanggal, waktu, lokasi, barcode, link_foto, shift, "1", "0", imei, "-", "-");
                } else {
                    call = getResponse.inputPresensi(id, tanggal, waktu, lokasi, barcode, link_foto, shift, "1", "1", imei, "-", "-");
                }
                break;
            default:
                if (SaveSharedPreference.getPresensiUserLain(FotoActivity.this).equals("0")) {
                    call = getResponse.inputPresensi(id, tanggal, waktu, lokasi, barcode, link_foto, shift, "1", "0", imei, "-", "-");
                } else {
                    call = getResponse.inputPresensi(id, tanggal, waktu, lokasi, barcode, link_foto, shift, "1", "1", imei, "-", "-");
                }
                break;
        }


        call.enqueue(new Callback<Presensi>() {
            @Override
            public void onResponse(Call<Presensi> call, Response<Presensi> response) {
                if (response.body().getValue().equals(1)) {
                    dialog.hide();
                    uploadFoto();

                } else {
                    dialog.hide();
                    Toast.makeText(FotoActivity.this, "Try it", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Presensi> call, Throwable t) {
                dialog.hide();
                Toast.makeText(FotoActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadFoto() {
        final ProgressDialog dialogUpload = new ProgressDialog(this);
        dialogUpload.setTitle("Upload image");
        dialogUpload.setMessage("Uploading ...");
        dialogUpload.setCancelable(false);
        dialogUpload.show();

        String pulang;

        Map<String, RequestBody> map = new HashMap<>();
        File file = new File(postPath);

        final RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        RequestBody requestBody1;
        switch (getIntent().getStringExtra("presensi")) {
            case "pegawai":
                requestBody1 = RequestBody.create(MediaType.parse("*/*"), SaveSharedPreference.getIdUser(FotoActivity.this));
                if (SaveSharedPreference.getPresensiUser(FotoActivity.this).equals("0")) {
                    pulang = "0";
                } else {
                    pulang = "1";
                }
                break;
            case "luarKota":
                requestBody1 = RequestBody.create(MediaType.parse("*/*"), SaveSharedPreference.getIdUser(FotoActivity.this));
                if (SaveSharedPreference.getPresensiUser(FotoActivity.this).equals("0")) {
                    pulang = "0";
                } else {
                    pulang = "1";
                }
                break;
            case "luarKotaLain":
                requestBody1 = RequestBody.create(MediaType.parse("*/*"), SaveSharedPreference.getIdUserLain(FotoActivity.this));
                if (SaveSharedPreference.getPresensiUserLain(FotoActivity.this).equals("0")) {
                    pulang = "0";
                } else {
                    pulang = "1";
                }
                break;
            case "pegawaiLain":
                requestBody1 = RequestBody.create(MediaType.parse("*/*"), SaveSharedPreference.getIdUserLain(FotoActivity.this));
                if (SaveSharedPreference.getPresensiUserLain(FotoActivity.this).equals("0")) {
                    pulang = "0";
                } else {
                    pulang = "1";
                }
                break;
            default:
                requestBody1 = RequestBody.create(MediaType.parse("*/*"), SaveSharedPreference.getIdUserLain(FotoActivity.this));
                if (SaveSharedPreference.getPresensiUserLain(FotoActivity.this).equals("0")) {
                    pulang = "0";
                } else {
                    pulang = "1";
                }
                break;
        }

        RequestBody requestBody2 = RequestBody.create(MediaType.parse("*/*"), getIntent().getStringExtra("tanggal"));

        RequestBody requestBody3 = RequestBody.create(MediaType.parse("*/*"), pulang);
        map.put("file\"; filename=\"" + file.getName() + "\"", requestBody);
        map.put("id_user", requestBody1);
        map.put("tanggal", requestBody2);
        map.put("pulang", requestBody3);

        UserClient getResponse = ApiConfig.getRetrofit().create(UserClient.class);

        Call<ServerResponse> call = getResponse.upload(map);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body().getSuccess()) {
                    dialogUpload.hide();
                    Intent intent = new Intent(FotoActivity.this, SuccessActivity.class);
                    intent.putExtra("nama",txtNama.getText().toString());
                    switch (getIntent().getStringExtra("presensi")) {
                        case "luarKota":
                            loadTokenAdmin();
                            break;
                        case "pegawaiLain":
                            loadTokenAdmin();
                            break;

                    }

                    startActivity(intent);
                    finish();

                } else {
                    dialogUpload.hide();
                    Toast.makeText(FotoActivity.this, "Try it", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                dialogUpload.hide();
                Toast.makeText(FotoActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Notification(String body, String title, String sented, String token){
        Data data = new Data("Pegawai", R.drawable.damartana, body, title,
                sented);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(UserClient.class);

        Sender sender = new Sender(data, token);
        apiService.sendNotification(sender)
                .enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        if (response.code() == 200) {
                            if (response.body().success != 1) {
                                Toast.makeText(FotoActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        Toast.makeText(FotoActivity.this, "check your internet connection!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void loadTokenAdmin() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading ...");
        dialog.setCancelable(false);
        dialog.show();

        UserClient getResponse = ApiConfig.getRetrofit().create(UserClient.class);
        Call<ListAdmin> call = getResponse.getListTokenAdmin();

        call.enqueue(new Callback<ListAdmin>() {
            @Override
            public void onResponse(Call<ListAdmin> call, Response<ListAdmin> response) {
                if (response.body().getValue().equals("1")) {
                    results = response.body().getResult();

                  for (int i = 0 ; i < results.size(); i++){
                      Notification(txtNama.getText().toString(),"Presensi Luar Kota",txtNama.getText().toString(),results.get(i).getToken());
                  }

                    dialog.hide();
                } else {
                    dialog.hide();
                }


            }

            @Override
            public void onFailure(Call<ListAdmin> call, Throwable t) {
                dialog.hide();
                Toast.makeText(FotoActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
