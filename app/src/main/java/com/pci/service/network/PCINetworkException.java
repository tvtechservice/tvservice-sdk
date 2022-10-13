package com.pci.service.network;

import java.io.IOException;

public class PCINetworkException extends IOException {
    private int code;

    PCINetworkException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
