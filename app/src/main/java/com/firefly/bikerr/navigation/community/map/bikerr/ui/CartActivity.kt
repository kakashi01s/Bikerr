package com.firefly.bikerr.navigation.community.map.bikerr.ui


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.adapters.CartAdapter
import com.firefly.bikerr.navigation.community.map.bikerr.model.Cart.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartActivity : AppCompatActivity(), CartAdapter.cartlitemClickListener {
    var db = FirebaseDatabase.getInstance()
    var rv_Cart : RecyclerView? = null
    var cartAdapter : CartAdapter? = null
    var cartitemList : ArrayList<CartItem>? = ArrayList()
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    var checkout : Button? = null


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)


        initViews()
        getCartItems()

    }



    private fun initViews() {
        rv_Cart = findViewById(R.id.rvCart)
        checkout = findViewById(R.id.checkout_button)

    }



    private fun sendDatatoorders() {
        var total : Long = 0
        for (i in cartitemList!!)
        {
            total += i.price.toString().toLong()
        }
        Log.d("total", total.toString())
        db.getReference("Orders").child(uid!!).setValue(cartitemList)
        val intent = Intent(this , CheckoutActivity::class.java)
        intent.putExtra("total", total)
        startActivity(intent)
    }

    private fun getCartItems() {
        db.getReference("Cart").child(uid!!)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists())
                    {
                        for (i in snapshot.children)
                        {
                            val items = i.getValue(CartItem::class.java)
                            cartitemList!!.add(items!!)
                        }
                        Log.d("cartitems",cartitemList.toString())
                        cartAdapter = CartAdapter(this@CartActivity,cartitemList,this@CartActivity)
                        rv_Cart?.adapter = cartAdapter
                        rv_Cart?.layoutManager = LinearLayoutManager(this@CartActivity,
                            LinearLayoutManager.VERTICAL,false)
                        rv_Cart?.itemAnimator = null

                        if (!cartitemList.isNullOrEmpty())
                        {
                            checkout?.setOnClickListener {

                                sendDatatoorders()
                            }

                        }else{
                            Toast.makeText(this@CartActivity, "Add Something in Your Cart", Toast.LENGTH_SHORT).show()
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("cartitems", "loadPost:onCancelled", error.toException())
                }
            })
    }


    override fun deleteCardClick(
        position: Int,
        itemname: String
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Remove Item")
        builder.setMessage("Do You Want to Remove This Item")
        builder.setPositiveButton("CANCEL"
        ) { dialog, which -> dialog?.cancel() }
        builder.setNegativeButton("REMOVE"
        ) { dialog, which ->
            val db = FirebaseDatabase.getInstance()
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            db.getReference("Cart").child(uid!!).child(itemname).removeValue()
            Log.d("removeitem", "Removed $itemname")
            cartitemList!!.remove(cartitemList!![position])
            cartitemList!!.clear()
            cartAdapter!!.notifyItemRemoved(position)
            Log.d("cartList", cartitemList.toString())
        }
        builder.show()
    }




}