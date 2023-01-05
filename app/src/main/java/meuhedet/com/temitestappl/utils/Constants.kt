package meuhedet.com.temitestappl.utils

import meuhedet.com.temitestappl.R
import meuhedet.com.temitestappl.models.Question

object Constants {


    const val NEWS_SERVICE: String = "http://192.168.146.227:8080/news/"
    const val QUEUE_SERVICE: String = "http://192.168.146.227:8000/queue/order/"
    const val USER_ID = "062757e0ffbe70a80f77f18d548a35f1" // 062757e0ffbe70a80f77f18d548a35f1 - Viktor; 7738b80514a452cd3fe60f9d4c377d76 - Yaki
    const val USER_NAME = "Victor Bero" // Victor Bero - Viktor; יקי סלומון - Yaki
    val OPTION1: String = getEmojiByUnicode(0x1F60A) + "מרוצה במידה רבה מאוד "
    val OPTION2: String = getEmojiByUnicode(0x1F60C) + "מרוצה במידה רבה "
    val OPTION3: String = getEmojiByUnicode(0x1F614) + "מרוצה במידה בינונית "
    val OPTION4: String = getEmojiByUnicode(0x1F621) + "כלל לא מרוצה "


    fun getQuestions(): ArrayList<Question> {
        val questionsList = ArrayList<Question>()
        // 1
        val question1 = Question(
            1, "כיצד היית מדרג את שביעות רצונך מהשירות שניתן לך במרפאה?",
            OPTION1, OPTION2, OPTION3, OPTION4
        )
        questionsList.add(question1)
        // 2
        val question2 = Question(
            2, "כיצד היית מדרג את שביעות רצונך מהסדר והניקיון במרפאה?",
            OPTION1, OPTION2, OPTION3, OPTION4
        )
        questionsList.add(question2)
        // 3
        val question3 = Question(
            3, "כיצד היית מדרג את שביעות רצונך מזמן ההמתנה לקבלת שירות במרפאה?",
            OPTION1, OPTION2, OPTION3, OPTION4
        )
        questionsList.add(question3)
        // 4
        val question4 = Question(
            4, "כיצד היית מדרג את שביעות רצונך מהיחס והאדיבות של הרופא?",
            OPTION1, OPTION2, OPTION3, OPTION4
        )
        questionsList.add(question4)
        return questionsList
    }

    private fun getEmojiByUnicode(unicode: Int): String? {
        return String(Character.toChars(unicode))
    }
}