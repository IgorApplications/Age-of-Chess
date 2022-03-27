package com.iapp.chess;

import com.iapp.chess.util.CallListener;

public interface Launcher {

    void setRequestedVertically();

    void setRequestedHorizontally();

    void addOnFinish(CallListener callListener);

    int[] getDeviceScreen();
}
