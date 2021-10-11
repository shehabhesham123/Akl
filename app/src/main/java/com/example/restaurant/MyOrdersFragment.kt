package com.example.restaurant

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.toast.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MyOrdersFragment : Fragment(){

    private var listener : MyOrdersFragmentListener? =null
    private lateinit var adapter : MyOrdersAdapter


    interface MyOrdersFragmentListener{
        fun onEditClick(order: Order)
        fun quarterTimeExecute(editButton: Button,fragment: TimerFragment,adapter:MyOrdersAdapter)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{listener = context as MyOrdersFragmentListener}
        catch(e:Exception){throw Exception("You don't implements from MyOrdersFragmentListener")}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            db = AccessDatabase.getInstance(context!!)
        }catch (e:Exception){}

        val bundle = arguments
        if(bundle != null) {
            val username = bundle.getString(USERNAME)
            if(username != null) {
                try {
                    client = db.customer(username)
                    client!!.open(context!!)
                }catch (e:Exception){}
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return inflater.inflate(R.layout.my_orders_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myTask = MyTask( fragmentManager!!){listOfOrders, headOrder, durationRemaining ->
            val appBar : AppBarLayout= view.findViewById(R.id.MyOrdersFragment_AppBar)
            val orderId : TextView = view.findViewById(R.id.MyOrdersFragment_TextView_OrderId)
            val totalCoat :TextView = view.findViewById(R.id.MyOrdersFragment_TextView_TotalCost)
            val edit : Button = view.findViewById(R.id.MyOrdersFragment_Button_Edit)
            val list : RecyclerView = view.findViewById(R.id.MyOrdersFragment_RecyclerView_Orders)
            adapter = MyOrdersAdapter(listOfOrders)

            if(headOrder != null) {
                appBar.visibility = View.VISIBLE
                adapter.isEnable(false)
                orderId.text = headOrder.orderId().toString()
                totalCoat.text = headOrder.totalCost().toString()

                val timerFragment = TimerFragment.getInstance(headOrder.duration(), durationRemaining)
                val ft = childFragmentManager.beginTransaction()
                ft.replace(R.id.MyOrdersFragment_FrameLayout_Timer,timerFragment)
                ft.commit()

                listener?.quarterTimeExecute(edit,timerFragment,adapter)

                edit.setOnClickListener {
                    listener?.onEditClick(headOrder)
                }
            }
            list.adapter = adapter
            list.layoutManager = GridLayoutManager(context,1)
        }
        myTask.execute()
    }

    companion object{
        private lateinit var db : AccessDatabase
        private var client : Client? = null

        fun getInstance(username:String):MyOrdersFragment{
            val bundle = Bundle()
            bundle.putString(USERNAME,username)
            val fragment = MyOrdersFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private class MyTask(private val fm : FragmentManager, private val `fun`:(listOfOrders:ArrayList<Order>,headOrder:Order?,durationRemaining:Int)->Unit) : AsyncTask<Int, Int, ArrayList<Order>?>(){

        override fun doInBackground(vararg params: Int?): ArrayList<Order>? {
            publishProgress()
            try {
                val orders = db.orders()
                for(i in orders) {
                    val pair = db.cartItem(i.orderId())
                    i.items(pair.first)
                    i.totalCost(pair.second)
                    i.customerInfo(client)
                }
                return orders
            }catch (e:Exception){}
            return null
        }

        override fun onPostExecute(result:ArrayList<Order>?) {
            super.onPostExecute(result)
            if(result != null) {
                var lastOrder : Order? = null
                try {
                    val _pair = CartActivity.lastOrder(db, client)
                    if(_pair.second && _pair.first !=null){
                        lastOrder = _pair.first
                        val pair = db.cartItem(lastOrder!!.orderId())
                        lastOrder.items(pair.first)
                        lastOrder.totalCost(pair.second)
                        lastOrder.customerInfo(client)
                    }
                }
                catch (e:Exception){
                }

                val sdf = SimpleDateFormat("yyyy/MM/dd KK:mm:ss")
                if( lastOrder != null &&CartActivity.dateAfterDuration(lastOrder.date(),lastOrder.duration()) > sdf.format(Date())){
                    for(i in 0 until result.size){
                        if(result[i].orderId() == lastOrder.orderId()){
                            result.removeAt(i)
                            break
                        }
                    }
                    val durationRemaining = (lastOrder.duration() - MyOrdersActivity.duration(lastOrder.date(), sdf.format(Date())))
                    `fun`(result,lastOrder,durationRemaining)
                } else {
                    `fun`(result, null, 0)
                }
            }
        }
    }
}


