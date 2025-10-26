package com.example.aplicatie.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.aplicatie.R

private const val ARG_USERNAME = "arg_username"

class ProfileFragment : Fragment() {

    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username = arguments?.getString(ARG_USERNAME)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view.findViewById<TextView>(R.id.profile_text).text =
            "Welcome, ${username ?: "ANONIM"}!"
        return view
    }

    companion object {
        fun newInstance(username: String): ProfileFragment {
            val f = ProfileFragment()
            f.arguments = Bundle().apply { putString(ARG_USERNAME, username) }
            return f
        }
    }
}
