package com.pci.service.network;

import com.pci.beacon.C;
import com.pci.beacon.PCI;
import com.pci.service.model.*;
import com.pci.service.redux.state.PCIState;

import static com.pci.service.network.PCIApiTarget.*;

@SuppressWarnings("UnnecessaryReturnStatement")
public class PCIApi implements C {

    public static void requestAgreeTerms(final PCIState state) throws PCINetworkException {
        PCI3001.Request requestData = new PCI3001.Request()
            .agree(true)
//            .uuid(state.getUuid())
            .uuid(state.getAdid())
            .mobile_num(state.getPhoneNumber())
            .package_name(state.getPackageName())
            .adid_use(state.isAdidUseAgreed())
            .ad_push(state.isAdPushAgreed())
            .mac(state.getMac())
            .otm_suid(state.getOtmSuid())
            .package_key(state.getPackageKey())
            .mic_use(false)
            .ble_use(true);

        PCIApiResponse<PCI3001.Response> response = PCIApiCommander.getInstance().send(
            new PCIApiRequest(PCI_3001_UPLOAD_TERM_AGREEMENTS, requestData,state), PCI3001.Response.class
        );

        if (response.isSuccessful()) {
            if (response.getData() != null && response.getData().p_id() != null) {
                state.setPid(response.getData().p_id());
            } else throw new PCINetworkException("No pid", response.getCode());
        } else throw new PCINetworkException("Unsuccessful API Request", response.getCode());
    }

    public static void requestDisagreeTerms(final PCIState state) throws PCINetworkException {
        PCI3001.Request requestData = new PCI3001.Request()
            .agree(false)
//            .uuid(state.getUuid())
            .uuid(state.getAdid())
            .mobile_num(state.getPhoneNumber())
            .package_name(state.getPackageName())
            .adid_use(state.isAdidUseAgreed())
            .ad_push(state.isAdPushAgreed())
            .mac(state.getMac())
            .otm_suid(state.getOtmSuid())
            .package_key(state.getPackageKey())
            .mic_use(true)
            .ble_use(false);

        PCIApiResponse<PCI3001.Response> response = PCIApiCommander.getInstance().send(
            new PCIApiRequest(PCI_3001_UPLOAD_TERM_AGREEMENTS, requestData, state),
            PCI3001.Response.class
        );

        if (response.isSuccessful()) {
            if (response.getData() != null && response.getData().p_id() != null) {
                state.setPid(response.getData().p_id());
            } else throw new PCINetworkException("No PID", response.getCode());
        } else throw new PCINetworkException("Unsuccessful API Request", response.getCode());
    }


    public static void requestFetchPolicy(final PCIState state) throws PCINetworkException {
        PCI3002.Request requestData = new PCI3002.Request()
            .p_id(state.getPid());

        PCIApiResponse<PCI3002.Response> response = PCIApiCommander.getInstance().send(
            new PCIApiRequest(PCIApiTarget.PCI_3002_FETCH_POLICY, requestData, state),
            PCI3002.Response.class
        );

        if (response.isSuccessful()) {
            if (response.getData() != null) {
                final PCIPolicy policy = response.getData().toPolicy();
                state.setPolicy(policy);
            } else throw new PCINetworkException("No Data", response.getCode());
        } else throw new PCINetworkException("Unsuccessful API Request", response.getCode());
    }



}
