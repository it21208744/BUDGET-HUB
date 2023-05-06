package com.example.firebasekotlin.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasekotlin.models.transModel
import com.example.firebasekotlin.R
import com.example.firebasekotlin.adapters.TransAdapter
import com.google.firebase.database.*

class FetchingActivity : AppCompatActivity() {

    private lateinit var transRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var transactionList: ArrayList<transModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        transRecyclerView = findViewById(R.id.rvTrans)
        transRecyclerView.layoutManager = LinearLayoutManager(this)
        transRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        transactionList = arrayListOf<transModel>()

        getTransactionsData()

    }

    private fun getTransactionsData() {

        transRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("incomeAndExpenses")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                transactionList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val transactionData = empSnap.getValue(transModel::class.java)
                        transactionList.add(transactionData!!)
                    }
                    val mAdapter = TransAdapter(transactionList)
                    transRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : TransAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FetchingActivity, TransactionDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("transactionId", transactionList[position].transactionId)
                            intent.putExtra("transactionType", transactionList[position].transactionType)
                            intent.putExtra("transactionDescription", transactionList[position].transactionDescription)
                            intent.putExtra("transactionAmount", transactionList[position].transactionAmount)
                            intent.putExtra("balance", transactionList[position].balance)
                            startActivity(intent)
                        }

                    })

                    transRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}