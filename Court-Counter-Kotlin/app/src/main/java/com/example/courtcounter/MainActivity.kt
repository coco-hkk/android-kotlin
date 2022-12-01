package com.example.courtcounter

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private var scoreTeamA: Int = 0
    private var scoreTeamB: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun addThreeForTeamA(view: View?) {
        scoreTeamA += 3
        displayForTeamA(scoreTeamA)
    }

    fun addTwoForTeamA(view: View?) {
        scoreTeamA += 2
        displayForTeamA(scoreTeamA)
    }

    fun addOneForTeamA(view: View?) {
        scoreTeamA += 1
        displayForTeamA(scoreTeamA)
    }

    fun addThreeForTeamB(view: View?) {
        scoreTeamB += 3
        displayForTeamB(scoreTeamB)
    }

    fun addTwoForTeamB(view: View?) {
        scoreTeamB += 2
        displayForTeamB(scoreTeamB)
    }

    fun addOneForTeamB(view: View?) {
        scoreTeamB += 1
        displayForTeamB(scoreTeamB)
    }

    fun resetScore(view: View?) {
        scoreTeamB = 0
        scoreTeamA = 0

        displayForTeamA(scoreTeamA)
        displayForTeamB(scoreTeamB)
    }

    private fun displayForTeamA(score: Int) {
        val scoreView = findViewById<View>(R.id.team_a_score) as TextView
        scoreView.text = score.toString()
    }

    private fun displayForTeamB(score: Int) {
        val scoreView = findViewById<View>(R.id.team_b_score) as TextView
        scoreView.text = score.toString()
    }
}