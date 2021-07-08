package com.example.performancemanagementsystem

class FeedbackModel(
    val uid : String,
    val companyCode : String,
    var member : String,
    var questionList: ArrayList<String>?
){
    constructor():this("","","",null)
}