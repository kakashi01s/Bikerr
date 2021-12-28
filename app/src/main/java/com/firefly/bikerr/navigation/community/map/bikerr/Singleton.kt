package com.firefly.bikerr.navigation.community.map.bikerr

import android.app.Application
import android.util.Log
import com.firefly.bikerr.navigation.community.map.bikerr.data.DataFactory
import com.firefly.bikerr.navigation.community.map.bikerr.data.DataService
import com.google.android.gms.ads.MobileAds
import com.google.android.libraries.places.api.Places
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.onesignal.OneSignal
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.livedata.ChatDomain
import io.reactivex.Scheduler
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import java.util.*
const val ONESIGNAL_APP_ID = "bcca4e19-c947-4f12-875e-f85dcf87d63d"
class Singleton : Application() {
    companion object{

        var application: Singleton? = null

        fun get():Singleton? {
            return application
        }
    }


    override fun onCreate() {
        super.onCreate()


        // Set up the client for API calls and the domain for offline storage
        val client = ChatClient.Builder("az987pt7e6m5", applicationContext)
            .logLevel(ChatLogLevel.ALL) // Set to NOTHING in prod
            .build()
        ChatDomain.Builder(client, applicationContext).build()

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_maps_key), Locale.US);
        }

        RxJavaPlugins.setErrorHandler { throwable: Throwable ->
            Log.e(
                "TAG",
                "onCreate: " + throwable.message
            )
        }

        MobileAds.initialize(this){}


    }






}