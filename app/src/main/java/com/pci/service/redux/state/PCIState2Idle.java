package com.pci.service.redux.state;

import androidx.annotation.NonNull;

import com.pci.service.redux.action.ActionDowngradeState;
import com.pci.service.redux.action.ActionFetchPolicy;
import com.pci.service.redux.core.PCIStore;

import static com.pci.service.redux.state.PCIState.Type.IDLE;


public class PCIState2Idle extends PCIState {

    public PCIState2Idle(PCIState oldState) {
        super(oldState);
        this.type = IDLE;
    }

    @Override
    public void onKeep() {

        if (this.maxType().getValue() < this.type.getValue()) {
            PCIStore.getInstance(context).dispatchTriggeredByInternalRedux(new ActionDowngradeState(this.maxType()));
        }
    }

    @Override
    public void onEnter(@NonNull PCIState.Type prevStateLevel) {
        if (this.type.getValue() < prevStateLevel.getValue()) { // downgraded

            this.setStbid(null); // check-in
            this.setSaid(null); // check-in
            this.setMicUseAgreed(false); // opt-in
            this.setOptIn(false); // opt-in
        } else { // upgraded
            PCIStore.getInstance(context).dispatchTriggeredByInternalRedux(new ActionFetchPolicy());
        }
    }

    @Override
    public void onLeave(@NonNull PCIState.Type nextStateLevel) {

    }

}
