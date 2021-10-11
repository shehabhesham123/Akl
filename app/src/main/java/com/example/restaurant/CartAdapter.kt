package com.example.restaurant

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(private var listOfItem : ArrayList<CartItem>) :RecyclerView.Adapter<CartAdapter.VH>(){

    private var listener :CartListener? =null
    private var totalPrice : Float =0f

    init {
        for(i in listOfItem){
            val item = RestaurantItem.getItem(i.menuItem())
            totalPrice += item.sizes()[i.sizePosition()].price() * i.quantity()
        }
    }

    interface CartListener{
        fun getTotalPrice(totalPrice : Float)
        fun onDeleteItem(position: Int, cartItem: CartItem, totalPrice: Float)
    }

    class VH(viewItem: View):RecyclerView.ViewHolder(viewItem)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        try{listener = parent.context as CartListener}
        catch (e:Exception){throw Exception("You don't implements from CartListener")}

        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item,null,false)
        return VH(view)
    }

    override fun getItemCount(): Int {
        return listOfItem.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val view = holder.itemView

        val delete :ImageView =  view.findViewById(R.id.CartItem_ImageView_Delete)
        val image :ImageView = view.findViewById(R.id.CartItem_ImageView_Image)
        val name :TextView = view.findViewById(R.id.CartItem_TextView_Name)
        val quantity :TextView = view.findViewById(R.id.CartItem_TextView_Quantity)
        val size : TextView = view.findViewById(R.id.CartItem_TextView_Size)
        val price :TextView = view.findViewById(R.id.CartItem_TextView_Price)

        try {
            val item = RestaurantItem.getItem(listOfItem[position].menuItem())
            var n = item.name()
            if(n.length > 20)
                n = n.substring(0,20)+"..."
            name.text = n
            quantity.text = listOfItem[position].quantity().toString()
            val sizeAndPrice = item.sizes()[listOfItem[position].sizePosition()]
            size.text = sizeAndPrice.size()
            price.text = String.format("%.2f", (sizeAndPrice.price() * listOfItem[position].quantity()))
            image.setImageURI(Uri.parse(item.images()[0]))
        }catch (e:Exception){}
        delete.setOnClickListener {
            listener?.onDeleteItem(position,listOfItem[position],totalPrice)
        }
        listener?.getTotalPrice(totalPrice)
    }

    fun deleteItem(listOfItem: ArrayList<CartItem>,totalPrice: Float){
        this.listOfItem  = listOfItem
        this.totalPrice = totalPrice
        notifyDataSetChanged()
    }
}