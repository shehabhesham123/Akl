package com.example.restaurant

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AnnouncementAdapter(private val listOfAnnouncement : ArrayList<Announcement>):RecyclerView.Adapter<AnnouncementAdapter.VH>() {

    private lateinit var  listener : AnnouncementRecyclerListener

    interface AnnouncementRecyclerListener{
        fun onAnnouncementClick(id: Int)
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        try { listener = parent.context as AnnouncementRecyclerListener }
        catch (e:Exception){ throw Exception("You don't implements from AnnouncementRecyclerListener") }

        val view = LayoutInflater.from(parent.context).inflate(R.layout.announcement,parent,false)
        return VH(view)
    }

    override fun getItemCount(): Int {
        return listOfAnnouncement.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val view = holder.itemView

        val image:ImageView = view.findViewById(R.id.Announcement_ImageView_Image)
        val date : TextView = view.findViewById(R.id.Announcement_TextView_Date)
        val type : TextView = view.findViewById(R.id.Announcement_TextView_Type)
        val description : TextView = view.findViewById(R.id.Announcement_TextView_Description)
        var des = listOfAnnouncement[position].announcement()
        if(des.length>235)
            des = des.substring(0,235)+"...."

        image.setImageURI(Uri.parse(listOfAnnouncement[position].image()))
        date.text = listOfAnnouncement[position].date()
        type.text = listOfAnnouncement[position].type()
        description.text = des

        view.setOnClickListener {
            listener.onAnnouncementClick(listOfAnnouncement[position].id())
        }
    }
}