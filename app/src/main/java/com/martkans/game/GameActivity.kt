package com.martkans.game

import android.content.Context
import android.graphics.Point
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.martkans.game.logic.views.GameView


class GameActivity : AppCompatActivity() {
    private var gameView: GameView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gameView = GameView(this, sensorManager, size.x, size.y)


        setContentView(gameView)
    }

    override fun onPause() {
        super.onPause()
        gameView!!.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView!!.resume()
    }
}
