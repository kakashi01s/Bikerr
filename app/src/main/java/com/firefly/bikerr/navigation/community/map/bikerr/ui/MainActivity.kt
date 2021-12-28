package com.firefly.bikerr.navigation.community.map.bikerr.ui



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.firefly.bikerr.navigation.community.map.bikerr.AppPagerAdapter.AppPagerAdapter
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.Utils.CustomViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User


class MainActivity : AppCompatActivity() {
    var tabLayout: TabLayout? = null
    var viewPager: CustomViewPager? = null
    var viewPagerAdapter: AppPagerAdapter? = null
    var uname : String? = null
    var unumber : String? = null
    var uemail : String? = null
    var uimg :String? = null
    var databaseRef: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initViews
        initViews()
        streamuser()
        setupViewPager()
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.d("TAG", "onTabSelected: " + tab.position)
                val bundleAppUsage = Bundle()
                bundleAppUsage.putString("tab_click", tab.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        tabLayout!!.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_explore_24)
        tabLayout!!.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_store_24)
        tabLayout!!.getTabAt(2)!!.setIcon(R.drawable.ic_home_black_24dp)
        tabLayout!!.getTabAt(3)!!.setIcon(R.drawable.ic_baseline_near_me_24_white)
        tabLayout!!.getTabAt(4)!!.setIcon(R.drawable.ic_baseline_person_24)


    }

    private fun streamuser() {

        val cl = ChatClient.instance()
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        databaseRef = FirebaseDatabase.getInstance().getReference("Users")
        databaseRef!!.child(uid).get().addOnSuccessListener {
            if (it.exists()) {
                uname = it.child("name").value?.toString()
                uemail = it.child("email").value?.toString()
                unumber = it.child("phoneNumber").value?.toString()
                uimg = it.child("image").value?.toString()
                Log.d("uemail", uemail.toString())

                val user = User(
                    id = uid,
                    extraData = mutableMapOf(
                        "name" to "$uname",
                        "image" to "$uimg",
                    ),
                )
                cl.connectUser(
                    user = user,
                    token = cl.devToken(uid)
                ).enqueue {
                    if (it.isSuccess) {
                        Log.d("stream", it.data().connectionId)


                    } else {
                        Log.d("stream", it.error().message.toString())

                    }

                }

            }
        }

    }


    private fun setupViewPager() {
        viewPagerAdapter = AppPagerAdapter(supportFragmentManager)
        viewPager!!.adapter = viewPagerAdapter
        val limit = if ((viewPagerAdapter as AppPagerAdapter).getCount() > 1) (viewPagerAdapter as AppPagerAdapter).getCount() - 1 else 1
        viewPager!!.offscreenPageLimit = limit;
        viewPager!!.currentItem = 2;

        viewPager!!.setSwipePagingEnabled(false)

        tabLayout!!.setupWithViewPager(viewPager)
    }


    private fun initViews() {

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.vpPager)
        viewPagerAdapter = AppPagerAdapter(supportFragmentManager)

    }

}
