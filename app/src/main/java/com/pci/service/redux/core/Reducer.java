package com.pci.service.redux.core;

public interface Reducer {
    State reduce(State currentState, Action action);
}
