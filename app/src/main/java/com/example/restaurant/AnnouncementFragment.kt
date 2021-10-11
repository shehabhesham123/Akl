package com.example.restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ID_CODE="ID"
private const val IMAGE_CODE="IMAGE"
private const val DATE_CODE="DATE"
private const val TYPE_CODE="TYPE"
private const val DESCRIPTION_CODE="DESCRIPTION"
private var SIZE : Int = 0

class AnnouncementFragment : Fragment()  {

    private val listOfAnnouncement : ArrayList<Announcement> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if(bundle != null) {
            for (i in 0 until SIZE) {
                val id: Int = bundle.getInt("$i$ID_CODE", -1)
                val image: String = bundle.getString("$i$IMAGE_CODE", "")
                val date: String = bundle.getString("$i$DATE_CODE", null)
                val type: String = bundle.getString("$i$TYPE_CODE", null)
                val description: String = bundle.getString("$i$DESCRIPTION_CODE", null)
                listOfAnnouncement.add(Announcement(id,image, date, type, description))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.announcement_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view as RecyclerView
        recyclerView.layoutManager = GridLayoutManager(context , 1,GridLayoutManager.VERTICAL , false)
        recyclerView.adapter = AnnouncementAdapter(listOfAnnouncement)
    }

    companion object{
        fun getInstance(list : ArrayList<Announcement>?):AnnouncementFragment{
            if(list == null)
                throw Exception()

            SIZE = list.size
            val bundle = Bundle()
            for(i in 0 until list.size){
                bundle.putString("$i$IMAGE_CODE", list[i].image())
                bundle.putString("$i$DATE_CODE",list[i].date())
                bundle.putString("$i$TYPE_CODE",list[i].type())
                bundle.putString("$i$DESCRIPTION_CODE",list[i].announcement())
                bundle.putInt("$i$ID_CODE",list[i].id())
            }
            val fragment = AnnouncementFragment()
            fragment.arguments=bundle
            return fragment
        }
    }
}


