package com.example.firebasekotlin.activities
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasekotlin.models.LoannModel
import com.example.firebasekotlin.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var dtLnName: EditText
    private lateinit var dtLnAmount: EditText
    private lateinit var dtLnDate: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        dtLnName = findViewById(R.id.dtLnName)
        dtLnAmount = findViewById(R.id.dtLnAmount)
        dtLnDate = findViewById(R.id.dtLnDate)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Loans")

        btnSaveData.setOnClickListener {
            saveLnData()
        }
    }

    private fun saveLnData() {

        //getting values
        val lnName = dtLnName.text.toString()
        val lnAmount = dtLnAmount.text.toString()
        val lnDate = dtLnDate.text.toString()

        if (lnName.isEmpty()||lnAmount.isEmpty()||lnDate.isEmpty()){

        if (lnName.isEmpty()) {
            dtLnName.error = "Please enter name"
        }
        if (lnAmount.isEmpty()) {
            dtLnAmount.error = "Please enter Amount"
        }
        if (lnDate.isEmpty()) {
            dtLnDate.error = "Please enter Date"
        }
            Toast.makeText(this, "Data inserted unsuccessfully", Toast.LENGTH_LONG).show()
        }
        else{

        val lnId = dbRef.push().key!!

        val loann = LoannModel(lnId, lnName, lnAmount, lnDate)

        dbRef.child(lnId).setValue(loann)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                dtLnName.text.clear()
                dtLnAmount.text.clear()
                dtLnDate.text.clear()


            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
        }

    }

}