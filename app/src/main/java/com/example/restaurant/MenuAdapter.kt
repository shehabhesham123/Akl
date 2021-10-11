package com.example.restaurant

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(private var listOfItem : ArrayList<MenuItem>):RecyclerView.Adapter<MenuAdapter.VH>() {

    private lateinit var listener : MenuRecyclerListener

    interface MenuRecyclerListener{
        fun onMenuItemClick(menuItem: MenuItem)
        fun onMenuItemLongClick(menuItem: MenuItem)
    }

    class VH(viewItem: View) : RecyclerView.ViewHolder(viewItem)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        try { listener = parent.context as MenuRecyclerListener }
        catch (e:Exception){ throw Exception("You don't implements from FoodRecyclerListener") }

        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item,null,false)
        return VH(view)
    }

    override fun getItemCount(): Int {
        return listOfItem.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val view = holder.itemView

        val image : ImageView = view.findViewById(R.id.MenuItemView_ImageView_Image)
        val name : TextView = view.findViewById(R.id.MenuItemView_TextView_Name)
        val ingredients :TextView = view.findViewById(R.id.MenuItemView_TextView_Ingredients)
        val rate : RatingBar = view.findViewById(R.id.MenuItemView_RatingBar_Rate)
        val rateText : TextView = view.findViewById(R.id.MenuItemView_TextView_Rate)
        val price :TextView = view.findViewById(R.id.MenuItemView_TextView_Price)

        try {
            val item: RestaurantItem = RestaurantItem.getItem(listOfItem[position])
            image.setImageURI(Uri.parse(item.images()[0]))
            name.text = item.name()
            ingredients.text = item.ingredients()
            rate.rating = item.rate()
            rateText.text = item.rate().toString()
            price.text = String.format("%.2f", item.sizes()[0].price() - item.sizes()[0].price() * (item.discount() / 100) )    // as default
        }
        catch (e:Exception){}

        view.setOnClickListener {
            listener.onMenuItemClick(listOfItem[position])
        }

        view.setOnLongClickListener {
            listener.onMenuItemLongClick(listOfItem[position])
            true
        }
    }

    fun changeList(listOfItem : ArrayList<MenuItem>?){
        if(listOfItem != null) {
            this.listOfItem = listOfItem
            notifyDataSetChanged()
        }
    }
}