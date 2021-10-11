package com.example.restaurant

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val USERNAME_CODE="USERNAME"

class AnnouncementActivity :
    AppCompatActivity(),
    AnnouncementAdapter.AnnouncementRecyclerListener,
    NavigationView.OnNavigationItemSelectedListener,
    RatingDialogFragment.RatingDialogFragmentListener,
    FeedbackDialogFragment.FeedbackFragmentListener,
    ExitFragment.ExitListener{

    private lateinit var client: Client
    private lateinit var container:FrameLayout
    private lateinit var toolbar : androidx.appcompat.widget.Toolbar
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navigationView : NavigationView
    private lateinit var db : AccessDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.announcement_activity)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val window = window
            window.statusBarColor = resources.getColor(R.color.statusBar)
        }

        ////////////////////////////////////////////////////////////////
        db = AccessDatabase.getInstance(baseContext)
        try {
            client = db.customer("Shehab")
            client.open(baseContext)
        }catch (e:Exception){
            Toast.makeText(baseContext,e.message,Toast.LENGTH_SHORT).show()
        }
        ////////////////////////////////////////////////////////////////

        container = findViewById(R.id.AnnouncementActivity_Container)
        toolbar = findViewById(R.id.AnnouncementActivity_toolbar)
        drawerLayout = findViewById(R.id.AnnouncementActivity_DrawerLayout)
        navigationView = findViewById(R.id.AnnouncementActivity_NavigationView)

        navigationView.setNavigationItemSelectedListener(this)

        val myTask = MyTask(db, supportFragmentManager) {
            setSupportActionBar(toolbar)
            supportActionBar?.elevation = 0f
            val toggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close)
            toggle.syncState()
            drawerLayout.addDrawerListener(toggle)
            toolbar.visibility = View.VISIBLE
         }
        myTask.execute()
    }

    private class MyTask(private val db:AccessDatabase, private val fm : FragmentManager, private val toolbar : () -> Unit):AsyncTask<Int,Int,ArrayList<Announcement>>(){

        override fun doInBackground(vararg params: Int?): ArrayList<Announcement> {
           return db.announcements()
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            val fragmentTransition = fm.beginTransaction()
            fragmentTransition.replace(R.id.AnnouncementActivity_Container,LoadingFragment())
            fragmentTransition.commit()
        }

        override fun onPostExecute(result: ArrayList<Announcement>?) {
            super.onPostExecute(result)
            val fragment = AnnouncementFragment.getInstance(result)
            val fragmentTransition = fm.beginTransaction()
            fragmentTransition.replace(R.id.AnnouncementActivity_Container,fragment)
            fragmentTransition.commit()
            toolbar()
        }
    }

    override fun onNavigationItemSelected(p: MenuItem): Boolean {

        var intent :Intent?= null
        var fragment :androidx.fragment.app.DialogFragment? = null
        when (p.itemId) {
            R.id.NavigationMenu_Food -> {
                intent = Intent(baseContext, MenuActivity::class.java)
                intent.putExtra(USERNAME_CODE,client.username())
            }
            R.id.NavigationMenu_Order -> {
                intent = Intent(baseContext, MyOrdersActivity::class.java)
                intent.putExtra(USERNAME,client.username())
            }
            R.id.NavigationMenu_Feedback -> fragment = FeedbackDialogFragment()
            R.id.NavigationMenu_Rate -> {
                fragment = RatingDialogFragment.getInstance(db.rate(client.username()))
            }
        }
        if(intent==null) fragment?.show(supportFragmentManager,null)
        else startActivity(intent)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun exit() {
        super.onBackPressed()
    }

    override fun onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START)
        else {
            val fragment = ExitFragment()
            fragment.show(supportFragmentManager,null)
        }
    }

    override fun onAnnouncementClick(id: Int) {
        try {
            val b = client.interact(id)
            if(b){
                val intent = Intent(baseContext,MenuActivity::class.java)
                intent.putExtra(USERNAME_CODE,client.username())
                startActivity(intent)
            }
        }catch (e:Exception){
            Toast.makeText(baseContext,e.message,Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRatingClick(rating: Int) {
        try {
            val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm:ss aa")
            val b = client.rateApp(Rating(client, rating, sdf.format(Date())))
        }
        catch (e:Exception){ Toast.makeText(baseContext,e.message,Toast.LENGTH_SHORT).show() }

        if(rating > 2)
            Toast.makeText(baseContext,"${resources.getText(R.string.RatingToastHappy)} $rating ${resources.getText(R.string.Star)} (:", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(baseContext,resources.getText(R.string.RatingTosatSad), Toast.LENGTH_LONG).show()
    }

    override fun onFeedBackClick(isHappy: Boolean, experience: String) {
        try {
            val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm:ss aa")
            val feedBack = FeedBack(client, isHappy, experience, sdf.format(Date()))
            client.makeFeedback(feedBack)
        }catch (e:Exception){
            Toast.makeText(baseContext,e.message,Toast.LENGTH_SHORT).show()
        }
    }


}