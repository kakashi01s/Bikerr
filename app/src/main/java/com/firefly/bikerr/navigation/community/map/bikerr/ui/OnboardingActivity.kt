package com.firefly.bikerr.navigation.community.map.bikerr.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.google.firebase.auth.FirebaseAuth


class OnboardingActivity : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //slide 1
        addSlide(
            AppIntroFragment.newInstance(
                title = "Welcome Mate!",
                description = "Bikerr is A Community App\n We Hope You Grow with Us\nLet's Get You Started!",
                imageDrawable = R.drawable.applogopng,
                titleColor = Color.BLACK,
                descriptionColor = Color.BLACK,
                backgroundColor = Color.LTGRAY,
                titleTypefaceFontRes = R.font.hemihead,
        ))
        //slide 2
        addSlide(AppIntroFragment.newInstance(
            title = "CHAT",
            description = "We Offer You a Chat Experience \n Where You can create Biking Groups/Join Groups to Know More People Around You ",
            imageDrawable = R.drawable.introchat,
            titleColor = Color.BLACK,
            descriptionColor = Color.BLACK,
            backgroundColor = Color.LTGRAY,
            titleTypefaceFontRes = R.font.hemihead,
        ))
        //slide 3
        addSlide(AppIntroFragment.newInstance(
            title = "Nearby",
            description = "Gets You the Nearby Fuel Stations and Garages in Just One click!",
            imageDrawable = R.drawable.intronearby,
            titleColor = Color.BLACK,
            descriptionColor = Color.BLACK,
            backgroundColor = Color.LTGRAY,
            titleTypefaceFontRes = R.font.hemihead,
        ))
        //slide 4
        addSlide(AppIntroFragment.newInstance(
            title = "SHOP",
            description = "We Provide You TOP Quality Accessories needed for your Daily Riding Needs, May it be Safety or Drip, We got you Covered!",
            imageDrawable = R.drawable.cart_logo,
            titleColor = Color.BLACK,
            descriptionColor = Color.BLACK,
            backgroundColor = Color.LTGRAY,
            titleTypefaceFontRes = R.font.hemihead,
        ))
        //slide 5
        addSlide(AppIntroFragment.newInstance(
            title = "Consent Matters",
            description = "To make the App work with full Efficiency, We require a Few Permissions \n(NOTE: We Don't Store or Share Any User Data with Anybody.)",
            imageDrawable = R.drawable.intropermission,
            titleColor = Color.BLACK,
            descriptionColor = Color.BLACK,
            backgroundColor = Color.LTGRAY,
            titleTypefaceFontRes = R.font.hemihead,
        ))
        //slide 6
        addSlide(AppIntroFragment.newInstance(
            title = "Last But Not Least",
            description = "Being a Bikerr, Its already Understandable to keep the App clean from Toxic and Vulgar Stuff, If you see something inappropriate please Flag the message or share with us.",
            imageDrawable = R.drawable.introimp,
            titleColor = Color.BLACK,
            descriptionColor = Color.BLACK,
            backgroundColor = Color.LTGRAY,
            titleTypefaceFontRes = R.font.hemihead,
        ))
        askForPermissions(
            permissions = arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            slideNumber = 5,
            required = false)

    }
    override fun onStart() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        super.onStart()
        if (uid?.isNotEmpty() == true)
        {
            val intent = Intent(this , MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }



}