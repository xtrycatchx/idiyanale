package com.orozco.netreport.flux;

import android.support.annotation.Nullable;

/**
 * @author A-Ar Andrew Concepcion
 */
public class Action {

    private final String type;
    private final Object data;

    public static final String ACTION_NO_ACTION = "ACTION_NO_ACTION";

    public Action(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    public static Action create(String type, @Nullable Object data) {
        return new Action(type, data);
    }

    /**
     * Type of the action
     */
    public String type() {
        return type;
    }

    /**
     * Payload of the action
     */
    @Nullable
    public Object data() {
        return data;
    }
}
