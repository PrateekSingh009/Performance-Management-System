package com.example.performancemanagementsystem.Fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.performancemanagementsystem.R
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.JOYFUL_COLORS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.fragment_status.*


class StatusFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_status, container, false)
        val txt : TextView = view.findViewById(R.id.txtview)
        val dbrefGraphData = FirebaseDatabase.getInstance().getReference("GraphData")
        var barList : ArrayList<BarEntry>
        var lineDataSet : BarDataSet
        var barData : BarData

        dbrefGraphData.child(FirebaseAuth.getInstance().currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    txt.visibility = View.INVISIBLE
                    val arraylist : ArrayList<Double> = snapshot.getValue() as ArrayList<Double>
                    barList =ArrayList()
                    barList.add(BarEntry(1f,4f))
                    barList.add(BarEntry(2f, 2f))
                    barList.add(BarEntry(3f, 1f))
                    barList.add(BarEntry(4f, 3.5f))
                    barList.add(BarEntry(5f, 1.5f))
                    barList.add(BarEntry(6f, 2f))
                    barList.add(BarEntry(7f, 3.5f))
                    barList.add(BarEntry(8f,4f))
                    barList.add(BarEntry(9f, 2f))
                    barList.add(BarEntry(10f, 2.5f))
                    barList.add(BarEntry(11f, 3.5f))
                    barList.add(BarEntry(12f, 1.5f))
                    barList.add(BarEntry(13f, 2f))
                    barList.add(BarEntry(14f, 1f))
                    lineDataSet = BarDataSet(barList,"Feedback Chart")
                    barData = BarData(lineDataSet)
                    lineDataSet.setColor(ColorTemplate.COLOR_NONE, 250)
                    barchart.data = barData
                    barData.barWidth = 0.2f
                    lineDataSet.valueTextColor= Color.BLACK
                    lineDataSet.valueTextSize = 5f




                }




            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        return view
    }


}


