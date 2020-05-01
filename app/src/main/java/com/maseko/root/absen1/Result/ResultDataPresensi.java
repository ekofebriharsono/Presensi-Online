package com.maseko.root.absen1.Result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultDataPresensi {

    @SerializedName("tanggal")
    @Expose
    private String tanggal;
    @SerializedName("hadir")
    @Expose
    private String hadir;
    @SerializedName("pulang")
    @Expose
    private String pulang;
    @SerializedName("jam_kerja")
    @Expose
    private String jamKerja;

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getHadir() {
        return hadir;
    }

    public void setHadir(String hadir) {
        this.hadir = hadir;
    }

    public String getPulang() {
        return pulang;
    }

    public void setPulang(String pulang) {
        this.pulang = pulang;
    }

    public String getJamKerja() {
        return jamKerja;
    }

    public void setJamKerja(String jamKerja) {
        this.jamKerja = jamKerja;
    }

}
