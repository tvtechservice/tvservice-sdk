package com.pci.service.redux.reducer;

import android.content.Context;
import androidx.annotation.NonNull;

import com.pci.service.model.PCIPolicy;
import com.pci.service.network.PCIApi;
import com.pci.service.network.PCINetworkException;
import com.pci.service.redux.action.ActionFetchPolicy;

import com.pci.service.redux.core.Action;
import com.pci.service.redux.core.PCIReducer;

import com.pci.service.redux.state.PCIState;
import com.pci.service.util.PCILog;
import com.pci.service.util.PCIStorage;
import com.pci.service.util.PCIStorageKey;


public class ReducerFetchPolicy implements PCIReducer {

    @NonNull
    @Override
    public PCIState reduce(@NonNull PCIState currentState, @NonNull Action action) {

        ActionFetchPolicy actionFetchPolicy = (ActionFetchPolicy) action;

        // Fetch Policy
        try {
            PCIApi.requestFetchPolicy(currentState);
        } catch (PCINetworkException e) {
            PCILog.e(e);
        }

        final Context context = currentState.getContext();
        final PCIPolicy policy = currentState.getPolicy();


        return currentState;
    }
}
