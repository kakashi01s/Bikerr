package com.firefly.bikerr.navigation.community.map.bikerr.Listeners

import com.firefly.bikerr.navigation.community.map.bikerr.base.BaseRecyclerListener

interface SpareShopClickListener <T> : BaseRecyclerListener {
    fun OnitemShopSpareClick(item: T)
}