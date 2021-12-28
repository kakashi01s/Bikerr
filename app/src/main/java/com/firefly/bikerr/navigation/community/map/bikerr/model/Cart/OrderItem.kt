package com.firefly.bikerr.navigation.community.map.bikerr.model.Cart

data class OrderItem(var name: String?, val price: Any?, val image: String?,var Quantity : Int?)
{
    constructor() : this("", "", "" , 0) {}
}
