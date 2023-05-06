package com.example.firebasekotlin.activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.firebasekotlin.R
import com.example.firebasekotlin.models.GoalModel
import com.google.firebase.database.FirebaseDatabase

class GoalDetailsActivity : AppCompatActivity() {

    private lateinit var tvGlId: TextView
    private lateinit var tvGlName: TextView
    private lateinit var tvGlDes: TextView
    private lateinit var tvGlPrice: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("glId").toString(),
                intent.getStringExtra("glName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("glId").toString()
            )
        }
    }

    private fun initView() {
        tvGlId = findViewById(R.id.tvGlId)
        tvGlName = findViewById(R.id.tvGlName)
        tvGlDes = findViewById(R.id.tvGlDes)
        tvGlPrice = findViewById(R.id.tvGlPrice)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvGlId.text = intent.getStringExtra("glId")
        tvGlName.text = intent.getStringExtra("glName")
        tvGlDes.text = intent.getStringExtra("glDes")
        tvGlPrice.text = intent.getStringExtra("glPrice")

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Goalss").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Goal deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openUpdateDialog(
        glId: String,
        glName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etGlName = mDialogView.findViewById<EditText>(R.id.etGlName)
        val etGlDes = mDialogView.findViewById<EditText>(R.id.etGlDes)
        val etGlPrice = mDialogView.findViewById<EditText>(R.id.etGlPrice)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etGlName.setText(intent.getStringExtra("glName").toString())
        etGlDes.setText(intent.getStringExtra("glDes").toString())
        etGlPrice.setText(intent.getStringExtra("glPrice").toString())

        mDialog.setTitle("Updating $glName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                glId,
                etGlName.text.toString(),
                etGlDes.text.toString(),
                etGlPrice.text.toString()
            )

            Toast.makeText(applicationContext, "Goal Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvGlName.text = etGlName.text.toString()
            tvGlDes.text = etGlDes.text.toString()
            tvGlPrice.text = etGlPrice.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        id: String,
        name: String,
        age: String,
        salary: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Goalss").child(id)
        val empInfo = GoalModel(id, name, age, salary)
        dbRef.setValue(empInfo)
    }

}