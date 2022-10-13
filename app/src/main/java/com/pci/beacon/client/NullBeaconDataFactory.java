package com.pci.beacon.client;

import android.os.Handler;

import com.pci.beacon.Beacon;
import com.pci.beacon.BeaconDataNotifier;

public class NullBeaconDataFactory implements BeaconDataFactory {

    @Override
    public void requestBeaconData(Beacon beacon, final BeaconDataNotifier notifier) {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                notifier.beaconDataUpdate(null, null, new DataProviderException("You need to configure a beacon data service to use this feature."));
            }
        });
    }
}

