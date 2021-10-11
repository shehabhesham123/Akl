package com.example.restaurant

import androidx.fragment.app.Fragment

class Page {
    private var name : String     // from R
    private val fragment : Fragment

    constructor(name: String, fragment: Fragment) {
        this.name = name
        this.fragment = fragment
    }

    fun name():String{ return this.name }

    fun name(name:String){this.name = name}

    fun fragment():Fragment{
        return this.fragment
    }
}