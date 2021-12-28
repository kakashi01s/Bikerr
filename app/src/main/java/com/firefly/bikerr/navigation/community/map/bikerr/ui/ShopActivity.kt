package com.firefly.bikerr.navigation.community.map.bikerr.ui

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import com.firefly.bikerr.navigation.community.map.bikerr.R
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firefly.bikerr.navigation.community.map.bikerr.adapters.ShopAdapter
import com.firefly.bikerr.navigation.community.map.bikerr.model.Cart.CartItem
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


class ShopActivity : AppCompatActivity(), ShopAdapter.ShopitemClickListener {
    var db = FirebaseFirestore.getInstance()
    var data : String? = null
    var shopAdapter : ShopAdapter? = null
    var rvShop : RecyclerView? = null

    val uid = FirebaseAuth.getInstance().currentUser?.uid
    private var shopheading : TextView? = null
    var shopbannerad2 :AdView? = null
    var opencart1 : ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        initViews()
        data = intent.getStringExtra("shopItem")
        getShopItems(data)
        LoadAds()
        opencart1!!.setOnClickListener {
            val intent = Intent(this , CartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViews() {
        rvShop = findViewById(R.id.rvshop)
        shopheading = findViewById(R.id.shop_title)
        opencart1 = findViewById(R.id.open_cart1)
        shopbannerad2 = findViewById(R.id.shopBanneradView2)

    }
    private fun LoadAds() {
        val adrequest = AdRequest.Builder().build()
        shopbannerad2!!.loadAd(adrequest)



    }

    private fun getShopItems(data: String?) {
        db.collection(data!!)
            .get()
            .addOnSuccessListener { result ->
                val shopitems = result.documents
                Log.d("ShopItems", shopitems.toString())
                shopAdapter = ShopAdapter(this,shopitems , this)
                rvShop?.adapter = shopAdapter
                rvShop?.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL,false)
                shopheading!!.text = data

            }
            .addOnFailureListener { exception ->
                Log.d("ShopItems", "Error getting documents: ", exception)
            }
    }

    override fun OnShopCardClick(position: Int, itemname: String, itemprice: Any?, itemimg:String) {
        val price = itemprice.toString()
        val intent = Intent(this, ItemActivity::class.java)
        intent.putExtra("itemname",itemname)
        intent.putExtra("itemimg",itemimg)
        intent.putExtra("itemprice",price)
        startActivity(intent)


    }
}