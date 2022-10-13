package com.pci.beacon.pciutil;

import android.content.Context;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.pci.beacon.pciutil.PCIWM;

import java.util.concurrent.TimeUnit;

public class PCIWork {
    public static String PCIJob= "BeaconWM";

    public static void PCIWM_Req(Context wkContext , int playPeriod){
        PeriodicWorkRequest pciwm_req = new PeriodicWorkRequest.Builder(PCIWM.class, playPeriod, TimeUnit.MINUTES).addTag(PCIJob).build();
        WorkManager.getInstance(wkContext).enqueueUniquePeriodicWork(PCIJob, ExistingPeriodicWorkPolicy.REPLACE, pciwm_req);
    }
}
