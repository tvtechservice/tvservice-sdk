package com.pci.service.redux.reducer;

import android.content.Context;
import androidx.annotation.NonNull;

import com.pci.service.redux.action.ActionDisagreeTerms;
import com.pci.service.redux.action.ActionDowngradeState;
import com.pci.service.redux.core.Action;
import com.pci.service.redux.core.PCIReducer;
import com.pci.service.redux.core.PCIStore;
import com.pci.service.redux.state.PCIState;

public class ReducerDisagreeTerms implements PCIReducer {

    @NonNull
    @Override
    public PCIState reduce(@NonNull PCIState currentState, @NonNull Action action) {

        ActionDisagreeTerms actionDisagreeTerms = (ActionDisagreeTerms) action;
        final Context context = currentState.getContext();

        PCIStore.getInstance(context).dispatchTriggeredByInternalRedux(new ActionDowngradeState(PCIState.Type.DEFAULT));

        return currentState;
    }

}
