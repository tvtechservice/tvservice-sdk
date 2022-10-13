package com.pci.service.redux.core;

import androidx.annotation.NonNull;
import com.pci.service.redux.state.PCIState;

public interface State {
    void onKeep();
    void onEnter(@NonNull PCIState.Type fromStateLevel);
    void onLeave(@NonNull PCIState.Type toStateLevel);
    void writePersistent();
    PCIState.Type getType();
}
