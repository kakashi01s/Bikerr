package com.firefly.bikerr.navigation.community.map.bikerr.model.Users

import android.net.Uri


data class Users(val phoneNumber : String? = null, val name : String? = null, val email : String?, val userid : String?, val image :String?)
{
    constructor() : this("", "", "","", "") {}
}
