package com.orozco.netreport.ui.main;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import com.google.gson.Gson;
import com.orozco.netreport.R;
import com.orozco.netreport.model.Coordinates;
import com.orozco.netreport.model.Data;
import com.orozco.netreport.post.api.RestAPI;
import com.orozco.netreport.ui.BaseDeviceActivity;
import com.orozco.netreport.utils.SharedPrefUtil;
import com.skyfishjy.library.RippleBackground;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import javax.inject.Inject;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class MainActivity extends BaseDeviceActivity implements MainPresenter.View {

    @Inject
    MainPresenter presenter;

    @BindView(R.id.main_view)
    MainView mainView;
    @BindView(R.id.content)
    RippleBackground rippleBackground;

    private RestAPI restApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        mainView.setButtonvisibility(View.INVISIBLE);
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

        Data data = new Data();
        data.setCoordinates(new Coordinates(getLocation().getLatitude(), getLocation().getLongitude()));
        data.setConnection(getConnection());
        data.setCarrier("TODO"); //TODO
        data.setTimestamp(System.currentTimeMillis());
        data.setImei(getDeviceId());
        data.setoSversion(android.os.Build.VERSION.RELEASE);
        data.setPhoneModel(Build.MANUFACTURER + " " + Build.MODEL + " " + Build.VERSION.RELEASE + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName());
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

    private RestAPI getRestApi() {
        if (restApi == null) {
            restApi = RestAPI.Factory.create();
        }
        return restApi;
    }
}
