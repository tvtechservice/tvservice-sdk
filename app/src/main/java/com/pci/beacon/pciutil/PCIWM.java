package com.pci.beacon.pciutil;

import static com.pci.beacon.PCI.maid;
import static com.pci.beacon.PCI.minor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.ResolvableFuture;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;
import com.pci.beacon.PCIAdvertise;
import com.pci.service.util.PCILog;

import java.util.Timer;
import java.util.TimerTask;



public class PCIWM extends ListenableWorker {
    String TAG = "emarttest";
    String Sub = "KTBeaocnWM2";

    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public PCIWM(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        PCILog.d("PCIWM start");
        PCIAdvertise.getInstance().start(appContext, "start", maid, minor);

    }

    @SuppressLint("RestrictedApi")
    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        ResolvableFuture<Result> mFuture = ResolvableFuture.create();
        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                PCIAdvertise.getInstance().finish();
                Result result = Result.success();
                mFuture.set(result);

                PCILog.d("PCIWM Finish");
            }
        };
        timer.schedule(timerTask,  3 * 60 * 1000);

        return mFuture;
    }

}
