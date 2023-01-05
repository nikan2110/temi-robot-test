package meuhedet.com.temitestappl

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import meuhedet.com.temitestappl.dto.ResponseUploadDto
import meuhedet.com.temitestappl.retrofit.RetrofitClient
import meuhedet.com.temitestappl.services.FaceRecognitionService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RegisterUserActivity : AppCompatActivity() {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var btnSaveToDataBaseFolder: Button
    private lateinit var btnTakePicture: Button
    private lateinit var btnBack: Button
    private lateinit var etId: EditText
    private lateinit var etName: EditText
    private lateinit var currentPhotoPath: String
    private lateinit var iwProfilePicture: ImageView
    private lateinit var mProgressDialog: ProgressDialog
    private var retrofit = RetrofitClient.getClient()
    private var client = retrofit.create(FaceRecognitionService::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        btnSaveToDataBaseFolder = findViewById(R.id.btn_save_to_data_base_folder)
        btnTakePicture = findViewById(R.id.btn_take_a_picture)
        btnBack = findViewById(R.id.btn_back)
        etId = findViewById(R.id.et_id)
        etName = findViewById(R.id.et_name)
        iwProfilePicture = findViewById(R.id.iw_profile_picture)
//        iwProfilePicture.setRotation(180F)
        mProgressDialog = ProgressDialog(this)
        mProgressDialog.isIndeterminate = true
        mProgressDialog.setMessage("Loading...")

        //after taken a picture
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                if (it.resultCode == RESULT_OK) {
//                    var bundle = it.data!!.extras
//                    var bitMap: Bitmap? = bundle?.get("data") as? Bitmap
                    val imageFile = File(currentPhotoPath)
                    if (imageFile.exists()) {
                        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                        iwProfilePicture.setImageBitmap(bitmap)
                    }
                }
            })

        btnTakePicture.setOnClickListener {
            if (etId.text.isEmpty()) {
                Toast.makeText(this, "ID not filled in", Toast.LENGTH_LONG).show()
            }
            if (etName.text.isEmpty()) {
                Toast.makeText(this, "Name not filled in", Toast.LENGTH_LONG).show()
            }
            if (etId.text.toString().trim().length < 9) {
                Toast.makeText(this, "ID too small", Toast.LENGTH_LONG).show()
            }
            if (etId.text.isNotEmpty() && etName.text.isNotEmpty() && etId.text.toString().trim().length == 9) {
                dispatchTakePictureIntent()
            }
        }

        btnSaveToDataBaseFolder.setOnClickListener{
            if (iwProfilePicture.drawable == null) {
                Toast.makeText(this, "First, take a picture", Toast.LENGTH_LONG).show()
            } else {
                mProgressDialog.show()
                uploadImageToDataBase()
                etId.text.clear()
                etName.text.clear()
                iwProfilePicture.setImageDrawable(null)
            }
        }

        btnBack.setOnClickListener {
            val destinationActivity = MainActivity::class.java
            val mainActivityIntent = Intent(this@RegisterUserActivity, destinationActivity)
            startActivity(mainActivityIntent)
        }
    }

    private fun putImageForSend(): MultipartBody.Part {
        val imageFile = File(currentPhotoPath)
        val filePart = RequestBody.create(MultipartBody.FORM, imageFile)
        return MultipartBody.Part.createFormData("image", imageFile.name, filePart)
    }

    private fun uploadImageToDataBase() {
        Log.i("RegisterUserActivityUpload", "started upload")
        val userId: RequestBody = RequestBody.create(MultipartBody.FORM, etId.text.toString())
        val userName: RequestBody = RequestBody.create(MultipartBody.FORM, etName.text.toString())
        val imageFile = putImageForSend()
        val call  = client.uploadPhotoToDataBase(userId, userName, imageFile)
        try {
            call.enqueue(object: Callback<ResponseUploadDto> {
                override fun onResponse(
                    call: Call<ResponseUploadDto>,
                    response: Response<ResponseUploadDto>
                ) {
                    mProgressDialog.dismiss()
                    val responseFromServer = response.body()?.message
                    Log.i("RegisterUserActivityResponseSuccess", "Response from server $responseFromServer")
//                    Toast.makeText(this@RegisterUserActivity, responseFromServer, Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<ResponseUploadDto>, t: Throwable) {
                    mProgressDialog.dismiss()
                    Toast.makeText(
                        this@RegisterUserActivity,
                        "Something wrong, please do photo again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } catch (e: Exception) {
            Log.i("RegisterUserActivityResponseFail", "ERROR: ${e.message}")
            mProgressDialog.dismiss()
            Toast.makeText(this, "Something wrong, please do photo again", Toast.LENGTH_LONG).show()
        }
        Log.i("RegisterUserActivityUpload", "finished upload")
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    ex.printStackTrace()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "meuhedet.com.temitestappl.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING",1)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    activityResultLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        Log.i("RegisterUserActivityStorageDir", "Storage DIR: $storageDir")
        if (etId.text.isEmpty()) {
            val dateTime = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val stringDate = dateTime.format(formatter)
            etId.text = Editable.Factory.getInstance().newEditable(stringDate)
        }
        return File.createTempFile(
            etId.text.toString() + "_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}