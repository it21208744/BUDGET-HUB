package com.example.firebasekotlin.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasekotlin.R
import com.example.firebasekotlin.models.BudgetModel
import com.google.firebase.database.FirebaseDatabase

class EmployeeDetailsActivity : AppCompatActivity() {
    private lateinit var lmBudgetId: TextView
    private lateinit var lmBgName: TextView
    private lateinit var lmBgCategory: TextView
    private lateinit var lmBgAmount: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("budgetID").toString(),
                intent.getStringExtra("bgName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("budgetID").toString()
            )
        }

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Budgets").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Budget bgCategory deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        lmBudgetId = findViewById(R.id.lmBudgetId)
        lmBgName = findViewById(R.id.lmBgName)
        lmBgCategory = findViewById(R.id.tvCatergory)
        lmBgAmount = findViewById(R.id.lmBgAmount)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        lmBudgetId.text = intent.getStringExtra("budgetID")
        lmBgName.text = intent.getStringExtra("bgName")
        lmBgCategory.text = intent.getStringExtra("bgCategory")
        lmBgAmount.text = intent.getStringExtra("bgAmount")

    }

    private fun openUpdateDialog(
        budgetID: String,
        bgName: String
    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog,null)

        mDialog.setView(mDialogView)
        val lnBgName =  mDialogView.findViewById<EditText>(R.id.lnBgName)
        val lnBgCategory =  mDialogView.findViewById<EditText>(R.id.lnBgCategory)
        val lnBgAmount =  mDialogView.findViewById<EditText>(R.id.lnBgAmount)
        val btnUpdateData =  mDialogView.findViewById<Button>(R.id.btnUpdateData)

        lnBgName.setText(intent.getStringExtra("bgName").toString())
        lnBgCategory.setText(intent.getStringExtra("bgCategory").toString())
        lnBgAmount.setText(intent.getStringExtra("bgAmount").toString())

        mDialog.setTitle("Updating $bgName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener{
            updateBgData(
                budgetID,
                lnBgName.text.toString(),
                lnBgCategory.text.toString(),
                lnBgAmount.text.toString()
            )

            Toast.makeText(applicationContext, "Budget Updated", Toast.LENGTH_LONG).show()

            lmBgName.text = lnBgName.text.toString()
            lmBgCategory.text = lnBgCategory.text.toString()
            lmBgAmount.text = lnBgAmount.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateBgData(
        id:String,
        name:String,
        category:String,
        amount:String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Budgets").child(id)
        val bgInfo = BudgetModel(id, name, category, amount)
        dbRef.setValue(bgInfo)
    }

}