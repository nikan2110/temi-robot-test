package meuhedet.com.temitestappl

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.UserInfo
import com.robotemi.sdk.constants.SdkConstants
import com.robotemi.sdk.listeners.OnBeWithMeStatusChangedListener
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener
import com.robotemi.sdk.listeners.OnRobotReadyListener
import meuhedet.com.temitestappl.dto.ResponseCameraDto
import meuhedet.com.temitestappl.retrofit.RetrofitClient
import meuhedet.com.temitestappl.services.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class MainActivity : AppCompatActivity(), OnRobotReadyListener, Robot.AsrListener,
    OnBeWithMeStatusChangedListener, Robot.WakeupWordListener, OnGoToLocationStatusChangedListener {

    private lateinit var robot: Robot
    private lateinit var buttonPharmacy: Button
    private lateinit var buttonDoctor: Button
    private lateinit var buttonQuestionnaire: Button
    private lateinit var buttonFaceRecognition: Button
    private lateinit var buttonFindDoctor: Button
    private lateinit var buttonPlayMovie: Button
    private var contactsAfterFilter: List<UserInfo> = emptyList()
    private var followService: FollowService? = null
    private var botService = ChatBotService()
    private var newsService = NewsService()
    private var queueService = QueueService()
    private lateinit var mProgressDialog: ProgressDialog
    private var retrofit = RetrofitClient.getClient()
    private var client = retrofit.create(FaceRecognitionService::class.java)


    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: MainActivity
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiKey = BuildConfig.GOOGLE_API_KEY
        System.setProperty("GOOGLE_API_KEY", apiKey)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        instance = this
        buttonPharmacy = findViewById(R.id.button_pharmacy)
        buttonDoctor = findViewById(R.id.button_doctor)
        buttonQuestionnaire = findViewById(R.id.button_questionnaire)
        buttonFaceRecognition = findViewById(R.id.button_face_recognition)
        buttonFindDoctor = findViewById(R.id.button_find_doctor)
        buttonPlayMovie = findViewById(R.id.button_play_movie)
        mProgressDialog = ProgressDialog(this)
        mProgressDialog.isIndeterminate = true
        mProgressDialog.setMessage("Loading...")
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, IntentFilter("photoPath"))
        robot = Robot.getInstance()

        buttonPharmacyAction(buttonPharmacy)
        buttonDoctorAction(buttonDoctor)
        buttonQuestionnaireAction(buttonQuestionnaire)
        buttonFaceRecognitionAction(buttonFaceRecognition)
        buttonFindDoctorAction(buttonFindDoctor)
        buttonPlayMovie(buttonPlayMovie)
    }

    private fun buttonPlayMovie(buttonPlayMovie: Button) {
        buttonPlayMovie.setOnClickListener {
            val destinationActivity = MovieActivity::class.java
            val movieActivityIntent = Intent(this@MainActivity, destinationActivity)
            startActivity(movieActivityIntent)
        }
    }

    private fun buttonFindDoctorAction(buttonFindDoctor: Button) {
            buttonFindDoctor.setOnClickListener {
                val destinationActivity = FindDoctorActivity::class.java
                val findDoctorIntent = Intent(this@MainActivity, destinationActivity)
                startActivity(findDoctorIntent)
            }
    }

    private fun buttonFaceRecognitionAction(buttonFaceRecognition: Button) {
        buttonFaceRecognition.setOnClickListener {
            val destinationActivity = FaceRecognitionActivity::class.java
            val faceRecognitionIntent = Intent(this@MainActivity, destinationActivity)
            startActivity(faceRecognitionIntent)
        }
    }

    private fun buttonQuestionnaireAction(buttonQuestionnaire: Button) {
        buttonQuestionnaire.setOnClickListener {
            val destinationActivity = QuestionaryActivity::class.java
            val questionnaireIntent = Intent(this@MainActivity, destinationActivity)
            startActivity(questionnaireIntent)
        }

    }

    private fun buttonPharmacyAction(buttonPharmacy: Button?) {
        buttonPharmacy?.setOnClickListener {
            val number = queueService.orderQueue("farmacy")
            val destinationActivity = NumberActivity::class.java
            val numberIntent = Intent(this@MainActivity, destinationActivity)
            numberIntent.putExtra(Intent.EXTRA_TEXT, number)
            startActivity(numberIntent)
            Log.i("ButtonPharmacy", "Received number: $number")
        }
    }

    private fun buttonDoctorAction(buttonDoctor: Button?) {
        buttonDoctor?.setOnClickListener {
            val number = queueService.orderQueue("doctor")
            val destinationActivity = NumberActivity::class.java
            val numberIntent = Intent(this@MainActivity, destinationActivity)
            numberIntent.putExtra(Intent.EXTRA_TEXT, number)
            startActivity(numberIntent)
            Log.i("ButtonDoctor", "Received number: $number")
        }
    }

    fun findDoctor(doctorName: String) {
        val nameWithoutSpace = doctorName.filter { !it.isWhitespace() }
        Log.i("Find doctor", "Receive doctor's name $nameWithoutSpace")
        Log.i("Find doctor", "List of locations ${robot.locations}")
        for(location in robot.locations) {
            if (location.equals(nameWithoutSpace, true)) {
                robot.speak(TtsRequest.create("Please, follow me", true, TtsRequest.Language.EN_US))
                robot.goTo(nameWithoutSpace)
                return
            }
        }
        robot.speak(TtsRequest.create("Couldn't find doctor with this name", true, TtsRequest.Language.EN_US))
    }

    override fun onStart() {
        super.onStart()
        val t = Thread {
            while (true) {
                if (MailService.check()) {
                    Log.i("MailServiceCheck", "Started trigger")
                    callHelp("ניקיטה דורושנקו")
                }
                Thread.sleep(10000)
            }
        }
        t.start()
        robot.addOnRobotReadyListener(this)
        robot.addAsrListener(this)
        robot.addOnBeWithMeStatusChangedListener(this)
        robot.addWakeupWordListener(this)
        robot.addOnGoToLocationStatusChangedListener(this)

    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
    }

    override fun onStop() {
        super.onStop()
        robot.removeOnRobotReadyListener(this)
        robot.removeAsrListener(this)
        robot.removeOnBeWithMeStatusChangedListener(this)
        robot.removeWakeupWordListener(this)
        robot.removeOnGoToLocationStatusChangedListener(this)
    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady) {
            try {
                robot.requestToBeKioskApp()
                Log.i("SelectedKiosk", "Is selected kiosk: ${robot.isSelectedKioskApp()}")
                robot.tiltBy(55, 0.5f)
            } catch (e: PackageManager.NameNotFoundException) {
                throw RuntimeException(e)
            }
        }
    }

    override fun onAsrResult(asrResult: String) {
        Log.i("AsrResult", "Received asrResult: $asrResult")
        try {
            val metadata = packageManager
                .getApplicationInfo(packageName, PackageManager.GET_META_DATA).metaData
                ?: return
            if (!robot.isSelectedKioskApp()) {
                return
            }
            if (!metadata.getBoolean(SdkConstants.METADATA_OVERRIDE_NLU)) {
                return
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return
        }
        when {
            asrResult.contains("הצילו", ignoreCase = true) -> {
                robot.askQuestion(getString(R.string.הצילו))
            }
            asrResult.contains("תתקשרי", ignoreCase = true) -> {
                val name = asrResult.substring(8)
                callHelp(name)
            }
            asrResult.contains("שגיאה", ignoreCase = true) -> {
                robot.askQuestion(getString(R.string.שגיאה))
            }
            asrResult.contains("אנשי קשר מרובים", ignoreCase = true) -> {
                robot.askQuestion(getString(R.string.multiplyNames))
            }
            asrResult.contains("חדשות", ignoreCase = true) -> {
                robot.askQuestion("על איזה נושא לחפש חדשות?")
            }
            asrResult.contains("בסדר", ignoreCase = true) -> {
                if (followService != null) {
                    Log.i("FollowServiceStatus", "Customer said, that he is ok")
                    followService!!.interrupt()
                }
                robot.speak(TtsRequest.create("שמח שאתה בסדר"))
            }
            asrResult.contains("תספרי על", ignoreCase = true) -> {
                val theme = asrResult.substring(8)
                Log.i("NewsServiceAsr", "Theme for search news: $theme")
                val articles = newsService.returnArticlesByTheme(theme)
                val quantityArticles = articles.size
                Log.i("NewsServiceAsr", "Quantity of articles are: $quantityArticles")
                if (quantityArticles == 0) {
                    return robot.askQuestion("לא הצלחתי למצוא מאמרים בנושא זה, רוצה לשאול משהו אחר?")
                }
                if (quantityArticles == 1) {
                    val article = articles.get(0)
                    return robot.speak(TtsRequest.create(article.description))
                } else {
                    val numberOfArticle = (0..quantityArticles - 1).random()
                    val article = articles.get(numberOfArticle)
                    val articleWithQuestion = article.description + "רוצה לשאול משהו אחר?"
                    robot.askQuestion(articleWithQuestion)
                }
            }
            else -> {
                try {
                    val request = asrResult
                    Log.i("ChatBotAsr", "Request before translate:  $request")
                    val requestAfterTranslate = botService.translateRequest(request)
                    Log.i("ChatBotAsr", "Request after translate:  $requestAfterTranslate")
                    val assistantResponse =
                        botService.sendRequestAndReceiveResponse(requestAfterTranslate)
                    Log.i("ChatBotAsr", "Response from bot (before translate): $assistantResponse")
                    val responseAfterTranslate: String
                    if (!isNumeric(assistantResponse)) {
                        responseAfterTranslate = botService.translateResponse(assistantResponse)
                        Log.i("ChatBotAsr", "response after translate: $responseAfterTranslate")
                    } else {
                        responseAfterTranslate = assistantResponse
                    }
                    robot.askQuestion(responseAfterTranslate)
                } catch (err: Exception) {
                    Log.e("ChatBotAsr", "ERROR: ${err.message}")
                    robot.askQuestion(getString(R.string.other))
                }
            }
        }

    }

    private fun isNumeric(assistantResponse: String): Boolean {
        return assistantResponse.all { char -> char.isDigit() }
    }

    override fun onBeWithMeStatusChanged(status: String) {
        Log.i("FollowServiceStatus", "status $status")
        if (status.equals("search")) {
            Log.i("FollowServiceStatus", "Status is search")
            followService = FollowService()
            followService!!.start()
        }
        if (status.equals("start") || status.equals("track")) {
            if (followService != null) {
                Log.i("FollowServiceStatus", "Status start or track")
                followService!!.interrupt()
            }
        }
    }

    fun callHelp(name: String) {
        if (contactsAfterFilter.isEmpty()) {
            Log.i("CallHelp", "Contacts after filter is empty")
            val contacts = robot.allContact
            Log.i("CallHelpContacts", "Contacts $contacts")
            contactsAfterFilter = contacts.filter { contact -> contact.name.contains(name) }
        } else {
            Log.i("CallHelp", "Contacts after filter is not empty")
            contactsAfterFilter = contactsAfterFilter.filter { contact -> contact.name.contains(name) }
        }
        Log.i("CallHelpName", "Received name: $name")
        Log.i("CallHelpContactSize", "Received contacts size: ${contactsAfterFilter.size}")
        if (contactsAfterFilter.isEmpty()) {
            return onAsrResult("שגיאה")
        }
        if (contactsAfterFilter.size > 1) {
            return onAsrResult("אנשי קשר מרובים")
        } else {
            val userName = contactsAfterFilter[0].name
            val userId = contactsAfterFilter[0].userId
            call(userName, userId)
        }
        contactsAfterFilter = emptyList()
    }

    fun greeting(name: String) {
        robot.askQuestion("שלום $name. איך אוכל לעזור לך?")
    }

    fun askQuestion(question: String) {
        robot.askQuestion(  question)
    }

    private fun call(name: String, userId: String) {
        Log.i("CallHelpCalling", "Received name: $name, Received user id: $userId")
        robot.finishConversation()
        robot.speak(TtsRequest.create(getString(R.string.nameSpeech) + name))
        robot.startTelepresence(name, userId)
    }

    override fun onWakeupWord(wakeupWord: String, direction: Int) {
        robot.tiltBy(55, 0.5f)
        Log.i("WakeUp", "Wake up world $wakeupWord")
        val serviceIntent = Intent(this, CameraService::class.java)
        startService(serviceIntent)
    }

    //after service finished
    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            // Get extra data included in the Intent
            val path = intent.getStringExtra("path")
            if (path != null) {
                Log.i("CameraServiceReceiver", "Receiver started...")
                mProgressDialog.show()
                uploadImageCamera(path)
                Log.i("CameraServiceReceiver", "Receiver finished...")
            }
            // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    private fun uploadImageCamera(path: String) {
        val imageFile = putImageForSend(path)
        val call  = client.uploadPhotoToCameraFolder(imageFile)
        try {
            call.enqueue(object: Callback<ResponseCameraDto> {
                override fun onResponse(
                    call: Call<ResponseCameraDto>,
                    response: Response<ResponseCameraDto>
                ) {
                    mProgressDialog.dismiss()
                    if (response.body()?.message != null) {
                        Toast.makeText(this@MainActivity, response.body()?.message , Toast.LENGTH_LONG).show()
                    } else {
                        val responseFromServerName = response.body()?.userName
                        val responseFromServerIsTheSame = response.body()?.verified()
                        Log.i("ResponseFromServerSuccess", "Name: $responseFromServerName, Is the same: $responseFromServerIsTheSame")
                        Toast.makeText(this@MainActivity, "Succeed", Toast.LENGTH_LONG).show()
                        if (responseFromServerName != null) {
                           greeting(responseFromServerName)
                        }
                    }
                }
                override fun onFailure(call: Call<ResponseCameraDto>, t: Throwable) {
                    mProgressDialog.dismiss()
                    Log.i("ResponseFromServerFail", "ERROR: ${t.message}")
                    Toast.makeText(
                        this@MainActivity,
                        "Something wrong, please do photo again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } catch (e: Exception) {
            Log.i("ResponseFromServerFail", "ERROR: ${e.message}")
            mProgressDialog.dismiss()
            Toast.makeText(this, "Something wrong, please do photo again", Toast.LENGTH_LONG).show()
        }
    }

    private fun putImageForSend(path: String): MultipartBody.Part {
        val imageFile = File(path)
        Log.i("FilePath", "File name ${imageFile.name}")
        val filePart = RequestBody.create(MultipartBody.FORM, imageFile)
        return MultipartBody.Part.createFormData("image", imageFile.name, filePart)
    }

    override fun onGoToLocationStatusChanged(location: String, status: String, descriptionId: Int, description: String) {
        println("Location: $location, Status: $status, DescriptionId: $descriptionId")
    }
}