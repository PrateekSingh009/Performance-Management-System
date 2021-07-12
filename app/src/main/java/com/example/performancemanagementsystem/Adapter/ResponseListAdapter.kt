package com.example.performancemanagementsystem.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.performancemanagementsystem.R

class ResponseListAdapter(list : ArrayList<String>) : RecyclerView.Adapter<ResponseListAdapter.ViewHolder>(){
    private var list : ArrayList<String> = list
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val txt : TextView = view.findViewById(R.id.qRlist)

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
    }
}