package com.maseko.root.absen1.Respone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.maseko.root.absen1.Result.ResultListKaryawan;

import java.util.List;

public class ListKaryawan {

    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("result")
    @Expose
    private List<ResultListKaryawan> result = null;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public List<ResultListKaryawan> getResult() {
        return result;
    }

    public void setResult(List<ResultListKaryawan> result) {
        this.result = result;
    }

}
