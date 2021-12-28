package com.firefly.bikerr.navigation.community.map.bikerr.ui.ChatActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.firefly.bikerr.navigation.community.map.bikerr.AppPagerAdapter.AppPagerAdapter
import com.firefly.bikerr.navigation.community.map.bikerr.AppPagerAdapter.communityPagerAdapter
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.Utils.CustomViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.analytics.FirebaseAnalytics

class CommunityActivity : AppCompatActivity() {

    var tabLayout: TabLayout? = null
    var viewPager: CustomViewPager? = null
    var viewPagerAdapter: communityPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)
        initViews()
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
    }



    private fun initViews() {

        tabLayout = findViewById(R.id.tab_layout2)
        viewPager = findViewById(R.id.vpPager2)
        viewPagerAdapter = communityPagerAdapter(supportFragmentManager)

    }

    private fun setupViewPager() {
        viewPagerAdapter = communityPagerAdapter(supportFragmentManager)
        viewPager!!.adapter = viewPagerAdapter
        val limit = if ((viewPagerAdapter as communityPagerAdapter).getCount() > 1) (viewPagerAdapter as communityPagerAdapter).getCount() - 1 else 1
        viewPager!!.offscreenPageLimit = limit;
        viewPager!!.currentItem = 0;

        viewPager!!.setSwipePagingEnabled(false)

        tabLayout!!.setupWithViewPager(viewPager)
    }

}