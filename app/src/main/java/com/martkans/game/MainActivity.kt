package com.martkans.game

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        startBTN.setOnClickListener {
            startGame()
        }

        exitBTN.setOnClickListener {
            exit()
        }
    }

    private fun startGame() {
        val startIntent = Intent(this, GameActivity::class.java)
        startActivity(startIntent)
    }

    private fun exit() {
        val exitIntent = Intent(Intent.ACTION_MAIN)
        exitIntent.addCategory(Intent.CATEGORY_HOME)
        exitIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(exitIntent)
    }
}
