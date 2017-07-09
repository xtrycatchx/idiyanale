package com.orozco.netreport.flux.store

import com.orozco.netreport.flux.Action
import com.orozco.netreport.flux.AppError
import com.orozco.netreport.flux.Store
import com.orozco.netreport.flux.action.UserActionCreator
import com.orozco.netreport.model.Token
import rx.Observable

/**
 * @author A-Ar Andrew Concepcion
 */
class UserStore : Store<UserStore>() {
    var token: Token? = null
        private set
    var action: String? = null
        private set
    var error: AppError? = null
        private set

    fun observableWithFilter(filter: String): Observable<UserStore> {
        return observable().filter { store -> filter == store.action }
    }

    private fun updateState() {
        error = null
    }

    private fun updateData(action: Action) {
        if (action.data() is Token) {
            token = action.data() as Token?
        }
    }

    private fun updateError(action: Action) {
        if (action.data() != null && action.data() is AppError) {
            error = action.data() as AppError?
        }
    }

    private fun updateAction(action: Action) {
        this.action = action.type()
    }

    override fun onReceiveAction(action: Action) {
        when (action.type()) {
            UserActionCreator.ACTION_AUTHENTICATE_S,
            UserActionCreator.ACTION_AUTHENTICATE_F,
            UserActionCreator.ACTION_REGISTER_S,
            UserActionCreator.ACTION_REGISTER_F -> {
                updateState()
                updateData(action)
                updateError(action)
                updateAction(action)
                notifyStoreChanged(this)
            }
        }
    }
}
