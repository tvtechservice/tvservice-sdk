package com.pci.service.model;


import android.util.Log;

import java.util.Date;

import androidx.annotation.VisibleForTesting;
import com.google.gson.annotations.SerializedName;
import com.pci.service.network.PCIPayload;

import com.pci.service.util.PCIFormatter;
import com.pci.service.util.PCILog;
import com.pci.beacon.C;

public class PCIPolicy extends PCIPayload implements C {

    @VisibleForTesting @SerializedName("expired_datetime") String expired_datetime;
    @VisibleForTesting @SerializedName("checkin_submit_cycle") int checkin_submit_cycle;
    @VisibleForTesting @SerializedName("checkout_check_maxcount") int checkout_check_maxcount;
    @VisibleForTesting @SerializedName("app_installation_info_submit_cycle") int app_installation_info_submit_cycle;
    @VisibleForTesting @SerializedName("app_installation_info_submit_time") String app_installation_info_submit_time;
    @VisibleForTesting @SerializedName("app_installation_result_submit_term") int app_installation_result_submit_term;
    @VisibleForTesting @SerializedName("kill_switch") String kill_switch;

    public PCIPolicy() {}

    public Date expired_datetime() {
        return PCIFormatter.dateFromYyyymmddhhmiss(expired_datetime);
    }

    public long checkin_submit_cycle() {
        try {
            return checkin_submit_cycle * MINUTE_MS;
        } catch (NumberFormatException e) {
            PCILog.e(e);
            return 60 * MINUTE_MS; // Todo : determine default value
        }
    }

    public int checkout_check_maxcount() {
        try {
            return checkout_check_maxcount;
        } catch (NumberFormatException e) {
            PCILog.e(e);
            return 3; // Todo : determine default value
        }
    }

    public long app_installation_info_submit_cycle() {
        try {
            return app_installation_info_submit_cycle * WEEK_MS;
        } catch (NumberFormatException e) {
            PCILog.e(e);
            return 26 * WEEK_MS; // Todo : determine default value
        }
    }

    public String app_installation_info_submit_time() {
        return app_installation_info_submit_time; // HHmmss
    }

    public long app_installation_result_submit_term() {
        try {
            return app_installation_result_submit_term * MINUTE_MS;
        } catch (NumberFormatException e) {
            PCILog.e(e);
            return 20 * MINUTE_MS; // Todo : determine default value
        }
    }
    public String kill_switch() {
        return kill_switch;
    }

}
