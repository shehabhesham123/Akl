package com.example.restaurant

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.app.NotificationCompat
import kotlin.Exception

const val ORDER_ID = "OrderID"
const val ORDER_DATE = "OrderDate"
const val ORDER_DURATION = "OrderDuration"
const val USERNAME = "username"
const val REQUEST = 2
const val EDIT_EXTRA = "edit"
const val REORDER_EXTRA = "reorder"

class MyOrdersActivity :
    AppCompatActivity(),
    MyOrdersAdapter.MyOrdersListener,
    MyOrdersFragment.MyOrdersFragmentListener{

    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editSharedPreferences : SharedPreferences.Editor

    companion object{
        private lateinit var db : AccessDatabase
        private var client : Client? = null
        fun duration(date1:String ,date2:String):Int{
            var duration:Int = 0
            var year1 = "${date1[0]}${date1[1]}${date1[2]}${date1[3]}".toInt()
            var month1 = "${date1[5]}${date1[6]}".toInt()
            var day1 = "${date1[8]}${date1[9]}".toInt()
            var hour1 = "${date1[11]}${date1[12]}".toInt()
            var minute1 = "${date1[14]}${date1[15]}".toInt()
            var second1 = "${date1[17]}${date1[18]}".toInt()

            val year2 = "${date2[0]}${date2[1]}${date2[2]}${date2[3]}".toInt()
            val month2 = "${date2[5]}${date2[6]}".toInt()
            val day2 = "${date2[8]}${date2[9]}".toInt()
            val hour2 = "${date2[11]}${date2[12]}".toInt()
            val minute2 = "${date2[14]}${date2[15]}".toInt()
            val second2 = "${date2[17]}${date2[18]}".toInt()

            val thread = Thread()
            thread.run {
                while (true) {
                    if (year1 == year2 && month1 == month2 && day1 == day2 && hour1 == hour2 && minute1 == minute2 && second1 == second2) {
                        break
                    }
                    duration++
                    second1++
                    if (second1 == 60) {
                        second1 = 1
                        minute1++
                    }
                    if (minute1 == 60) {
                        minute1 = 1
                        hour1++
                    }
                    if (hour1 == 24) {
                        hour1 = 1
                        day1++
                    }
                    if (day1 == 31 && (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12)) {
                        day1 = 1
                        month1++
                    } else if (day1 == 30 && (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11)) {
                        day1 = 1
                        month1++
                    } else if (day1 == 28 && month1 == 2 && !CartActivity.leapYear(year1)) {
                        day1 = 1
                        month1++
                    } else if (day1 == 29 && month1 == 2 && CartActivity.leapYear(year1)) {
                        day1 = 1
                        month1++
                    }
                    if (month1 == 12) {
                        month1 = 1
                        year1++
                    }
                }
                return duration
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_orders_activity)

        sharedPreferences = getSharedPreferences(MenuActivity.SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE)
        editSharedPreferences = sharedPreferences.edit()

        db = AccessDatabase.getInstance(baseContext)
        try{
            client = db.customer(intent.getStringExtra(USERNAME)!!)
        }
        catch (e:Exception){}
    }

    override fun onResume() {
        super.onResume()
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.MyOrdersActivity_container, MyOrdersFragment.getInstance(client!!.username()))
        ft.commit()
    }

    override fun onReorderClick(order: Order) {
        editSharedPreferences.clear().apply()
        val size = order.items().size
        editSharedPreferences.putInt(MenuActivity.SHARE_PREFERENCES_STRING_SIZE, size)
        for(i in 0 until   order.items().size){
            val item = RestaurantItem.getItem(order.items()[i].menuItem())
            editSharedPreferences.putInt("$i${MenuActivity.SHARE_PREFERENCES_ITEM_ID}",item.id())
            editSharedPreferences.putInt("$i${MenuActivity.SHARE_PREFERENCES_QUANTITY}", order.items()[i].quantity())
            editSharedPreferences.putInt("$i${MenuActivity.SHARE_PREFERENCES_SIZE_POSITION}", order.items()[i].sizePosition())
        }
        editSharedPreferences.apply()
        val intent = Intent(baseContext,CartActivity::class.java)
        intent.putExtra(USERNAME_CODE, client?.username())
        intent.putExtra(REORDER_EXTRA, true)
        startActivityForResult(intent,REQUEST)
    }

    override fun onEditClick(order: Order) {
        var sharedPreferences = getSharedPreferences(MenuActivity.SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE)
        var edit = sharedPreferences.edit()
        val size = order.items().size
        edit.clear().apply()
        edit.putInt(MenuActivity.SHARE_PREFERENCES_STRING_SIZE, size)
        for(i in 0 until size){
            val item = RestaurantItem.getItem(order.items()[i].menuItem())
            edit.putInt("$i${MenuActivity.SHARE_PREFERENCES_ITEM_ID}",item.id())
            edit.putInt("$i${MenuActivity.SHARE_PREFERENCES_QUANTITY}", order.items()[i].quantity())
            edit.putInt("$i${MenuActivity.SHARE_PREFERENCES_SIZE_POSITION}", order.items()[i].sizePosition())
        }
        edit.apply()

        sharedPreferences = getSharedPreferences(MenuActivity.SHARE_PREFERENCES_OLD_NAME, Context.MODE_PRIVATE)
        edit = sharedPreferences.edit()
        edit.clear().apply()
        edit.putInt(ORDER_ID,order.orderId())
        edit.putString(ORDER_DATE,order.date())
        edit.putInt(ORDER_DURATION,order.duration())
        edit.putInt(MenuActivity.SHARE_PREFERENCES_OLD_STRING_SIZE, size)
        for(i in 0 until size){
            val item = RestaurantItem.getItem(order.items()[i].menuItem())
            edit.putInt("$i${MenuActivity.SHARE_PREFERENCES_OLD_ITEM_ID}",item.id())
            edit.putInt("$i${MenuActivity.SHARE_PREFERENCES_OLD_QUANTITY}", order.items()[i].quantity())
            edit.putInt("$i${MenuActivity.SHARE_PREFERENCES_OLD_SIZE_POSITION}", order.items()[i].sizePosition())
        }
        edit.apply()

        val intent = Intent(baseContext,MenuActivity::class.java)
        intent.putExtra(EDIT_EXTRA, true)
        intent.putExtra(USERNAME_CODE, client?.username())
        startActivityForResult(intent, REQUEST)
    }

    override fun quarterTimeExecute(editButton: Button, fragment: TimerFragment,adapter:MyOrdersAdapter) {
        fragment.setButton(editButton)
        fragment.setAdapter(adapter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST)
            editSharedPreferences.clear().apply()
    }

}

