package com.example.restaurant

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import java.lang.Exception

private const val USERNAME_EXTRA = "USERNAME"

class MenuFragment : Fragment() {

    private lateinit var client: Client
    private lateinit var tabs : TabLayout
    private lateinit var search : EditText
    private lateinit var listener : MenuListener

    interface MenuListener{
        fun cart()
    }

    companion object {
        private lateinit var db :AccessDatabase
        private var adapter: PagerAdapter? = null
        private lateinit var pager : ViewPager

        fun getInstance(username:String):MenuFragment{
            val bundle = Bundle()
            bundle.putString(USERNAME_EXTRA,username)
            val fragment = MenuFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    fun refreshAdapter(){
        val text = search.text.toString()
        val list = when(tabs.selectedTabPosition){
            0-> getItems(text,FOOD_CODE)
            1-> getItems(text,DRINK_CODE)
            else-> getItems(text,DESSERT_CODE)
        }
        adapter?.changeList(list,tabs.selectedTabPosition)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{listener = context as MenuListener}
        catch (e:Exception){throw Exception("You don't implements from MenuListener")}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var username : String? = null
        if(context != null)
            db = AccessDatabase.getInstance(context!!)

        val bundle = arguments
        if(bundle != null)
            username = bundle.getString(USERNAME_EXTRA,"")

        try {
            if(!username.isNullOrEmpty() && !username.isNullOrBlank()) {
                client = db.customer(username)
            }
            client.open(context!!)
        }
        catch (e: Exception){}

        val task = MyTask(fragmentManager!!) { text: Int -> resources.getString(text) }        // food || drink || dessert
        task.execute(null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.menu_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val deleteSearch : ImageView = view.findViewById(R.id.MenuToolbar_ImageView_ClearSearch)
        search = view.findViewById(R.id.MenuToolbar_EditText_SearchFlied)
        val searchIcon : ImageView = view.findViewById(R.id.MenuToolbar_ImageView_SearchIcon)
        val cartIcon : ImageView = view.findViewById(R.id.MenuToolbar_ImageView_Cart)
        tabs = view.findViewById(R.id.MenuActivity_tabLayout)
        pager = view.findViewById(R.id.MenuActivity_pager)
        tabs.setupWithViewPager(pager)

        deleteSearch.setOnClickListener {
            search.setText(R.string.MenuToolbar_nullSearch)
            try {
                val list = when (tabs.selectedTabPosition) {
                    0 -> getItems(FOOD_CODE)
                    1 -> getItems(DRINK_CODE)
                    else -> getItems(DESSERT_CODE)
                }
                adapter?.changeList(list, tabs.selectedTabPosition)
            }catch (e:Exception){}
        }

        search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s?.length!! > 0)
                    deleteSearch.visibility = View.VISIBLE
                else
                    deleteSearch.visibility = View.GONE
            }
        })

        cartIcon.setOnClickListener {
            listener.cart()
        }

        searchIcon.setOnClickListener {
            val text = search.text.toString()
            val list = when (tabs.selectedTabPosition) {
                0 -> getItems(text, FOOD_CODE)
                1 -> getItems(text, DRINK_CODE)
                else ->getItems(text, DESSERT_CODE)
            }
            adapter?.changeList(list, tabs.selectedTabPosition)
        }

        tabs.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                if(p0?.position != null) {
                    try {
                        val list = when(p0.position){
                            0-> getItems(FOOD_CODE)
                            1-> getItems(DRINK_CODE)
                            else-> getItems(DESSERT_CODE)
                        }
                        adapter?.changeList(list, p0.position)
                    }catch (e:Exception){}
                }
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                when (p0?.position) {
                    0 -> {
                        search.setText(R.string.MenuToolbar_nullSearch)
                        search.setHint(R.string.MenuToolbar_searchHintFood)
                    }
                    1 -> {
                        search.setText(R.string.MenuToolbar_nullSearch)
                        search.setHint(R.string.MenuToolbar_searchHintDrink)
                    }
                    else -> {
                        search.setText(R.string.MenuToolbar_nullSearch)
                        search.setHint(R.string.MenuToolbar_searchHintDessert)
                    }
                }
            }
        })

    }

    private class MyTask(private val fm: FragmentManager, private val `fun`: (Int) -> String):
        AsyncTask<String, Int, Pair<ArrayList<MenuItem>, Pair<ArrayList<MenuItem>, ArrayList<MenuItem>>>>(){

        override fun doInBackground(vararg params: String?): Pair<ArrayList<MenuItem>,Pair<ArrayList<MenuItem>,ArrayList<MenuItem>>> {
            var foods = ArrayList<MenuItem>()
            var drinks = ArrayList<MenuItem>()
            var desserts = ArrayList<MenuItem>()
            try {
                foods = db.items(FOOD_CODE)
                drinks = db.items(DRINK_CODE)
                desserts = db.items(DESSERT_CODE)
            }catch (e:Exception){}
            return Pair(foods, Pair(drinks, desserts))
        }

        override fun onPostExecute(result: Pair<ArrayList<MenuItem>,Pair<ArrayList<MenuItem>,ArrayList<MenuItem>>>?) {
            super.onPostExecute(result)
            if (result != null) {
                val foods = result.first
                val drinks = result.second.first
                val desserts = result.second.second

                val foodFragment = PagerFragment(foods)
                val drinkFragment = PagerFragment(drinks)
                val dessertFragment = PagerFragment(desserts)

                adapter = PagerAdapter(
                    fm, arrayListOf(
                        Page(`fun`(R.string.Food), foodFragment),
                        Page(`fun`(R.string.Drinks), drinkFragment),
                        Page(`fun`(R.string.Desserts), dessertFragment)
                    )
                )
                pager.adapter = adapter
            }
        }
    }

    private fun getItems(name:String, type:String):ArrayList<MenuItem>? {
        var list: ArrayList<MenuItem>? = null
        try {
            list = client.search(name, type)
        }catch (e:Exception){}
        return list
    }

    private fun getItems(type:String):ArrayList<MenuItem>?{
        var list : ArrayList<MenuItem>? = null
        try {
            list = db.items(type)
        }catch (e:Exception){}
        return list
    }


}