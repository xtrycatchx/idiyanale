package com.orozco.netreport.ui.main

import android.Manifest.permission.*
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import butterknife.OnClick
import cn.pedant.SweetAlert.SweetAlertDialog
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.Profile
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.github.pwittchen.reactivewifi.AccessRequester
import com.orozco.netreport.R
import com.orozco.netreport.flux.action.DataCollectionActionCreator
import com.orozco.netreport.flux.action.UserActionCreator
import com.orozco.netreport.flux.store.DataCollectionStore
import com.orozco.netreport.flux.store.UserStore
import com.orozco.netreport.model.Data
import com.orozco.netreport.post.api.RestAPI
import com.orozco.netreport.ui.BaseActivity
import com.orozco.netreport.utils.SharedPrefUtil
import kotlinx.android.synthetic.main.activity_main.*
import rx.Observable
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import java.net.InetAddress
import java.net.URI
import java.util.*
import javax.inject.Inject

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */
class MainActivity : BaseActivity() {
    override val layoutRes: Int = R.layout.activity_main

    @Inject lateinit internal var mDataCollectionActionCreator: DataCollectionActionCreator
    @Inject lateinit internal var mDataCollectionStore: DataCollectionStore
    @Inject lateinit internal var userActionCreator: UserActionCreator
    @Inject lateinit internal var userStore: UserStore
    private var pDialog: SweetAlertDialog? = null
    private val callbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }

    private fun requestCoarseLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(ACCESS_COARSE_LOCATION),
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION)
        }
    }

    private fun requestPhoneStatePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(READ_PHONE_STATE),
                    PERMISSIONS_REQUEST_READ_PHONE_STATE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION || requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                centerImage.performClick()
            }
            return
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)

        initFlux()
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onError(p0: FacebookException?) {
            }

            override fun onSuccess(p0: LoginResult?) {
                val profile = Profile.getCurrentProfile()
                userActionCreator.register(mapOf("username" to profile.id, "password" to profile.id))
            }

            override fun onCancel() {
            }
        } )

        isConnected.subscribe({
            SharedPrefUtil.retrieveTempData(this)?.let {
                postToServer(it)
            }
        }, Timber::e)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun initFlux() {
        addSubscriptionToUnsubscribe(mDataCollectionStore.observable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ store ->
                    when (store.action) {
                        DataCollectionActionCreator.ACTION_COLLECT_DATA_S -> {
                            resetView()
                            postToServer(store.data)
                        }
                        DataCollectionActionCreator.ACTION_SEND_DATA_S -> showShareResultDialog()
                        DataCollectionActionCreator.ACTION_SEND_DATA_F -> showErrorDialog()
                        DataCollectionActionCreator.ACTION_COLLECT_DATA_F -> resetView()
                    }
                }) { resetView() }
        )

        addSubscriptionToUnsubscribe(userStore.observable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when(it.action) {
                        UserActionCreator.ACTION_AUTHENTICATE_S -> prepareAndBeginTest()
                        UserActionCreator.ACTION_REGISTER_S -> prepareAndBeginTest()
                        UserActionCreator.ACTION_AUTHENTICATE_F -> resetView()
                        UserActionCreator.ACTION_REGISTER_F -> {
                            // register failed, maybe it is already registered? Try authenticating
                            val profile = Profile.getCurrentProfile()
                            userActionCreator.authenticate(mapOf("username" to profile.id, "password" to profile.id))
                        }
                    }
                }))
    }

    private fun showShareResultDialog() {
        val result = mDataCollectionStore.data?.toString(this@MainActivity)
        pDialog?.setTitleText("Sent! Here's your data")
                ?.setCancelText("I'm Done")
                ?.setCancelClickListener({ it.dismissWithAnimation() })
                ?.setConfirmText("Share Results")
                ?.setContentText(result)
                ?.setConfirmClickListener { dialog ->
                    if (ShareDialog.canShow(ShareLinkContent::class.java)) {
                        val linkContent = ShareLinkContent.Builder()
                                .setContentTitle("My BASS Results")
                                .setImageUrl(Uri.parse("https://scontent.fmnl4-6.fna.fbcdn.net/v/t1.0-9/17796714_184477785394716_1700205285852495439_n.png?oh=40acf149ffe8dcc0e24e60af7f844514&oe=595D6465"))
                                .setContentDescription(result)
                                .setContentUrl(Uri.parse("https://bass.bnshosting.net/device"))
                                .setShareHashtag(ShareHashtag.Builder()
                                        .setHashtag("#BASSparaSaBayan")
                                        .build())
                                .build()

                        ShareDialog.show(this@MainActivity, linkContent)
                    }
                }
                ?.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        // TODO: Don't treat shared prefs as database
        SharedPrefUtil.clearTempData(this)
        resetView()
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this)
                .setTitle("Error : ${mDataCollectionStore.error?.statusCode}")
                .setMessage(mDataCollectionStore.error?.errorMessage)
                .show()
    }

    @OnClick(R.id.centerImage)
    fun onCenterImageClicked() {
        if (rippleBackground.isRippleAnimationRunning) {
            endTest()
        } else {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
        }
    }

    private fun prepareAndBeginTest() {
        Answers.getInstance().logCustom(CustomEvent("Begin Test"))
        reportText.visibility = View.INVISIBLE
        centerImage.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.signal_on))
        rippleBackground.startRippleAnimation()

        runOnUiThreadIfAlive(Runnable { this.beginTest() }, 1000)
    }

    fun beginTest() {
        val fineLocationPermissionNotGranted = ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED
        val coarseLocationPermissionNotGranted = ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED
        val phoneStatePermissionNotGranted = ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PERMISSION_GRANTED

        if (fineLocationPermissionNotGranted && coarseLocationPermissionNotGranted) {
            requestCoarseLocationPermission()
            endTest()
            return
        }
        if (phoneStatePermissionNotGranted) {
            requestPhoneStatePermission()
            endTest()
            return
        }

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN) {
            val provider = Settings.Secure.getString(contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
            if (!provider.contains(LocationManager.GPS_PROVIDER)) {
                runOnUiThread { AccessRequester.requestLocationAccess(this) }
                endTest()
                return
            }
        } else {
            if (!AccessRequester.isLocationEnabled(this)) {
                runOnUiThread { AccessRequester.requestLocationAccess(this) }
                endTest()
                return
            }
        }

        mDataCollectionActionCreator!!.collectData()
    }

    fun endTest() {
        runOnUiThread { this.resetView() }
    }

    fun resetView() {
        reportText.visibility = View.VISIBLE
        rippleBackground.stopRippleAnimation()
        centerImage.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.signal))
    }

    fun postToServer(data: Data?) {
        data?.let {
            SharedPrefUtil.saveTempData(this, it)
            pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            pDialog?.progressHelper?.barColor = Color.parseColor("#A5DC86")
            pDialog?.titleText = "Loading"
            pDialog?.setCancelable(false)
            pDialog?.show()
            mDataCollectionActionCreator.sendData(it)
        }
    }

    // TODO: Can be improved
    val isConnected: Single<Boolean>
        get() = Observable.fromCallable { InetAddress.getByName(URI.create(RestAPI.BASE_URL).host) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { inetAddress -> inetAddress != null }
                .toSingle()

    override fun onDestroy() {
        super.onDestroy()
        if (pDialog != null && pDialog!!.isShowing) {
            pDialog!!.dismiss()
        }
    }

    companion object {

        private val PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1000
        private val PERMISSIONS_REQUEST_READ_PHONE_STATE = 1001
    }
}
