package com.example.restaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyOrdersAdapter(private val listOfOrders:ArrayList<Order>) : RecyclerView.Adapter<MyOrdersAdapter.VH>(){

    private var listener:MyOrdersListener? = null
    private var isEnable:Boolean = true

    interface MyOrdersListener{
        fun onReorderClick(order: Order)
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        try{listener = parent.context as MyOrdersListener}
        catch (e:Exception){throw Exception("You don't implements from MyOrdersListener")}
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_orders_item,null,false)
        return VH(view)
    }

    override fun getItemCount(): Int {
        return listOfOrders.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val view = holder.itemView
        val orderID :TextView = view.findViewById(R.id.MyOrdersItemView_TextView_OrderId)
        val totalCost:TextView = view.findViewById(R.id.MyOrdersItemView_TextView_TotalCost)
        val reorder :TextView = view.findViewById(R.id.MyOrdersItemView_TextView_Reorder)
        val orderDate :TextView = view.findViewById(R.id.MyOrdersItemView_TextView_OrderDate)

        orderID.text = listOfOrders[getInversePosition(position)].orderId().toString()
        totalCost.text = listOfOrders[getInversePosition(position)].totalCost().toString()
        orderDate.text = listOfOrders[getInversePosition(position)].date()
        reorder.isEnabled = isEnable

        reorder.setOnClickListener {
            listener?.onReorderClick(listOfOrders[getInversePosition(position)])
        }
    }

    private fun getInversePosition(position: Int):Int{  // علشان يعرضهم بالعكس
        return (listOfOrders.size-1)-position
    }

    fun isEnable(isEnable:Boolean){
        this.isEnable = isEnable
    }
}
