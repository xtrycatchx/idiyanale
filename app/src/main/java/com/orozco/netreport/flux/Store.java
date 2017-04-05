package com.orozco.netreport.flux;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import rx.Observable;
import rx.Scheduler;
import rx.subjects.PublishSubject;

/**
 * This class reacts to actions dispatched by the {@link Dispatcher} and is
 * responsible for storing and updating its own state.
 *
 * @author A-Ar Andrew Concepcion
 */
public abstract class Store<T extends Store> {

    private final PublishSubject<T> mPublisher = PublishSubject.create();

    /**
     * Returns the {@link Observable} to subscribe, by default this will run
     * synchronously on the UI Thread so we must not call
     * {@link Observable#subscribeOn(Scheduler)} or {@link Observable#observeOn(Scheduler)}
     * unless thread safety is not needed.
     */
    public Observable<T> observable() {
        return mPublisher.asObservable();
    }

    /**
     * Dispatch an action to the store, this method must be called on the UI Thread.
     */
    @UiThread
    public void dispatchAction(Action action) {
        onReceiveAction(action);
    }

    /**
     * Called when the {@link Dispatcher} dispatches an action.
     * This methods run synchronously on the UI Thread so we must not do
     * heavy computations so that we dont stall the UI.
     */
    protected abstract void onReceiveAction(Action action);

    /**
     * Notify the subscribers that the store changes.
     */
    protected void notifyStoreChanged(@NonNull T t) {
        mPublisher.onNext(t);
    }
}
