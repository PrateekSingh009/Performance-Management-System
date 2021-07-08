package com.example.performancemanagementsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.performancemanagementsystem.Fragments.FirstFragment
import com.example.performancemanagementsystem.Fragments.LoginFragment
import com.example.performancemanagementsystem.Fragments.NewMemberFragment
import com.example.performancemanagementsystem.Fragments.RegisterFragment

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        supportFragmentManager.beginTransaction()
                .add(R.id.auth_container, LoginFragment())
                .commit()
    }
}