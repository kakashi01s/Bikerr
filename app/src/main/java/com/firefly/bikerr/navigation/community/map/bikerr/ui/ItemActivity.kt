package com.firefly.bikerr.navigation.community.map.bikerr.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.model.Cart.CartItem
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

class ItemActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener  {
    var sizespinner : Spinner? = null
    var quantityspinner : Spinner? = null
    var size : String? = null
    var itmname : String? = null
    var itmprice : String? = null
    var itmimg : String? = null
    var ItemmImg : ImageView? = null
    var ItemmTitle :TextView? = null
    var ItemmPrice : TextView? = null
    var btnaddtocart : Button? = null
    var opencart : ImageView? = null
    var itembanner1 : AdView? = null
    var itembanner2 : AdView? = null
    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        initViews()
        itmname = intent.getStringExtra("itemname")
        itmprice = intent.getStringExtra("itemprice")
        itmimg = intent.getStringExtra("itemimg")
        setitemspecs()
        LoadAds()

        if (sizespinner!= null)
        {

            ArrayAdapter.createFromResource(
                this,
                R.array.size,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                sizespinner!!.adapter = adapter
                sizespinner!!.onItemSelectedListener = this
            }

        }
        if (quantityspinner!= null)
        {
            ArrayAdapter.createFromResource(
                this,
                R.array.Quantity,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                quantityspinner!!.adapter = adapter
                quantityspinner!!.onItemSelectedListener = this

            }

        }
        btnaddtocart!!.setOnClickListener {
            sendDatatoFirebase()
        }
        opencart!!.setOnClickListener {
            val intent = Intent(this , CartActivity::class.java)
            startActivity(intent)
        }

    }

    private fun LoadAds() {
        val adrequest = AdRequest.Builder().build()
        itembanner1!!.loadAd(adrequest)

        val adrequest2 = AdRequest.Builder().build()
        itembanner2!!.loadAd(adrequest2)
    }

    private fun sendDatatoFirebase() {
        val newprice = itmprice!!.toInt()
        val quantity = Integer.parseInt(quantityspinner!!.selectedItem.toString())
        val cartprice = newprice * quantity
        val cartitem = CartItem(itmname,cartprice,itmimg, quantityspinner!!.selectedItem.toString(), sizespinner!!.selectedItem.toString())
        val database = FirebaseDatabase.getInstance()
        val databaseRef = database.getReference("Cart")
        databaseRef.child(uid!!).child(itmname!!).setValue(cartitem)
            .addOnSuccessListener {
                Toast.makeText(this, " Item Added to Cart", Toast.LENGTH_SHORT).show()
                Log.d("addtocart", "Item Added to Cart")}
            .addOnFailureListener { Log.d("addtocart", it.message.toString()) }
    }

    private fun setitemspecs() {
        Glide.with(this)
            .load(itmimg)
            .into(ItemmImg!!)
        ItemmTitle!!.text = itmname
        ItemmPrice!!.text = itmprice
    }

    private fun initViews() {
        sizespinner = findViewById(R.id.itemsizeSpinner)
        quantityspinner = findViewById(R.id.itemQuantityspinner)
        ItemmImg = findViewById(R.id.ItemImg)
        ItemmPrice = findViewById(R.id.ItemPriceET)
        ItemmTitle = findViewById(R.id.Itemtitle)
        btnaddtocart = findViewById(R.id.button_addtocart)
        opencart = findViewById(R.id.open_cart2)
        itembanner1 = findViewById(R.id.itemBanneradView1)
        itembanner2 = findViewById(R.id.itemBanneradView2)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        size = p0!!.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Toast.makeText(this, "Please select a size and Quantity", Toast.LENGTH_SHORT).show()
    }
}