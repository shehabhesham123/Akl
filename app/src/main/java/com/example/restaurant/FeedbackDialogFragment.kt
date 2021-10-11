package com.example.restaurant

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class FeedbackDialogFragment : DialogFragment() {

    private var listener : FeedbackFragmentListener? = null

    interface FeedbackFragmentListener{
        fun onFeedBackClick(isHappy:Boolean, experience:String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {listener = context as FeedbackFragmentListener}
        catch (e:Exception){throw Exception("You don't implements from FeedbackFragmentListener")}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.feedback_fragment_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radioGroup:RadioGroup = view.findViewById(R.id.FeedbackFragment_RadioGroup)
        val experience: TextInputEditText = view.findViewById(R.id.FeedbackFragment_TextInputEditText_Experience)
        val experienceLength: TextView = view.findViewById(R.id.FeedbackFragment_TextView_ExperienceLetterLength)
        val submit : ImageButton = view.findViewById(R.id.FeedbackFragment_ImageButton_Submit)

        experience.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                experienceLength.text = "${s!!.length}/500"
            }
        })

        submit.setOnClickListener {
            val check:String = when( radioGroup.checkedRadioButtonId){
                R.id.FeedbackFragment_RadioButton_Happy-> "Happy"
                R.id.FeedbackFragment_RadioButton_Sad -> "Sad"
                else-> "-1"
            }
            if(check != "-1" ){
                listener?.onFeedBackClick(check == "Happy", experience.text.toString() )
                dismiss()
            }
        }
    }

}