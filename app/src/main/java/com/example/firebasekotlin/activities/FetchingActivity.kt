package com.example.firebasekotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasekotlin.models.GoalModel
import com.example.firebasekotlin.R
import com.example.firebasekotlin.adapters.GoalAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FetchingActivity : AppCompatActivity() {

    private lateinit var glRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var glList: ArrayList<GoalModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        glRecyclerView = findViewById(R.id.rvEmp)
        glRecyclerView.layoutManager = LinearLayoutManager(this)
        glRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        glList = arrayListOf<GoalModel>()

        getGoalssData()

    }

    private fun getGoalssData() {

        glRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Goalss")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                glList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(GoalModel::class.java)
                        glList.add(empData!!)
                    }
                    val mAdapter = GoalAdapter(glList)
                    glRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : GoalAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FetchingActivity, GoalDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("glId", glList[position].glId)
                            intent.putExtra("glName", glList[position].glName)
                            intent.putExtra("glDes", glList[position].glDes)
                            intent.putExtra("glPrice", glList[position].glPrice)
                            startActivity(intent)
                        }

                    })

                    glRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}