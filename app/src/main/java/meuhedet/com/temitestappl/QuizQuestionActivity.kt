package meuhedet.com.temitestappl

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import meuhedet.com.temitestappl.models.Question
import meuhedet.com.temitestappl.utils.Constants

class QuizQuestionActivity : AppCompatActivity(), View.OnClickListener {

    private var currentPosition: Int = 1
    private lateinit var questionList: ArrayList<Question>
    private var selectedOptionPosition: Int = 0

    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgress: TextView
    private lateinit var tvQuestion: TextView

    private lateinit var ivOptionOne: ImageView
    private lateinit var ivOptionTwo: ImageView
    private lateinit var ivOptionThree: ImageView
    private lateinit var ivOptionFour: ImageView

    private lateinit var btnSubmitQuestion: Button
    private lateinit var btnFinish: Button

    private var smiles:ArrayList<ImageView> = ArrayList<ImageView>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)
        progressBar = findViewById(R.id.progress_bar)
        tvProgress = findViewById(R.id.tv_progress)
        tvQuestion = findViewById(R.id.tv_question)
        ivOptionOne = findViewById(R.id.tv_option_one)
        ivOptionTwo = findViewById(R.id.tv_option_two)
        ivOptionThree = findViewById(R.id.tv_option_three)
        ivOptionFour = findViewById(R.id.tv_option_four)
        btnSubmitQuestion = findViewById(R.id.btn_submit_question)
        btnFinish = findViewById(R.id.btn_finish)
        btnFinish.visibility = View.GONE

        smiles.add(ivOptionOne)
        smiles.add(ivOptionTwo)
        smiles.add(ivOptionThree)
        smiles.add(ivOptionFour)

        ivOptionOne.setOnClickListener(this)
        ivOptionTwo.setOnClickListener(this)
        ivOptionThree.setOnClickListener(this)
        ivOptionFour.setOnClickListener(this)
        btnSubmitQuestion.setOnClickListener(this)
        btnFinish.setOnClickListener(this)

        questionList = Constants.getQuestions()
        setQuestion()

    }

    private fun setQuestion() {
        Log.i("Question list size is", "${questionList.size}")
        defaultOptionsView()
        val question: Question = questionList[currentPosition - 1]
        progressBar.progress = currentPosition
        tvProgress.text = "$currentPosition/${progressBar.max}"
        tvQuestion.text = question.description

//        if (currentPosition == questionList.size) {
//            btnSubmitQuestion.background = ContextCompat.getDrawable(this, R.drawable.check_mark)
//        } else {
//            btnSubmitQuestion.background = ContextCompat.getDrawable(this, R.drawable.button_next)
//        }
    }

    private fun defaultOptionsView() {
        ivOptionOne.background = ContextCompat.getDrawable(this, R.drawable.angry_face)
        ivOptionTwo.background = ContextCompat.getDrawable(this, R.drawable.normal_face)
        ivOptionThree.background = ContextCompat.getDrawable(this, R.drawable.smile_face)
        ivOptionFour.background = ContextCompat.getDrawable(this, R.drawable.happy_face)
    }

    private fun selectedOptionView(iv: ImageView, selectedOptionNum: Int) {
        defaultOptionsView()
        var smilesWithoutChoose = smiles.filter { ivList -> ivList.id != iv.id }
        smilesWithoutChoose.forEach { iv -> iv.background = ContextCompat.getDrawable(this, com.google.android.material.R.drawable.m3_tabs_transparent_background)}
        selectedOptionPosition = selectedOptionNum
        btnSubmitQuestion.background = ContextCompat.getDrawable(this, R.drawable.check_mark)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv_option_one -> {
                selectedOptionView(ivOptionOne, 1)
            }
            R.id.tv_option_two -> {
                selectedOptionView(ivOptionTwo, 2)
            }
            R.id.tv_option_three -> {
                selectedOptionView(ivOptionThree, 3)
            }
            R.id.tv_option_four -> {
                selectedOptionView(ivOptionFour, 4)
            }
            R.id.btn_finish -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_submit_question -> {
                if (selectedOptionPosition == 0) {
                    currentPosition++
                    when {
                        currentPosition <= questionList.size -> {
                            setQuestion()
                        }
                    }
                } else {
                    if(currentPosition == questionList.size) {
                        btnSubmitQuestion.visibility = View.GONE
                        btnFinish.visibility = View.VISIBLE
                    } else {
                        btnSubmitQuestion.background = ContextCompat.getDrawable(this, R.drawable.button_next)
                    }

                    selectedOptionPosition = 0
                }
            }
        }
    }
}