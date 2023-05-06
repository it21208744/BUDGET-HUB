package com.example.firebasekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.firebasekotlin.activities.FetchingActivity
import com.example.firebasekotlin.activities.InsertionActivity
import com.example.firebasekotlin.activities.MainActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class menuActivity : AppCompatActivity() {

    private lateinit var btnExpense: Button
    private lateinit var btnGoal: Button
    private lateinit var btnLoan: Button
    private lateinit var btnBudget: Button



    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val firebase : DatabaseReference = FirebaseDatabase.getInstance().getReference()


        btnExpense = findViewById(R.id.btnExpense)
        btnGoal = findViewById(R.id.btnGoal)
        btnLoan = findViewById(R.id.btnLoans)
        btnBudget = findViewById(R.id.btnBudgets)

        btnExpense.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnGoal.setOnClickListener {
            val intent = Intent(this, FetchingActivity::class.java)
            startActivity(intent)
        }

        btnLoan.setOnClickListener {
            val intent = Intent(this, FetchingActivity::class.java)
            startActivity(intent)
        }

        btnBudget.setOnClickListener {
            val intent = Intent(this, FetchingActivity::class.java)
            startActivity(intent)
        }

    }
}