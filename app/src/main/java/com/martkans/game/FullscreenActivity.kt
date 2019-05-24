package com.martkans.game

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import kotlinx.android.synthetic.main.activity_fullscreen.*


class FullscreenActivity : AppCompatActivity(), SensorEventListener {

    companion object {
        private const val SPEED_RATIO = 5
        private const val LX_TRESHOLD = 50
    }

    private var currentY = 0f
    private var heightScreen = 0f
    private lateinit var sensorManager: SensorManager
    private lateinit var animationNyanCat: SpringAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)
        supportActionBar?.hide()

        val display = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(display)
        val density = resources.displayMetrics.density
        heightScreen = display.heightPixels / density


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        registerSensors()

        animationNyanCat = SpringAnimation(findViewById<View>(R.id.catIV), DynamicAnimation.TRANSLATION_Y, currentY)

    }

    private fun registerSensors() {
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_GAME
        )

        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
            SensorManager.SENSOR_DELAY_FASTEST
        )
    }

    private fun changeBackgroundImage(lx: Float) {
        if (lx < LX_TRESHOLD)
            mainCL.background = getDrawable(R.drawable.night_scene)
        else
            mainCL.background = getDrawable(R.drawable.day_scene)
    }

    private fun calculateCurrentY(currentYModifier: Float) {

        when {
            currentY + currentYModifier < -heightScreen -> currentY = -heightScreen
            currentY + currentYModifier > heightScreen -> currentY = heightScreen
            else -> currentY += currentYModifier
        }
    }

    private fun moveNyanCat() {

        val forceY = SpringForce(currentY)
            .setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY)
            .setStiffness(SpringForce.STIFFNESS_MEDIUM)

        animationNyanCat.spring = forceY
        animationNyanCat.start()

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {

        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_LIGHT)
                changeBackgroundImage(event.values[0])
            else {
                calculateCurrentY(event.values[1] * SPEED_RATIO)
                moveNyanCat()
            }
        }
    }
}
