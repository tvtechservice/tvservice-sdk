package com.pci.service.network;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;
import com.pci.beacon.C;

public class PCIApiResponseHolder implements C {

    @SerializedName("res_code") private int code;
    @SerializedName("res_msg") private String message;
    @SerializedName("data") private Map<String, Object> data = new HashMap<>();

    public PCIApiResponseHolder() {}

    public boolean isSuccessful() {
        return code / 100 == 2;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public <T> T getData(final Class<T> responseDataClass) {
        if (data != null) {
            return gson.fromJson(gson.toJson(data), responseDataClass);
        } else {
            return null;
        }
    }
}
