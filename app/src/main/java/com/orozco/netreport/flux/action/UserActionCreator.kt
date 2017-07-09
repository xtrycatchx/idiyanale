package com.orozco.netreport.flux.action

import com.orozco.netreport.flux.Action
import com.orozco.netreport.flux.Dispatcher
import com.orozco.netreport.flux.Utils
import com.orozco.netreport.flux.model.UserModel

/**
 * @author A-Ar Andrew Concepcion
 */
class UserActionCreator(private val mDispatcher: Dispatcher, private val mUtils: Utils, private val mModel: UserModel) {

    companion object {
        const val ACTION_AUTHENTICATE_S = "ACTION_AUTHENTICATE_S"
        const val ACTION_AUTHENTICATE_F = "ACTION_AUTHENTICATE_F"
        const val ACTION_REGISTER_S = "ACTION_REGISTER_S"
        const val ACTION_REGISTER_F = "ACTION_REGISTER_F"
    }

    fun register(credentials: Map<String, String>) {
        mModel.register(credentials)
                .flatMap { mModel.authenticate(credentials) }
                .subscribe(
                        { token -> mDispatcher.dispatch(Action.create(ACTION_REGISTER_S, token)) })
                        { throwable -> mDispatcher.dispatch(Action.create(ACTION_REGISTER_F, mUtils.getError(throwable))) }
    }

    fun authenticate(credentials: Map<String, String>) {
        mModel.authenticate(credentials)
                .subscribe(
                        { token -> mDispatcher.dispatch(Action.create(ACTION_AUTHENTICATE_S, token)) })
                        { throwable -> mDispatcher.dispatch(Action.create(ACTION_AUTHENTICATE_F, mUtils.getError(throwable))) }
    }
}
