package com.maseko.root.absen1.Respone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.maseko.root.absen1.Result.ResultTokenAdmin;

import java.util.List;

public class ListAdmin {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("result")
    @Expose
    private List<ResultTokenAdmin> result = null;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<ResultTokenAdmin> getResult() {
        return result;
    }

    public void setResult(List<ResultTokenAdmin> result) {
        this.result = result;
    }

}
