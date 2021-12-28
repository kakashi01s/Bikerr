package com.firefly.bikerr.navigation.community.map.bikerr.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    var nextbtn: Button? = null
    var sname : EditText? = null
    var sEmail : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        initviews()
        nextbtn!!.setOnClickListener {
            val username = sname!!.text.toString()
            val email = sEmail!!.text.toString()
            val intent = Intent(this, SignInActivity::class.java)
            intent.putExtra("username",username)
            intent.putExtra("email",email)
            startActivity(intent)
            finish()
            Toast.makeText(this, email,Toast.LENGTH_SHORT).show()
        }


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
        nextbtn = findViewById(R.id.next_btn)
        sname = findViewById(R.id.signup_name)
        sEmail = findViewById(R.id.signup_Email)

    }


}