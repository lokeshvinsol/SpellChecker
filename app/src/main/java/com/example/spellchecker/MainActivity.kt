package com.example.spellchecker

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.textservice.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.view.textservice.SuggestionsInfo


class MainActivity : AppCompatActivity(), SpellCheckerSession.SpellCheckerSessionListener {
    lateinit var tsm: TextServicesManager
    lateinit var session: SpellCheckerSession
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        tsm =
            getSystemService(android.content.Context.TEXT_SERVICES_MANAGER_SERVICE) as android.view.textservice.TextServicesManager
        session = tsm.newSpellCheckerSession(null, Locale.US, this, false)

        get_suggestion.setOnClickListener {
            /*fetchSuggestionsFor("Peter livs in Brlin")
            fetchSuggestionsFor("Adti Leson")
            fetchSuggestionsFor("tpe qte hre")*/
//            fetchSuggestionsFor("Peter livs in Brlin")
//            fetchSuggestionsFor("Adti Leson")
            /*val sb = StringBuilder()
            sb.append(field.text.toString())*/
//            fetchSuggestionsFor(field.text.toString())
//            fetchSuggestionsFor("Peter livs in Brlin")

            fetchSuggestionsFor(field.text.toString())
        }

        fetchSuggestionsFor("Peter livs in Brlin")

    }


    override fun onGetSuggestions(results: Array<SuggestionsInfo>) {

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onGetSentenceSuggestions(results: Array<SentenceSuggestionsInfo>) {
        val sb = StringBuffer("")
        for (result in results) {
            val n = result.suggestionsCount
            for (i in 0 until n) {

                if (result.getSuggestionsInfoAt(i).suggestionsAttributes and SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO !== SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO)
                    continue

                val m = result.getSuggestionsInfoAt(i).suggestionsCount

                for (k in 0 until m) {
                    sb.append(result.getSuggestionsInfoAt(i).getSuggestionAt(k))
                        .append("\n")
                }
                sb.append("\n")
            }
        }

        runOnUiThread { Log.e("AlucarD", sb.toString()) }
    }

    private val NUMBER_OF_SUGGESTIONS = 20

    private fun fetchSuggestionsFor(input: String) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            session.getSentenceSuggestions(arrayOf(TextInfo(input)), NUMBER_OF_SUGGESTIONS)
        }
    }

}
