package com.example.restaurant

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)

        if (ContextCompat.checkSelfPermission(
                baseContext,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            val handler = Handler()
            handler.postDelayed({
                startActivity(parentActivityIntent)
                super.onBackPressed()
            }, 1500)

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.statusBarColor = Color.WHITE
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startActivity(parentActivityIntent)
        }
        else if(requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED){
            super.onBackPressed()
        }

    }
}