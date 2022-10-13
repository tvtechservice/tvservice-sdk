package com.pci.service.redux.reducer;

import androidx.annotation.NonNull;

import com.pci.beacon.C;
import com.pci.service.model.PCIPolicy;
import com.pci.service.network.PCIApi;
import com.pci.service.network.PCINetworkException;
import com.pci.service.redux.action.ActionDowngradeState;
import com.pci.service.redux.core.Action;
import com.pci.service.redux.core.PCIReducer;
import com.pci.service.redux.state.*;
import com.pci.service.redux.state.PCIState.Type;
import com.pci.service.util.PCILog;

import java.security.Policy;

import static com.pci.service.redux.state.PCIState.Type.ACTIVE;
import static com.pci.service.redux.state.PCIState.Type.IDLE;

public class ReducerDowngradeState implements PCIReducer {

    @NonNull
    @Override
    public PCIState reduce(@NonNull PCIState currentState, @NonNull Action action) {

        ActionDowngradeState actionDowngradeState = (ActionDowngradeState) action;
        final Type targetStateType = actionDowngradeState.getStateType();

        switch (targetStateType) {
            case DEFAULT:
                break;
            case IDLE:
                break;
            case ACTIVE:
                break;
            default:
                throw new IllegalStateException("Invalid state type : " + targetStateType);
        }

        PCIState newState = null;


        // Opt out
//        if (currentState.getType().getValue() >= ACTIVE.getValue() && targetStateType.getValue() < ACTIVE.getValue()) {
//            newState = new PCIState2IDLE(currentState);
//
//            try {
//                PCIApi.requestOptOut(newState);
//                newState.setOptIn(false);
//            } catch (PCINetworkException e) {
//                PCILog.e(e);
//                newState.setOptIn(false);
//            }
//        }

        // Disagree terms
        if (currentState.getType().getValue() >= IDLE.getValue() && targetStateType.getValue() < IDLE.getValue()) {
            newState = new PCIState1Default(currentState);
            newState.setTermAgreed(false);

            try {
                PCIApi.requestDisagreeTerms(newState);
                return newState;
            } catch (PCINetworkException e) {
                PCILog.e(e);
                return newState;
            }
        }

        return newState;
    }

}
