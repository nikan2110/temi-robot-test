package meuhedet.com.temitestappl

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class FaceRecognitionActivity : AppCompatActivity() {

    private lateinit var btnRegisterPicture: Button
    private lateinit var btnWindowPermission: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_recognition)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        btnRegisterPicture = findViewById(R.id.button_register_picture)
        btnWindowPermission = findViewById(R.id.button_permission)
        btnBack = findViewById(R.id.btn_back)


        btnWindowPermission.setOnClickListener {
            val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            myIntent.data = Uri.parse("package:$packageName")
            startActivity(myIntent)
        }

        btnRegisterPicture.setOnClickListener {
            val destinationActivity = RegisterUserActivity::class.java
            val faceRegisterIntent = Intent(this@FaceRecognitionActivity, destinationActivity)
            startActivity(faceRegisterIntent)
        }

        btnBack.setOnClickListener {
            val destinationActivity = MainActivity::class.java
            val mainActivityIntent = Intent(this@FaceRecognitionActivity, destinationActivity)
            startActivity(mainActivityIntent)
        }

    }


}