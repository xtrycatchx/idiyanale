package com.orozco.netreport.ui.main;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.orozco.netreport.R;
import com.orozco.netreport.ui.BaseView;

import butterknife.BindView;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */
// TODO: This doesn't serve any purpose, just move it to the main layout
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
        setButtonVisibility(VISIBLE);
    }


    public void setButtonVisibility(int visibility) {
        reportBtn.setVisibility(visibility);
    }
}
