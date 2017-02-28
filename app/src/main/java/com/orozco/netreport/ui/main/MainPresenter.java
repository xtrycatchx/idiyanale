package com.orozco.netreport.ui.main;

import android.content.Context;

import com.orozco.netreport.inject.PerActivity;
import com.orozco.netreport.handler.StartTest;
import com.orozco.netreport.model.Data;

import javax.inject.Inject;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

@PerActivity
public class MainPresenter {

    private final StartTest startTest;

    private final CompositeSubscription subscriptions;
    private View view;

    @Inject
    public MainPresenter(StartTest startTest) {
        this.startTest = startTest;
        this.subscriptions = new CompositeSubscription();

    }

    public void startTest(View view, Context context) {
        this.view = view;
        startTest.execute(context).subscribe(new TestSubscriber());
    }

    public void stopTest() {
        if (null != subscriptions) {
            subscriptions.unsubscribe();
        }
    }

    private class TestSubscriber extends Subscriber<Data> {
        @Override
        public void onNext(Data result) {
            view.endTest();
            view.displayResults(result);
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onCompleted() {
        }
    }

    public interface View {
        void beginTest();

        void endTest();

        void displayResults(Data result);
    }
}
