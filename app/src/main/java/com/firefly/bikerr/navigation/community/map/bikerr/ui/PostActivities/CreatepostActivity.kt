package com.firefly.bikerr.navigation.community.map.bikerr.ui.PostActivities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.model.Posts
import com.firefly.bikerr.navigation.community.map.bikerr.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class CreatepostActivity : AppCompatActivity() {
    var createPost_button : Button? = null
    var postimg : ImageView? = null
    var PostCaption : TextView? = null
    var backbutton : ImageView? = null
    var IMAGE_PICK_CODE = 1000
    var db = FirebaseDatabase.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    var progress : ProgressBar? = null
    private var filePath: Uri? = null
    var feeditems : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createpost)
        initViews()

        feeditems = intent.getIntExtra("feeditems", 0)
        postimg!!.setOnClickListener {
            pickImageFromGallery()
        }
    }

    private fun initViews() {
        createPost_button =findViewById(R.id.createPostButton)
        postimg= findViewById(R.id.Post_Item)
        PostCaption = findViewById(R.id.post_caption)
        backbutton = findViewById(R.id.back_button_feed)
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE) // GIVE AN INTEGER VALUE FOR IMAGE_PICK_CODE LIKE 1000
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            // I'M GETTING THE URI OF THE IMAGE AS DATA AND SETTING IT TO THE IMAGEVIEW
            postimg!!.setImageURI(data?.data)
            filePath = data?.data
            createPost_button!!.setOnClickListener {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading New Feed....")
                progressDialog.show()
                val StorageRef = FirebaseStorage.getInstance().reference
                    .child("Posts/${FirebaseAuth.getInstance().currentUser?.uid}"+ System.currentTimeMillis().toString())
               val uploadpostimg = StorageRef.putFile(filePath!!)
                // Register observers to listen for when the download is done or if it fails
                uploadpostimg.addOnFailureListener {
                    Log.d("uploadpost", "Failed to upload")
                    // Handle unsuccessful uploads
                }.addOnSuccessListener { taskSnapshot ->
                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                    // ...
                    val urlTask = uploadpostimg.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        StorageRef.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            val postid = "PostNo$feeditems"+"${UUID.randomUUID()}"
                            val post = Posts(uid, "$postid",
                                downloadUri.toString(),
                                PostCaption!!.text.toString(),0,0)
                            db.getReference("Feed").child("$postid").setValue(post)
                            db.getReference("Posts").child(uid).child("$postid").setValue(post).addOnCompleteListener {
                                Toast.makeText(this, "Post Uploaded", Toast.LENGTH_SHORT).show()
                                Log.d("uploadpost", downloadUri.toString())
                                var intent = Intent(this , MainActivity::class.java)
                                startActivity(intent)
                                progressDialog.dismiss()
                            }
                        } else {
                            // Handle failures
                            // ...
                            Log.d("uploadpost", "Img download failed")
                        }
                    }
                }
            }
        }
    }


}