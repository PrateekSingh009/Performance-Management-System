package com.example.performancemanagementsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.performancemanagementsystem.Adapter.EmployeeListAdapter
import com.example.performancemanagementsystem.Adapter.FeedbackListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ManagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)

        val dbrefFeedback = FirebaseDatabase.getInstance().getReference("Feedback")
        val dbrefCompanyInfo = FirebaseDatabase.getInstance().getReference("CompanyInfo")
        val dbrefFeedbackList = FirebaseDatabase.getInstance().getReference("FeedbackList")
        val dbrefAnswers = FirebaseDatabase.getInstance().getReference("Answers")
        val recyclerView : RecyclerView = findViewById(R.id.employeeList)

        dbrefCompanyInfo.child(FirebaseAuth.getInstance().currentUser!!.uid).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){

                    val companyInfoModel = snapshot.getValue(CompanyInfoModel::class.java)
                    val memberlist = companyInfoModel!!.memberList
                    val memberlistname : ArrayList<String> = ArrayList()
                    val memberlistid: ArrayList<String> = ArrayList()

                    for(i in memberlist!!)
                    {
                        memberlistname.add(i.name)
                        memberlistid.add(i.uid)
                    }
                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(context)
                        setHasFixedSize(true)
                        adapter = EmployeeListAdapter(context,memberlistname,memberlistid)
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
}