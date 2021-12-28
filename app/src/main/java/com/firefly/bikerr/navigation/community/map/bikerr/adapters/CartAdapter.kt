package com.firefly.bikerr.navigation.community.map.bikerr.adapters

import android.app.AlertDialog
import android.content.Context
import android.icu.number.IntegerWidth
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.model.Cart.CartItem
import com.google.android.libraries.places.internal.i
import com.google.common.eventbus.EventBus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class CartAdapter(
    var context: Context,
    var items: List<CartItem>?,
    var listener : cartlitemClickListener
) : RecyclerView.Adapter<CartAdapter.CartVIewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartVIewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_cart, parent, false)
        return CartVIewHolder(view)
    }

    override fun onBindViewHolder(holder: CartVIewHolder , position: Int) {
        val item = items!![position]
        holder.itemName.text = item.name
        holder.itemprice.text = item.price.toString()
        holder.itemquantity.text = item.quantity
        holder.itemsize.text = item.Size

        Glide.with(context)
            .load(item.image)
            .into(holder.itemimg)


    }

    override fun getItemCount(): Int {
        return items!!.size
    }



    inner class CartVIewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName = itemView.findViewById<TextView>(R.id.cart_item_name)
        var itemprice = itemView.findViewById<TextView>(R.id.cart_item_price)
        var itemimg = itemView.findViewById<ImageView>(R.id.cart_item_img)
        var removeitem = itemView.findViewById<Button>(R.id.remove_button)
        var itemsize = itemView.findViewById<TextView>(R.id.cart_item_size)
        var itemquantity = itemView.findViewById<TextView>(R.id.cart_item_Quantity)
        init {
            removeitem.setOnClickListener {
                    val position = adapterPosition
                val item = items!![position]
                val itemname = item.name.toString()
                    listener.deleteCardClick(position, itemname)
            }
        }
    }

    interface cartlitemClickListener {
        fun deleteCardClick(position: Int , itemname :String )
    }

}

