package com.ddinfo.flashman.impl;

/**
 * Created by fuh on 2017/3/22.
 * Email：unableApe@gmail.com
 */

public interface OnSocketResultListener {
    void onResult(final Object... args);
    void onConnect(final Object... args);
    void onDisconnect(final Object... args);
    void onConnectError(final Object... args);
    void onConnectTimeoutError(final Object... args);
}
