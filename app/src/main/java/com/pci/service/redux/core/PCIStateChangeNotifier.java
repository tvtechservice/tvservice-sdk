package com.pci.service.redux.core;

import androidx.annotation.NonNull;

import com.pci.service.redux.state.PCIState;

public interface PCIStateChangeNotifier {

    void stateChangeNotify(@NonNull PCIState prevState, @NonNull PCIState newState);
}
