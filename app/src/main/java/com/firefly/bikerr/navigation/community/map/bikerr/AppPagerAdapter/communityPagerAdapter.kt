package com.firefly.bikerr.navigation.community.map.bikerr.AppPagerAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.firefly.bikerr.navigation.community.map.bikerr.ui.CommunityFragments.CommcreateFragment
import com.firefly.bikerr.navigation.community.map.bikerr.ui.CommunityFragments.CommsearchFragment
import com.firefly.bikerr.navigation.community.map.bikerr.ui.fragments.ChannelsFragment

class communityPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    val NUM_ITEMS = 3;

    override fun getCount(): Int {
        return NUM_ITEMS
    }

    override fun getItem(position: Int): Fragment {
        when (position){
            0 -> {
                return ChannelsFragment.newInstance(position, "chat")
            }
            1 -> {

                return CommsearchFragment.newInstance(position, "search")
            }
            2 -> {
                return CommcreateFragment.newInstance(position,"create")
            }

            else -> return ChannelsFragment.newInstance(position, "chat")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title : String? = null;
        if (position == 0) {
            title = "Chat"

        } else if (position == 1) {
            title = "SEARCH"
        }
        else if (position == 2) {
            title = "CREATE"
        }


        return title
    }
}