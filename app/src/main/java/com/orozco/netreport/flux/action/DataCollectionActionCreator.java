package com.orozco.netreport.flux.action;

import android.support.annotation.StringDef;

import com.orozco.netreport.flux.Action;
import com.orozco.netreport.flux.Dispatcher;
import com.orozco.netreport.flux.Utils;
import com.orozco.netreport.flux.model.DataCollectionModel;
import com.orozco.netreport.model.Data;

import java.lang.annotation.Retention;

import static com.orozco.netreport.flux.action.DataCollectionActionCreator.DataCollectionAction.ACTION_COLLECT_DATA_F;
import static com.orozco.netreport.flux.action.DataCollectionActionCreator.DataCollectionAction.ACTION_COLLECT_DATA_S;
import static com.orozco.netreport.flux.action.DataCollectionActionCreator.DataCollectionAction.ACTION_SEND_DATA_F;
import static com.orozco.netreport.flux.action.DataCollectionActionCreator.DataCollectionAction.ACTION_SEND_DATA_S;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author A-Ar Andrew Concepcion
 */
public class DataCollectionActionCreator {

    private final Dispatcher mDispatcher;
    private final Utils mUtils;
    private final DataCollectionModel mModel;

    public DataCollectionActionCreator(Dispatcher dispatcher, Utils utils, DataCollectionModel model) {
        mDispatcher = dispatcher;
        mUtils = utils;
        mModel = model;
    }

    @StringDef(value = {
            ACTION_COLLECT_DATA_S,
            ACTION_COLLECT_DATA_F,
    })
    @Retention(SOURCE)
    public @interface DataCollectionAction {
        final String ACTION_COLLECT_DATA_S = "ACTION_COLLECT_DATA_S";
        final String ACTION_COLLECT_DATA_F = "ACTION_COLLECT_DATA_F";
        final String ACTION_SEND_DATA_S = "ACTION_SEND_DATA_S";
        final String ACTION_SEND_DATA_F = "ACTION_SEND_DATA_F";
    }

    public void collectData() {
        mModel.executeNetworkTest().toObservable()
                .subscribe(data -> {
                    mDispatcher.dispatch(Action.create(ACTION_COLLECT_DATA_S, data));
                }, throwable -> {
                    mDispatcher.dispatch(Action.create(ACTION_COLLECT_DATA_F, mUtils.getError(throwable)));
                });
    }

    public void sendData(Data data) {
        mModel.sendData(data)
                .subscribe(mVoid -> {
                    mDispatcher.dispatch(Action.create(ACTION_SEND_DATA_S, data));
                }, throwable -> {
                    mDispatcher.dispatch(Action.create(ACTION_SEND_DATA_F, mUtils.getError(throwable)));
                });
    }
}
