package com.orozco.netreport.ui.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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


import java.io.IOError;
import java.io.IOException;

import java.net.InetAddress;

import butterknife.BindView;
import butterknife.OnClick;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import rx.schedulers.Schedulers;

import javax.inject.Inject;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RESTART_PACKAGES;
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
        if (buttonSubscription == null) {
            buttonSubscription = getButtonSubscription();
        }
        else if(buttonSubscription.isUnsubscribed()) {
            buttonSubscription = null;
            buttonSubscription = getButtonSubscription();
        }
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
        safelyUnsubscribe(buttonSubscription);
    }

    private void safelyUnsubscribe(Subscription... subscriptions) {
        for (Subscription subscription : subscriptions) {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
    }

    Subscription buttonSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(isConnected()) {
                        Data savedData = SharedPrefUtil.retrieveTempData(MainActivity.this);
                        if(savedData != null) {
                            postToServer(savedData);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();




        mainView.setButtonVisibility(View.INVISIBLE);
        buttonSubscription = getButtonSubscription();
    }

    private Subscription getButtonSubscription() {
        return RxView.clicks(centerImage).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (rippleBackground.isRippleAnimationRunning()) {

                    endTest();
                } else {
                    mainView.setButtonVisibility(View.INVISIBLE);
                    centerImage.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.signal_on));
                    rippleBackground.startRippleAnimation();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
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
            endTest();
            return;
        }
        if (phoneStatePermissionNotGranted) {
            requestPhoneStatePermission();
            endTest();
            return;
        }

        if (!AccessRequester.isLocationEnabled(this)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AccessRequester.requestLocationAccess(MainActivity.this);

                }
            });
            endTest();
            return;
        }


        presenter.startTest(this, MainActivity.this);
    }

    @Override
    public void endTest() {

        presenter.stopTest();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resetView();
            }
        });
    }

    public void resetView() {
        mainView.setButtonVisibility(View.INVISIBLE);
        rippleBackground.stopRippleAnimation();
        centerImage.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.signal));
    }

    @Override
    public void displayResults(final Data results) {

        mainView.setText("GONNA CHANGE THIS LATER");

        rippleBackground.stopRippleAnimation();
        centerImage.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.signal));
        SharedPrefUtil.saveTempData(this, results);

    }

    @OnClick(R.id.reportBtn)
    public void onReportSubmit() {

        Data data = SharedPrefUtil.retrieveTempData(this);
        if(data != null) {
            postToServer(data);
        }


    }

    public void postToServer(final Data data) {
        getRestApi().record(new Gson().toJson(data)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                MainActivity.this);
                        builder.setTitle("Error : " + e.getMessage());
                        builder.setMessage(e.getMessage());
                        builder.show();
                    }

                    @Override
                    public void onNext(String response) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                MainActivity.this);
                        builder.setTitle("Data Reported");
                        builder.setMessage(response);
                        builder.setPositiveButton("See Data Reported", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new AlertDialog.Builder(
                                        MainActivity.this)
                                        .setMessage(new GsonBuilder().setPrettyPrinting().create().toJson(data)).show();
                            }
                        });
                        builder.show();
                        SharedPrefUtil.clearTempData(MainActivity.this);
                        resetView();
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

    public boolean isConnected()  {
        try {
            InetAddress ipAddr = InetAddress.getByName(RestAPI.BASE_URL);
            Log.d("isConnected",ipAddr.toString());
            return !ipAddr.equals("");
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
