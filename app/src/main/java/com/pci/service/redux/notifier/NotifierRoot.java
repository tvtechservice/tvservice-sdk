package com.pci.service.redux.notifier;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.TreeMap;

import com.pci.service.redux.action.ActionType;
import com.pci.service.redux.core.Action;
import com.pci.service.redux.core.Notifier;
import com.pci.service.redux.core.PCINotifier;
import com.pci.service.redux.core.PCIStateChangeNotifier;
import com.pci.service.redux.core.State;
import com.pci.service.redux.state.PCIState;
import com.pci.service.util.PCILog;

import static com.pci.service.redux.action.ActionType.*;
import static com.pci.service.redux.action.ActionType.PCI_AGREE_TERMS;
import static com.pci.service.redux.action.ActionType.PCI_CHECKIN;
import static com.pci.service.redux.action.ActionType.PCI_CHECKIN_QUIETLY;
import static com.pci.service.redux.action.ActionType.PCI_CHECKOUT;
import static com.pci.service.redux.action.ActionType.PCI_CHECKOUT_QUIETLY;
import static com.pci.service.redux.action.ActionType.PCI_DOWNGRADE_STATE;
import static com.pci.service.redux.action.ActionType.PCI_EXPIRE_BITSOUND;
import static com.pci.service.redux.action.ActionType.PCI_OPT_IN;
import static com.pci.service.redux.action.ActionType.PCI_OPT_OUT;

public class NotifierRoot implements Notifier {

    private boolean isActionToBeNotified(ActionType actionType) {
        if(actionType == PCI_AGREE_TERMS
//                || actionType == PCI_DISAGREE_TERMS // / The reducer directly redirects it to PCI_DOWNGRADE_STATE
                || actionType == PCI_DOWNGRADE_STATE
                ) {
            return true;
        }
        else {
            return false;
        }
    }

    // TODO : refactor this to better code...
    // mNotifierMap is deprecated since PCI 1.2.0 (19/01/30, soonwon ka, Soundlly Inc.)
    // TreeMap 을 쓰면 values 의 순서가 항상 일정하게 보장된다.
    private Map<String, PCINotifier> mNotifierMap = new TreeMap<String, PCINotifier>() {{
//        put("ads-push", new NotifierAdsPush());
    }};
    // TreeMap 을 쓰면 values 의 순서가 항상 일정하게 보장된다.
    private Map<String, PCIStateChangeNotifier> mStateChangeNotifierMap = new TreeMap<String, PCIStateChangeNotifier>() {{
//        put("ads-push", new NotifierAdsPush());
    }};
    // TreeMap 을 쓰면 values 의 순서가 항상 일정하게 보장된다.
    private Map<String, PCINotifier> mVerboseNotifierMap = new TreeMap<String, PCINotifier>() {{
//        put("ads-push", new NotifierAdsPush());
    }};

    @Override
    public void notify(@NonNull State state, @NonNull Action action) {
        if(isActionToBeNotified(action.getType())) {
            PCILog.d("Notify : " + state + ", " + action);

            for (PCINotifier notifier : mNotifierMap.values()) {
                notifier.notify((PCIState) state);
            }
        }
        else {
            PCILog.v("Notify is not supported for " + action);
        }
    }

    @Override
    public void stateChangeNotify(@NonNull State prevState, @NonNull State newState, @NonNull Action action) {
        if(!prevState.getType().equals(newState.getType())) {
            PCILog.d("StateChangeNotify : prevState (" + prevState.getType() + ") to newState(" + newState.getType() + "), " + action);

            for (PCIStateChangeNotifier notifier : mStateChangeNotifierMap.values()) {
                notifier.stateChangeNotify((PCIState) prevState, (PCIState) newState);
            }
        }
        else {
            PCILog.v("StateChangeNotify is not supported for (prev:" + prevState.getType() + ", new:" + newState.getType() + ")-" + action);
        }
    }

    @Override
    public void verboseNotify(@NonNull State state, @NonNull Action action) {
        PCILog.d("VerboseNotify : " + state + ", " + action);
        for (PCINotifier notifier : mVerboseNotifierMap.values()) {
            notifier.notify((PCIState) state);
        }
    }

    public void addSubNotifier(String name, PCINotifier notifier) {
        mNotifierMap.put(name, notifier);
    }

    public boolean removeSubNotifier(String name) {
        PCINotifier ret = mNotifierMap.remove(name);
        if (ret == null) {
            return false;
        }
        else {
            return true;
        }
    }

    public void addSubStateChangeNotifier(String name, PCIStateChangeNotifier notifier) {
        mStateChangeNotifierMap.put(name, notifier);
    }

    public boolean removeSubStateChangeNotifier(String name) {
        PCIStateChangeNotifier ret = mStateChangeNotifierMap.remove(name);
        if (ret == null) {
            return false;
        }
        else {
            return true;
        }
    }


    public void addSubVerboseNotifier(String name, PCINotifier notifier) {
        mVerboseNotifierMap.put(name, notifier);
    }

    public boolean removeSubVerboseNotifier(String name) {
        PCINotifier ret = mVerboseNotifierMap.remove(name);
        if (ret == null) {
            return false;
        }
        else {
            return true;
        }
    }
}
