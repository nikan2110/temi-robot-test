package meuhedet.com.temitestappl

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class FindDoctorActivity : AppCompatActivity() {

    private lateinit var doctorName: EditText
    private lateinit var buttonFind: Button
    private lateinit var buttonDoctorOne: Button
    private lateinit var buttonDoctorTwo: Button
    private lateinit var btnBack: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_doctor)

        doctorName = findViewById(R.id.et_doctor_name)
        buttonDoctorOne = findViewById(R.id.button_doctor_one)
        buttonDoctorTwo = findViewById(R.id.button_doctor_two)
        buttonFind = findViewById(R.id.button_find)
        btnBack = findViewById(R.id.btn_back)

        buttonDoctorOne.setOnClickListener {
            goTheDoctor("doctor 1")
            finish()
        }

        buttonDoctorTwo.setOnClickListener {
            goTheDoctor("doctor 2")
            finish()
        }

        buttonFind.setOnClickListener {
            if (doctorName.text.isEmpty()) {
                Toast.makeText(this, "First, write the doctor's name", Toast.LENGTH_LONG).show()
            } else {
                goTheDoctor(doctorName.text.toString().lowercase())
                finish()
            }

        }

        btnBack.setOnClickListener {
            val destinationActivity = MainActivity::class.java
            val mainActivityIntent = Intent(this@FindDoctorActivity, destinationActivity)
            startActivity(mainActivityIntent)
        }

    }

    private fun goTheDoctor(doctorName: String) {
        MainActivity.instance.findDoctor(doctorName)
    }

}