package com.orozco.netreport.flux.model

import com.orozco.netreport.model.Token
import com.orozco.netreport.post.api.RestAPI
import rx.Observable

/**
 * @author A-Ar Andrew Concepcion
 */
class UserModel(private val mRestApi: RestAPI) {
    fun authenticate(credentials: Map<String, String>): Observable<Token> {
        return mRestApi.authenticate(credentials)
    }

    fun register(credentials: Map<String, String>): Observable<Unit> {
        return mRestApi.register(credentials)
    }
}
