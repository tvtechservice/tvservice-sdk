package com.pci.service.redux.reducer;

import java.util.HashMap;
import java.util.Map;

import com.pci.service.redux.action.ActionType;
import com.pci.service.redux.core.*;
import com.pci.service.redux.state.PCIState;
import com.pci.service.util.PCILog;

import static com.pci.service.redux.action.ActionType.*;

import static com.pci.service.redux.action.ActionType.PCI_AGREE_TERMS;
import static com.pci.service.redux.action.ActionType.PCI_DISAGREE_TERMS;
import static com.pci.service.redux.action.ActionType.PCI_DOWNGRADE_STATE;
import static com.pci.service.redux.action.ActionType.PCI_FETCH_POLICY;
import static com.pci.service.redux.state.PCIState.Type.*;
import static com.pci.service.redux.state.PCIState.Type.ACTIVE;
import static com.pci.service.redux.state.PCIState.Type.DEFAULT;
import static com.pci.service.redux.state.PCIState.Type.IDLE;

public class ReducerRoot implements Reducer {

    // TODO : refactor state - action - reducer mapping table to better code
    private final Map<PCIState.Type, Map<ActionType, PCIReducer>> subReducers = new HashMap<PCIState.Type, Map<ActionType, PCIReducer>>() {{
        put(DEFAULT, new HashMap<ActionType, PCIReducer>() {{
            put(PCI_AGREE_TERMS, new ReducerAgreeTerms());
        }});

        put(IDLE, new HashMap<ActionType, PCIReducer>() {{
            put(PCI_FETCH_POLICY, new ReducerFetchPolicy());
            put(PCI_DISAGREE_TERMS, new ReducerDisagreeTerms());
            put(PCI_DOWNGRADE_STATE, new ReducerDowngradeState());
        }});

        put(ACTIVE, new HashMap<ActionType, PCIReducer>() {{
            put(PCI_FETCH_POLICY, new ReducerFetchPolicy());
            put(PCI_DISAGREE_TERMS, new ReducerDisagreeTerms());
            put(PCI_DOWNGRADE_STATE, new ReducerDowngradeState());
        }});
    }};

    @Override
    public State reduce(State currentState, Action action) {
        PCIReducer reducer = getReducer(currentState, action);
        if (reducer != null) {
            PCILog.d("[DEBUG][State %8s] Reduce %s to %s", currentState.getType(), action.getClass().getSimpleName(), reducer.getClass().getSimpleName());
            return reducer.reduce((PCIState) currentState, action);
        } else {
            PCILog.d("[DEBUG][State %8s] Reduce %s to nothing (no matching reducer)", currentState.getType(), action.getClass().getSimpleName());
            return currentState;
        }
    }

    private PCIReducer getReducer(State state, Action action) {
        if (subReducers.containsKey(state.getType()) && subReducers.get(state.getType()).containsKey(action.getType())) {
            return subReducers.get(state.getType()).get(action.getType());
        } else {
            return null;
        }
    }

}
