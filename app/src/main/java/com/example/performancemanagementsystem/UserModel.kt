package com.example.performancemanagementsystem

data class UserModel(
    var name : String,
    val email : String,
    val uid: String
){
    constructor():this("","","")
}
