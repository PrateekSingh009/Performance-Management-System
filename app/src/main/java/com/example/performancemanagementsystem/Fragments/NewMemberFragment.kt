package com.example.performancemanagementsystem.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.performancemanagementsystem.R
import com.example.performancemanagementsystem.databinding.FragmentNewMemberBinding


class NewMemberFragment(username : String, email : String, uid : String) : Fragment() {

    val username = username
    val email = email
    val uid= uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val newMemberBinding : FragmentNewMemberBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_new_member,container,false)

        newMemberBinding.newOrg.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.auth_container, NewOrgFragment(username,email,uid))
                .commit()

        }

        newMemberBinding.existOrg.setOnClickListener {
            CustomDialog(username,email,uid).show(requireFragmentManager(),"Custom Dialog")

        }




        return newMemberBinding.root
    }


}