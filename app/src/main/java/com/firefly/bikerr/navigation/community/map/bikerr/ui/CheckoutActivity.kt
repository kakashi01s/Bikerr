package com.firefly.bikerr.navigation.community.map.bikerr.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.firefly.bikerr.navigation.community.map.bikerr.ApiInterface.RazorpayApiInterface
import com.firefly.bikerr.navigation.community.map.bikerr.model.Order
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.model.Cart.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.GsonBuilder
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.StringBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CheckoutActivity : AppCompatActivity() , PaymentResultWithDataListener {
    var db = FirebaseDatabase.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    var orderitemList : ArrayList<CartItem>? = ArrayList()
    var orderitems : TextView? = null
    var cartttl : TextView? = null
    var payment : Button? = null
    var orderphnumber : EditText? = null
    var ordername : EditText? = null
    var orderAddress : EditText? = null

    lateinit var retrofit: Retrofit
    lateinit var retroInterface: RazorpayApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        initViews()
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build())
        getorderitems()
        var total = intent.getLongExtra("total", 0)
        cartttl?.setText(total.toString())
        Checkout.preload(applicationContext)
        val gson = GsonBuilder().setLenient()
        retrofit = Retrofit.Builder()
            .baseUrl("http://13.233.73.219:80")
            .addConverterFactory(GsonConverterFactory.create(gson.create()))
            .build()

        retroInterface = retrofit.create(RazorpayApiInterface::class.java)


        if (!ordername?.text.isNullOrEmpty() && !orderphnumber?.text.isNullOrEmpty())
        {
            Toast.makeText(this, "Please Fill All the Details before checking out",Toast.LENGTH_SHORT).show()

        }else
        {
            payment?.setOnClickListener {
                if (!orderAddress!!.text.isNullOrEmpty()) {
                    val amount = total.toString()
                    if (amount.isEmpty()) {
                        return@setOnClickListener
                    }
                    getOrderId(amount)
                }
                else{
                    orderAddress!!.setHintTextColor(resources.getColor(R.color.stream_ui_accent_red))
                    orderAddress!!.setHint("Please Enter Your Address Before checking Out(with postal code)")
                }
            }
        }

    }


    private fun initViews() {
        orderitems = findViewById(R.id.allorderitems)
        cartttl = findViewById(R.id.cart_total)
        payment = findViewById(R.id.proceed_to_payment)
        orderAddress = findViewById(R.id.fullAddress)
        ordername = findViewById(R.id.fullName)
        orderphnumber = findViewById(R.id.phNumber)
    }

    private fun getOrderId(amount: String) {

        val map = HashMap<String, String>()
        map["amount"] = amount

        retroInterface
            .getOrderId(map).enqueue(object : Callback<Order> {
                override fun onFailure(call: Call<Order>, t: Throwable) {
                    Toast.makeText(this@CheckoutActivity, t.message, Toast.LENGTH_LONG).show()
                }
                override fun onResponse(call: Call<Order>, response: Response<Order>) {
                    if (response.body() != null)
                        initiatePayment(amount, response.body()!!)
                }
            })
    }


    private fun initiatePayment(amount: String, order: Order) {

        val activity:Activity = this
        val co = Checkout()
        var amounttopay = "${amount}00"
        try {
            val options = JSONObject()
            options.put("name","Bikerr")
            options.put("description","Lets Complete Your Payment")
            options.put("theme.color", "#3399cc")
            options.put("currency","INR")
            options.put("order_id", order.getOrderId())
            options.put("amount","$amounttopay")//pass amount in currency subunits
            co.setKeyID(order.getKeyId())
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }

    override fun onPaymentSuccess(p0: String?, p1:PaymentData?) {
        Log.d("sucessss", p0.toString())
        Log.d("sucessss", p1!!.orderId.toString())
        Log.d("sucessss", p1!!.signature.toString())
        Log.d("sucessss", p1!!.paymentId.toString())
        val map = HashMap<String, String>()
        map["orderId"] = p0.toString()
        map["payment_Id"] = p1.paymentId.toString()
        map["signature"] = p1.signature.toString()
        map["name"] = ordername!!.text.toString()
        map["number"] = orderphnumber!!.text.toString()
        map["address"] = orderAddress!!.text.toString()
        map["items"] = orderitems?.text.toString()


        db.getReference("Users").child(uid!!).get()
            .addOnSuccessListener {
                val emailId = it.child("email").value.toString()
                val customermap = HashMap<String, String>()
                customermap["orderId"] = p0.toString()
                customermap["name"] = ordername!!.text.toString()
                customermap["Phonenumber"] = orderphnumber!!.text.toString()
                customermap["Address"] = orderAddress!!.text.toString()
                customermap["Items"] = orderitems?.text.toString()
                customermap["email"] = emailId

                retroInterface.updateTransaction(map).enqueue(object : Callback<String> {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.body().equals("success")) {
                            Toast.makeText(
                                this@CheckoutActivity,
                                "Payment successful",
                                Toast.LENGTH_LONG
                            ).show()

                            val current = LocalDateTime.now()
                            val formatter = DateTimeFormatter.BASIC_ISO_DATE
                            val formatted = current.format(formatter)
                            db.getReference("ConfirmedOrders").child("$formatted/$p0").setValue(customermap)
                            db.getReference("Cart").child(uid!!).setValue(null)
                            retroInterface.sendEmail(customermap).enqueue(object : Callback<String>{
                                override fun onResponse(
                                    call: Call<String>,
                                    response: Response<String>
                                ) {
                                    Log.d("Emailconfirm", "Email confirm")
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Log.d("Emailconfirm", "Email fail")
                                }

                            })
                            val intent = Intent(this@CheckoutActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(this@CheckoutActivity, t.message, Toast.LENGTH_LONG).show()
                    }
                })
            }
    }
    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Payment failed", Toast.LENGTH_LONG).show()
    }



    private fun getorderitems() {
        db.getReference("Orders").child(uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists())
                    {
                        var sb = StringBuilder()
                        for (i in snapshot.children)
                        {
                            val items = i.getValue(CartItem::class.java)
                            orderitemList!!.add(items!!)
                        }
                        for (i in orderitemList!!)
                        {
                            sb.append("Name:${i.name}\n-Quantity:${i.quantity}-Price:${i.price}-Size:${i.Size}-Quantity:${i.quantity}\n")
                            Log.d("pricee", i.price.toString())
                        }
                        orderitems!!.setText(sb)
                        Log.d("cartitems",orderitemList.toString())

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("cartitems", "loadPost:onCancelled", error.toException())
                }
            })
    }







}