package com.flatstack.android.utils;

import java.util.TimerTask;

/**
 * Created by Revern on 07.06.2017.
 */

public class NetworkCheckingTimer extends TimerTask {

    private Runnable runnable;

    public NetworkCheckingTimer(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override public void run() {
        runnable.run();
    }
}
