package com.example.restaurant

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.lang.Exception

private const val NAME_EXTRA = "name"
private const val ID_EXTRA = "id"
private const val IS_RATE_EXTRA = "isRate"
private const val PREVIOUS_RATE_EXTRA = "rate"
class RateItemDialogFragment : DialogFragment() {

    private var name : String? = null
    private var itemID : Int = -1
    private var isRate : Boolean = false
    private var previousRate : Float = 0f
    private var listener : RateItemListener? = null

    interface RateItemListener{
        fun onSubmitRate(rate:Float,itemID:Int)
        fun onSubmitRateWithEdit(rate:Float,itemID:Int)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{listener = context as RateItemListener}
        catch (e:Exception){throw Exception("you don't implements from RateItemListener")}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if(bundle != null) {
            name = bundle.getString(NAME_EXTRA)
            itemID = bundle.getInt(ID_EXTRA)
            isRate = bundle.getBoolean(IS_RATE_EXTRA)
            previousRate = bundle.getFloat(PREVIOUS_RATE_EXTRA)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.rate_item_fragment_dialog, container, false)
        val sentence : TextView = view.findViewById(R.id.RateItemDialogFragment_TextView_RateSentence)
        val name :TextView = view.findViewById(R.id.RateItemDialogFragment_TextView_Name)
        val rate :RatingBar = view.findViewById(R.id.RateItemDialogFragment_RatingBar_Rate)
        val submit :ImageView = view.findViewById(R.id.RateItemDialogFragment_ImageView_Submit)
        val edit :ImageView = view.findViewById(R.id.RateItemDialogFragment_ImageView_EditRate)

        var oldProcess : Float = -1f

        if(isRate){
            sentence.text = resources.getText(R.string.RateItemDialogFragment_RateSentence2)
            rate.rating = previousRate
            edit.visibility = View.VISIBLE
            submit.isEnabled = false
            rate.isEnabled = false
        }
        name.text = this.name
        submit.setOnClickListener {
            if(oldProcess == -1f)
                listener?.onSubmitRate(rate.rating, itemID)
            else if(oldProcess != rate.rating)
                listener?.onSubmitRateWithEdit(rate.rating,itemID)
            dismiss()
        }
        edit.setOnClickListener {
            oldProcess = rate.rating
            sentence.text = resources.getText(R.string.RateItemDialogFragment_RateSentence)
            edit.visibility = View.GONE
            submit.isEnabled = true
            rate.isEnabled = true
        }
        return view
    }

    companion object{

        fun getInstance(name:String, id:Int, b:Pair<Boolean,Float>): RateItemDialogFragment{
            val bundle = Bundle()
            bundle.putString(NAME_EXTRA, name)
            bundle.putInt(ID_EXTRA, id)
            bundle.putBoolean(IS_RATE_EXTRA, b.first)
            bundle.putFloat(PREVIOUS_RATE_EXTRA, b.second)
            val instance = RateItemDialogFragment()
            instance.arguments = bundle
            return instance
        }
    }

}