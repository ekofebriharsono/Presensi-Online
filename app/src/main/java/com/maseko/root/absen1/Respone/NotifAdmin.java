package com.maseko.root.absen1.Respone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.maseko.root.absen1.Result.ResultNotifAdmin;

import java.util.List;

public class NotifAdmin {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("result")
    @Expose
    private List<ResultNotifAdmin> result = null;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<ResultNotifAdmin> getResult() {
        return result;
    }

    public void setResult(List<ResultNotifAdmin> result) {
        this.result = result;
    }
}
