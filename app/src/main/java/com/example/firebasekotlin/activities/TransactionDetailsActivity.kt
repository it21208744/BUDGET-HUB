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
import com.example.firebasekotlin.models.transModel
import com.google.firebase.database.FirebaseDatabase

class TransactionDetailsActivity : AppCompatActivity() {
    private lateinit var tvTransactionId: TextView
    private lateinit var tvType: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvAmount: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("transactionId").toString(),
                intent.getStringExtra("transactionType").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("transactionId").toString()
            )
        }

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("incomeAndExpenses").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Record deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        tvTransactionId = findViewById(R.id.tvEmpId)
        tvType = findViewById(R.id.tvEmpName)
        tvDescription = findViewById(R.id.tvDescription)
        tvAmount = findViewById(R.id.tvAmount)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvTransactionId.text = intent.getStringExtra("transactionId")
        tvType.text = intent.getStringExtra("transactionType")
        tvDescription.text = intent.getStringExtra("transactionDescription")
        tvAmount.text = intent.getStringExtra("transactionAmount")


    }

    private fun openUpdateDialog(
        transactionId: String,
        transactionType: String
    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog,null)

        mDialog.setView(mDialogView)
        val etTransType =  mDialogView.findViewById<EditText>(R.id.etTransType)
        val etTransDescription =  mDialogView.findViewById<EditText>(R.id.etTransDescription)
        val etTransAmount =  mDialogView.findViewById<EditText>(R.id.etTransAmount)
        val btnUpdateData =  mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etTransType.setText(intent.getStringExtra("transactionType").toString())
        etTransDescription.setText(intent.getStringExtra("transactionDescription").toString())
        etTransAmount.setText(intent.getStringExtra("transactionAmount").toString())

        mDialog.setTitle("Updating $transactionType Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener{
            updateEmpData(
                transactionId,
                etTransType.text.toString(),
                etTransDescription.text.toString(),
                etTransAmount.text.toString()
            )

            Toast.makeText(applicationContext, "Record updated", Toast.LENGTH_LONG).show()

            tvType.text = etTransType.text.toString()
            tvDescription.text = etTransDescription.text.toString()
            tvAmount.text = etTransAmount.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        id:String,
        type:String,
       description:String,
        amount:String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("incomeAndExpenses").child(id)
        val transInfo = transModel(id, type,description, amount)
        dbRef.setValue(transInfo)
    }

}