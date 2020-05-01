package com.maseko.root.absen1.Respone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.maseko.root.absen1.Result.ResultNotifPegawai;

import java.util.List;

public class NotifPegawai {
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("result")
    @Expose
    private List<ResultNotifPegawai> result = null;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<ResultNotifPegawai> getResult() {
        return result;
    }

    public void setResult(List<ResultNotifPegawai> result) {
        this.result = result;
    }
}
