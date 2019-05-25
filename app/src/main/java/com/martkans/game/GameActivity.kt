package com.martkans.game

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import kotlinx.android.synthetic.main.activity_game.*


class GameActivity : AppCompatActivity(), SensorEventListener {

    companion object {
        private const val SPEED_RATIO = 5
        private const val LX_TRESHOLD = 10
    }

    private var currentY = 0f
    private var heightScreen = 0f
    private lateinit var sensorManager: SensorManager
    private lateinit var animationNyanCat: SpringAnimation
    private lateinit var popupWindow: PopupWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_game)
        supportActionBar?.hide()

        val display = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(display)
        val density = resources.displayMetrics.density
        heightScreen = display.heightPixels / density


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        registerSensors()

        animationNyanCat = SpringAnimation(
            findViewById<View>(R.id.catIV),
            DynamicAnimation.TRANSLATION_Y, currentY
        )
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

    override fun onBackPressed() {
        handlePausePopup()
    }


    private fun handlePausePopup() {
        sensorManager.unregisterListener(this)

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_pause, null)

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT

        popupWindow = PopupWindow(popupView, width, height, true)

        val resumeBtn = popupView.findViewById<Button>(R.id.resumeBTN)
        val menuBtn = popupView.findViewById<Button>(R.id.menuBTN)

        resumeBtn.setOnClickListener {
            registerSensors()
            popupWindow.dismiss()
        }

        menuBtn.setOnClickListener {
            super.onBackPressed()
        }

        popupWindow.showAtLocation(findViewById(R.id.mainCL), Gravity.CENTER, 0, 0)
    }
}
