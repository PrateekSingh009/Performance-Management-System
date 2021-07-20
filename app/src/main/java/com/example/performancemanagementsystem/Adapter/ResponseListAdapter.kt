package com.example.performancemanagementsystem.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.performancemanagementsystem.R

class ResponseListAdapter(list : ArrayList<String>,private var answers : Array<String> ) : RecyclerView.Adapter<ResponseListAdapter.ViewHolder>(){
    private var list : ArrayList<String> = list


    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val txt : TextView = view.findViewById(R.id.qRlist)
        val radiogrp : RadioGroup = view.findViewById(R.id.cardRg)
        val radiobtn1 : RadioButton = view.findViewById(R.id.cardR1)
        val radiobtn2 : RadioButton = view.findViewById(R.id.cardR2)
        val radiobtn3 : RadioButton = view.findViewById(R.id.cardR3)
        val radiobtn4 : RadioButton = view.findViewById(R.id.cardR4)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponseListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.responsequeslist,parent,false)
        return ResponseListAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ResponseListAdapter.ViewHolder, position: Int) {
        holder.txt.text = list[position]

        holder.radiogrp.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                when(p1){
                    R.id.cardR1 -> {
                        Log.i("Pressed 1",holder.radiobtn1.text.toString())
                    }
                    R.id.cardR2 -> {
                        Log.i("Pressed 2",holder.radiobtn2.text.toString())
                    }
                    R.id.cardR3 -> {
                        Log.i("Pressed 3",holder.radiobtn3.text.toString())
                    }
                    R.id.cardR4 -> {
                        Log.i("Pressed 4",holder.radiobtn4.text.toString())
                    }

                }
                val checked: RadioButton = holder.radiogrp.findViewById(p1)
                answers[position]= checked.text.toString()
                Log.i("Answers+Position", "$answers[position],$position")
            }


        })


    }
}



