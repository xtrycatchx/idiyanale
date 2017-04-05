package com.orozco.netreport.ui.main;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.github.pwittchen.reactivewifi.AccessRequester;
import com.orozco.netreport.R;
import com.orozco.netreport.flux.action.DataCollectionActionCreator;
import com.orozco.netreport.flux.store.DataCollectionStore;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.orozco.netreport.flux.action.DataCollectionActionCreator.DataCollectionAction.ACTION_COLLECT_DATA_F;
import static com.orozco.netreport.flux.action.DataCollectionActionCreator.DataCollectionAction.ACTION_COLLECT_DATA_S;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */
public class MainActivity extends BaseActivity {

    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1000;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 1001;

    @Inject DataCollectionActionCreator mDataCollectionActionCreator;
    @Inject DataCollectionStore mDataCollectionStore;
    @Inject RestAPI restApi;
    @BindView(R.id.main_view) MainView mainView;
    @BindView(R.id.centerImage) ImageView centerImage;
    @BindView(R.id.content) RippleBackground rippleBackground;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        initFlux();

        isConnected()
                .subscribe(isConnected -> {
                    Data savedData = SharedPrefUtil.retrieveTempData(this);
                    if(savedData != null) {
                        postToServer(savedData);
                    }
                }, throwable -> {

                });

        mainView.setButtonVisibility(View.INVISIBLE);
    }

    private void initFlux() {
        addSubscriptionToUnsubscribe(
                mDataCollectionStore.observable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(store -> ACTION_COLLECT_DATA_S.equals(store.getAction()) ||
                                ACTION_COLLECT_DATA_F.equals(store.getAction()))
                        .subscribe(store -> {
                            resetView();
                            // Uncomment this once server is up
//                            if (store.getError() != null) {
//                                displayResults(store.getData());
//                            }
                            // Remove this once server is up
                            displayResults(store.getData());
                        }, throwable -> resetView())
        );
    }

    @OnClick(R.id.centerImage)
    public void onCenterImageClicked() {
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
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    public void beginTest() {
        boolean fineLocationPermissionNotGranted = ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED;
        boolean coarseLocationPermissionNotGranted = ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED;
        boolean phoneStatePermissionNotGranted = ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PERMISSION_GRANTED;

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

        mDataCollectionActionCreator.collectData();
    }

    public void endTest() {
        runOnUiThread(this::resetView);
    }

    public void resetView() {
        mainView.setButtonVisibility(View.INVISIBLE);
        rippleBackground.stopRippleAnimation();
        centerImage.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.signal));
    }

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
            postToServer(mDataCollectionStore.getData());
        }
    }

    public void postToServer(final Data data) {

        new AlertDialog.Builder(this)
                .setTitle(R.string.successTitle)
                .setMessage(data.toString(this))
                .setPositiveButton(getString(R.string.testAgain), (dialog, which) -> centerImage.callOnClick())
                .setCancelable(true)
                .show();

        // TODO: Don't treat shared prefs as database
        SharedPrefUtil.clearTempData(this);
        resetView();
        return;
        // TODO: Disable for now
//        restApi.record(data)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(response -> {
//                    new AlertDialog.Builder(this)
//                            .setTitle(R.string.successTitle)
//                            .setMessage(data.toString(this))
//                            .setPositiveButton(getString(R.string.testAgain), (dialog, which) -> centerImage.callOnClick())
//                            .setCancelable(true)
//                            .show();
//
//                    // TODO: Don't treat shared prefs as database
//                    SharedPrefUtil.clearTempData(this);
//                    resetView();
//                }, e -> {
//                    new AlertDialog.Builder(this)
//                            .setTitle("Error : " + e.getMessage())
//                            .setMessage(e.getMessage())
//                            .show();
//                });
    }

    // TODO: Can be improved
    public Single<Boolean> isConnected()  {
        return Observable.fromCallable(() -> InetAddress.getByName(URI.create(RestAPI.BASE_URL).getHost()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(inetAddress -> inetAddress != null)
                .toSingle();
    }
}
