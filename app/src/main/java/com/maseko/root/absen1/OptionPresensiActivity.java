package com.maseko.root.absen1;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.maseko.root.absen1.Adapter.MenuAdapter;
import com.maseko.root.absen1.Api.UserClient;
import com.maseko.root.absen1.Config.ApiConfig;
import com.maseko.root.absen1.Respone.CekPresensiHariIni;
import com.maseko.root.absen1.Respone.Login;
import com.maseko.root.absen1.Respone.NotifAdmin;
import com.maseko.root.absen1.Respone.NotifPegawai;
import com.maseko.root.absen1.Result.ResultNotifAdmin;
import com.maseko.root.absen1.Result.ResultNotifPegawai;
import com.maseko.root.absen1.SharePreference.SaveSharedPreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.maseko.root.absen1.NetworkUtil.isAvaiableNetwork;
import static com.maseko.root.absen1.NetworkUtil.isOnline;
import static com.maseko.root.absen1.NetworkUtil.isWifi;
import static com.maseko.root.absen1.ScanQrCode.getTanggal;
import static com.maseko.root.absen1.ScanQrCode.getWaktu;

public class OptionPresensiActivity extends AppCompatActivity {

    @BindView(R.id.txtNip)
    TextView txtNip;

    @BindView(R.id.countBadge)
    TextView countBadge;

    @BindView(R.id.txtNama)
    TextView txtNama;

    @BindView(R.id.txtWaktuHadir)
    TextView txtWaktuHadir;

    @BindView(R.id.txtWaktuPulang)
    TextView txtWaktuPulang;

    @BindView(R.id.btnNotif)
    ImageButton btnNotif;

    @BindView(R.id.btnRetri)
    Button btnRetri;

    @BindView(R.id.btnLocal)
    Button btnLocal;

