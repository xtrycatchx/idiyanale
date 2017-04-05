package com.orozco.netreport.flux;

/**
 * @author A-Ar Andrew Concepcion
 */
public interface AliveUiThread {

    /**
     * Runs the [Runnable] if the current context is alive.
     */
    public void runOnUiThreadIfAlive(Runnable runnable);

    /**
     * Runs the [Runnable] if the current context is alive.
     */
    public void runOnUiThreadIfAlive(Runnable runnable, long delayMillis);
}
