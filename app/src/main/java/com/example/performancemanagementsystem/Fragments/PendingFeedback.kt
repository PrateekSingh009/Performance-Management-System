package com.example.performancemanagementsystem.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.performancemanagementsystem.Adapter.FeedbackListAdapter
import com.example.performancemanagementsystem.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_pending_feedback.*


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

        val btn : ImageView = view.findViewById(R.id.backbtn)
        val recyclerView : RecyclerView = view.findViewById(R.id.feedbackList)


        btn.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.dash_container, DashFragment())
                .commit()
        }



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
                            adapter = FeedbackListAdapter(activity , feedarray)
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