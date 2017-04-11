package com.orozco.netreport.ui.main;

import android.app.AlertDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
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
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.orozco.netreport.flux.action.DataCollectionActionCreator.DataCollectionAction.ACTION_COLLECT_DATA_S;
import static com.orozco.netreport.flux.action.DataCollectionActionCreator.DataCollectionAction.ACTION_SEND_DATA_F;
import static com.orozco.netreport.flux.action.DataCollectionActionCreator.DataCollectionAction.ACTION_SEND_DATA_S;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */
public class MainActivity extends BaseActivity {

    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1000;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 1001;

    @Inject DataCollectionActionCreator mDataCollectionActionCreator;
    @Inject DataCollectionStore mDataCollectionStore;
    @Inject RestAPI restApi;
    @BindView(R.id.main_view) RelativeLayout mainView;
    @BindView(R.id.centerImage) ImageView centerImage;
    @BindView(R.id.content) RippleBackground rippleBackground;
    @BindView(R.id.reportBtn) Button reportBtn;
    private SweetAlertDialog pDialog;

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

        reportBtn.setVisibility(View.INVISIBLE);
    }

    private void initFlux() {
        addSubscriptionToUnsubscribe(
                mDataCollectionStore.observable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(store -> {
                            switch (store.getAction()) {
                                case ACTION_COLLECT_DATA_S:
                                    resetView();
                                    displayResults(store.getData());
                                    break;
                                case ACTION_SEND_DATA_S:
                                    pDialog.setTitleText("Sent!")
                                        .setContentText("Your data has been sent")
                                        .setConfirmText("See my results")
                                        .setConfirmClickListener(sweetAlertDialog -> {
                                            final String result = store.getData().toString(MainActivity.this);
                                            sweetAlertDialog
                                                    .setTitleText("Here's your data")
                                                    .setCancelText("I'm Done")
                                                    .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                                                    .setConfirmText("Share Results")
                                                    .setContentText(result)
                                                    .setConfirmClickListener(dialog->{
                                                        if (ShareDialog.canShow(ShareLinkContent.class)) {
                                                            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                                                    .setContentTitle("My BASS Results")
                                                                    .setImageUrl(Uri.parse("https://scontent.fmnl4-6.fna.fbcdn.net/v/t1.0-9/17796714_184477785394716_1700205285852495439_n.png?oh=40acf149ffe8dcc0e24e60af7f844514&oe=595D6465"))
                                                                    .setContentDescription(result)
                                                                    .setContentUrl(Uri.parse("https://bass.bnshosting.net/device"))
                                                                    .build();

                                                            ShareDialog.show(MainActivity.this, linkContent);
                                                        }
                                                    })
                                                    .changeAlertType(SweetAlertDialog.NORMAL_TYPE);
                                        })
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    // TODO: Don't treat shared prefs as database
                                    SharedPrefUtil.clearTempData(this);
                                    resetView();
                                    break;
                                case ACTION_SEND_DATA_F:
                                    new AlertDialog.Builder(this)
                                            .setTitle("Error : " + store.getError().getStatusCode())
                                            .setMessage(store.getError().getErrorMessage())
                                            .show();
                                    break;

                            }

                        }, throwable -> resetView())
        );
    }

    @OnClick(R.id.centerImage)
    public void onCenterImageClicked() {
        if (rippleBackground.isRippleAnimationRunning()) {
            endTest();
        } else {
            reportBtn.setVisibility(View.INVISIBLE);
            centerImage.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.signal_on));
            rippleBackground.startRippleAnimation();
            runOnUiThreadIfAlive(this::beginTest, 1000);
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
        reportBtn.setVisibility(View.INVISIBLE);
        rippleBackground.stopRippleAnimation();
        centerImage.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.signal));
    }

    public void displayResults(final Data results) {
        reportBtn.setVisibility(View.VISIBLE);
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
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        mDataCollectionActionCreator.sendData(data);
    }

    // TODO: Can be improved
    public Single<Boolean> isConnected()  {
        return Observable.fromCallable(() -> InetAddress.getByName(URI.create(RestAPI.BASE_URL).getHost()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(inetAddress -> inetAddress != null)
                .toSingle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}
