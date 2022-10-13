package com.pci.service.redux.core;

import androidx.annotation.NonNull;

import com.pci.service.redux.state.PCIState;

public interface PCIReducer {
    @NonNull
    PCIState reduce(@NonNull PCIState currentState, @NonNull Action action);
}
