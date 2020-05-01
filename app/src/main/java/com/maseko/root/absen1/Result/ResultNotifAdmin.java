package com.maseko.root.absen1.Result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultNotifAdmin {

    @SerializedName("id_presensi")
    @Expose
    private String idPresensi;
    @SerializedName("tanggal")
    @Expose
    private String tanggal;
    @SerializedName("lokasi")
    @Expose
    private String lokasi;
    @SerializedName("link_foto")
    @Expose
    private String linkFoto;
    @SerializedName("lokasi_pulang")
    @Expose
    private String lokasiPulang;
    @SerializedName("link_foto_pulang")
    @Expose
    private String linkFotoPulang;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("nip")
    @Expose
    private String nip;
    @SerializedName("token")
    @Expose
    private String token;

    public String getIdPresensi() {
        return idPresensi;
    }

    public void setIdPresensi(String idPresensi) {
        this.idPresensi = idPresensi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getLinkFoto() {
        return linkFoto;
    }

    public void setLinkFoto(String linkFoto) {
        this.linkFoto = linkFoto;
    }

    public String getLokasiPulang() {
        return lokasiPulang;
    }

    public void setLokasiPulang(String lokasiPulang) {
        this.lokasiPulang = lokasiPulang;
    }

    public String getLinkFotoPulang() {
        return linkFotoPulang;
    }

    public void setLinkFotoPulang(String linkFotoPulang) {
        this.linkFotoPulang = linkFotoPulang;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
