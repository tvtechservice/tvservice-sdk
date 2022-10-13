package com.pci.service.redux.action;

import java.util.HashMap;

import com.pci.service.redux.core.Action;

public class ActionFetchPolicy extends Action {

    public ActionFetchPolicy() {
        super(ActionType.PCI_FETCH_POLICY, new HashMap<String, Object>() {{
        }});
    }

}
