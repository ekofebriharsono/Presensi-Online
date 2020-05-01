package com.maseko.root.absen1.Respone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.maseko.root.absen1.Result.ResultDataPerizinanLuarKota;

import java.util.List;

public class DataPrizinanLuarKota {


    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("result")
    @Expose
    private List<ResultDataPerizinanLuarKota> result = null;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public List<ResultDataPerizinanLuarKota> getResult() {
        return result;
    }

    public void setResult(List<ResultDataPerizinanLuarKota> result) {
        this.result = result;
    }
}
