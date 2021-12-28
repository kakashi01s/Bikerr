package com.firefly.bikerr.navigation.community.map.bikerr.ui.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.ui.AboutActivities.AboutActivity
import com.firefly.bikerr.navigation.community.map.bikerr.ui.RegisterActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.internal.it
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import io.getstream.chat.android.client.models.Attachment
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null
    private var param2: String? = null
    var uploadimage : ImageView? = null
    var signout: ImageButton? = null
    var databaseRef: DatabaseReference? = null
    var profileNumber: TextView? = null
    var profileName: TextView? = null
    var profileEmail: TextView? = null
    var IMAGE_PICK_CODE = 1000
    var about : ImageView? = null
    var contactus : ImageView? = null
    var privacy : ImageView? = null
    private var filePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        readData(uid)
        initViews(view)
        uploadimage!!.setOnClickListener {
            pickImageFromGallery()
        }
        signout!!.setOnClickListener {
            signoutuser()

        }
        about!!.load(R.drawable.about){
            transformations(CircleCropTransformation())

        }
        contactus!!.load(R.drawable.help){
            transformations(CircleCropTransformation())
        }
        privacy!!.load(R.drawable.privacy){
            transformations(CircleCropTransformation())
        }
        about!!.setOnClickListener {
            val intent = Intent(activity, AboutActivity::class.java)
            intent.putExtra("heading","about")
            startActivity(intent)
        }
        contactus!!.setOnClickListener {
            val intent = Intent(activity, AboutActivity::class.java)
            intent.putExtra("heading","contactus")
            startActivity(intent)
        }
        privacy!!.setOnClickListener {
            val intent = Intent(activity, AboutActivity::class.java)
            intent.putExtra("heading","privacy")
            startActivity(intent)
        }
    }

    private fun signoutuser() {


        val builder = AlertDialog.Builder(context)
        builder.setTitle("SignOut?")
        builder.setMessage("You will be Signed Out If You Click OK")
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            builder.show()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(context, android.R.string.no, Toast.LENGTH_SHORT).show()
        }

        builder.show()
    }

    fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE) // GIVE AN INTEGER VALUE FOR IMAGE_PICK_CODE LIKE 1000
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }
            filePath = data.data
            uploadImageandSaveUri()
        }
    }

    private fun uploadImageandSaveUri() {
        val StorageRef = FirebaseStorage.getInstance().reference
            .child("Avatar/${FirebaseAuth.getInstance().currentUser?.uid}")
        val uploadTask = StorageRef.putFile(filePath!!)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            Log.d("uploadprofile", "Failed to upload")
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                StorageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    uploadimage!!.load(downloadUri){
                        this.transformations(CircleCropTransformation())
                    }
                    Log.d("uploadprofile", downloadUri.toString())
                    databaseRef = FirebaseDatabase.getInstance().getReference("Users")
                    databaseRef!!.child(FirebaseAuth.getInstance().currentUser!!.uid).child("image").setValue(downloadUri.toString())

                } else {
                    // Handle failures
                    // ...
                    Log.d("uploadprofile", "Img download failed")
                }
            }
        }
    }

    private fun readData(uid: String) {
        databaseRef = FirebaseDatabase.getInstance().getReference("Users")
        databaseRef!!.child(uid).get().addOnSuccessListener {
            if (it.exists())
            {
                val uname = it.child("name").value
                val uemail = it.child("email").value
                val unumber = it.child("phoneNumber").value
                val uimage = it.child("image").value
                profileName!!.text = uname.toString()
                profileNumber!!.text = uemail.toString()
                profileEmail!!.text = unumber.toString()
                uploadimage!!.load(uimage.toString()){
                    this.transformations(CircleCropTransformation())
                }
            }else
            {
                Log.d("register", "Failed to register")
            }
        }
    }

    private fun initViews(view: View) {
        signout = view.findViewById(R.id.signout_btn)
        profileNumber = view.findViewById(R.id.Profile_number)
        profileName = view.findViewById(R.id.Profile_name)
        profileEmail = view.findViewById(R.id.Profile_email)
        uploadimage = view.findViewById(R.id.Profile_img)
        about = view.findViewById(R.id.about)
        contactus = view.findViewById(R.id.contactus)
        privacy = view.findViewById(R.id.privacypolicy)

    }

    override fun onDestroy() {

        super.onDestroy()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}