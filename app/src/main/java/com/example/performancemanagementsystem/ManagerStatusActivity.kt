package com.example.performancemanagementsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.performancemanagementsystem.Fragments.MangerStatusFragment
import com.example.performancemanagementsystem.Fragments.StatusFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ManagerStatusActivity : AppCompatActivity() {

    private lateinit var dbrefGraphData : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_status)

        val extras : Bundle = intent.extras!!
        val username : String = extras.get("Name").toString()
        val uid : String = extras.get("ID").toString()
        dbrefGraphData = FirebaseDatabase.getInstance().getReference("GraphData")
        val dbrefAnswer = FirebaseDatabase.getInstance().getReference("Answers")
        Log.i("Uid Current", FirebaseAuth.getInstance().currentUser!!.uid)
        dbrefAnswer.addListenerForSingleValueEvent(object : ValueEventListener {


            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    for(snap in snapshot.children)
                    {
                        val model = snap.getValue(AnswersModel::class.java)
                        Log.i("Uid Current",model!!.memberId)
                        if(model!!.memberId == uid)
                        {
                            Log.i("Uid Current", uid)
                            val avglist : Array<Int> = Array(100){0}
                            val noOfMembers = model.reponses.size
                            for(i in model.reponses)
                            {
                                val answerforeach :List<String> = i.value

                                for(j in answerforeach.indices){
                                    var value = 0
                                    when(answerforeach[j])
                                    {
                                        "Excellent"->{
                                            value = 4
                                        }
                                        "Good"->{
                                            value = 3
                                        }
                                        "Bad"->{
                                            value = 2
                                        }
                                        "Worst"->{
                                            value = 1
                                        }

                                    }
                                    avglist[j] = avglist[j]+value
                                }
                            }

                            val arrayList : ArrayList<Double> =ArrayList()
                            for(x in 0..99)
                            {
                                if(avglist[x]!=0){
                                    val d = (avglist[x].toDouble())/noOfMembers
                                    arrayList.add(d)
                                }
                            }
                            Log.i("Avg lsit valies",arrayList.toString())
                            Toast.makeText(this@ManagerStatusActivity,arrayList.toString(), Toast.LENGTH_SHORT).show()





//                            val map : HashMap<String,ArrayList<Double>> = hashMapOf()
//                            map.put(uid,arrayList)
//                            dbrefGraphData.setValue(map)

                            dbrefGraphData.addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if(snapshot.exists()){
                                        dbrefGraphData.child(uid).setValue(arrayList)
                                    }
                                    else{
                                        val map : HashMap<String,ArrayList<Double>> = hashMapOf()
                                        map.put(uid,arrayList)
                                        dbrefGraphData.setValue(map)

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }


                            })




                                        break
                        }
                    }

                }
                else{
                    Log.i("Snapshot","Doesnot exist")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Database","Doesnot exist")
            }


        })

        supportFragmentManager.beginTransaction()
            .add(R.id.managerstatusContainer, MangerStatusFragment(username,uid))
            .commit()


    }
}