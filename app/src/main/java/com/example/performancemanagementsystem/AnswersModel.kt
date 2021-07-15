package com.example.performancemanagementsystem

data class AnswersModel(
    val memberId : String,
    val reponses : HashMap<String,List<String>>
)
{
    constructor():this("",HashMap())
}