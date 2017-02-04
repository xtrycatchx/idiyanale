package com.orozco.netreport.ui.main;

import android.app.AlertDialog;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.orozco.netreport.R;
import com.orozco.netreport.model.Data;
import com.orozco.netreport.post.api.ApiUtils;
import com.orozco.netreport.ui.BaseDeviceActivity;
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

    private Data data;

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this);
        builder.setTitle("Data (this popup will be removed soon)");

        StringBuilder sb = new StringBuilder();
        Location loc = getLocation();
        if (null != loc) {
            sb.append("Latitude  : ");
            sb.append(getLocation().getLatitude());
            sb.append("\n");
            sb.append("Longitude  : ");
            sb.append(getLocation().getLongitude());
        }
        sb.append("\n");
        sb.append("Imei : ");
        sb.append(getDeviceId());
        sb.append("\n");
        sb.append("Connection : ");
        sb.append(getConnection());
        sb.append("\n");
        sb.append("Quality : ");
        sb.append(results);
        builder.setMessage(sb.toString());
        builder.show();

        //TODO
        data = new Data();


    }

    @OnClick(R.id.reportBtn)
    public void onReportSubmit() {
        ApiUtils.getAPIService().record(data).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(MainActivity.this, "Reported : ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Toast.makeText(MainActivity.this, "Result : " + responseBody.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