    @BindView(R.id.btnLuarKota)
    Button btnLuarKota;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.menu)
    GridView menu;

    @BindView(R.id.statusNetwork)
    ImageView statusNetwork;

    @BindView(R.id.indicatorNetwork)
    ImageView indicatorNetwork;

    private List<ResultNotifAdmin> results = new ArrayList<>();
    private List<ResultNotifPegawai> resultsPegawai = new ArrayList<>();
    int badge = 0;
    boolean isAdmin = false;
    boolean isConnectedNetwork = false;

    String title[] = {"Presensi", "Website", "Contact Us", "Tentang"};
    int flags[] = {R.drawable.ic_assignment, R.drawable.ic_language, R.drawable.ic_contact_mail, R.drawable.ic_phone_android};
    private Object NetworkUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_presensi);
        ButterKnife.bind(this);

        if (getIntent().getStringExtra("optionPresensi").equals("pegawai")) {
            txtNip.setText(SaveSharedPreference.getNipUser(OptionPresensiActivity.this));
            txtNama.setText(SaveSharedPreference.getNamaUser(OptionPresensiActivity.this));
            isAdmin();
        } else {
            txtNip.setText(SaveSharedPreference.getNipUserLain(OptionPresensiActivity.this));
            txtNama.setText(SaveSharedPreference.getNamaUserLain(OptionPresensiActivity.this));
            btnNotif.setVisibility(View.GONE);
            btnNotif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isConnectedNetwork)
                        startActivity(new Intent(OptionPresensiActivity.this, NotifPegawaiActivity.class));
                    else dialogIndikatorNetwork();
                }
            });
        }

        cekHadirHariIni();

        doTheAutoRefresh();

        MenuAdapter menuAdapter = new MenuAdapter(getApplicationContext(), flags, title, "");
        menu.setAdapter(menuAdapter);
        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (title[position]) {
                    case "Presensi":
                        if (isConnectedNetwork) dialogMenuPresensi();
                        else dialogIndikatorNetwork();
                        break;
                    case "Website":
                        if (isConnectedNetwork) {
                            String url = "http://presensi.damartana.com";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        } else dialogIndikatorNetwork();
                        break;
                    case "Contact Us":
                        startActivity(new Intent(OptionPresensiActivity.this, ContactUsActivity.class));
                        break;
                    case "Tentang":
                        startActivity(new Intent(OptionPresensiActivity.this, TentangActivity.class));
                        break;
                }
            }
        });

    }

    public void isAdmin() {
        if (SaveSharedPreference.getAdmin(OptionPresensiActivity.this).equals("1")) {
            loadBadgeAdmin();
            btnNotif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isConnectedNetwork)
                        startActivity(new Intent(OptionPresensiActivity.this, NotifActivity.class));
                    else dialogIndikatorNetwork();
                }
            });
            isAdmin = true;
        } else {
            loadBadgePegawai();
            btnNotif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isConnectedNetwork)
                        startActivity(new Intent(OptionPresensiActivity.this, NotifPegawaiActivity.class));
                    else dialogIndikatorNetwork();
                }
            });
            isAdmin = false;
        }
    }

    public void dialogMenuPresensi() {
        final Dialog dialog = new Dialog(OptionPresensiActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_menu_presensi);
        final GridView menuPresensi = dialog.findViewById(R.id.menuPresensi);


        if (isAdmin) {
            final String title[] = {"Presensi", "Presensi Luar Kota", "Presensi Team", "Data Presensi", "Data Presensi Kar", "Data Perizinan"};
            int flags[] = {R.drawable.ic_person, R.drawable.ic_flight_takeoff, R.drawable.ic_people_friend, R.drawable.ic_assignment, R.drawable.ic_assignment, R.drawable.ic_assignment};
            MenuAdapter menuAdapter = new MenuAdapter(getApplicationContext(), flags, title, "");
            menuPresensi.setAdapter(menuAdapter);
            menuPresensi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (title[position]) {
                        case "Presensi":
                            presensiLokal();
                            break;
                        case "Presensi Luar Kota":
                            presensiLuarKota();
                            break;
                        case "Presensi Team":
                            cariNip();
                            break;
                        case "Data Presensi":
                            startActivity(new Intent(OptionPresensiActivity.this, DataPresensiActivity.class));
                            break;
                        case "Data Presensi Kar":
                            startActivity(new Intent(OptionPresensiActivity.this, DataPresensiPegawaiActivity.class));
                            break;
                        case "Data Perizinan":
                            startActivity(new Intent(OptionPresensiActivity.this, DataPerizinanLuarKotaActivity.class));
                            break;
                    }
                }
            });
        } else {
            final String title[] = {"Presensi", "Presensi Luar Kota", "Data Presensi"};
            int flags[] = {R.drawable.ic_person, R.drawable.ic_flight_takeoff, R.drawable.ic_assignment};
            MenuAdapter menuAdapter = new MenuAdapter(getApplicationContext(), flags, title, "");
            menuPresensi.setAdapter(menuAdapter);
            menuPresensi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (title[position]) {
                        case "Presensi":
                            presensiLokal();
                            break;
                        case "Presensi Luar Kota":
                            presensiLuarKota();
                            break;
                        case "Data Presensi":
                            startActivity(new Intent(OptionPresensiActivity.this, DataPresensiActivity.class));
                            break;

                    }
                }
            });

        }


        dialog.show();
    }


    @OnClick(R.id.btnRetri)
    public void retriCekKehadiranHariIni() {
        btnRetri.setVisibility(View.GONE);
        cekHadirHariIni();
    }

    public void cariNip() {
        final Dialog dialog = new Dialog(OptionPresensiActivity.this);
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
                            SaveSharedPreference.setIdUserLain(OptionPresensiActivity.this, response.body().getIdUser());
                            SaveSharedPreference.setNipUserLain(OptionPresensiActivity.this, response.body().getNip());
                            SaveSharedPreference.setNamaUserLain(OptionPresensiActivity.this, response.body().getNama());
                            SaveSharedPreference.setPresensiUserLain(OptionPresensiActivity.this, response.body().getPresensi());
                            txtNip.setText(SaveSharedPreference.getNipUserLain(OptionPresensiActivity.this));
                            txtNama.setText(SaveSharedPreference.getNamaUserLain(OptionPresensiActivity.this));

                            if (!response.body().getNip().equals(SaveSharedPreference.getNipUser(OptionPresensiActivity.this))) {
                                Intent intent = new Intent(OptionPresensiActivity.this, OptionPresensiActivity.class);
                                intent.putExtra("optionPresensi", "pegawaiLain");
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(OptionPresensiActivity.this, "ITU ANDA!", Toast.LENGTH_SHORT).show();
                            }


                            dialogLoading.hide();

                        } else {

                            Toast.makeText(OptionPresensiActivity.this, "Pegawai tidak ditemukan!", Toast.LENGTH_SHORT).show();

                            dialogLoading.hide();
                        }
                    }

                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {
                        dialogLoading.hide();
                        Toast.makeText(OptionPresensiActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        dialog.show();
    }

    @OnClick(R.id.btnLocal)
    public void presensiLokal() {
        if (getIntent().getStringExtra("optionPresensi").equals("pegawai")) {
            switch (SaveSharedPreference.getPresensiUser(OptionPresensiActivity.this)) {
                /*case "0":
                    Toast.makeText(OptionPresensiActivity.this, "Maaf, Anda Belum Melakukan Presensi HADIR!", Toast.LENGTH_SHORT).show();
                    break;*/
                /*case "1":
                    Toast.makeText(OptionPresensiActivity.this, "Maaf, Anda Telah Presensi HADIR!", Toast.LENGTH_SHORT).show();
                    break;*/
                case "2":
                    Toast.makeText(OptionPresensiActivity.this, "Maaf, Telah Melakukan Presensi Hari Ini", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    startActivity(new Intent(OptionPresensiActivity.this, MainActivity.class));
                    break;
            }
        } else {
            switch (SaveSharedPreference.getPresensiUserLain(OptionPresensiActivity.this)) {
               /* case "0":
                    Toast.makeText(OptionPresensiActivity.this, "Maaf, Anda Belum Melakukan Presensi HADIR!", Toast.LENGTH_SHORT).show();
                    break;*/
              /*  case "1":
                    Toast.makeText(OptionPresensiActivity.this, "Maaf, Anda Telah Presensi HADIR!", Toast.LENGTH_SHORT).show();
                    break;*/
                case "2":
                    Toast.makeText(OptionPresensiActivity.this, "Maaf, Telah Melakukan Presensi Hari Ini", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    startActivity(new Intent(OptionPresensiActivity.this, MainPegawaiLainActivity.class));
                    break;
            }
        }
    }

    @OnClick(R.id.btnLuarKota)
    public void presensiLuarKota() {
        Intent intent = new Intent(OptionPresensiActivity.this, FotoActivity.class);
        intent.putExtra("shift", "Non-Shift");
        intent.putExtra("waktu", getWaktu());
        intent.putExtra("tanggal", getTanggal());
        if (getIntent().getStringExtra("optionPresensi").equals("pegawai")) {
            intent.putExtra("presensi", "luarKota");
            switch (SaveSharedPreference.getPresensiUser(OptionPresensiActivity.this)) {
           /* case "0":
                Toast.makeText(OptionPresensiActivity.this, "Maaf, Anda Belum Melakukan Presensi HADIR!", Toast.LENGTH_SHORT).show();
                break;*/
           /* case "1":
                Toast.makeText(OptionPresensiActivity.this, "Maaf, Anda Telah Presensi HADIR!", Toast.LENGTH_SHORT).show();
                break;*/
                case "2":
                    Toast.makeText(OptionPresensiActivity.this, "Maaf, Telah Melakukan Presensi Hari Ini", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    startActivity(intent);
                    break;
            }

        } else {
            intent.putExtra("presensi", "luarKotaLain");
            switch (SaveSharedPreference.getPresensiUserLain(OptionPresensiActivity.this)) {
           /* case "0":
                Toast.makeText(OptionPresensiActivity.this, "Maaf, Anda Belum Melakukan Presensi HADIR!", Toast.LENGTH_SHORT).show();
                break;*/
           /* case "1":
                Toast.makeText(OptionPresensiActivity.this, "Maaf, Anda Telah Presensi HADIR!", Toast.LENGTH_SHORT).show();
                break;*/
                case "2":
                    Toast.makeText(OptionPresensiActivity.this, "Maaf, Telah Melakukan Presensi Hari Ini", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    startActivity(intent);
                    break;
            }

        }


    }

    public void cekHadirHariIni() {

        progressBar.setVisibility(View.VISIBLE);
        btnLocal.setEnabled(false);
        btnLuarKota.setEnabled(false);

        UserClient getResponse = ApiConfig.getRetrofit().create(UserClient.class);
        Call<CekPresensiHariIni> call;

        switch (getIntent().getStringExtra("optionPresensi")) {
            case "pegawai":
                call = getResponse.cekPresensiHariIni(SaveSharedPreference.getIdUser(OptionPresensiActivity.this), getTanggal());
                break;
            case "pegawaiLain":
                call = getResponse.cekPresensiHariIni(SaveSharedPreference.getIdUserLain(OptionPresensiActivity.this), getTanggal());
                break;
            default:
                call = getResponse.cekPresensiHariIni(SaveSharedPreference.getIdUser(OptionPresensiActivity.this), getTanggal());
                break;

        }

        call.enqueue(new Callback<CekPresensiHariIni>() {
            @Override
            public void onResponse(Call<CekPresensiHariIni> call, Response<CekPresensiHariIni> response) {
                if (response.body().getValue().equals(1)) {

                    btnLocal.setEnabled(true);
                    btnLuarKota.setEnabled(true);
                    progressBar.setVisibility(View.GONE);

                    txtWaktuHadir.setText("Hadir \n" + response.body().getWaktu());
                    txtWaktuPulang.setText("Pulang \n" + response.body().getWaktuPulang());


                    if (!response.body().getWaktu().equals("-") && !response.body().getWaktuPulang().equals("-")) {
                        if (getIntent().getStringExtra("optionPresensi").equals("pegawai")) {
                            SaveSharedPreference.setPresensiUser(OptionPresensiActivity.this, "2");
                        } else {
                            SaveSharedPreference.setPresensiUserLain(OptionPresensiActivity.this, "2");
                        }

                    } else {
                        if (getIntent().getStringExtra("optionPresensi").equals("pegawai")) {
                            SaveSharedPreference.setPresensiUser(OptionPresensiActivity.this, "1");
                        } else {
                            SaveSharedPreference.setPresensiUserLain(OptionPresensiActivity.this, "1");
                        }
                    }


                } else {
                    btnLocal.setEnabled(true);
                    btnLuarKota.setEnabled(true);
                    progressBar.setVisibility(View.GONE);

                    txtWaktuPulang.setText("");
                    txtWaktuHadir.setText("Anda Belum Melakukan Presensi Hari Ini!");

                    SaveSharedPreference.setPresensiUser(OptionPresensiActivity.this, "0");
                }
            }

            @Override
            public void onFailure(Call<CekPresensiHariIni> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
//                Toast.makeText(OptionPresensiActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void SignalStatus() {
        if (isAvaiableNetwork(OptionPresensiActivity.this)) {
            if (isWifi(OptionPresensiActivity.this)) {
                statusNetwork.setImageResource(R.drawable.ic_wifi);
            } else {
                statusNetwork.setImageResource(R.drawable.ic_compare_arrows);
            }
            if (isOnline()) {
                indicatorNetwork.setImageResource(R.drawable.ic_brightness_green);
                isConnectedNetwork = true;
            } else {
                indicatorNetwork.setImageResource(R.drawable.ic_brightness_blue);
                isConnectedNetwork = false;
            }
        } else {
            statusNetwork.setImageResource(R.drawable.ic_do_not_disturb_alt);
            indicatorNetwork.setImageResource(R.drawable.ic_brightness_red);
            isConnectedNetwork = false;
        }
    }

    public void loadBadgeAdmin() {
        results.clear();
        badge = 0;

        UserClient getResponse = ApiConfig.getRetrofit().create(UserClient.class);
        Call<NotifAdmin> call = getResponse.getListNotifAdmin();

        call.enqueue(new Callback<NotifAdmin>() {
            @Override
            public void onResponse(Call<NotifAdmin> call, Response<NotifAdmin> response) {
                if (response.body().getValue().equals("1")) {
                    results = response.body().getResult();

                    for (int i = 0; i < results.size(); i++) {
                        badge++;
                    }

                    if (badge == 0) {
                        countBadge.setVisibility(View.GONE);
                    } else if (badge >= 0) {
                        countBadge.setVisibility(View.VISIBLE);
                        countBadge.setText(Integer.toString(badge));
                    }
                }
            }

            @Override
            public void onFailure(Call<NotifAdmin> call, Throwable t) {
                //  Toast.makeText(OptionPresensiActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadBadgePegawai() {
        resultsPegawai.clear();
        badge = 0;

        UserClient getResponse = ApiConfig.getRetrofit().create(UserClient.class);
        Call<NotifPegawai> call = getResponse.notifPegawai(SaveSharedPreference.getNipUser(OptionPresensiActivity.this));

        call.enqueue(new Callback<NotifPegawai>() {
            @Override
            public void onResponse(Call<NotifPegawai> call, Response<NotifPegawai> response) {
                if (response.body().getValue().equals("1")) {
                    resultsPegawai = response.body().getResult();

                    for (int i = 0; i < resultsPegawai.size(); i++) {
                        badge++;
                    }

                    if (badge == 0) {
                        countBadge.setVisibility(View.GONE);
                    } else if (badge >= 0) {
                        countBadge.setVisibility(View.VISIBLE);
                        countBadge.setText(Integer.toString(badge));
                    }

                }

            }

            @Override
            public void onFailure(Call<NotifPegawai> call, Throwable t) {
                // Toast.makeText(OptionPresensiActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void dialogIndikatorNetwork() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                OptionPresensiActivity.this);
        alertDialogBuilder.setTitle("Peringatan");
        alertDialogBuilder
                .setMessage("Tunggu lampu indikator berwarna hijau!")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private final Handler handler = new Handler();

    private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getIntent().getStringExtra("optionPresensi").equals("pegawai")) {
                    isAdmin();
                }
                SignalStatus();
                cekHadirHariIni();
                doTheAutoRefresh();
            }
        }, 1000);
    }

//    public void testSpeedConnection(Context context){
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        //should check null because in airplane mode it will be null
//        NetworkCapabilities nc = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
//        }
//        int downSpeed = nc.getLinkDownstreamBandwidthKbps();
//        int upSpeed = nc.getLinkUpstreamBandwidthKbps();
//
//        txtNama.setText(Integer.toString(downSpeed));
//    }

}
