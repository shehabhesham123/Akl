package com.example.restaurant


import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception


private var SHARE_PREFERENCES_INT_SIZE = 0
private var SHARE_PREFERENCES_OLD_INT_SIZE = 0
private var REQUEST_CODE = 1
private var REQUEST_ORDER_ACTIVITY_CODE = 2
var RESULT_CODE_CANCELED = 3

class MenuActivity :
    AppCompatActivity() ,
    MenuAdapter.MenuRecyclerListener,
    RateItemDialogFragment.RateItemListener,
    ItemDetailsSizeAndPriceAdapter.SizeListener,
    ItemDetailsFragment.ItemOrderListener,
    MenuFragment.MenuListener {

    companion object{
        var isEdit : Boolean = false
        var isReorder : Boolean = false
        const val SHARE_PREFERENCES_NAME = "cartItems"
        const val SHARE_PREFERENCES_ITEM_ID = "id"
        const val SHARE_PREFERENCES_SIZE_POSITION = "sizePosition"
        const val SHARE_PREFERENCES_QUANTITY = "quantity"
        var SHARE_PREFERENCES_STRING_SIZE = "size"
        const val SHARE_PREFERENCES_OLD_NAME = "old"
        const val SHARE_PREFERENCES_OLD_ITEM_ID = "id"
        const val SHARE_PREFERENCES_OLD_SIZE_POSITION = "sizePosition"
        const val SHARE_PREFERENCES_OLD_QUANTITY = "quantity"
        var SHARE_PREFERENCES_OLD_STRING_SIZE = "size"
    }

    private lateinit var client: Client
    private lateinit var db :AccessDatabase
    private lateinit var itemDetailsFragment : ItemDetailsFragment
    private lateinit var menuFragment : MenuFragment
    private val listOfCartItem = ArrayList<CartItem>()
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editSharedPreferences : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_activity)

        sharedPreferences = getSharedPreferences(SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE)

        editSharedPreferences = sharedPreferences.edit()
        db = AccessDatabase.getInstance(baseContext)

        val intent = intent
        val isNotification = intent.getBooleanExtra(NOTIFICATION,false)
        if(isNotification){
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(1)
        }
        isEdit = intent.getBooleanExtra(EDIT_EXTRA,false)
        isReorder = intent.getBooleanExtra(REORDER_EXTRA,false)
        try {
            val username = intent.getStringExtra(USERNAME_CODE)
            if(!username.isNullOrEmpty() && !username.isNullOrBlank()) {
                client = db.customer(username)
                client.open(baseContext)
            }
        }
        catch (e:Exception){ Toast.makeText(baseContext,e.message,Toast.LENGTH_SHORT).show() }
    }

    override fun onResume() {
        super.onResume()
        listOfCartItem.clear()

        // restore cart item data from shared preferences and put them in listOfItems then clear shared ////////////
        val size = sharedPreferences.getInt(SHARE_PREFERENCES_STRING_SIZE,0)
        for(i in 0 until size){
            val id = sharedPreferences.getInt("$i$SHARE_PREFERENCES_ITEM_ID",-1)
            val quantity = sharedPreferences.getInt("$i$SHARE_PREFERENCES_QUANTITY",-1)
            val sizePosition = sharedPreferences.getInt("$i$SHARE_PREFERENCES_SIZE_POSITION",-1)
            listOfCartItem.add(CartItem(getItem(id),sizePosition,quantity))
        }
        editSharedPreferences.clear().apply()
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        menuFragment = MenuFragment.getInstance(client.username())
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.MenuActivity_Container,menuFragment)
        ft.commit()
    }

    override fun onPause() {
        super.onPause()
        SHARE_PREFERENCES_INT_SIZE = listOfCartItem.size
        editSharedPreferences.putInt(SHARE_PREFERENCES_STRING_SIZE, SHARE_PREFERENCES_INT_SIZE)
        for (i in 0 until listOfCartItem.size) {
            val item = RestaurantItem.getItem(listOfCartItem[i].menuItem())
            editSharedPreferences.putInt("$i$SHARE_PREFERENCES_ITEM_ID", item.id())
            editSharedPreferences.putInt(
                "$i$SHARE_PREFERENCES_QUANTITY",
                listOfCartItem[i].quantity()
            )
            editSharedPreferences.putInt(
                "$i$SHARE_PREFERENCES_SIZE_POSITION",
                listOfCartItem[i].sizePosition()
            )
        }
        editSharedPreferences.apply()

    }

    override fun onMenuItemClick(menuItem: MenuItem) {
        itemDetailsFragment = ItemDetailsFragment.getInstance(menuItem)
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.MenuActivity_Container,itemDetailsFragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    override fun onMenuItemLongClick(menuItem: MenuItem) {
        val item = RestaurantItem.getItem(menuItem)
        val b = db.itemRate(client.username(),item.id())
        val fragment = RateItemDialogFragment.getInstance(item.name(),item.id(),b)
        fragment.show(supportFragmentManager,null)
    }

    override fun onSubmitRate(rate:Float,itemID:Int) {
        client.rateItem(rate,itemID)
        // to refresh
        menuFragment.refreshAdapter()
    }

    override fun onSubmitRateWithEdit(rate: Float, itemID: Int) {
        try{ val d = client.editRateItem(rate,itemID) }
        catch (e:Exception){}
        // to refresh
        menuFragment.refreshAdapter()
    }

    override fun sizeChanged(position : Int) {
        itemDetailsFragment.sizeChanged(position)
    }

    override fun addToCart(cartItem: CartItem?) {
        if(cartItem == null){
            Toast.makeText(baseContext,"ERROR",Toast.LENGTH_SHORT).show()
        }
        else {
            var flag = true
            for(i in listOfCartItem){
                val item1 = RestaurantItem.getItem(i.menuItem())
                val item2 = RestaurantItem.getItem(cartItem.menuItem())
                if(item1.name() == item2.name() && item1.sizes()[i.sizePosition()].size() == item2.sizes()[cartItem.sizePosition()].size()) {
                    flag = false
                    break
                }
            }
            if(flag) {
                listOfCartItem.add(cartItem)
                val menuItem = cartItem.menuItem()
                val item = RestaurantItem.getItem(menuItem)
                val toast = Toast(baseContext)
                val view = LayoutInflater.from(baseContext).inflate(R.layout.toast, null, false)
                val image: ImageView = view.findViewById(R.id.Toast_ImageView_Image)
                val title: TextView = view.findViewById(R.id.Toast_TextView_Title)
                image.setImageURI(Uri.parse(item.images()[0]))
                var n = item.name()
                if(n.length > 20)
                    n = n.substring(0,20)+"..."
                title.text = n
                toast.view = view
                toast.duration = Toast.LENGTH_LONG
                toast.show()
            }
            else{
                val item = RestaurantItem.getItem(cartItem.menuItem())
                val view :FrameLayout= findViewById(R.id.MenuActivity_Container)
                Snackbar.make(view,"${item.name()} ${resources.getText(R.string.MenuActivity_SnackBar_Sentence1)}",Snackbar.LENGTH_SHORT).setAction("${resources.getText(R.string.MenuActivity_SnackBar_Sentence2)}",
                    View.OnClickListener {
                        itemDetailsFragment = ItemDetailsFragment.getInstance(cartItem.menuItem())
                        val ft = supportFragmentManager.beginTransaction()
                        ft.add(R.id.MenuActivity_Container,itemDetailsFragment)
                        ft.addToBackStack(null)
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        ft.commit()
                    }).show()
            }
            super.onBackPressed()
        }
    }

    override fun cart() {
        val intent = Intent(baseContext,CartActivity::class.java)
        intent.putExtra(USERNAME_CODE,client.username())
        if(isEdit || isReorder)
            startActivityForResult(intent, REQUEST_ORDER_ACTIVITY_CODE)
        else
            startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ORDER_ACTIVITY_CODE && resultCode == Activity.RESULT_OK){
            val intent = Intent()
            intent.putExtra(USERNAME,client.username())
            setResult(REQUEST,intent)
            finish()
        }
        else if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val intent = Intent(baseContext,MyOrdersActivity::class.java)
            intent.putExtra(USERNAME,client.username())
            startActivity(intent)
            finish()
        }
        else if(resultCode == RESULT_CODE_CANCELED){
            Toast.makeText(baseContext,"May be error has happened",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getItem(id:Int):MenuItem{
        var menuItem : MenuItem
        val thread = Thread()
        thread.run {
            menuItem = db.item(id)
        }
        return menuItem
    }

}
