package com.example.performancemanagementsystem.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.performancemanagementsystem.Adapter.FeedbackListAdapter
import com.example.performancemanagementsystem.DashScreenActivity
import com.example.performancemanagementsystem.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class PendingFeedback(cmpcode: String) : Fragment() {

    val cmpcode = cmpcode
   private lateinit var dbrefFeedbackList : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_pending_feedback, container, false)
        dbrefFeedbackList = FirebaseDatabase.getInstance().getReference("FeedbackList")
        val recyclerView : RecyclerView = view.findViewById(R.id.feedbackList)



        dbrefFeedbackList.child(cmpcode).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children)
                {

                    if(snap.key == FirebaseAuth.getInstance().uid){

                        val feedarray: ArrayList<String> = ArrayList()
                        for(i in snap.children)
                        {
                            feedarray.add(i.value.toString())
                        }

                        recyclerView.apply {
                            layoutManager = LinearLayoutManager(context)
                            setHasFixedSize(true)
                            adapter = FeedbackListAdapter(activity as DashScreenActivity, feedarray)
                        }
                    }
                }

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })


        return view
    }





}