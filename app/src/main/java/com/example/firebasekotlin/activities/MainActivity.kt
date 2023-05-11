package com.example.firebasekotlin.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.firebasekotlin.R
import com.example.firebasekotlin.adapters.EmpAdapter
import com.example.firebasekotlin.models.LoannModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var btnInsertData: Button
    private lateinit var btnFetchData: Button
    private lateinit var totalLoan: TextView
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firebase: DatabaseReference = FirebaseDatabase.getInstance().getReference()


        btnInsertData = findViewById(R.id.btnInsertData)
        btnFetchData = findViewById(R.id.btnFetchData)
        totalLoan = findViewById(R.id.totalLoan)

        btnInsertData.setOnClickListener {
            val intent = Intent(this, InsertionActivity::class.java)
            startActivity(intent)
        }

        btnFetchData.setOnClickListener {
            val intent = Intent(this, FetchingActivity::class.java)
            startActivity(intent)
        }

        getTotalLoanAmount()
    }

    private fun getTotalLoanAmount() {
        dbRef = FirebaseDatabase.getInstance().getReference("Loans")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                var totalLoanAmount:Double = 0.0

                for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(LoannModel::class.java)
                    totalLoanAmount += empData?.lnAmount?.toDouble()!!
                    }
                    totalLoan.text = "Rs.${totalLoanAmount.toInt().toString()}"
            }}

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}