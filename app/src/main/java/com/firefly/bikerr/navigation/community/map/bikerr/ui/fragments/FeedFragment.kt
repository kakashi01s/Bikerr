package com.firefly.bikerr.navigation.community.map.bikerr.ui.fragments



import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.adapters.FeedAdapter
import com.firefly.bikerr.navigation.community.map.bikerr.base.BaseFragment
import com.firefly.bikerr.navigation.community.map.bikerr.databinding.FragmentFeedBinding
import com.firefly.bikerr.navigation.community.map.bikerr.model.Posts
import com.firefly.bikerr.navigation.community.map.bikerr.ui.ChatActivities.CommunityActivity
import com.firefly.bikerr.navigation.community.map.bikerr.ui.PostActivities.CreatepostActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedFragment :BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null
    private var param2: String? = null
    var mychats : ImageView? = null
    var db = FirebaseDatabase.getInstance()
    var rv_Feed : RecyclerView? = null
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    var feeditemList : ArrayList<Posts>? = ArrayList()
    var feedAdapter : FeedAdapter? = null
    var PERMISSION_CODE_READ = 1001
    var PERMISSION_CODE_WRITE = 1002
    var opencreatepost: FloatingActionButton? = null
    private var binding_ : FragmentFeedBinding? = null
    override val bindingVariable: Int
        get() = 0
    override val layoutId: Int
        get() = 0
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
        binding_ = FragmentFeedBinding.inflate(inflater, container,false)
        return binding_!!.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        checkPermissionForImage()
        getFeedItems()
        mychats?.setOnClickListener {
            val intent = Intent(activity, CommunityActivity::class.java)
            startActivity(intent)
        }

        opencreatepost?.setOnClickListener {
            val intent = Intent(activity, CreatepostActivity::class.java)
            intent.putExtra("feeditems", feeditemList!!.size)
            startActivity(intent)
        }
    }
    private fun initViews(view: View) {
        mychats = view.findViewById(R.id.channel_button)
        opencreatepost = view.findViewById(R.id.open_create_post)
        rv_Feed = view.findViewById(R.id.rvFeeds)

    }
    private fun checkPermissionForImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((PermissionChecker.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED!!)
                && (PermissionChecker.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED!!)
            ) {
                val permission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                val permissionCoarse = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(permission, PERMISSION_CODE_READ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001
                requestPermissions(permissionCoarse, PERMISSION_CODE_WRITE) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
            }
        }
    }
    private fun getFeedItems() {
        var feedslist : Int = 0

        db.getReference("Feed").orderByKey()
            .addValueEventListener(object : ValueEventListener, FeedAdapter.FeeditemClickListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists())
                    {
                        for (i in snapshot.children)
                        {
                            val items = i.getValue(Posts::class.java)
                            feeditemList!!.add(items!!)
                            feedAdapter?.notifyDataSetChanged()

                        }

                        Log.d("Feeditems",feeditemList.toString())
                        feeditemList!!.reverse()
                        feedAdapter = FeedAdapter(requireContext(),feeditemList,this)
                        rv_Feed?.adapter = feedAdapter
                        rv_Feed?.layoutManager = LinearLayoutManager(requireContext(),
                            LinearLayoutManager.VERTICAL,false)
                        Log.d("feedlist", feedslist.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("Feeditems", "loadPost:onCancelled", error.toException())
                }

                override fun LikeButtonListener() {
                    feeditemList!!.clear()
                }
            })
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            FeedFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}