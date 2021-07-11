package com.example.performancemanagementsystem.Fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.performancemanagementsystem.R
import com.example.performancemanagementsystem.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException


class RegisterFragment : Fragment() {

    private lateinit var auth : FirebaseAuth
    private lateinit var registerBinding: FragmentRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()

        registerBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_register,container,false)


        registerBinding.signupButton.setOnClickListener {

            signup()
        }



        return registerBinding.root
    }

    private fun signup() {

        val username = registerBinding.signName.text.toString()

        val email = registerBinding.signMail.text.toString()
        val password = registerBinding.singupPass.text.toString()


        val conPassword = registerBinding.singupConfirmPass.text.toString()

        if(username.isEmpty())
        {
            registerBinding.signName.error = "Field Required"
            return
        }

        if(email.isEmpty())
        {
            registerBinding.signMail.error = "Field Required"
            return
        }
        if(password.isEmpty())
        {
            registerBinding.singupPass.error = "Field Required"
            return
        }
        if(conPassword.isEmpty())
        {
            registerBinding.singupConfirmPass.error = "Field Required"
            return
        }

        if(password != conPassword)
        {
            Toast.makeText(context,"Password doesn't match",Toast.LENGTH_SHORT).show()
            return
        }




        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(ContentValues.TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.auth_container, NewMemberFragment(username,email,user!!.uid))
                            .commit()

                    } else {

                        if (task.exception is FirebaseAuthUserCollisionException) {
                            Toast.makeText(context, "User with this email already exist.", Toast.LENGTH_SHORT).show()}
                        Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)


                    }
                }





    }


}