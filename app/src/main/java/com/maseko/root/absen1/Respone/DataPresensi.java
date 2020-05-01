package com.maseko.root.absen1.Respone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.maseko.root.absen1.Result.ResultDataPresensi;

import java.util.List;

public class DataPresensi {

    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("result")
    @Expose
    private List<ResultDataPresensi> result = null;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public List<ResultDataPresensi> getResult() {
        return result;
    }

    public void setResult(List<ResultDataPresensi> result) {
        this.result = result;
    }
}
