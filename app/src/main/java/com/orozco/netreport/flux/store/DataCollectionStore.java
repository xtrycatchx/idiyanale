package com.orozco.netreport.flux.store;

import com.orozco.netreport.flux.Action;
import com.orozco.netreport.flux.AppError;
import com.orozco.netreport.flux.Store;
import com.orozco.netreport.model.Data;

import rx.Observable;

import static com.orozco.netreport.flux.action.DataCollectionActionCreator.DataCollectionAction.ACTION_COLLECT_DATA_F;
import static com.orozco.netreport.flux.action.DataCollectionActionCreator.DataCollectionAction.ACTION_COLLECT_DATA_S;
import static com.orozco.netreport.flux.action.DataCollectionActionCreator.DataCollectionAction.ACTION_SEND_DATA_F;
import static com.orozco.netreport.flux.action.DataCollectionActionCreator.DataCollectionAction.ACTION_SEND_DATA_S;

/**
 * @author A-Ar Andrew Concepcion
 */
public class DataCollectionStore extends Store<DataCollectionStore> {
    private Data data = null;
    private String action = null;
    private AppError error = null;

    public Observable<DataCollectionStore> observableWithFilter(String filter) {
        return observable().filter(store -> filter.equals(store.action));
    }

    private void updateState() {
        error = null;
    }

    private void updateData(Action action) {
        data = (Data) action.data();
    }

    private void updateError(Action action) {
        if (action.data() != null && action.data() instanceof AppError) {
            error = (AppError) action.data();
        }
    }

    private void updateAction(Action action) {
        this.action = action.type();
    }

    @Override
    protected void onReceiveAction(Action action) {
        switch (action.type()) {
            case ACTION_COLLECT_DATA_S:
            case ACTION_COLLECT_DATA_F:
            case ACTION_SEND_DATA_S:
            case ACTION_SEND_DATA_F:
                updateState();
                updateData(action);
                updateError(action);
                updateAction(action);
                notifyStoreChanged(this);
                break;
        }
    }

    public Data getData() {
        return data;
    }

    public String getAction() {
        return action;
    }

    public AppError getError() {
        return error;
    }
}
