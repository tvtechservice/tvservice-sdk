package com.pci.beacon.startup;

import android.content.Context;

import com.pci.beacon.MonitorNotifier;

public interface BootstrapNotifier extends MonitorNotifier {
    public Context getApplicationContext();
}
