package com.example.restaurant

import android.content.Context

class Admin(context: Context) {

    private val db : AccessDatabase = AccessDatabase.getInstance(context)

    fun addMenuItem(menuItem: MenuItem):Boolean{
        return db.item(menuItem)
    }

    fun addAnnouncement(announcement: Announcement):Boolean{
        return db.announcement(announcement)
    }
}