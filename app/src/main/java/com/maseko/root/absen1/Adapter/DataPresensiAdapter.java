package com.maseko.root.absen1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maseko.root.absen1.R;
import com.maseko.root.absen1.Result.ResultDataPresensi;

import java.util.List;

public class DataPresensiAdapter extends RecyclerView.Adapter<DataPresensiAdapter.ViewHolder> {
    private Context context;
    private List<ResultDataPresensi> results;

    public DataPresensiAdapter(Context context, List<ResultDataPresensi> results) {
        this.context = context;
        this.results = results;
    }


    @Override
    public DataPresensiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_presensi, parent, false);
        DataPresensiAdapter.ViewHolder holder = new DataPresensiAdapter.ViewHolder(v);

        return holder;
    }


    @Override
    public void onBindViewHolder(final DataPresensiAdapter.ViewHolder holder, int position) {
        final ResultDataPresensi result = results.get(position);

        holder.tanggal.setText(result.getTanggal());
        holder.hadir.setText(result.getHadir());
        holder.pulang.setText(result.getPulang());
        holder.jamKerja.setText(result.getJamKerja());

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tanggal, hadir, pulang, jamKerja;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tanggal = itemView.findViewById(R.id.tanggal);
            hadir = itemView.findViewById(R.id.hadir);
            pulang = itemView.findViewById(R.id.pulang);
            jamKerja = itemView.findViewById(R.id.jamKerja);

        }

        @Override
        public void onClick(View view) {

        }

    }
}
