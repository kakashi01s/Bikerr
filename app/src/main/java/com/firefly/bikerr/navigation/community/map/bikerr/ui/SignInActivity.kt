package com.firefly.bikerr.navigation.community.map.bikerr.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.model.Shop.ShopItems
import com.firefly.bikerr.navigation.community.map.bikerr.model.Users.Users
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class SignInActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var getotp : Button? = null
    var phoneNumber : TextView? = null
    lateinit var databaseRef : DatabaseReference
    var mobileNumber: String = ""
    var verificationID: String = ""
    var token_: String = ""
    var verifytext: TextView? = null
    var UserNumber : String? = null
    var countrycode : TextView? = null
    var progess : ProgressBar? = null
    var email : String? = null
    var username : String? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initviews()
        progess?.visibility = View.INVISIBLE
        auth = FirebaseAuth.getInstance()
        email = intent.getStringExtra("email")
        username = intent.getStringExtra("username")
        initializeUI()


    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initviews() {

        phoneNumber = findViewById(R.id.Phone_number)
        getotp = findViewById(R.id.get_otp)
        verifytext = findViewById(R.id.verify_text)
        countrycode = findViewById(R.id.country_code)
        progess = findViewById(R.id.progressBar)
    }



    private fun initializeUI() {
        getotp!!.setOnClickListener {
            mobileNumber = "+91"+phoneNumber!!.text.toString().trim()
            UserNumber = mobileNumber
            if (!mobileNumber.isEmpty()){
                progess?.visibility = View.VISIBLE

                logintask()

            }else{
                phoneNumber!!.hint = "Enter Your Phone Number"
                phoneNumber!!.setHintTextColor(resources.getColor(R.color.stream_ui_accent_red))
            }
        }
    }



    // phone login
    private fun logintask() {

        val mCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(p0)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("otp", p0.localizedMessage)
                Toast.makeText(this@SignInActivity, "Invalid PhoneNumber/Verification failed", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                progess!!.visibility = View.INVISIBLE
                verificationID = p0
                token_ = p1.toString()
                countrycode!!.visibility = View.INVISIBLE
                phoneNumber!!.setText("")
                verifytext!!.setText("Dont Worry!,Your Data is Safe With Us")
                phoneNumber!!.hint = "Enter OTP"
                getotp!!.text = "Verify OTP"


                getotp!!.setOnClickListener {
                    VerifyAuthentication(verificationID,phoneNumber!!.text.toString())
                }


                Log.e("Login : verificationId ", verificationID)
                Log.e("Login : token ", token_)
            }

            override fun onCodeAutoRetrievalTimeOut(verificationID: String) {
                super.onCodeAutoRetrievalTimeOut(verificationID)
                Toast.makeText(this@SignInActivity, "Verification Timeout", Toast.LENGTH_SHORT).show()
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(mobileNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallBacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        getotp!!.setOnClickListener {
            auth.signInWithCredential(credential)

                .addOnCompleteListener(this
                ) { p0 ->
                    if (p0.isSuccessful) {
                        val firebaseuser = p0.result.user!!.uid



                            databaseRef = FirebaseDatabase.getInstance().getReference("Users")
                            val Users = Users(mobileNumber,username,email,firebaseuser, "https://firebasestorage.googleapis.com/v0/b/bikerr-319612.appspot.com/o/bikerr.png?alt=media&token=f96de115-98f0-417c-8118-8f0060fc2bc2")
                            databaseRef.child(firebaseuser).setValue(Users).addOnSuccessListener {
                                Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_LONG).show()
                                Log.d("register","User Registered Successfully")
                            }.addOnFailureListener {
                                Toast.makeText(this, "User Registration Error", Toast.LENGTH_LONG).show()
                                Log.d("register", it.message.toString())
                            }
                            val intent = Intent(this, MainActivity::class.java).apply {
                                putExtra("username", username)
                            }

                            startActivity(intent)
                        }

                     else {
                        Toast.makeText(this, "Invalid Otp", Toast.LENGTH_SHORT).show()

                    }
                }

        }
    }
    private fun VerifyAuthentication(verificationID: String, otpText: String) {
        val phoneAuthCredential = PhoneAuthProvider.getCredential(verificationID,otpText)
        signInWithPhoneAuthCredential(phoneAuthCredential)

    }




}