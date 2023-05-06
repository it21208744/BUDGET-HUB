package com.example.firebasekotlin.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasekotlin.models.BudgetModel
import com.example.firebasekotlin.R
import com.example.firebasekotlin.adapters.BgAdapter
import com.google.firebase.database.*

class FetchingActivity : AppCompatActivity() {

    private lateinit var bgRecyclerView: RecyclerView
    private lateinit var lmLoadingData: TextView
    private lateinit var bgList: ArrayList<BudgetModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        bgRecyclerView = findViewById(R.id.rvEmp)
        bgRecyclerView.layoutManager = LinearLayoutManager(this)
        bgRecyclerView.setHasFixedSize(true)
        lmLoadingData = findViewById(R.id.lmLoadingData)

        bgList = arrayListOf<BudgetModel>()

        getBudgetsData()

    }

    private fun getBudgetsData() {

        bgRecyclerView.visibility = View.GONE
        lmLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Budgets")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bgList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(BudgetModel::class.java)
                        bgList.add(empData!!)
                    }
                    val mAdapter = BgAdapter(bgList)
                    bgRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : BgAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FetchingActivity, BudgetDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("budgetID", bgList[position].budgetID)
                            intent.putExtra("bgName", bgList[position].bgName)
                            intent.putExtra("bgCategory", bgList[position].bgCategory)
                            intent.putExtra("bgAmount", bgList[position].bgAmount)
                            startActivity(intent)
                        }

                    })

                    bgRecyclerView.visibility = View.VISIBLE
                    lmLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}