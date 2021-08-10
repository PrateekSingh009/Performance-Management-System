package com.example.performancemanagementsystem.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.performancemanagementsystem.FeedbackModel
import com.example.performancemanagementsystem.Fragments.ResponseFragment
import com.example.performancemanagementsystem.R
import com.google.firebase.database.*

class FeedbackListAdapter(private val activity: FragmentActivity?, list: ArrayList<String> ) :  RecyclerView.Adapter<FeedbackListAdapter.ViewHolder>(){

    private var list : ArrayList<String> = list
    private lateinit var dbrefFeedback : DatabaseReference

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val txt : TextView
        init {
            txt= view.findViewById(R.id.feedname)
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feedback_list_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        dbrefFeedback = FirebaseDatabase.getInstance().getReference("Feedback")
            dbrefFeedback.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(snap in snapshot.children){
                            if(snap.key == list[position])
                            {
                                val feedbackm = snap.getValue(FeedbackModel::class.java)
                                holder.txt.text = feedbackm!!.member

                            }

                        }

                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })

        holder.itemView.setOnClickListener {

            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.dash_container, ResponseFragment(list[position])).addToBackStack(null)
                .commit()

            Log.i("Item Clicked Key",position.toString())
        }


    }

    override fun getItemCount(): Int {
        return list.size

    }
}