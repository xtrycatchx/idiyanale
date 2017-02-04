package com.orozco.netreport.ui.main;

import com.orozco.netreport.inject.PerActivity;
import com.orozco.netreport.handler.StartTest;

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

    public void startTest(View view) {
        this.view = view;
        startTest.execute().subscribe(new TestSubscriber());
    }

    public void stopTest() {
        subscriptions.unsubscribe();
    }

    private class TestSubscriber extends Subscriber<String> {
        @Override
        public void onNext(String result) {
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

        void displayResults(String result);
    }
}
