package com.example.performancemanagementsystem.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import com.example.performancemanagementsystem.R
import com.example.performancemanagementsystem.databinding.FragmentDashBinding
import kotlinx.android.synthetic.main.fragment_new_org.*


class DashFragment(companyCode : String) : Fragment() {

    private var code = companyCode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_dash,container,false)

        val createCard : soup.neumorphism.NeumorphCardView = view.findViewById(R.id.createFeed)
        val pendingCard :soup.neumorphism.NeumorphCardView = view.findViewById(R.id.pendingFeed)

        createCard.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.dash_container, newFeedFragment(code))
                .commit()


        }


        return view
    }


}