package com.firefly.bikerr.navigation.community.map.bikerr.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.adapters.FeedAdapter.FeedVIewHolder
import com.firefly.bikerr.navigation.community.map.bikerr.model.Posts
import com.github.chrisbanes.photoview.PhotoView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class FeedAdapter(
    var context: Context,
    var items: List<Posts>?,
    var listener : FeeditemClickListener
) : RecyclerView.Adapter<FeedVIewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedVIewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_feed, parent, false)
        return FeedVIewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedVIewHolder, position: Int) {
        val item = items!![position]
        holder.itemName.text = item.PostCaption

        holder.itemimg.load(item.PostImg)
        holder.setIsRecyclable(false)
        var databaseRef: DatabaseReference? = null
        databaseRef = FirebaseDatabase.getInstance().getReference("Users")
        databaseRef.child("${item.userid}").get().addOnSuccessListener {
            if (it.exists())
            {
                val uimage = it.child("image").value
                val uname = it.child("name").value
                holder.feeduserid.text = uname.toString()

                holder.feedusername.text = uname.toString()
                holder.feeduserimg.load(uimage.toString()){
                    this.transformations(CircleCropTransformation())
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    inner class FeedVIewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName = itemView.findViewById<TextView>(R.id.feed_caption)
        var itemimg = itemView.findViewById<PhotoView>(R.id.feed_item_img)
        var feedusername = itemView.findViewById<TextView>(R.id.feed_username)
        var feeduserimg = itemView.findViewById<PhotoView>(R.id.feed_userimg)
        var feeduserid = itemView.findViewById<TextView>(R.id.feeduserid)

    }



    interface FeeditemClickListener {
        fun LikeButtonListener()
    }
}

