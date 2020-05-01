package com.maseko.root.absen1.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.RecyclerView;

import com.maseko.root.absen1.Api.UserClient;
import com.maseko.root.absen1.Config.ApiConfig;
import com.maseko.root.absen1.Notification.Client;
import com.maseko.root.absen1.Notification.Data;
import com.maseko.root.absen1.Notification.MyResponse;
import com.maseko.root.absen1.Notification.Sender;
import com.maseko.root.absen1.R;
import com.maseko.root.absen1.Respone.Presensi;
import com.maseko.root.absen1.Result.ResultNotifAdmin;
import com.maseko.root.absen1.SharePreference.SaveSharedPreference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.maseko.root.absen1.ScanQrCode.getTanggal;
import static com.maseko.root.absen1.ScanQrCode.getWaktu;

public class AccForAdminAdapter extends RecyclerView.Adapter<AccForAdminAdapter.ViewHolder> {
    private Context context;
    private List<ResultNotifAdmin> results;
    UserClient apiService;

    // NumberFormat formatterMoney = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));

    public AccForAdminAdapter(Context context, List<ResultNotifAdmin> results) {
        this.context = context;
        this.results = results;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notif_admin, parent, false);
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ResultNotifAdmin result = results.get(position);

        holder.nama.setText(result.getNama());
        holder.nip.setText(result.getNip());

        final String id = result.getIdPresensi();
        final String presensi_tanggal = result.getTanggal();
        final String nip = result.getNip();
        final String token = result.getNToken();

        Picasso.with(context)
                .load(result.getLinkFoto())
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_img_error)
                .into(holder.imgFoto);

       /* Picasso.with(context)
                .load(result.getLinkFoto())
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_img_error)
                .into(holder.img_preview);*/

       /* holder.imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.dialogTutorial.show();
            }
        });*/

        holder.btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btnTerima.setEnabled(false);
                holder.btnTolak.setEnabled(false);
                final ProgressDialog dialog = new ProgressDialog(context);
                dialog.setMessage("Loading ...");
                dialog.setCancelable(false);
                dialog.show();

                UserClient getResponse = ApiConfig.getRetrofit().create(UserClient.class);
                Call<Presensi> call = getResponse.accAdmin(id, "terima", "hadir", nip, getWaktu(), getTanggal(), presensi_tanggal, "Presensi anda diterima!");

                call.enqueue(new Callback<Presensi>() {
                    @Override
                    public void onResponse(Call<Presensi> call, final Response<Presensi> response) {
                        if (response.body().getValue().equals(1)) {
                            dialog.hide();
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            // loadNotifAdmin();
                            holder.btnTerima.setText("Di Terima");
                            holder.btnTolak.setVisibility(View.GONE);
                            holder.Notification(SaveSharedPreference.getNamaUser(context)+": Presensi anda diterima!","Presensi Diterima", SaveSharedPreference.getNamaUser(context),token);

                        } else {
                            dialog.hide();
                            Toast.makeText(context, "Try it", Toast.LENGTH_SHORT).show();
                            holder.btnTerima.setEnabled(true);
                            holder.btnTolak.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<Presensi> call, Throwable t) {
                        Toast.makeText(context, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        holder.btnTerima.setEnabled(true);
                        holder.btnTolak.setEnabled(true);
                    }
                });

            }
        });

        holder.btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btnTerima.setEnabled(false);
                holder.btnTolak.setEnabled(false);
                final ProgressDialog dialog = new ProgressDialog(context);
                dialog.setMessage("Loading ...");
                dialog.setCancelable(false);
                dialog.show();

                UserClient getResponse = ApiConfig.getRetrofit().create(UserClient.class);
                Call<Presensi> call = getResponse.accAdmin(id, "tolak", "hadir", nip, getWaktu(), getTanggal(), presensi_tanggal, "Presensi anda ditolak!");

                call.enqueue(new Callback<Presensi>() {
                    @Override
                    public void onResponse(Call<Presensi> call, final Response<Presensi> response) {
                        if (response.body().getValue().equals(1)) {
                            dialog.hide();
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            // loadNotifAdmin();
                            holder.btnTolak.setText("Di Tolak");
                            holder.btnTerima.setVisibility(View.GONE);

                            holder.Notification(SaveSharedPreference.getNamaUser(context)+": Presensi anda ditolak!","Presensi Ditolak", SaveSharedPreference.getNamaUser(context),token);


                        } else {
                            dialog.hide();
                            Toast.makeText(context, "Try it", Toast.LENGTH_SHORT).show();
                            holder.btnTerima.setEnabled(true);
                            holder.btnTolak.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<Presensi> call, Throwable t) {
                        Toast.makeText(context, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        holder.btnTerima.setEnabled(true);
                        holder.btnTolak.setEnabled(true);
                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nama, nip;
        private ImageView imgFoto;
        private Button btnTerima, btnTolak;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            nama = itemView.findViewById(R.id.txtNama);
            nip = itemView.findViewById(R.id.txtNip);
            btnTerima = itemView.findViewById(R.id.btnTerima);
            btnTolak = itemView.findViewById(R.id.btnTolak);
            imgFoto = itemView.findViewById(R.id.imgFoto);


           /* dialogTutorial = new Dialog(context);
            dialogTutorial.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogTutorial.setContentView(R.layout.layout_preview_image);

            img_preview = dialogTutorial.findViewById(R.id.img_preview);
            nama_preview = dialogTutorial.findViewById(R.id.nama_preview);*/

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
                                    Toast.makeText(context, "Tidak Mengirim Notifikasi!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Toast.makeText(context, "check your internet connection!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        @Override
        public void onClick(View view) {

        }


    }

    public void updateList(List<ResultNotifAdmin> newList) {

        results = new ArrayList<>();
        results.addAll(newList);
        notifyDataSetChanged();

    }
}
