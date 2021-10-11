package com.example.restaurant

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment

private const val PAIR_BOOLEAN_CODE = "pairBoolean"
private const val PAIR_INT_CODE = "pairInt"
class RatingDialogFragment : DialogFragment() {

    private var listener : RatingDialogFragmentListener? = null
    private lateinit var pair : Pair<Boolean,Int>
    interface RatingDialogFragmentListener{
        fun onRatingClick(rating:Int)
    }

    companion object{
        fun getInstance(pair : Pair<Boolean,Int>) : RatingDialogFragment{
            val fragment = RatingDialogFragment()
            val bundle = Bundle()
            bundle.putBoolean(PAIR_BOOLEAN_CODE,pair.first)
            bundle.putInt(PAIR_INT_CODE,pair.second)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{listener = context as RatingDialogFragmentListener}
        catch (e:Exception){ throw Exception("You don't implements from RatingDialogFragmentListener")}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if(bundle != null){
            val b = bundle.getBoolean(PAIR_BOOLEAN_CODE)
            val rate = bundle.getInt(PAIR_INT_CODE)
            pair = Pair(b,rate)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.rating_fragment_dialog,container,false)
        val rating :RatingBar = view.findViewById(R.id.RatingDialogFragment_RatingBar_Rate)
        val submit :TextView = view.findViewById(R.id.RatingDialogFragment_TextView_Submit)
        val cancel : TextView = view.findViewById(R.id.RatingDialogFragment_TextView_Cancel)

        if(pair.first){
            rating.progress = pair.second
            rating.isEnabled = false
            submit.visibility = View.GONE
        }
        submit.setOnClickListener {
            listener?.onRatingClick(rating.progress)
            dismiss()
        }
        cancel.setOnClickListener {
            dismiss()
        }
        return view
    }
}