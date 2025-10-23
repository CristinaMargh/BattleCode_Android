package com.example.aplicatie.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.aplicatie.R

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Log.d("ProfileFragment", "Fragment loaded!")
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val textView = view.findViewById<TextView>(R.id.profile_text)
        textView.text = "Welcome to BattleCode!"

        return view
    }
}
