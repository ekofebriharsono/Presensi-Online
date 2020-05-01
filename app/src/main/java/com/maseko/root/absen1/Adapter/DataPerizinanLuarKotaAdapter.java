package com.maseko.root.absen1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maseko.root.absen1.R;
import com.maseko.root.absen1.Result.ResultDataPerizinanLuarKota;
import com.maseko.root.absen1.Result.ResultDataPresensi;

import java.util.List;

public class DataPerizinanLuarKotaAdapter extends RecyclerView.Adapter<DataPerizinanLuarKotaAdapter.ViewHolder> {
    private Context context;
    private List<ResultDataPerizinanLuarKota> results;

    public DataPerizinanLuarKotaAdapter(Context context, List<ResultDataPerizinanLuarKota> results) {
        this.context = context;
        this.results = results;
    }


    @Override
    public DataPerizinanLuarKotaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_perizinan, parent, false);
        DataPerizinanLuarKotaAdapter.ViewHolder holder = new DataPerizinanLuarKotaAdapter.ViewHolder(v);

        return holder;
    }


    @Override
    public void onBindViewHolder(final DataPerizinanLuarKotaAdapter.ViewHolder holder, int position) {
        final ResultDataPerizinanLuarKota result = results.get(position);

        holder.tanggal.setText(result.getTanggal());
        holder.hadir.setText(result.getLokasi());
        holder.pulang.setText(result.getLokasiPulang());
        holder.nama.setText(result.getNama());

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tanggal, hadir, pulang, nama;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tanggal = itemView.findViewById(R.id.tanggal);
            hadir = itemView.findViewById(R.id.hadir);
            pulang = itemView.findViewById(R.id.pulang);
            nama = itemView.findViewById(R.id.nama);

        }

        @Override
        public void onClick(View view) {

        }

    }
}
