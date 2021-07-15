package com.example.performancemanagementsystem.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.performancemanagementsystem.CompanyInfoModel
import com.example.performancemanagementsystem.DashScreenActivity

import com.example.performancemanagementsystem.R
import com.example.performancemanagementsystem.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CustomDialog(username: String, email: String, uid: String) :DialogFragment() {

    val username = username
    val email = email
    val uid= uid


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.custom_dialog_text_fragment,container,false)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog)
        var btnsave : TextView
        var btncancel : TextView
        var editText :EditText

        val auth = FirebaseAuth.getInstance()

        btnsave = view!!.findViewById(R.id.save_button)
        btncancel = view!!.findViewById(R.id.cancel_button)
        editText =view!!.findViewById(R.id.et_code)


            btnsave.setOnClickListener {


                val ccode = editText.text.toString()
                Log.d("CompanyCode",ccode)

                val feedlist = ArrayList<String>()
                feedlist!!.add("")
                var dbrefFeedbackList = FirebaseDatabase.getInstance().getReference("FeedbackList")

                val dbref = FirebaseDatabase.getInstance().getReference("CompanyInfo")
                var once = 0
                dbref.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot!!.exists())
                        {
                            for(snap in snapshot.children)
                            {
                                val companyInfoModel = snap.getValue(CompanyInfoModel::class.java)
                                //Log.d("Values",companyInfoModel!!.companyCode.toString())

                                val user = UserModel(username,email,uid)
                                if(companyInfoModel!!.companyCode.toString()==ccode){
                                    Log.d("equal success",companyInfoModel!!.companyCode.toString())
                                    companyInfoModel!!.memberList!!.add(user)
                                    if(once==0){
                                        dbref.child(companyInfoModel.uid).setValue(companyInfoModel)
                                        dbrefFeedbackList.child(ccode).child(auth.uid!!).setValue(feedlist)
                                        val intent = Intent(context, DashScreenActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(intent)
                                        activity?.finish()

                                        dialog!!.dismiss()
                                    }
                                    else
                                        break

                                }
                                else
                                {
                                  //  Toast.makeText(context,"Company with this code doesn't EXIST",Toast.LENGTH_SHORT).show()
                                      editText.error = "Wrong Code"
                                    Log.d("equal fail","failure")
                                }


                            }

                        }
                        else
                        {
                            Log.d("Values","Snap doesn't exist")
                        }
                        once++
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }


                })






            }



        btncancel.setOnClickListener {
            dialog!!.dismiss()
        }


        return view
    }
    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.42).toInt()
        dialog!!.window?.setLayout(width,height)
    }
}




