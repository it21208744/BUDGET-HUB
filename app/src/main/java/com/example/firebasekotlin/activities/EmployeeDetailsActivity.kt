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
import com.example.firebasekotlin.models.LoannModel
import com.google.firebase.database.FirebaseDatabase

class EmployeeDetailsActivity : AppCompatActivity() {
    private lateinit var stLnId: TextView
    private lateinit var stLnName: TextView
    private lateinit var stLnAmount: TextView
    private lateinit var stLnDate: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("lnId").toString(),
                intent.getStringExtra("lnName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("lnId").toString()
            )
        }

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Loans").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Employee data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        stLnId = findViewById(R.id.stLnId)
        stLnName = findViewById(R.id.stLnName)
        stLnAmount = findViewById(R.id.stLnAmount)
        stLnDate = findViewById(R.id.stLnDate)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        stLnId.text = intent.getStringExtra("lnId")
        stLnName.text = intent.getStringExtra("lnName")
        stLnAmount.text = intent.getStringExtra("lnAmount")
        stLnDate.text = intent.getStringExtra("lnDate")

    }

    private fun openUpdateDialog(
        lnId: String,
        lnName: String
    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog,null)

        mDialog.setView(mDialogView)
        val dtLnName =  mDialogView.findViewById<EditText>(R.id.dtLnName)
        val dtLnAmount =  mDialogView.findViewById<EditText>(R.id.dtLnAmount)
        val dtLnDate =  mDialogView.findViewById<EditText>(R.id.dtLnDate)
        val btnUpdateData =  mDialogView.findViewById<Button>(R.id.btnUpdateData)

        dtLnName.setText(intent.getStringExtra("lnName").toString())
        dtLnAmount.setText(intent.getStringExtra("lnAmount").toString())
        dtLnDate.setText(intent.getStringExtra("lnDate").toString())

        mDialog.setTitle("Updating $lnName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener{
            updateLnData(
                lnId,
                dtLnName.text.toString(),
                dtLnAmount.text.toString(),
                dtLnDate.text.toString()
            )

            Toast.makeText(applicationContext, "Loan Data Updated", Toast.LENGTH_LONG).show()

            stLnName.text = dtLnName.text.toString()
            stLnAmount.text = dtLnAmount.text.toString()
            stLnDate.text = dtLnDate.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateLnData(
        id:String,
        name:String,
        amount:String,
        date:String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Loans").child(id)
        val empInfo = LoannModel(id, name, amount , date)
        dbRef.setValue(empInfo)
    }

}