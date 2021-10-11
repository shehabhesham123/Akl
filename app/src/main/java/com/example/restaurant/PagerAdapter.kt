package com.example.restaurant

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class PagerAdapter(fm:FragmentManager, private val list : ArrayList<Page>):FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return list[position].fragment()
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return list[position].name()
    }

    fun changeList(list:ArrayList<MenuItem>?, position: Int){
        (this.list[position].fragment() as PagerFragment).changeList(list)
    }
}