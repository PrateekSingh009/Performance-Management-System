package com.example.performancemanagementsystem.Fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.performancemanagementsystem.*
import com.example.performancemanagementsystem.Adapter.FeedbackListAdapter
import com.example.performancemanagementsystem.Adapter.QuestionListAdapter
import com.example.performancemanagementsystem.Fragments.NewOrgFragment.Companion.generatedcompanyCode
import com.example.performancemanagementsystem.R
import com.example.performancemanagementsystem.databinding.FragmentNewFeedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class newFeedFragment() : Fragment() {

    private lateinit var newFeedBinding: FragmentNewFeedBinding

    private lateinit var code: String


    private var arrayList: ArrayList<String> = ArrayList()


    private var feedb = hashMapOf<String, ArrayList<String>>()

    private lateinit var member: String
    private lateinit var dbref: DatabaseReference
    private lateinit var dbrefCompanyInfo: DatabaseReference
    private lateinit var dbrefFeedbackList: DatabaseReference

    private var keyValue = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dbrefCompanyInfo = FirebaseDatabase.getInstance().getReference("CompanyInfo")

        dbrefFeedbackList = FirebaseDatabase.getInstance().getReference("FeedbackList")









        newFeedBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_feed,
            container,
            false
        )

        dbref = FirebaseDatabase.getInstance().getReference("Feedback")
        code = "0"


        newFeedBinding.createFeedbackbtn.setOnClickListener {

            val intent = Intent(context, DashScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()

        }

//        if(generatedcompanyCode==0)
//        {
        dbrefCompanyInfo.child(FirebaseAuth.getInstance().uid!!).child("companyCode").get()
            .addOnSuccessListener {
                code = it.value.toString()
                Log.i("firebase", "Got value ${it.value}")
            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
//        }
//        else
//            code = generatedcompanyCode.toString()


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


        member = newFeedBinding.memberName.text.toString()
        if (member.isEmpty()) {
            newFeedBinding.memberName.error = "Field Required"
            return
        }
        var toadd = 0
       var exi =0
        //Feedback Already Exist
         dbref.addListenerForSingleValueEvent(object : ValueEventListener{
             override fun onDataChange(snapshot: DataSnapshot) {
                 if(snapshot.exists()) {
                     for (snap in snapshot.children) {
                         val feed = snap.getValue(FeedbackModel::class.java)

                         if (feed!!.member == member) {
                             Toast.makeText(context, "Feedback Already Exist", Toast.LENGTH_SHORT)
                                 .show()
                             newFeedBinding.memberName.error = "Active Feedback Already Exist"
                             toadd = 1
                             return
                         }
                     }
                 }
                     //Member Exist or not
                     if(toadd == 0){
                         dbrefCompanyInfo.child(FirebaseAuth.getInstance().currentUser!!.uid!!)
                             .addListenerForSingleValueEvent(object : ValueEventListener {
                                 override fun onDataChange(snapshot: DataSnapshot) {
                                     if(snapshot.exists()){
                                         val companyInfoModel = snapshot.getValue(CompanyInfoModel::class.java)
                                         val memberlist: ArrayList<UserModel> = companyInfoModel!!.memberList!!

                                         Log.i("Member",member)
                                         for(i in memberlist){
                                             Log.i("i name",i.name)
                                             if(i.name == member){
                                                 exi = 1

                                             }
                                         }

                                         if(exi == 1) {

                                             val ques: String = newFeedBinding.question.text.toString()
                                             if (ques.isEmpty()) {
                                                 newFeedBinding.question.error = "Field Required"
                                                 return
                                             }



                                             if (arrayList.isEmpty()) {
                                                 val key = dbref.push().key
                                                 keyValue = key.toString()



                                                 arrayList.add(ques)
                                                 if (key != null) {


                                                     val feedback = FeedbackModel(key!!, code, "", arrayList)
                                                     dbref.child(key).setValue(feedback)
                                                 }

                                             } else {
                                                 arrayList.add(ques)
                                                 val feedback = FeedbackModel(keyValue, code, "", arrayList)
                                                 dbref.child(keyValue).setValue(feedback)

                                             }

                                             val empty: String = ""
                                             newFeedBinding.question.setText(empty)


                                             dbref.child(keyValue).addValueEventListener(object : ValueEventListener {
                                                 override fun onDataChange(snapshot: DataSnapshot) {
                                                     val feedback = snapshot.getValue(FeedbackModel::class.java)
                                                     val quelist = feedback!!.questionList
                                                     newFeedBinding.questionList.apply {
                                                         layoutManager = LinearLayoutManager(context)
                                                         setHasFixedSize(true)
                                                         adapter = QuestionListAdapter(quelist!!)
                                                     }


                                                 }

                                                 override fun onCancelled(error: DatabaseError) {
                                                     TODO("Not yet implemented")
                                                 }

                                             })

                                         }
                                         else{
                                             newFeedBinding.memberName.error = "Member doesn't EXIST"
                                             return}
                                     }
                                 }

                                 override fun onCancelled(error: DatabaseError) {
                                     TODO("Not yet implemented")
                                 }

                             })


                     }




             }

             override fun onCancelled(error: DatabaseError) {
                 TODO("Not yet implemented")
             }

         })



    }

    private fun funcsubmit() {


        var arrayList = ArrayList<String>()

//        dbfirestore.collection(code).document(FirebaseAuth.getInstance().uid!!).get().addOnSuccessListener { document ->
//            if (document != null) {
//                Log.d(TAG, "DocumentSnapshot data: ${document.data}")
//                arrayList = document["Feedback Key",ArrayList<String>]
//                feedb = document.data as HashMap<String,ArrayList<String>>
//
//            } else {
//                feedb = hashMapOf()
//                Log.d(TAG, "No such document")
//            }
//        }
//            .addOnFailureListener { exception ->
//                Log.d(TAG, "get failed with ", exception)
//            }
//
//
//        if(feedb.isEmpty()){
//            feedb.put("Feedback Key", keyValue)
//
//        }
//        else{
//            feedb.put("Feedback Key", keyValue)
//
//        }
//        feedb.put("Feedback Key",keyValue )
//        Log.i("feedb",feedb.toString())


//        dbfirestore.collection(code).document(FirebaseAuth.getInstance().uid!!).set(feedb).addOnSuccessListener {
//            Log.i("Document", "Completed")
//        }
//            .addOnFailureListener {
//                Log.i("Document", "Failure")
//            }
//


        member = newFeedBinding.memberName.text.toString()
        if (member.isEmpty()) {
            newFeedBinding.memberName.error = "Field Required"
            return
        }

        var exist = 0
        dbrefCompanyInfo.child(FirebaseAuth.getInstance().uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val companyinfo = snapshot.getValue(CompanyInfoModel::class.java)
                    val arlist = companyinfo!!.memberList
                    Log.i("Employee List",arlist.toString())
                    for(i in arlist!!){
                        if(i.name != member){
                            dbrefFeedbackList.child(code).child(i.uid)
                                .addListenerForSingleValueEvent(object : ValueEventListener {

                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if(dataSnapshot.exists()) {



                                                    val feedList = dataSnapshot.getValue() as ArrayList<String>

                                                    feedList.add(keyValue)
                                                    Log.i("Key", snapshot.key!!)
                                                    dbrefFeedbackList.child(code)
                                                        .child(dataSnapshot.key!!)
                                                        .setValue(feedList)


                                        }
                                        else
                                        {
                                            val feedList: ArrayList<String> =ArrayList()
                                            feedList.add(keyValue)
                                            dbrefFeedbackList.child(code)
                                                .child(dataSnapshot.key!!)
                                                .setValue(feedList)
                                        }


                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }


                                })
                        }


                    }
                    for (i in arlist!!) {
                        if (i.name == member) {

                            dbref.child(keyValue).child("member").setValue(member)

                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.dash_container, DashFragment())
                                .commit()
                            exist = 1
                            break

                        }


                    }
                    if (exist == 0) {
                        newFeedBinding.memberName.error = "Member doesn't EXIST"
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

    }

}