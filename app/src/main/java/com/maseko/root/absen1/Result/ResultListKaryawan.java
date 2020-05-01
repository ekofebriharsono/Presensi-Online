package com.maseko.root.absen1.Result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultListKaryawan {

    @SerializedName("id_user")
    @Expose
    private String idUser;
    @SerializedName("nip")
    @Expose
    private String nip;
    @SerializedName("nama")
    @Expose
    private String nama;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
