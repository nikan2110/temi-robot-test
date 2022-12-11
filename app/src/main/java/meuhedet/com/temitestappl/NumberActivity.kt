package meuhedet.com.temitestappl

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class NumberActivity : AppCompatActivity() {

    private lateinit var numberTextView: TextView
    private lateinit var buttonNewOrder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number)
        numberTextView = findViewById(R.id.tv_number)
        val intentMainActivity = intent
        if (intentMainActivity.hasExtra(Intent.EXTRA_TEXT)) {
            val number = intentMainActivity.getStringExtra(Intent.EXTRA_TEXT)
            numberTextView.text = number
        }
        buttonNewOrder = findViewById(R.id.button_new_order)
        buttonNewOrder.setOnClickListener {
            val destinationActivity = MainActivity::class.java
            val mainActivityIntent = Intent(this@NumberActivity, destinationActivity)
            startActivity(mainActivityIntent)
        }
    }
}