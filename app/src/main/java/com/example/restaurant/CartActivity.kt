package com.example.restaurant

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.toast.*
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import kotlin.Exception
import kotlin.collections.ArrayList
import kotlin.math.min


private var SHARE_PREFERENCES_INT_SIZE = 0

class CartActivity :
    AppCompatActivity(),
    CartAdapter.CartListener{

    private lateinit var cartList : RecyclerView
    private lateinit var client : Client
    private lateinit var price : TextView
    private lateinit var checkOut : Button
    private lateinit var db :AccessDatabase
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editSharedPreferences : SharedPreferences.Editor
    private val listOfCartItem = ArrayList<CartItem>()
    private lateinit var adapter : CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart_activity)

        sharedPreferences = getSharedPreferences(MenuActivity.SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE)
        editSharedPreferences = sharedPreferences.edit()
        db = AccessDatabase.getInstance(baseContext)

        try{
            client = db.customer(intent.getStringExtra(USERNAME_CODE)!!)
            client.open(baseContext)
        }catch (e:Exception){}

        // restore cart item data from shared preferences and put them in listOfItems then clear shared ////////////
        val size = sharedPreferences.getInt(MenuActivity.SHARE_PREFERENCES_STRING_SIZE,0)
        for(i in 0 until size){
            val id = sharedPreferences.getInt("$i${MenuActivity.SHARE_PREFERENCES_ITEM_ID}",-1)
            val quantity = sharedPreferences.getInt("$i${MenuActivity.SHARE_PREFERENCES_QUANTITY}",-1)
            val sizePosition = sharedPreferences.getInt("$i${MenuActivity.SHARE_PREFERENCES_SIZE_POSITION}",-1)
            try {
                listOfCartItem.add(CartItem(getItem(id),sizePosition,quantity))
            }catch (e:Exception){}
        }
        editSharedPreferences.clear().apply()
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        cartList = findViewById(R.id.CartActivity_RecyclerView_List)
        price = findViewById(R.id.CartActivity_TextView_Price)
        checkOut = findViewById(R.id.CartActivity_Button_CheckOut)

        if(listOfCartItem.size == 0) {
            checkOut.isEnabled = false
        }
        try {
            if(!MenuActivity.isEdit && listOfCartItem.size > 0 && !orderPermission(db)){
                Snackbar.make(cartList,resources.getText(R.string.CartActivity_SnackBar),Snackbar.LENGTH_LONG).show()
                checkOut.isEnabled = false
            }
        }catch (e:Exception){}

        checkOut.setOnClickListener {

            if(MenuActivity.isEdit){
                val oldList : ArrayList<CartItem> = ArrayList()
                MenuActivity.isEdit = false
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                val sharedPreferences = getSharedPreferences(MenuActivity.SHARE_PREFERENCES_OLD_NAME, Context.MODE_PRIVATE)
                val edit = sharedPreferences.edit()
                val orderId = sharedPreferences.getInt(ORDER_ID,-1)
                val date = sharedPreferences.getString(ORDER_DATE,"")
                val duration = sharedPreferences.getInt(ORDER_DURATION,0)
                val dateNow = SimpleDateFormat("yyyy/MM/dd KK:mm:ss").format(Date())
                var dateAfterDuration :String = dateNow
                if(date!= null)
                    dateAfterDuration = dateAfterDuration(date,duration/4)
                val size = sharedPreferences.getInt(MenuActivity.SHARE_PREFERENCES_OLD_STRING_SIZE,0)
                for(i in 0 until size){
                    val id = sharedPreferences.getInt("$i${MenuActivity.SHARE_PREFERENCES_OLD_ITEM_ID}",-1)
                    val quantity = sharedPreferences.getInt("$i${MenuActivity.SHARE_PREFERENCES_OLD_QUANTITY}",-1)
                    val sizePosition = sharedPreferences.getInt("$i${MenuActivity.SHARE_PREFERENCES_OLD_SIZE_POSITION}",-1)
                    oldList.add(CartItem(getItem(id),sizePosition,quantity))
                }
                edit.clear().apply()
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                val pair = getAddDeleteItems(oldList,listOfCartItem)

                val deleteList : ArrayList<CartItem> = pair.first
                val addList : ArrayList<CartItem> = pair.second
                var b  = false
                if(dateNow < dateAfterDuration) {
                    try { b = client.editOrder(orderId, addList, deleteList) }
                    catch (e: Exception) {}
                    if(b){
                        listOfCartItem.clear()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        setResult(RESULT_CODE_CANCELED)
                        finish()
                    }
                }
                else{
                    Toast.makeText(baseContext,R.string.CartActivity_SnackBar2,Toast.LENGTH_LONG).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
            else {
                val sdf = SimpleDateFormat("yyyy/MM/dd KK:mm:ss")
                val order = Order(client, sdf.format(Date()), 60, listOfCartItem)
                val intent = Intent(baseContext,MyService::class.java)
                intent.putExtra(USERNAME_CODE, client.username())
                intent.putExtra(ORDER_DURATION, order.duration())
                startService(intent)
                var pair: Pair<Boolean, Int>? = null
                try { pair = client.checkout(order) }
                catch (e: Exception) {}

                if (pair != null) {
                    if (pair.first) {        // دى بيقولى انه عمل insert ليها ف الجدول ولا لا
                        listOfCartItem.clear()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        setResult(RESULT_CODE_CANCELED)
                        finish()
                    }
                }
            }
        }

        adapter = CartAdapter(listOfCartItem)
        cartList.layoutManager = LinearLayoutManager(baseContext)
        cartList.adapter = adapter
    }

    private fun getAddDeleteItems(oldList:ArrayList<CartItem>,list:ArrayList<CartItem>):Pair<ArrayList<CartItem>,ArrayList<CartItem>>{

        val deleteList : ArrayList<CartItem> = ArrayList()
        val addList : ArrayList<CartItem> = ArrayList()

        for(i in oldList){
            var flag = true
            val item1 = RestaurantItem.getItem(i.menuItem())
            for (j in list){
                val item2 = RestaurantItem.getItem(j.menuItem())
                if(item1.id() == item2.id() && i.sizePosition() == j.sizePosition()){
                    flag = false
                    break
                }
            }
            if(flag)
                deleteList.add(i)
        }

        for(i in list){
            var flag = true
            val item1 = RestaurantItem.getItem(i.menuItem())
            for (j in oldList){
                val item2 = RestaurantItem.getItem(j.menuItem())
                if(item1.id() == item2.id() && i.sizePosition() == j.sizePosition()){
                    flag = false
                    break
                }
            }
            if(flag)
                addList.add(i)
        }
        return Pair(deleteList,addList)
    }

    override fun getTotalPrice(totalPrice: Float) {
        price.text = String.format("%.2f",totalPrice)
    }

    override fun onDeleteItem(position: Int, cartItem: CartItem, totalPrice: Float) {
        val item = RestaurantItem.getItem(cartItem.menuItem())
        val totalPrice2 = (totalPrice - item.sizes()[cartItem.sizePosition()].price() * cartItem.quantity())
        price.text = String.format("%.2f",totalPrice2)
        listOfCartItem.removeAt(position)
        if(listOfCartItem.size == 0)
            checkOut.isEnabled = false
        adapter.deleteItem(listOfCartItem, price.text.toString().toFloat())
    }

    private fun getItem(id:Int):MenuItem{
        var menuItem : MenuItem? = null
            try {
                menuItem = db.item(id)
            }catch (e:Exception){}

        if(menuItem != null)
            return menuItem
        throw Exception()
    }

    override fun onPause() {
        super.onPause()
        editSharedPreferences.clear().apply()
        SHARE_PREFERENCES_INT_SIZE = listOfCartItem.size
        editSharedPreferences.putInt(MenuActivity.SHARE_PREFERENCES_STRING_SIZE, SHARE_PREFERENCES_INT_SIZE)
        for(i in 0 until  listOfCartItem.size){
            val item = RestaurantItem.getItem(listOfCartItem[i].menuItem())
            editSharedPreferences.putInt("$i${MenuActivity.SHARE_PREFERENCES_ITEM_ID}",item.id())
            editSharedPreferences.putInt("$i${MenuActivity.SHARE_PREFERENCES_QUANTITY}",listOfCartItem[i].quantity())
            editSharedPreferences.putInt("$i${MenuActivity.SHARE_PREFERENCES_SIZE_POSITION}",listOfCartItem[i].sizePosition())
        }
        editSharedPreferences.apply()
    }

    private fun orderPermission(db:AccessDatabase):Boolean{
        val lastOrder = lastOrder(db,client)
        return if(lastOrder.second && lastOrder.first == null)
            true
        else if(lastOrder.second && lastOrder.first != null) {
            val date = lastOrder.first!!.date()
            val duration = lastOrder.first!!.duration()
            val sdf = SimpleDateFormat("yyyy/MM/dd KK:mm:ss")
            val newDate = dateAfterDuration(date, duration)

            sdf.format(Date()) >= newDate
        } else
            false
    }

    companion object {
        fun lastOrder(db:AccessDatabase,client: Client?): Pair<Order?,Boolean> {

            if(client != null) {
                val list2: ArrayList<Pair<Int, Long>> = ArrayList()
                val list = db.checkOrders(client.username())
                if(list.size == 0) {
                    return Pair(null, true)
                }

                // done date
                for (i in list) {
                    val date = i.date()
                    var str = ""
                    for (j in date) {
                        if (j in '0'..'9')
                            str += j
                    }
                    list2.add(Pair(i.orderId(), str.toLong()))
                }

                // sort
                for (i in 0 until list2.size) {
                    for (j in i + 1 until list2.size) {
                        if (list2[i].second < list2[j].second) {
                            val pair = list2[i]
                            list2[i] = list2[j]
                            list2[j] = pair
                        }
                    }
                }
                // get lastOrder
                var lastOrder: Order? = null
                for (i in list) {
                    if (i.orderId() == list2[0].first)
                        lastOrder = i
                }

                if (lastOrder == null)
                    return Pair(lastOrder,false)
                return Pair(lastOrder,true)
            }
            throw Exception()
        }

        fun dateAfterDuration(date: String,duration: Int):String{
            var duration = duration
            var year = "${date[0]}${date[1]}${date[2]}${date[3]}".toInt()
            var month = "${date[5]}${date[6]}".toInt()
            var day = "${date[8]}${date[9]}".toInt()
            var hour = "${date[11]}${date[12]}".toInt()
            var minute = "${date[14]}${date[15]}".toInt()
            var second = "${date[17]}${date[18]}".toInt()

            while(true){
                second++
                duration--
                if(duration==0)
                    break
                if(second == 60){
                    second = 1
                    minute++
                }
                if(minute == 60){
                    minute = 1
                    hour++
                }
                if(hour == 24){
                    hour = 1
                    day++
                }
                if(day == 31 &&(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)){
                    day = 1
                    month++
                }
                else if(day == 30 &&(month == 4 || month == 6 || month == 9 || month == 11)){
                    day = 1
                    month++
                }
                else if (day == 28 && month == 2 && !leapYear(year)){
                    day = 1
                    month++
                }
                else if (day == 29 && month == 2 && leapYear(year)){
                    day = 1
                    month++
                }
                if(month == 12){
                    month = 1
                    year++
                }
            }

            return "$year/${String.format("%02d",month)}/${String.format("%02d",day)} ${String.format("%02d",hour)}:${String.format("%02d",minute)}:${String.format("%02d",second)} "
        }

        fun leapYear(year:Int):Boolean{
            if(year%2 == 0 && ((year%4 == 0 && year%100 !=0)||(year%4 == 0 && year%100 == 0 && year%400 == 0)))
                return true
            return false
        }
    }
}

