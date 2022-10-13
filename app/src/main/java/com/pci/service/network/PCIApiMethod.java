package com.pci.service.network;

import androidx.annotation.NonNull;


public enum PCIApiMethod {
    GET("GET"),
    POST("POST"),
    NONE(""),;

    @NonNull
    String value;

    PCIApiMethod(@NonNull String value) {
        this.value = value;
    }
}
