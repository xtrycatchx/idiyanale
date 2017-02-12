package com.orozco.netreport.ui.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.google.gson.Gson;
import com.orozco.netreport.R;
import com.orozco.netreport.model.Coordinates;
import com.orozco.netreport.model.Data;
import com.orozco.netreport.model.DataTemp;
import com.orozco.netreport.model.Sources;
import com.orozco.netreport.post.api.RestAPI;
import com.orozco.netreport.ui.BaseActivity;
import com.orozco.netreport.utils.SharedPrefUtil;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import javax.inject.Inject;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class MainActivity extends BaseActivity implements MainPresenter.View {

    private static final int PERMISSIONS = 777;

    @Inject
    MainPresenter presenter;

    @BindView(R.id.main_view)
    MainView mainView;
    @BindView(R.id.content)
    RippleBackground rippleBackground;

    private RestAPI restApi;

    private Subscriber<DataTemp> dataSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        SharedPrefUtil.createTempData(this);
        mainView.setButtonvisibility(View.INVISIBLE);

        Sources sources = new Sources();
        Observable<DataTemp> source = Observable.concat(
                sources.carrier(this),
                sources.connection(this),
                sources.oSversion(),
                sources.phoneModel(),
                sources.imei(this)
        ).cast(DataTemp.class);

        dataSubscriber = new Subscriber<DataTemp>() {
            @Override
            public void onNext(DataTemp temp) {
                Data data = SharedPrefUtil.retrieveTempData(MainActivity.this);
                switch (temp.getFlag()) {
                    case IMEI:
                        data.setImei(temp.getDataRaw());
                        break;
                    case CARRIER:
                        data.setCarrier(temp.getDataRaw());
                        break;
                    case PHONE_MODEL:
                        data.setPhoneModel(temp.getDataRaw());
                        break;
                    case OSVERSION:
                        data.setoSversion(temp.getDataRaw());
                        break;
                    //TODO default
                }
                SharedPrefUtil.saveTempData(MainActivity.this, data);
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }
        };
        source.subscribe(dataSubscriber);
        checkPermissions();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.centerImage)
    public void clicked() {
        if (rippleBackground.isRippleAnimationRunning()) {
            mainView.setButtonvisibility(View.INVISIBLE);
            rippleBackground.stopRippleAnimation();
            endTest();
        } else {
            mainView.setButtonvisibility(View.INVISIBLE);
            rippleBackground.startRippleAnimation();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    beginTest();
                }
            });
            t.start();
        }

    }

    @Override
    public void beginTest() {
        presenter.startTest(this);
    }

    @Override
    public void endTest() {
        presenter.stopTest();
    }

    @Override
    public void displayResults(final String results) {

        mainView.setText(results);
        presenter.stopTest();
        rippleBackground.stopRippleAnimation();

        Data data = SharedPrefUtil.retrieveTempData(this);
        data.setTimestamp(System.currentTimeMillis());
        data.setSignal(results);
        SharedPrefUtil.saveTempData(this, data);

    }

    @OnClick(R.id.reportBtn)
    public void onReportSubmit() {
        final Data data = SharedPrefUtil.retrieveTempData(this);
        getRestApi().record(data).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                MainActivity.this);
                        builder.setTitle("Data (this popup will be removed soon)");
                        builder.setMessage(new Gson().toJson(data));
                        builder.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                MainActivity.this);
                        builder.setTitle("Error : " + e.getMessage());
                        builder.setMessage(new Gson().toJson(data));
                        builder.show();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                MainActivity.this);
                        builder.setTitle("Result : " + responseBody.toString());
                        builder.setMessage(new Gson().toJson(data));
                        builder.show();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.dataSubscriber.unsubscribe();
    }

    private void checkPermissions() {
        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(this);


        List<String> arrayPermissions = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            arrayPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            arrayPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            arrayPermissions.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            arrayPermissions.add(Manifest.permission.INTERNET);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            arrayPermissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            arrayPermissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        }

        if (!arrayPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    arrayPermissions.toArray(new String[arrayPermissions.size()]),
                    PERMISSIONS);
        }

        locationProvider.getLastKnownLocation()
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
                        Data data = SharedPrefUtil.retrieveTempData(MainActivity.this);//.createTempData(this);
                        data.setCoordinates(new Coordinates(location.getLatitude(), location.getLongitude()));
                        SharedPrefUtil.saveTempData(MainActivity.this, data);

                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    checkPermissions();
                }
                return;
            }
        }
    }

    private RestAPI getRestApi() {
        if (restApi == null) {
            restApi = RestAPI.Factory.create();
        }
        return restApi;
    }
}
