package com.orozco.netreport.ui.main;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.orozco.netreport.R;
import com.orozco.netreport.post.api.ApiUtils;
import com.orozco.netreport.ui.BaseView;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class MainView extends BaseView {
    @BindView(R.id.reportBtn)
    Button reportBtn;

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getViewComponent().inject(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.view_main;
    }

    public void setText(String text) {
        reportBtn.setText(text);
        setButtonvisibility(VISIBLE);
    }


    public void setButtonvisibility(int visibility) {
        reportBtn.setVisibility(visibility);
    }
}
