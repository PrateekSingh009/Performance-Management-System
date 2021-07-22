package com.example.performancemanagementsystem.Fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.performancemanagementsystem.*
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_status.*


class StatusFragment(private val username:String) : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_status, container, false)
        val txt: TextView = view.findViewById(R.id.txtview)
        val dbrefGraphData = FirebaseDatabase.getInstance().getReference("GraphData")
        var barList: ArrayList<BarEntry>
        var lineDataSet: BarDataSet
        var barData: BarData
        val dbrefFeedback = FirebaseDatabase.getInstance().getReference("Feedback")
        val dbrefAnswers = FirebaseDatabase.getInstance().getReference("Answers")
        var quesList: ArrayList<String> = ArrayList()
        val bck : ImageView = view.findViewById(R.id.statusbackbtn)

        bck.setOnClickListener {

            val intent = Intent(context, DashScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()

        }


        dbrefAnswers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val answersModel = snap.getValue(AnswersModel::class.java)
                        if (answersModel!!.memberId == FirebaseAuth.getInstance().currentUser!!.uid) {
                            dbrefFeedback.child(snap.key.toString())
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot1: DataSnapshot) {
                                        if (snapshot1.exists()) {
                                            StatusName.text =
                                                snapshot1.getValue(FeedbackModel::class.java)!!.member
                                            StatusMemberID.text =
                                                FirebaseAuth.getInstance().currentUser!!.uid
                                            quesList =
                                                snapshot1.getValue(FeedbackModel::class.java)!!.questionList!!

                                            dbrefGraphData.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                                .addListenerForSingleValueEvent(object :
                                                    ValueEventListener {
                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                        if (snapshot.exists()) {
                                                            txt.visibility = View.INVISIBLE
                                                            val arraylist: ArrayList<Double> =
                                                                snapshot.getValue() as ArrayList<Double>
                                                            barList = ArrayList()
//                    barList.add(BarEntry(1f,1f))
//                    barList.add(BarEntry(2f, 2f))
//                    barList.add(BarEntry(1f, 3f))
//                    barList.add(BarEntry(3.5f, 4f))
//                    barList.add(BarEntry(1.5f, 5f))
//                    barList.add(BarEntry(2f, 6f))
//                    barList.add(BarEntry(3.5f, 7f))

                                                            for (i in arraylist.indices) {
                                                                barList.add(
                                                                    BarEntry(
                                                                        i + 1.toFloat(),
                                                                        arraylist.elementAt(i)
                                                                            .toFloat()
                                                                    )
                                                                )
                                                            }
                                                            Log.i("Queslist", quesList.toString())
                                                            lineDataSet = BarDataSet(
                                                                barList,
                                                                "Performance Chart"
                                                            )
                                                            barData = BarData(lineDataSet)
                                                            lineDataSet.setColors(
                                                                ColorTemplate.JOYFUL_COLORS,
                                                                250
                                                            )
                                                            barchart.data = barData
                                                            barData.barWidth = 0.5f
                                                            lineDataSet.valueTextColor = Color.BLACK
                                                            lineDataSet.valueTextSize = 5f
                                                            val xAxis = barchart.xAxis
                                                            val yAxisLeft = barchart.axisLeft
                                                            val yAxisRight = barchart.axisRight
                                                            xAxis.setLabelCount(arraylist.size)
                                                            yAxisLeft.setLabelCount(5)
                                                            yAxisRight.setLabelCount(5)
                                                            val leftAxis: YAxis =
                                                                barchart.getAxisLeft()

                                                            val l1 = LimitLine(
                                                                1f,
                                                                "Worst"
                                                            )
                                                            l1.lineColor = Color.RED
                                                            l1.lineWidth = 1f
                                                            l1.textColor = Color.BLACK
                                                            l1.textSize = 12f

                                                            val l2 = LimitLine(
                                                                2f,
                                                                "Bad"
                                                            )
                                                            l2.lineColor = Color.RED
                                                            l2.lineWidth = 1f
                                                            l2.textColor = Color.BLACK
                                                            l2.textSize = 12f

                                                            val l3 = LimitLine(
                                                                3f,
                                                                "Good"
                                                            )
                                                            l3.lineColor = Color.RED
                                                            l3.lineWidth = 1f
                                                            l3.textColor = Color.BLACK
                                                            l3.textSize = 12f
                                                            val l4 = LimitLine(
                                                                4f,
                                                                "Excellent"
                                                            )
                                                            l4.lineColor = Color.RED
                                                            l4.lineWidth = 1f
                                                            l4.textColor = Color.BLACK
                                                            l4.textSize = 12f


                                                            leftAxis.addLimitLine(l1)
                                                            leftAxis.addLimitLine(l2)
                                                            leftAxis.addLimitLine(l3)
                                                            leftAxis.addLimitLine(l4)



                                                                var s = "X-Axis\n"
                                                            var j=1
                                                            for(i in quesList)
                                                            {
                                                                s = "$s$j. $i"
                                                                s += "\n"
                                                                j++
                                                            }
                                                            s+= "\nY-Axis\n1.Worst\n2.Bad\n3.Good\n4.Excellent\n"
                                                            gap.text = s

                                                        }


                                                    }

                                                    override fun onCancelled(error: DatabaseError) {
                                                        TODO("Not yet implemented")
                                                    }

                                                })

                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                })

                            break
                        }
                        else
                        {
                            StatusName.text =
                                username
                            StatusMemberID.text =
                                FirebaseAuth.getInstance().currentUser!!.uid
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })









        return view
    }


}


