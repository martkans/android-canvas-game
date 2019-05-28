package com.martkans.game

import android.content.Context
import android.graphics.Point
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.martkans.game.logic.views.GameView


class GameActivity : AppCompatActivity() {
    private var gameView: GameView? = null
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gameView = GameView(this, sensorManager, size.x, size.y)

        playTerribleMusic()

        gameView!!.setOnClickListener {
            gameView!!.initGame()
        }

        setContentView(gameView)
    }

    private fun playTerribleMusic() {

        mediaPlayer = MediaPlayer.create(this, R.raw.nyan_cat_sound)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.stop()
        gameView!!.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView!!.resume()
    }
}
