package com.example.firebasekotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.firebasekotlin.models.GoalModel
import com.example.firebasekotlin.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var etGlName: EditText
    private lateinit var etGlDes: EditText
    private lateinit var etGlPrice: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etGlName = findViewById(R.id.etGlName)
        etGlDes = findViewById(R.id.etGlDes)
        etGlPrice = findViewById(R.id.etGlPrice)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Goalss")

        btnSaveData.setOnClickListener {
            saveEmployeeData()
        }
    }

    private fun saveEmployeeData() {

        //getting values
        val glName = etGlName.text.toString()
        val glDes = etGlDes.text.toString()
        val glPrice = etGlPrice.text.toString()

        if (glName.isEmpty() || glDes.isEmpty() || glPrice.isEmpty()) {

            if (glName.isEmpty()) {
                etGlName.error = "Please enter name"
            }
            if (glDes.isEmpty()) {
                etGlDes.error = "Please enter Description"
            }
            if (glPrice.isEmpty()) {
                etGlPrice.error = "Please enter Price"
            }
        } else {


            val glId = dbRef.push().key!!

            val employee = GoalModel(glId, glName, glDes, glPrice)

            dbRef.child(glId).setValue(employee)
                .addOnCompleteListener {
                    Toast.makeText(this, "Goal Added successfully", Toast.LENGTH_LONG).show()

                    etGlName.text.clear()
                    etGlDes.text.clear()
                    etGlPrice.text.clear()


                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }

        }
    }

}