package com.example.firebasekotlin.activities
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasekotlin.models.transModel
import com.example.firebasekotlin.R

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var etType: EditText
    private lateinit var etDescription: EditText
    private lateinit var etAmount: EditText
    private lateinit var btnSaveData: Button
//    private lateinit var switchExpense: Switch



    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etType = findViewById(R.id.etTransType)
        etDescription = findViewById(R.id.etTransDescription)
        etAmount = findViewById(R.id.etTransAmount)
        btnSaveData = findViewById(R.id.btnSave)
//        switchExpense = findViewById(R.id.swExpense)

        dbRef = FirebaseDatabase.getInstance().getReference("incomeAndExpenses")

        btnSaveData.setOnClickListener {
            saveTransactionData()
        }
    }

    private fun saveTransactionData() {

        //getting values
        val transactionType = etType.text.toString()
        val transactionDescription = etDescription.text.toString()
        val transactionAmount = etAmount.text.toString()
//        val isExpense = switchExpense

        var balance = 0
        var transAmount = transactionAmount

        //val etBalance =
        if(transactionType.isEmpty() || transactionDescription.isEmpty() || transactionAmount.isEmpty()) {
            if (transactionType.isEmpty()) {
                etType.error = "Please enter a type"
            }
            if (transactionDescription.isEmpty()) {
                etDescription.error = "Please enter a description"
            }
            if (transactionAmount.isEmpty()) {
                etAmount.error = "Please enter the amount"
            }

            Toast.makeText(this, "Some fields are not filled", Toast.LENGTH_LONG).show()
        }
        else{
            balance += transAmount.toInt()

            val transactionId = dbRef.push().key!!

            val transaction = transModel(transactionId, transactionType, transactionDescription, transactionAmount, balance)


            dbRef.child(transactionId).setValue(transaction)
                .addOnCompleteListener {
                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                    etType.text.clear()
                    etDescription.text.clear()
                    etAmount.text.clear()


                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }}

}