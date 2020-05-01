package com.maseko.root.absen1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maseko.root.absen1.R;
import com.maseko.root.absen1.Result.ResultNotifPegawai;

import java.util.List;

public class NotifForPegawaiAdapter extends RecyclerView.Adapter<NotifForPegawaiAdapter.ViewHolder> {
    private Context context;
    private List<ResultNotifPegawai> results;

    public NotifForPegawaiAdapter(Context context, List<ResultNotifPegawai> results) {
        this.context = context;
        this.results = results;
    }


    @Override
    public NotifForPegawaiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notif_pegawai, parent, false);
        NotifForPegawaiAdapter.ViewHolder holder = new NotifForPegawaiAdapter.ViewHolder(v);

        return holder;
    }


    @Override
    public void onBindViewHolder(final NotifForPegawaiAdapter.ViewHolder holder, int position) {
        final ResultNotifPegawai result = results.get(position);

        holder.tanggal.setText("Presensi Tanggal " + result.getPresensiTanggal());
        holder.info.setText(result.getPesan());

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView info, tanggal;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            info = itemView.findViewById(R.id.txtInfo);
            tanggal = itemView.findViewById(R.id.txtTanggal);

        }

        @Override
        public void onClick(View view) {

        }

    }
}
