package com.example.firebasekotlin.activities
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasekotlin.models.BudgetModel
import com.example.firebasekotlin.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var lnBgName: EditText
    private lateinit var lnBgCategory: EditText
    private lateinit var lnBgAmount: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        lnBgName = findViewById(R.id.lnBgName)
        lnBgCategory = findViewById(R.id.lnBgCategory)
        lnBgAmount = findViewById(R.id.lnBgAmount)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Budgets")

        btnSaveData.setOnClickListener {
            saveBudgetData()
        }
    }

    private fun saveBudgetData() {

        //getting values
        val bgName = lnBgName.text.toString()
        val bgCategory = lnBgCategory.text.toString()
        val bgAmount = lnBgAmount.text.toString()

        if(bgName.isEmpty()||bgCategory.isEmpty()||bgAmount.isEmpty()) {

            if (bgName.isEmpty()) {
                lnBgName.error = "Please enter name"
            }
            if (bgCategory.isEmpty()) {
                lnBgCategory.error = "Please enter category"
            }
            if (bgAmount.isEmpty()) {
                lnBgAmount.error = "Please enter amount"
            }
            Toast.makeText(this, "Empty Data Detected", Toast.LENGTH_LONG).show()
        }

        else {


            val budgetID = dbRef.push().key!!

            val employee = BudgetModel(budgetID, bgName, bgCategory, bgAmount)

            dbRef.child(budgetID).setValue(employee)
                .addOnCompleteListener {
                    Toast.makeText(this, "Budget added successfully", Toast.LENGTH_LONG).show()

                    lnBgName.text.clear()
                    lnBgCategory.text.clear()
                    lnBgAmount.text.clear()


                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
        }

    }

}