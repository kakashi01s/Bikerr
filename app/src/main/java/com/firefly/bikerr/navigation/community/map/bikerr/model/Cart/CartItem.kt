package com.firefly.bikerr.navigation.community.map.bikerr.model.Cart

data class CartItem(var name: String?, val price: Any?, val image: String?,var quantity : String?, var Size: String?)
{
    constructor() : this("", "", "","", "") {}
}
