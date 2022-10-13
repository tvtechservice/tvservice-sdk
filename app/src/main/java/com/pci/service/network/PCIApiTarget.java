package com.pci.service.network;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import com.pci.beacon.BuildConfig;
import com.pci.service.util.PCILog;


public enum PCIApiTarget {
    PCI_3001_UPLOAD_TERM_AGREEMENTS(3001, "set_terms_agree"),
    PCI_3002_FETCH_POLICY(3002, "get_policy");

    int code;
    @NonNull
    String token;

    PCIApiTarget(int code, @NonNull String token) {
        this.code = code;
        this.token = token;
    }

    @Nullable
    public URL url() {
        try {
            return new URL(String.format(Locale.getDefault(),
                "%s/%s/PCI_%d/%s",
                BuildConfig.API_HOST,
                BuildConfig.API_VERSION,
                code,
                token
            ));
        } catch (MalformedURLException e) {
            PCILog.e(e);
            return null;
        }
    }

    @NonNull
    public PCIApiMethod method() {
        switch (this) {
            case PCI_3001_UPLOAD_TERM_AGREEMENTS:
                return PCIApiMethod.POST;
            case PCI_3002_FETCH_POLICY:
                return PCIApiMethod.GET;
            default:
                return PCIApiMethod.NONE;
        }
    }
}

