package com.pci.service.redux.action;

import java.util.HashMap;

import com.pci.service.redux.core.Action;
import com.pci.service.redux.state.PCIState;

public class ActionDowngradeState extends Action {

    private static final String KEY_STATE_TYPE = "state_type";

    public ActionDowngradeState(final PCIState.Type stateType) {
        super(ActionType.PCI_DOWNGRADE_STATE, new HashMap<String, Object>() {{
            put(KEY_STATE_TYPE, stateType);
        }});
    }

    public PCIState.Type getStateType() {
        return (PCIState.Type) this.getPayload().get(KEY_STATE_TYPE);
    }

}
