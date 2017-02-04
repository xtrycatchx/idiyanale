package com.orozco.netreport.ui.main;

import com.orozco.netreport.handler.RetrieveLocation;
import com.orozco.netreport.handler.RetrieveNetworkCarrier;
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
    private final RetrieveLocation retrieveLocation;
    private final RetrieveNetworkCarrier retrieveNetworkCarrier;


    private final CompositeSubscription subscriptions;
    private View view;

    @Inject
    public MainPresenter(StartTest startTest, RetrieveLocation retrieveLocation, RetrieveNetworkCarrier retrieveNetworkCarrier) {
        this.startTest = startTest;
        this.retrieveLocation = retrieveLocation;
        this.retrieveNetworkCarrier = retrieveNetworkCarrier;
        this.subscriptions = new CompositeSubscription();
    }

    public void startTest(View view) {
        this.view = view;
        retrieveLocation.execute().subscribe(new LocationSubscriber());
        retrieveNetworkCarrier.execute().subscribe(new CarrierSubscriber());
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

    private class LocationSubscriber extends Subscriber<String> {
        @Override
        public void onNext(String result) {
            //TODO append location
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onCompleted() {
        }
    }

    private class CarrierSubscriber extends Subscriber<String> {
        @Override
        public void onNext(String result) {
            //TODO append carrier
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
