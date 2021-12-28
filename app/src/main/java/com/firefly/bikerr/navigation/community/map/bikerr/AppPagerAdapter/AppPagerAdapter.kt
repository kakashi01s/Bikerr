package com.firefly.bikerr.navigation.community.map.bikerr.AppPagerAdapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.firefly.bikerr.navigation.community.map.bikerr.ui.fragments.*


class AppPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    val NUM_ITEMS = 5;

    override fun getCount(): Int {
        return NUM_ITEMS
    }

    override fun getItem(position: Int): Fragment {
        when (position){
            0 -> {
                return MapsFragment.newInstance(position, "Home")
            }
            1 -> {
                return ShopFragment.newInstance(position, "Home")
            }

            2 -> {
                return FeedFragment.newInstance(position, "Continent")
            }

            3 -> {
                return NearbyFragment.newInstance(position, "Country")
            }
            4 -> {
                return ProfileFragment.newInstance(position, "Maps")
            }

            else -> return MapsFragment.newInstance(position, "Home")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title : String? = null;
        if (position == 0) {
            title = "MAPS"

        } else if (position == 1) {
            title = "SHOP"
        }
        else if (position == 2) {
            title = "FEED"
        }
        else if (position == 3) {
            title = "NEAR"
        }
        else if (position == 4) {
            title = "ME"
        }
        return title
    }
}