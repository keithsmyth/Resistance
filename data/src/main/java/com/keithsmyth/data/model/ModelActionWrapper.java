package com.keithsmyth.data.model;

public class ModelActionWrapper<T> {

    public final T dataModel;
    public final boolean isAdded;

    public ModelActionWrapper(T dataModel, boolean isAdded) {
        this.dataModel = dataModel;
        this.isAdded = isAdded;
    }
}
