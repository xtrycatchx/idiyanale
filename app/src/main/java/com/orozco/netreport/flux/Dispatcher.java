package com.orozco.netreport.flux;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import timber.log.Timber;

/**
 * Class responsible for dispatching actions throughout the application to the stores.
 *
 * @author A-Ar Andrew Concepcion
 */
public class Dispatcher {

    private final List<Store> mStores;

    private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

    public Dispatcher(List<Store> stores) {
        mStores = Collections.unmodifiableList(stores);
    }

    /**
     * Dispatch actions to the stores in UI Thread
     */
    public void dispatch(@NonNull Action action) {
        mMainThreadHandler.post(() -> {
            Timber.d("#dispatch action : %s", action);
            for (Store store : mStores) {
                store.dispatchAction(action);
            }
        });
    }
}
