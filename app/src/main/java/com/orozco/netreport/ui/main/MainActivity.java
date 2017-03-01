package com.orozco.netreport.ui.main;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.github.pwittchen.reactivewifi.AccessRequester;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.rxbinding.view.RxView;
import com.orozco.netreport.R;

import com.orozco.netreport.model.Data;

import com.orozco.netreport.post.api.RestAPI;
import com.orozco.netreport.ui.BaseActivity;
import com.orozco.netreport.utils.SharedPrefUtil;
import com.skyfishjy.library.RippleBackground;


import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import rx.schedulers.Schedulers;

import javax.inject.Inject;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class MainActivity extends BaseActivity implements MainPresenter.View {

    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1000;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 1001; //arbitrary int

    @Inject
    MainPresenter presenter;

    @BindView(R.id.main_view)
    MainView mainView;

    @BindView(R.id.centerImage)
    ImageView centerImage;

    @BindView(R.id.content)
    RippleBackground rippleBackground;

    private RestAPI restApi;

    @Override
    public void onResume() {
        super.onResume();
    }

    private void requestCoarseLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        }
    }

    private void requestPhoneStatePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_PHONE_STATE},
                    PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        safelyUnsubscribe(buttonSubscriber);
    }

    private void safelyUnsubscribe(Subscription... subscriptions) {
        for (Subscription subscription : subscriptions) {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
    }

    Subscription buttonSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        SharedPrefUtil.createTempData(this);
        mainView.setButtonvisibility(View.INVISIBLE);

        buttonSubscriber = RxView.clicks(centerImage).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (rippleBackground.isRippleAnimationRunning()) {
                    mainView.setButtonvisibility(View.INVISIBLE);
                    rippleBackground.stopRippleAnimation();
                    centerImage.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.signal));
                    endTest();
                } else {
                    mainView.setButtonvisibility(View.INVISIBLE);
                    centerImage.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.signal_on));
                    rippleBackground.startRippleAnimation();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            beginTest();
                        }
                    }).start();
                }
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void beginTest() {

        boolean fineLocationPermissionNotGranted =
                ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED;
        boolean coarseLocationPermissionNotGranted =
                ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED;
        boolean phoneStatePermissionNotGranted =
                ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PERMISSION_GRANTED;

        if (fineLocationPermissionNotGranted && coarseLocationPermissionNotGranted) {
            requestCoarseLocationPermission();
            return;
        }
        if (phoneStatePermissionNotGranted) {
            requestPhoneStatePermission();
            return;
        }

        if (!AccessRequester.isLocationEnabled(this)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AccessRequester.requestLocationAccess(MainActivity.this);
                }
            });

            return;
        }


        presenter.startTest(this, MainActivity.this);
    }

    @Override
    public void endTest() {
        presenter.stopTest();
    }

    @Override
    public void displayResults(final Data results) {

        mainView.setText("GONNA CHANGE THIS LATER");
        presenter.stopTest();
        rippleBackground.stopRippleAnimation();
        centerImage.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.signal));
        SharedPrefUtil.saveTempData(this, results);

    }

    @OnClick(R.id.reportBtn)
    public void onReportSubmit() {

        Data coordinates = SharedPrefUtil.retrieveTempData(MainActivity.this);
        AlertDialog.Builder b2 = new AlertDialog.Builder(
                MainActivity.this);
        b2.setTitle("X ");
        Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
        String json2 = gson2.toJson(coordinates);
        b2.setMessage(json2);
        b2.show();

        final Data data = SharedPrefUtil.retrieveTempData(this);
        getRestApi().record(data).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                MainActivity.this);
                        builder.setTitle("Data (this popup will be removed soon)");
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String json = gson.toJson(data);
                        builder.setMessage(json);
                        builder.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                MainActivity.this);
                        builder.setTitle("Error : " + e.getMessage());
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String json = gson.toJson(data);
                        builder.setMessage(json);
                        builder.show();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                MainActivity.this);
                        builder.setTitle("Result : " + responseBody.toString());
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String json = gson.toJson(data);
                        builder.setMessage(json);
                        builder.show();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.stopTest();
    }

    private RestAPI getRestApi() {
        if (restApi == null) {
            restApi = RestAPI.Factory.create();
        }
        return restApi;
    }
}
