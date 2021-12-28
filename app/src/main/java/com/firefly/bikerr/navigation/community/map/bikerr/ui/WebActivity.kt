package com.firefly.bikerr.navigation.community.map.bikerr.ui

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.Utils.Constants
import com.google.android.gms.ads.AdView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class WebActivity : AppCompatActivity() {

    var webView: WebView? = null
    var appTitle: String? = null
    var appUrl: String? = null
    var appIcon: String? = null

    var rlWebSplash: RelativeLayout? = null
    var ivAppIcon: ImageView? = null

    private val TAG: String = WebActivity::class.java.simpleName
    private val TIME_DELAY = 2000
    private var back_pressed: Long = 0


    private var adView: AdView? = null
    lateinit var fullscreenView: View

    var firebaseRemoteConfig: FirebaseRemoteConfig? = null

    var mGeoLocationRequestOrigin: String? = null
    var mGeoLocationCallback: GeolocationPermissions.Callback? = null

    private val LOCATION_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        initViews()
        initData()

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        if(firebaseRemoteConfig!!.getBoolean(Constants().SHOW_ADS)){
//            onLoadFbInterstitial()
        }
        loadWebSplash()

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE
        )

        webViewSettings()

        webView?.loadUrl(appUrl!!)

    }

    fun initViews(){
        webView = findViewById(R.id.webViewMain)
        rlWebSplash = findViewById(R.id.rlWebSplash)
        ivAppIcon = findViewById(R.id.ivAppIcon)
    }

    fun initData(){
        val bundle: Bundle? = intent.extras
        appUrl = bundle?.getString("url")
        appIcon = bundle?.getString("app_icon")
        Log.d("TAG", "initData: " + bundle?.getString("url"))

    }

    fun webViewSettings(){
        webView!!.settings.loadsImagesAutomatically = true
        webView!!.settings.javaScriptEnabled = true
        webView!!.settings.allowContentAccess = true

        webView!!.settings.useWideViewPort = true
        webView!!.settings.loadWithOverviewMode = true
        webView!!.settings.domStorageEnabled = true
        webView!!.clearView()
        webView!!.isHorizontalScrollBarEnabled = false
        webView!!.settings.setAppCacheEnabled(true)
        webView!!.settings.databaseEnabled = true
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            webView!!.settings.databasePath = "/data/data/" + this.packageName + "/databases/"
        }
        webView!!.isVerticalScrollBarEnabled = false
        webView!!.settings.builtInZoomControls = true
        webView!!.settings.displayZoomControls = false
        webView!!.settings.allowFileAccess = true
        webView!!.settings.pluginState = WebSettings.PluginState.OFF
        webView!!.isScrollbarFadingEnabled = false
        webView!!.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView!!.settings.defaultZoom = WebSettings.ZoomDensity.FAR
        webView!!.webViewClient = WebViewClient()
        webView!!.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        webView!!.setInitialScale(1)

        webView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.url.toString())
                }
                return false
            }
        }

        webView!!.webChromeClient = object : WebChromeClient(){

            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback?
            ) {
                if (ContextCompat.checkSelfPermission(
                        this@WebActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this@WebActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        AlertDialog.Builder(this@WebActivity)
                            .setMessage("Please turn ON the GPS to make app work smoothly")
                            .setNeutralButton(
                                android.R.string.ok,
                                DialogInterface.OnClickListener { dialogInterface, i ->
                                    mGeoLocationCallback = callback
                                    mGeoLocationRequestOrigin = origin
                                    ActivityCompat.requestPermissions(
                                        this@WebActivity,
                                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                        1001
                                    )

                                })
                            .show()

                    } else {
                        //no explanation need we can request the locatio
                        mGeoLocationCallback = callback
                        mGeoLocationRequestOrigin = origin
                        ActivityCompat.requestPermissions(
                            this@WebActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1001
                        )
                    }
                } else {
                    //tell the webview that permission has granted
                    callback!!.invoke(origin, true, true)
                }
            }


            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if(newProgress >= 80){
                    rlWebSplash!!.visibility = View.GONE
                }
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1001 -> {
                //if permission is cancel result array would be empty
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    if (mGeoLocationCallback != null) {
                        mGeoLocationCallback!!.invoke(mGeoLocationRequestOrigin, true, true)
                    }
                } else {
                    //permission denied
                    if (mGeoLocationCallback != null) {
                        mGeoLocationCallback!!.invoke(mGeoLocationRequestOrigin, false, false)
                    }
                }
            }
        }

    }

    fun loadWebSplash(){
        Glide.with(ivAppIcon!!.context)
            .load(appIcon)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(ivAppIcon!!)
    }




    public override fun onPause() {
//        adView.pause()
        super.onPause()
    }

    // Called when returning to the activity
    public override fun onResume() {
        super.onResume()
//        adView.resume()
    }

    // Called before the activity is destroyed
    public override fun onDestroy() {
        if(adView!=null){
            adView!!.destroy()
        }
        super.onDestroy()
    }

//    override fun onBackPressed() {
//
//        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
//            super.onBackPressed()
//        } else {
//            Toast.makeText(getBaseContext(), "Double click to exit!",
//                Toast.LENGTH_SHORT).show();
//            if (webView!!.canGoBack()) {
//                webView!!.goBack()
//            }
//            else{
//                if (interstitialFbAd!=null && interstitialFbAd!!.isAdLoaded) {
//                    if (interstitialFbAd!!.isAdInvalidated) {
//                    } else {
//                        interstitialFbAd!!.show()
//                    }
//                }
//                else{
//                    super.onBackPressed()
//                }
//
//            }
//        }
//        back_pressed = System.currentTimeMillis();
//
//    }
//

}