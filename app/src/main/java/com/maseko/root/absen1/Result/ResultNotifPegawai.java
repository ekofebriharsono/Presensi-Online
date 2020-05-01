package com.maseko.root.absen1.Result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultNotifPegawai {

    @SerializedName("id_notif")
    @Expose
    private String idNotif;
    @SerializedName("waktu")
    @Expose
    private String waktu;
    @SerializedName("tanggal")
    @Expose
    private String tanggal;
    @SerializedName("presensi_tanggal")
    @Expose
    private String presensiTanggal;
    @SerializedName("pesan")
    @Expose
    private String pesan;

    public String getIdNotif() {
        return idNotif;
    }

    public void setIdNotif(String idNotif) {
        this.idNotif = idNotif;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getPresensiTanggal() {
        return presensiTanggal;
    }

    public void setPresensiTanggal(String presensiTanggal) {
        this.presensiTanggal = presensiTanggal;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }
}
