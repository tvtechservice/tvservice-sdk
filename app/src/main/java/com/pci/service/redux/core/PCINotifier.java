package com.pci.service.redux.core;


import androidx.annotation.NonNull;

import com.pci.service.redux.state.PCIState;

public interface PCINotifier {

    void notify(@NonNull PCIState state);
}
