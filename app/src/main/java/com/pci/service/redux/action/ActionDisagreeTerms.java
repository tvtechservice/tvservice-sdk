package com.pci.service.redux.action;

import java.util.HashMap;

import com.pci.service.redux.core.Action;

public class ActionDisagreeTerms extends Action {

    public ActionDisagreeTerms() {
        super(ActionType.PCI_DISAGREE_TERMS, new HashMap<String, Object>() {{
        }});
    }
}
