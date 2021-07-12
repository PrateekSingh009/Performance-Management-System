package com.example.performancemanagementsystem.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.performancemanagementsystem.Adapter.FeedbackListAdapter
import com.example.performancemanagementsystem.Adapter.QuestionListAdapter
import com.example.performancemanagementsystem.Adapter.ResponseListAdapter
import com.example.performancemanagementsystem.DashScreenActivity
import com.example.performancemanagementsystem.FeedbackModel
import com.example.performancemanagementsystem.R
import com.google.firebase.database.*


class ResponseFragment(private val feedId : String) : Fragment() {


    private lateinit var dbrefFeedback :DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_response, container, false)

        dbrefFeedback = FirebaseDatabase.getInstance().getReference("Feedback")
        val txt :TextView = view.findViewById(R.id.feedbackName)
        val resRecyclerView : RecyclerView = view.findViewById(R.id.responseQuesList)

        dbrefFeedback.child(feedId).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    val feedModel = snapshot.getValue(FeedbackModel::class.java)
                    val quesList = feedModel!!.questionList


                        resRecyclerView.apply {
                            layoutManager = LinearLayoutManager(context)
                            setHasFixedSize(true)
                            adapter = ResponseListAdapter(quesList!!)
                        }
                    }

                else
                    Toast.makeText(context,"Feedback Not Found",Toast.LENGTH_SHORT).show()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.message,Toast.LENGTH_SHORT).show()
            }


        })



        return view
    }


}