package com.example.performancemanagementsystem.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.performancemanagementsystem.R

class QuestionListAdapter(list : ArrayList<String>) : RecyclerView.Adapter<QuestionListAdapter.ViewHolder>(){
    private var list : ArrayList<String> = list
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val txt : TextView
        init {
            txt= view.findViewById(R.id.textlist)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.questionlistview,parent,false)
        return QuestionListAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionListAdapter.ViewHolder, position: Int) {
        holder.txt.text = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}