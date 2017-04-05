package com.orozco.netreport.flux;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author A-Ar Andrew Concepcion
 */
public abstract class FluxActivity extends AppCompatActivity implements AliveUiThread {
    private Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper());

    /**
     * Subscriptions to unsubcribe in [.onDestroy]
     */
    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.clear();
    }

    /**
     * Unsubscribe the subscription automatically when the [Activity] is destroyed.
     *
     * @param subscription The subscription to unsubscribe.
     */
    protected void addSubscriptionToUnsubscribe(Subscription subscription) {
        mSubscriptions.add(subscription);
    }

    @Override
    public void runOnUiThreadIfAlive(Runnable runnable) {
        runOnUiThreadIfAlive(runnable, 0);
    }

    /**
     * Execute the runnable on ui thread with specified delay.
     *
     * @param runnable    The runnable to execute.
     * @param delayMillis The delay before executing the runnable.
     */
    @Override
    public void runOnUiThreadIfAlive(Runnable runnable, long delayMillis) {
        if (isFinishing()) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
            return;
        }
        MAIN_THREAD_HANDLER.postDelayed(runnable, delayMillis);
    }


}