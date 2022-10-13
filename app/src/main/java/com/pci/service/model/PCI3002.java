package com.pci.service.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import com.google.gson.annotations.SerializedName;
import com.pci.service.network.PCIPayload;
import com.pci.service.redux.state.PCIState;

public class PCI3002 {

    public static class Request extends PCIPayload {

        @VisibleForTesting @SerializedName("p_id") String p_id;

        public Request p_id(String p_id) {
            this.p_id = p_id;
            return this;
        }
    }

    public static class Response extends PCIPayload {

        @VisibleForTesting @SerializedName("expired_datetime") String expired_datetime;
        @VisibleForTesting @SerializedName("checkin_submit_cycle") int checkin_submit_cycle;
        @VisibleForTesting @SerializedName("checkout_check_maxcount") int checkout_check_maxcount;
        @VisibleForTesting @SerializedName("app_installation_info_submit_cycle") int app_installation_info_submit_cycle;
        @VisibleForTesting @SerializedName("app_installation_info_submit_time") String app_installation_info_submit_time;
        @VisibleForTesting @SerializedName("app_installation_result_submit_term") int app_installation_result_submit_term;
        @VisibleForTesting @SerializedName("kill_switch") String kill_switch;

        public String expired_datetime() {
            return expired_datetime;
        }

        public void expired_datetime(String expired_datetime) {
            this.expired_datetime = expired_datetime;
        }

        public int checkin_submit_cycle() {
            return checkin_submit_cycle;
        }

        public void checkin_submit_cycle(int checkin_submit_cycle) {
            this.checkin_submit_cycle = checkin_submit_cycle;
        }

        public int checkout_check_maxcount() {
            return checkout_check_maxcount;
        }

        public void checkout_check_maxcount(int checkout_check_maxcount) {
            this.checkout_check_maxcount = checkout_check_maxcount;
        }

        public int app_installation_info_submit_cycle() {
            return app_installation_info_submit_cycle;
        }

        public void app_installation_info_submit_cycle(int app_installation_info_submit_cycle) {
            this.app_installation_info_submit_cycle = app_installation_info_submit_cycle;
        }

        public String app_installation_info_submit_time() {
            return app_installation_info_submit_time;
        }

        public void app_installation_info_submit_time(String app_installation_info_submit_time) {
            this.app_installation_info_submit_time = app_installation_info_submit_time;
        }

        public int app_installation_result_submit_term() {
            return app_installation_result_submit_term;
        }

        public void app_installation_result_submit_term(int app_installation_result_submit_term) {
            this.app_installation_result_submit_term = app_installation_result_submit_term;
        }

        public String kill_switch() {
            return kill_switch;
        }

        public void kill_switch(String kill_switch) {
            this.kill_switch = kill_switch;
        }



        @NonNull
        public PCIPolicy toPolicy() {
            PCIPolicy policy = new PCIPolicy();
            policy.expired_datetime = this.expired_datetime;
            policy.checkin_submit_cycle = this.checkin_submit_cycle;
            policy.checkout_check_maxcount = this.checkout_check_maxcount;
            policy.app_installation_info_submit_cycle = this.app_installation_info_submit_cycle;
            policy.app_installation_info_submit_time = this.app_installation_info_submit_time;
            policy.app_installation_result_submit_term = this.app_installation_result_submit_term;
            policy.kill_switch = this.kill_switch;

            return policy;
        }


    }
}



