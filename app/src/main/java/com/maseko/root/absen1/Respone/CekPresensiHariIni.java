package com.maseko.root.absen1.Respone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CekPresensiHariIni {

    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("waktu")
    @Expose
    private String waktu;
    @SerializedName("lokasi")
    @Expose
    private String lokasi;
    @SerializedName("waktu_pulang")
    @Expose
    private String waktuPulang;
    @SerializedName("lokasi_pulang")
    @Expose
    private String lokasiPulang;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getWaktuPulang() {
        return waktuPulang;
    }

    public void setWaktuPulang(String waktuPulang) {
        this.waktuPulang = waktuPulang;
    }

    public String getLokasiPulang() {
        return lokasiPulang;
    }

    public void setLokasiPulang(String lokasiPulang) {
        this.lokasiPulang = lokasiPulang;
    }
}
