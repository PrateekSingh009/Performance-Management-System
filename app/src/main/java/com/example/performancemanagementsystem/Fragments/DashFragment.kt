package com.example.performancemanagementsystem.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import com.example.performancemanagementsystem.R
import com.example.performancemanagementsystem.databinding.FragmentDashBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_new_org.*


class DashFragment() : Fragment() {



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

        val dbrefCompanyInfo = FirebaseDatabase.getInstance().getReference("CompanyInfo")


        dbrefCompanyInfo.addListenerForSingleValueEvent(
            object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.hasChild(FirebaseAuth.getInstance().uid!!)) {
                        createCard.visibility = INVISIBLE
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        createCard.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.dash_container, newFeedFragment())
                .commit()


        }


        return view
    }


}