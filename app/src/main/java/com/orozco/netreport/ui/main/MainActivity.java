package com.orozco.netreport.ui.main;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.github.pwittchen.reactivewifi.AccessRequester;
import com.jakewharton.rxbinding.view.RxView;
import com.orozco.netreport.R;
import com.orozco.netreport.model.Data;
import com.orozco.netreport.post.api.RestAPI;
import com.orozco.netreport.ui.BaseActivity;
import com.orozco.netreport.utils.SharedPrefUtil;
import com.skyfishjy.library.RippleBackground;

import java.net.InetAddress;
import java.net.URI;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class MainActivity extends BaseActivity implements MainPresenter.View {

    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1000;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 1001;

    @Inject MainPresenter presenter;
    @Inject RestAPI restApi;
    @BindView(R.id.main_view) MainView mainView;
    @BindView(R.id.centerImage) ImageView centerImage;
    @BindView(R.id.content) RippleBackground rippleBackground;

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

        isConnected()
                .subscribe(isConnected -> {
                    Data savedData = SharedPrefUtil.retrieveTempData(this);
                    if(savedData != null) {
                        postToServer(savedData);
                    }
                }, throwable -> {

                });

        mainView.setButtonVisibility(View.INVISIBLE);
        buttonSubscription = getButtonSubscription();
    }

    private Subscription getButtonSubscription() {
        return RxView.clicks(centerImage).subscribe(aVoid -> {
            if (rippleBackground.isRippleAnimationRunning()) {
                endTest();
            } else {
                mainView.setButtonVisibility(View.INVISIBLE);
                centerImage.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.signal_on));
                rippleBackground.startRippleAnimation();
                new Thread(() -> {
                    try {
                        // TODO: Don't do this
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    beginTest();
                }).start();
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
            runOnUiThread(() -> AccessRequester.requestLocationAccess(this));
            endTest();
            return;
        }

        presenter.startTest(this, MainActivity.this);
    }

    @Override
    public void endTest() {
        presenter.stopTest();
        runOnUiThread(this::resetView);
    }

    public void resetView() {
        mainView.setButtonVisibility(View.INVISIBLE);
        rippleBackground.stopRippleAnimation();
        centerImage.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.signal));
    }

    @Override
    public void displayResults(final Data results) {
        mainView.setText(getString(R.string.reportLabel));
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
        // TODO: Don't do this here
        restApi.record(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    StringBuilder sb = new StringBuilder();
                    if(!TextUtils.isEmpty(data.getOperator())) {
                        sb.append(getString(R.string.provider));
                        sb.append(data.getOperator());
                        sb.append("\n");
                    }
                    if(!TextUtils.isEmpty(data.getBandwidth())) {
                        sb.append(getString(R.string.bandwidth));
                        sb.append(data.getBandwidth());
                        sb.append("\n");
                    }
                    if(!TextUtils.isEmpty(data.getSignal())) {
                        sb.append(getString(R.string.signal));
                        sb.append(data.getSignal());
                    }

                    new AlertDialog.Builder(this)
                            .setTitle(R.string.successTitle)
                            .setMessage(sb.toString())
                            .setPositiveButton(getString(R.string.testAgain), (dialog, which) -> centerImage.callOnClick())
                            .setCancelable(true)
                            .show();

                    // TODO: Don't treat shared prefs as database
                    SharedPrefUtil.clearTempData(this);
                    resetView();
                }, e -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            MainActivity.this);
                    builder.setTitle("Error : " + e.getMessage());
                    builder.setMessage(e.getMessage());
                    builder.show();
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.stopTest();
    }

    public Single<Boolean> isConnected()  {
        return Observable.fromCallable(() -> InetAddress.getByName(URI.create(RestAPI.BASE_URL).getHost()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(inetAddress -> inetAddress != null)
                .toSingle();
    }
}
