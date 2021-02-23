package com.venomdevteam.venom.entity.event;

public interface Cancellable {

    void cancel(boolean toCancel);

    boolean isCancelled();

}
