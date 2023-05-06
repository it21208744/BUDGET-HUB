package com.example.firebasekotlin.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasekotlin.models.LoannModel
import com.example.firebasekotlin.R
import com.example.firebasekotlin.adapters.EmpAdapter
import com.google.firebase.database.*

class FetchingActivity : AppCompatActivity() {

    private lateinit var lnRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var lnList: ArrayList<LoannModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        lnRecyclerView = findViewById(R.id.rvEmp)
        lnRecyclerView.layoutManager = LinearLayoutManager(this)
        lnRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        lnList = arrayListOf<LoannModel>()

        getLoansData()

    }

    private fun getLoansData() {

        lnRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Loans")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lnList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(LoannModel::class.java)
                        lnList.add(empData!!)
                    }
                    val mAdapter = EmpAdapter(lnList)
                    lnRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : EmpAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FetchingActivity, EmployeeDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("lnId", lnList[position].lnId)
                            intent.putExtra("lnName", lnList[position].lnName)
                            intent.putExtra("lnAmount", lnList[position].lnAmount)
                            intent.putExtra("lnDate", lnList[position].lnDate)
                            startActivity(intent)
                        }

                    })

                    lnRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}