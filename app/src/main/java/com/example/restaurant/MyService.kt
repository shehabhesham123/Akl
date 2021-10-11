package com.example.restaurant

import android.app.IntentService
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.core.app.NotificationCompat

const val NOTIFICATION = "Notification"
class MyService : IntentService("MyService") {
    override fun onHandleIntent(intent: Intent?) {
        val duration = intent?.getIntExtra(ORDER_DURATION,0)
      synchronized(this){
          try {
              if(duration != null && duration > 0)
                  Thread.sleep((duration.toLong()*1000))
          }catch (e:InterruptedException){}
      }
        val username = intent?.getStringExtra(USERNAME_CODE)
        if(username!=null && duration!= null && duration > 0)
            notification(username)
    }

    private fun notification(username:String){
        val notification = NotificationCompat.Builder(baseContext)
        val intent2 = Intent(baseContext,MenuActivity::class.java)
        intent2.putExtra(NOTIFICATION,true)
        intent2.putExtra(USERNAME_CODE, username)
        val pendingIntent = PendingIntent.getActivity(baseContext,0, intent2, PendingIntent.FLAG_UPDATE_CURRENT)
        notification.setContentTitle("Akl").setContentText("Your previous order is over, now you can order another order").setSmallIcon(R.drawable.ic_restaurant)
            .setVibrate(longArrayOf(0,100,100,100)).addAction(R.drawable.ic_shopping_cart_24,"Shopping",pendingIntent).priority = NotificationCompat.PRIORITY_HIGH
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1,notification.build())
        val mediaPlayer = MediaPlayer.create(baseContext,R.raw.pop)
        mediaPlayer.start()
    }
}
