package com.pci.service.network;

import androidx.annotation.Nullable;
import com.pci.beacon.C;

public class PCIApiResponse<DATA> implements C {

    private int code;
    private String message;
    private DATA data;

    PCIApiResponse() {}

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

    @Nullable
    public DATA getData() {
        return data;
    }

    public void setData(DATA data) {
        this.data = data;
    }
}
