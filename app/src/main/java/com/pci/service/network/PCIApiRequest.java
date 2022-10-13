package com.pci.service.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pci.service.redux.state.PCIState;

public class PCIApiRequest {
    @NonNull
    private final PCIApiTarget target;
    @Nullable
    private final Object data;
    @NonNull
    private final PCIState state;

    PCIApiRequest(final @NonNull PCIApiTarget target, @Nullable final Object data, @NonNull final PCIState state) {
        this.target = target;
        this.data = data;
        this.state = PCIState.from(state);
    }

    @NonNull
    public PCIApiTarget getTarget() {
        return target;
    }

    @Nullable
    public Object getData() {
        return data;
    }

    @NonNull
    public PCIState getState() {
        return state;
    }

    public String description() {
        switch (target) {
            case PCI_3001_UPLOAD_TERM_AGREEMENTS:
                return "약관 동의/철회 정보 전송";
            case PCI_3002_FETCH_POLICY:
                return "SDK 정책 조회";
        }

        return null;
    }
}
