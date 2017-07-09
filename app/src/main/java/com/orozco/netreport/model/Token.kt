package com.orozco.netreport.model

import com.google.gson.annotations.SerializedName

/**
 * @author A-Ar Andrew Concepcion
 * @createdOn 07/07/2017
 */

data class Token(@SerializedName("access_token") val accessToken: String,
                 @SerializedName("token_type") val tokenType: String = "bearer",
                 @SerializedName("refresh_token") val refreshToken: String,
                 @SerializedName("expires_in") val expiresIn: Long,
                 @SerializedName("scope") val scope: String)