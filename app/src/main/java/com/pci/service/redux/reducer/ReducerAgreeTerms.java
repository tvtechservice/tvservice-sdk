package com.pci.service.redux.reducer;

import androidx.annotation.NonNull;

import com.pci.service.network.PCIApi;
import com.pci.service.network.PCINetworkException;
import com.pci.service.redux.action.ActionAgreeTerms;
import com.pci.service.redux.core.Action;
import com.pci.service.redux.core.PCIReducer;
import com.pci.service.redux.state.PCIState;
import com.pci.service.redux.state.PCIState2Idle;
import com.pci.service.util.PCILog;

public class ReducerAgreeTerms implements PCIReducer {

    @NonNull
    @Override
    public PCIState reduce(@NonNull PCIState currentState, @NonNull Action action) {

        ActionAgreeTerms actionAgreeTerms = (ActionAgreeTerms) action;

        PCIState2Idle newState = new PCIState2Idle(currentState);
        newState.setTermAgreed(true);
        newState.setAdid(actionAgreeTerms.getAdid());
        newState.setPhoneNumber(actionAgreeTerms.getPhoneNumber());
        newState.setAdPushAgreed(actionAgreeTerms.isAdPushAgreed());
        newState.setAdidUseAgreed(actionAgreeTerms.isAdidUseAgreed());

        // Report AgreeTerms to PCI Server
        try {
            PCIApi.requestAgreeTerms(newState);
        } catch (PCINetworkException e) {
            PCILog.e(e);
            return currentState;
        }

        return newState;
    }
}
