package com.example.performancemanagementsystem.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.performancemanagementsystem.FeedbackModel
import com.example.performancemanagementsystem.R
import com.example.performancemanagementsystem.databinding.FragmentNewFeedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.fragment_new_feed.*


class newFeedFragment(companyCode: String) : Fragment() {

    private lateinit var newFeedBinding: FragmentNewFeedBinding

    val code = companyCode

    private lateinit var dbfirestore : FirebaseFirestore


    private  var arrayList: ArrayList<String> = ArrayList()



    private lateinit var member : String
    private lateinit var dbref : DatabaseReference

    private var keyValue = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbfirestore = Firebase.firestore


        newFeedBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_feed,
            container,
            false
        )

        dbref = FirebaseDatabase.getInstance().getReference("Feedback")





       // arrayList = ArrayList()

        newFeedBinding.floating.setOnClickListener {

            addQues()
        }

        newFeedBinding.submit.setOnClickListener {

            funcsubmit()

        }


        return newFeedBinding.root
    }

    private fun addQues() {

        if(arrayList.isEmpty())
        {
            val key = dbref.push().key
            keyValue = key.toString()

            arrayList.add("How is the comunication skills?")
            if (key != null) {
                //val key = dbref.push().key

                val feedback = FeedbackModel(key!!,code,"",arrayList)
                dbref.child(key).setValue(feedback)
            }

        }
        else
        {
            arrayList.add("How is the Speaking skills?")
            val feedback = FeedbackModel(keyValue,code,"",arrayList)
            dbref.child(keyValue).setValue(feedback)

        }


    }

    private fun funcsubmit() {

        Log.i("Key Value",keyValue)

        val feedb = hashMapOf(
            "Feedback Key" to keyValue
        )


        dbfirestore.collection(code).document(FirebaseAuth.getInstance().uid!!).set(feedb).addOnSuccessListener {
            Log.i("Document","Completed")
        }
            .addOnFailureListener {
                Log.i("Document","Failure")
            }



        member = newFeedBinding.memberName.text.toString()
        dbref.child(keyValue).child("member").setValue(member)

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.dash_container, DashFragment(code))
            .commit()





    }


}