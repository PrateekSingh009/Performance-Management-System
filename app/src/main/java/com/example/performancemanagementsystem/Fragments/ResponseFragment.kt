package com.example.performancemanagementsystem.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.performancemanagementsystem.*
import com.example.performancemanagementsystem.Adapter.FeedbackListAdapter
import com.example.performancemanagementsystem.Adapter.QuestionListAdapter
import com.example.performancemanagementsystem.Adapter.ResponseListAdapter
import com.example.performancemanagementsystem.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.net.IDN


class ResponseFragment(private val feedId: String) : Fragment() {


    private lateinit var dbrefFeedback: DatabaseReference
    private lateinit var dbrefCompanyInfo: DatabaseReference
    private lateinit var answers: Array<String>
    private lateinit var dbrefAnswers : DatabaseReference
    private lateinit var dbrefFeedbackList : DatabaseReference
    private lateinit var nametxt : TextView
    private lateinit var Idtxt :TextView
    private lateinit var cmpnametxt :TextView
    private lateinit var cmpcode : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_response, container, false)

        dbrefFeedback = FirebaseDatabase.getInstance().getReference("Feedback")
        dbrefCompanyInfo = FirebaseDatabase.getInstance().getReference("CompanyInfo")
        dbrefFeedbackList = FirebaseDatabase.getInstance().getReference("FeedbackList")
        dbrefAnswers = FirebaseDatabase.getInstance().getReference("Answers")
        nametxt = view.findViewById(R.id.RName)
        val resRecyclerView: RecyclerView = view.findViewById(R.id.responseQuesList)
        Idtxt = view.findViewById(R.id.RMemberID)
        cmpnametxt = view.findViewById(R.id.RCmpName)
        val btn: Button = view.findViewById(R.id.submit_response)

        dbrefFeedback.child(feedId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val feedModel = snapshot.getValue(FeedbackModel::class.java)
                    val quesList = feedModel!!.questionList
                    nametxt.text = feedModel.member
                    cmpcode = feedModel.companyCode

                    dbrefCompanyInfo.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (snap in snapshot.children) {
                                    val cmpModel = snap.getValue(CompanyInfoModel::class.java)
                                    if (cmpModel!!.companyCode == feedModel.companyCode){
                                        cmpnametxt.text = cmpModel.companyName
                                        for(i in cmpModel.memberList!!){
                                            if(i.name == feedModel.member)
                                                Idtxt.text = i.uid
                                        }
                                    }

                                }


                            } else {
                                Log.i("ResponseFragmentCompany", "SnapShot not exist")
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }


                    })
                    val sizze = quesList!!.size
                    Log.i("Queslist Size", sizze.toString())
                    answers = Array(sizze) { "" }
                    resRecyclerView.apply {
                        layoutManager = LinearLayoutManager(context)
                        setHasFixedSize(true)
                        adapter = ResponseListAdapter(quesList, answers)
                    }
                } else
                    Toast.makeText(context, "Feedback Not Found", Toast.LENGTH_SHORT).show()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }


        })
        btn.setOnClickListener {
            Log.i("FinalAnswers", answers.contentToString())

            val answersAsList : List<String> = answers.toList()

            val map = hashMapOf<String,List<String>>()
            map.put(FirebaseAuth.getInstance().currentUser!!.uid , answersAsList)

//            val answerlist:ArrayList<List<String>> = ArrayList()
//            answerlist.add(answersAsList)

//            val resModel : AnswersModel = AnswersModel(Idtxt.text.toString(),map)
//
//            dbrefAnswers.child(feedId).setValue(resModel)

            dbrefAnswers.child(feedId).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val answersnap :AnswersModel = snapshot.getValue(AnswersModel::class.java)!!
                        val tempmap :HashMap<String,List<String>> = answersnap.reponses
                        tempmap.put(FirebaseAuth.getInstance().currentUser!!.uid , answersAsList)
                        val resModel : AnswersModel = AnswersModel(Idtxt.text.toString(),tempmap)

                        dbrefAnswers.child(feedId).setValue(resModel)


                    }
                    else
                    {
                        val resModel : AnswersModel = AnswersModel(Idtxt.text.toString(),map)

                        dbrefAnswers.child(feedId).setValue(resModel)

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })



            Log.i("Company Code ",cmpcode)

            dbrefFeedbackList.child(cmpcode).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(snap in snapshot.children){

                        if(snap.key == FirebaseAuth.getInstance().currentUser!!.uid) {

                            val feedlist : ArrayList<String> = snap.getValue() as ArrayList<String>
                            Log.i("FeedLsit",feedlist.toString())


                            feedlist.remove(feedId)

                            if(feedlist.isNullOrEmpty())
                                feedlist.add("")
                            Log.i("FeedLsitAfter removing",feedlist.toString())
                            dbrefFeedbackList.child(cmpcode).child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(feedlist)

                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })




//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.dash_container, PendingFeedback(cmpcode))
//                .commit()



        }


        return view
    }


}