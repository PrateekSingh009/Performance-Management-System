package com.example.performancemanagementsystem.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.performancemanagementsystem.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class DashFragment() : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_dash, container, false)

        val createCard : soup.neumorphism.NeumorphCardView = view.findViewById(R.id.createFeed)
        val pendingCard :soup.neumorphism.NeumorphCardView = view.findViewById(R.id.pendingFeed)
        val statusCard :soup.neumorphism.NeumorphCardView = view.findViewById(R.id.statusFeed)
        val cmpname :TextView = view.findViewById(R.id.nameComp)
        val username :TextView = view.findViewById(R.id.Username)
        val emailval :TextView = view.findViewById(R.id.email)
        val logout : ImageView = view.findViewById(R.id.logoutBtn)
        var cmpcode : String = ""

        val dbrefCompanyInfo = FirebaseDatabase.getInstance().getReference("CompanyInfo")

        val auth = FirebaseAuth.getInstance()
        Toast.makeText(context, auth.currentUser?.uid, Toast.LENGTH_SHORT).show()



        dbrefCompanyInfo.addValueEventListener(
            object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.hasChild(FirebaseAuth.getInstance().currentUser!!.uid!!)) {
                        createCard.visibility = INVISIBLE
                    }
                    for (snap in snapshot.children) {
                        val companyInfoModel = snap.getValue(CompanyInfoModel::class.java)
                        val arrayList: ArrayList<UserModel> = companyInfoModel!!.memberList!!
                        for (i in arrayList) {
                            if (i.uid == FirebaseAuth.getInstance().uid) {
                                cmpname.text = companyInfoModel.companyName
                                cmpcode = companyInfoModel.companyCode
                                emailval.text = i.email
                                username.text = i.name

                            }
                        }


                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i("DatabaseError", error.message)
                }
            })

        createCard.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.dash_container, newFeedFragment()).addToBackStack(null)
                .commit()


        }

        pendingCard.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.dash_container, PendingFeedback(cmpcode)).addToBackStack(null)
                .commit()
        }

        statusCard.setOnClickListener {
            var value = 0

            dbrefCompanyInfo.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(snap in snapshot.children){
                            if(snap.key == FirebaseAuth.getInstance().uid){
                                value = 1
                                break;
                            }
                        }

                        if(value == 1) {
                                val intent = Intent(context, ManagerActivity::class.java)

                                //intent.putExtra("Name", username.text)
                                startActivity(intent)


                        }
                        else{
                                val intent = Intent(context, StatusActivity::class.java)
                                
                                intent.putExtra("Name", username.text)
                                startActivity(intent)


                        }







                    }
                    else
                    {
                        Toast.makeText(context , "Company doesn't Exist",Toast.LENGTH_SHORT).show()
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })





        }

        logout.setOnClickListener {



            if(FirebaseAuth.getInstance().currentUser != null) {
                FirebaseAuth.getInstance()
                    .signOut()

                val intent = Intent(activity, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                activity?.finish()

            }

        }


        return view
    }


}