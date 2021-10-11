package com.example.restaurant

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class ExitFragment : DialogFragment() {

    private var listener :ExitListener? = null
    interface ExitListener{
        fun exit()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{listener = context as ExitListener}
        catch (e:Exception) {throw Exception("You don't implements from ExitListener")}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.exit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val yes :TextView = view.findViewById(R.id.ExitFragment_TextView_Yes)
        val no :TextView = view.findViewById(R.id.ExitFragment_TextView_No)
        yes.setOnClickListener {
            listener?.exit()
            dismiss()
        }
        no.setOnClickListener { dismiss() }

    }

}