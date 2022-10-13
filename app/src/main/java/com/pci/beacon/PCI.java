package com.pci.beacon;

import static com.pci.beacon.pciutil.PCIPackageUtil.isWorkScheduled;
import static com.pci.beacon.pciutil.PCITime.currentDate;
import static com.pci.beacon.pciutil.PCITime.currentTime;
import static com.pci.beacon.pciutil.PCITime.preDate;
import static com.pci.beacon.pciutil.PCIWork.PCIJob;
import static com.pci.beacon.pciutil.PCIWork.PCIWM_Req;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.work.WorkManager;

import java.io.InputStream;
import java.lang.annotation.Retention;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Timer;
import java.util.TimerTask;

import com.pci.service.redux.action.*;
import com.pci.service.redux.core.Action;
import com.pci.service.redux.core.PCIStore;
import com.pci.service.redux.state.PCIState;
import com.pci.service.util.PCILog;
import com.pci.beacon.pciutil.PCIChiper;
import com.pci.service.util.PCIStorage;


import static java.lang.Thread.sleep;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class PCI {

    @Retention(SOURCE)
    @IntDef({
        SUCCESS,
        READ_PHONE_STATE_PERMISSION_REQUIRED,
        RECORD_AUDIO_PERMISSION_REQUIRED,
        INVALID_PUSH_FORMAT,
        INVALID_ARGUMENTS,
        NO_MATCHED_NOTIFIER_NAME,
        MIC_USE_NOT_AGREED,
    })
    private @interface PCICode {}

    public static final int SUCCESS = 0x00000000;
    public static final int READ_PHONE_STATE_PERMISSION_REQUIRED = 0x00001000;
    public static final int RECORD_AUDIO_PERMISSION_REQUIRED = 0x00001001;
    public static final int INVALID_ARGUMENTS = 0x00002000;
    public static final int INVALID_PUSH_FORMAT = 0x00002001;
    public static final int NO_MATCHED_NOTIFIER_NAME = 0x00002002;
    public static final int MIC_USE_NOT_AGREED = 0x00003000;
    public boolean checkPermission = false;
    public int period = 0;
    public int prePeriod = 0;
    public static String maid = null;
    public static String minor = null;

    /* Internal usage */
    @NonNull public static final String VERSION = BuildConfig.VERSION_NAME;

    @SuppressLint("StaticFieldLeak")
    private static volatile PCI singleton;

    @NonNull
    private Context context;

    private PCI(@NonNull Context context) {
        this.context = context;
    }

    public static PCI with(@Nullable Context context) {
        if (context == null) throw new NullPointerException("Context is null");
        else if (singleton == null) {
            synchronized (PCI.class) {
                if (singleton == null) {
                    singleton = new PCI(context);
                }
            }
        }
        return singleton;
    }

//    @PCICode
//    public int agreeTerms(@Nullable String adid, @Nullable String phoneNumber, boolean isAdidUseAgreed, boolean isAdPushAgreed) {
//        PCILog.d("agreeTerms(%s, %s, %b, %b)", adid, phoneNumber, isAdidUseAgreed, isAdPushAgreed);
//
//        Action actionAgreeTerms = new ActionAgreeTerms(adid, phoneNumber, isAdidUseAgreed, isAdPushAgreed);
//        PCIStore.getInstance(context).dispatch(actionAgreeTerms);
//
//        return PCI.SUCCESS;
//    }



    @PCICode
    public int beaconPlay(String mobile_adid, String partnercode) {
        String cdate = currentDate("yyyyMMdd");
        maid = mobile_adid;
        minor = partnercode;

        if (mobile_adid == null || mobile_adid.isEmpty() || partnercode == null || partnercode.isEmpty())
            return INVALID_ARGUMENTS;

        //partner code에 따른 암호화
        if(partnercode.substring(0,1).equals("2")){
            mobile_adid = PCIChiper.Encrypt(mobile_adid, cdate);
            maid = mobile_adid;
            PCILog.d("MAID Encrypt : " + mobile_adid);
        }

        // pid 여부 , policy 여부
        if (beaconState() == 0 ){
            String phoneNumber = "010-0000-0000";
            boolean isAdidUseAgreed = true;
            boolean isAdPushAgreed = true;

            PCILog.d("beaconPlay(%s, %s, %b, %b)", mobile_adid, phoneNumber, isAdidUseAgreed, isAdPushAgreed);

            Action actionAgreeTerms = new ActionAgreeTerms(mobile_adid, phoneNumber, isAdidUseAgreed, isAdPushAgreed);
            PCIStore.getInstance(context).dispatch(actionAgreeTerms);

        }else if (beaconState() == 1){
            Action actionPolicy = new ActionFetchPolicy();
            PCIStore.getInstance(context).dispatch(actionPolicy);
        }
        if(onCheckPermission(context)) {

            Handler handler = new Handler(Looper.getMainLooper());
            String final_mobile_adid = mobile_adid;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 정책에 맞게....종료, 주기 설정
                    prePeriod = period;
                    try {
                        period = (int) PCIState.from(context).getPolicy().app_installation_result_submit_term() / 60000;
                    }catch (Exception e){
                        period = 15;
                    }
                    if (period == 0) {
                        // 종료
                        WorkManager.getInstance(context).cancelUniqueWork(PCIJob);
                        PCIAdvertise.getInstance().finish();
                        PCILog.d("Cancel PCI Work !!");

                    } else {
                        boolean wm_State;
                        wm_State = isWorkScheduled(context, PCIJob);
                        if (!wm_State) {
                            PCIWM_Req(context, period);
                            PCILog.d("PCI Work Request!!");
                        } else {
                            if (prePeriod != period) {
                                WorkManager.getInstance(context).cancelUniqueWork(PCIJob);
                                try {
                                    sleep(2 * 1000);
                                    PCIWM_Req(context, period);
                                    PCILog.d("PCI Work Period Value Changed !!");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                PCILog.d("PCI Work Period Value is Same !!");
                            }
                        }
                    }
                }
            }, 2 * 1000);
        }else{
            PCILog.d("Bluetooth Permission Check, Please!! ");
        }
        return PCI.SUCCESS;
    }

    public int beaconEnd() {

        Action actionDisagreeTerms = new ActionDisagreeTerms();
        PCIStore.getInstance(context).dispatch(actionDisagreeTerms);

        return PCI.SUCCESS;
    }


    public void beaconStart(String mobile_adid, String parterCode) {   //일회성 비콘
        PCILog.d("beaconStart !!");
        String cdate = currentDate("yyyyMMdd");

        String tAdid = mobile_adid;
        String pCode = parterCode;

        if(pCode.substring(0,1).equals("2")){   //partner code에 따른 암호화
            tAdid = PCIChiper.Encrypt(tAdid, cdate);
        }

        if(onCheckPermission(context)) {
            if (!PCIAdvertise.getInstance().isStarted()) {
                try {
                    if (checkPermission) {
                        PCIAdvertise.getInstance().start(context, "start", tAdid, pCode);
                    } else {
                        PCILog.d("This devices is not supported. ( Android 12+ - Bluetooth Advertise Permission )");
                    }
                } catch (Exception e) {
                    PCILog.d("Beacon Advertising error!!");
                }
                Timer timer = new Timer(true);
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        beaconStop();
                    }
                };
                timer.schedule(timerTask, 3 * 60 * 1000);
            } else {
                PCILog.d("Already Beacon Advertising ... ");
            }
        }
    }

    public void beaconStop() {
        try {
            PCIAdvertise.getInstance().finish();
            PCILog.d(" Beacon Advertising Stop !!" );
        }catch (Exception e){ PCILog.d("Beacon Advertising Stop error!!"); }
    }

    public boolean onCheckPermission(Context context){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
                //ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.BLUETOOTH_ADVERTISE}, PERMISSIONS_REQUEST);
                PCILog.d("Bluetooth Permission Check, Please!!" );
                checkPermission = false;
                return false;
            } else {
                PCILog.d("Bluetooth Permission is OK!");
                checkPermission = true;
                return true;
            }
        }else{
            if ((ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) ){
                PCILog.d("Need to Bluetooth Permission!" );
                checkPermission = false;
                return false;
            } else{
                PCILog.d("Bluetooth Permission is OK!");
                checkPermission = true;
                return true;
            }
        }
    }


    public int beaconState() {
        int case_code = 0;
        if(PCIState.from(context).getPid() == null || PCIState.from(context).getPid().isEmpty() && PCIState.from(context).getPolicy() == null){
            case_code = 0;  // pid, policy is null
        }else if(PCIState.from(context).getPid() != null){
            case_code = 1; // pid is not null
        }else {
            // ...
        }

        return case_code;
    }


    @NonNull
    public static String stringOf(@PCICode int code) {
        switch (code) {
            case SUCCESS:
                return "Success";
            case READ_PHONE_STATE_PERMISSION_REQUIRED:
                return "READ_PHONE_STATE Permission Required";
            case RECORD_AUDIO_PERMISSION_REQUIRED:
                return "RECORD_AUDIO Permission Required";
            case INVALID_ARGUMENTS:
                return "Invalid Arguments";
            case INVALID_PUSH_FORMAT:
                return "Invalid Push Format";
            case MIC_USE_NOT_AGREED:
                return "Microphone use not agreed";
            default:
                return "";
        }
    }
}

