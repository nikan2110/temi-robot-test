package meuhedet.com.temitestappl

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView

class MovieActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var buttonBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        videoView = findViewById(R.id.vw_movie)
        buttonBack = findViewById(R.id.btn_back)

        var uriVideo = Uri.parse("/storage/emulated/0/Download/מאוחדת - רמה אחרת גם ברפואת ילדים!.mkv")
        videoView.setVideoPath(uriVideo.toString())
        videoView.start()

        buttonBack.setOnClickListener {
            val destinationActivity = MainActivity::class.java
            val menuActivityIntent = Intent(this@MovieActivity, destinationActivity)
            startActivity(menuActivityIntent)
            finish()
        }

    }
}