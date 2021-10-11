package com.example.restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PagerFragment(private val list : ArrayList<MenuItem>?) : Fragment() {

    private lateinit var adapter : MenuAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pager_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView : RecyclerView = view.findViewById(R.id.MenuFragment_foodList)
        if(list != null)
          adapter = MenuAdapter(list)
        recyclerView.layoutManager = GridLayoutManager(context,1)
        recyclerView.adapter = adapter
    }

    fun changeList(listOfItem : ArrayList<MenuItem>?){
        adapter.changeList(listOfItem)
    }

}