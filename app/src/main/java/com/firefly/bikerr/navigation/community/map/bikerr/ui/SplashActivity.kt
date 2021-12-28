package com.firefly.bikerr.navigation.community.map.bikerr.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import coil.load
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    lateinit var mAuth : FirebaseAuth
    var splashimg : ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashimg = findViewById(R.id.splashimg)
        splashimg!!.load("https://firebasestorage.googleapis.com/v0/b/bikerr-319612.appspot.com/o/firefly.png?alt=media&token=ef181ecc-9ac5-4bb7-a0cc-6c362911ef2e") {
            crossfade(500)
        }
        mAuth = FirebaseAuth.getInstance()
        var user = mAuth.currentUser
        lifecycleScope.launch {
            delay(3500L)
            if (user != null) {
                val mapintent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(mapintent)
                finish()
            } else {
                val Onboardintent = Intent(this@SplashActivity, OnboardingActivity::class.java)
                startActivity(Onboardintent)
                finish()
            }

        }
    }
}