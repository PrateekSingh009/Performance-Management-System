package com.example.performancemanagementsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.performancemanagementsystem.Fragments.DashFragment
import com.example.performancemanagementsystem.Fragments.newFeedFragment
import com.google.firebase.auth.FirebaseAuth

class DashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_screen)





//        val extras : Bundle = intent.extras!!
//        val companyCode : String = extras.getInt("companyCode").toString()
        supportFragmentManager.beginTransaction()
            .add(R.id.dash_container, DashFragment())
            .commit()


    }
}