package com.example.performancemanagementsystem

data class CompanyInfoModel(
    val uid: String,
    var companyName: String,
    val hrName: String,
    val companyCode: String,
    var memberList: ArrayList<UserModel>?,
){
    constructor():this("","","","",null)

}
