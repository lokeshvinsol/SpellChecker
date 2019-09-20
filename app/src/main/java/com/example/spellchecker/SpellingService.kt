package com.example.spellchecker

import android.os.Build
import android.service.textservice.SpellCheckerService
import android.view.textservice.SentenceSuggestionsInfo
import android.view.textservice.SuggestionsInfo
import android.view.textservice.TextInfo
import java.util.*
import java.nio.file.Files.size
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class SpellingService : SpellCheckerService() {

    override fun createSession(): SpellCheckerService.Session {
        return MySpellingSession()
    }

    internal inner class MySpellingSession : SpellCheckerService.Session() {
        override fun onCreate() {

        }

        override fun onGetSuggestions(textInfo: TextInfo, suggestionsLimit: Int): SuggestionsInfo {

            val word = textInfo.text
            var suggestions: Array<String>? = null
            if (word == "Peter") {
                suggestions = arrayOf("Pedro", "Pietro", "Petar", "Pierre", "Petrus")
            } else {
                suggestions = arrayOf()
            }
            return SuggestionsInfo(SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO, suggestions)
        }

        override fun onGetSentenceSuggestionsMultiple(
            textInfos: Array<TextInfo>,
            suggestionsLimit: Int
        ): Array<SentenceSuggestionsInfo> {

            val suggestionsInfos = ArrayList<SuggestionsInfo>()

            for (i in 0 until textInfos.size) {
                val cur = textInfos[i]

                // Convert the sentence into an array of words
                val words =
                    cur.text.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (word in words) {
                    val tmp = TextInfo(word)
                    // Generate suggestions for each word
                    suggestionsInfos.add(onGetSuggestions(tmp, suggestionsLimit))
                }
            }
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                arrayOf(
                    SentenceSuggestionsInfo(
                        suggestionsInfos.toTypedArray(),
                        IntArray(suggestionsInfos.size),
                        IntArray(suggestionsInfos.size)
                    )
                )
            } else {
                TODO("VERSION.SDK_INT < JELLY_BEAN")
            }
        }
    }

}
