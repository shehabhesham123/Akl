package com.example.restaurant

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import kotlin.math.floor

private const val DURATION_EXTRA:String = "DURATION"
private const val DURATION_REMAINING_EXTRA:String = "DURATION_REMAINING"

class TimerFragment : Fragment() {

    private var duration :Int = 0
    private var durationRemaining :Int = 0
    private var quarterTime : Double = 0.0
    private lateinit var timer : TextView
    private lateinit var editButton :Button          //رار ال edit بتاع ال headOrder علشان لما يخلص ربع الوقت يخليه notEnable
    private lateinit var adapter:MyOrdersAdapter        // ده ال adapter اللى فيه كل ال orders علشان لما الوقت يخلص اخلى ال reorder تظهر

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if(bundle != null) {
            duration = bundle.getInt(DURATION_EXTRA)
            durationRemaining = bundle.getInt(DURATION_REMAINING_EXTRA)
            quarterTime = floor(duration.toFloat() - (duration.toFloat()/4f).toDouble())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.my_orders_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timer = view.findViewById(R.id.TimerFragment_TextView_Timer)
        timer()
    }

    private fun timer(){
        val handler= Handler()
        handler.post(object:Runnable{
            override fun run() {
                val time = getTime(durationRemaining)
                timer.text = time

                if(durationRemaining > 0) {
                    durationRemaining--
                    handler.postDelayed(this, 1000)
                }
                else{
                    adapter.isEnable(true)
                    adapter.notifyDataSetChanged()
                }

                if(durationRemaining <= quarterTime) {
                    editButton.isEnabled = false
                }
            }
        })
    }

    fun setButton(editButton:Button){
        this.editButton = editButton
    }

    fun setAdapter(adapter:MyOrdersAdapter){
        this.adapter = adapter
    }

    fun getTime(sec:Int):String{
        var hh = (sec/3600).toString()
        var mm = ((sec%3600)/60).toString()
        var ss = (sec%60).toString()
        if(hh.length == 1) hh ="0$hh"
        if(mm.length == 1) mm ="0$mm"
        if(ss.length == 1) ss ="0$ss"
        return "$hh:$mm:$ss"
    }

    companion object{
        fun getInstance(duration:Int, durationRemaining: Int):TimerFragment{
            val timerFragment = TimerFragment()
            val bundle = Bundle()
            bundle.putInt(DURATION_EXTRA,duration)
            bundle.putInt(DURATION_REMAINING_EXTRA,durationRemaining)
            timerFragment.arguments=bundle
            return timerFragment
        }
    }

}