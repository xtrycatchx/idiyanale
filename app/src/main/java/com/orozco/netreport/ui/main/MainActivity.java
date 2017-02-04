package com.orozco.netreport.ui.main;

import android.os.Bundle;
import android.view.View;

import com.orozco.netreport.R;
import com.orozco.netreport.ui.BaseActivity;
import com.skyfishjy.library.RippleBackground;

import butterknife.BindView;
import butterknife.OnClick;

import javax.inject.Inject;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class MainActivity extends BaseActivity implements MainPresenter.View {

    @Inject
    MainPresenter presenter;

    @BindView(R.id.main_view)
    MainView mainView;
    @BindView(R.id.content)
    RippleBackground rippleBackground;

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
    public void displayResults(String results) {
        mainView.setText(results);
        presenter.stopTest();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rippleBackground.stopRippleAnimation();
                    }
                });
            }
        });
        t.start();


    }
}
