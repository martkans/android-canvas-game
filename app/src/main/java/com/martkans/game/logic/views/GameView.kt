package com.martkans.game.logic.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.martkans.game.logic.models.NyanCat

class GameView(context: Context, private var sensorManager: SensorManager, screenX: Int, screenY: Int) :
    SurfaceView(context), Runnable, SensorEventListener {

    companion object {
        private const val LX_TRESHOLD = 5
        private const val SPEED_RATIO = 5
    }

    @Volatile
    private var playing: Boolean = false
    private lateinit var gameThread: Thread

    private var nyanCat: NyanCat = NyanCat(context, screenX, screenY)

    private var paint: Paint = Paint()
    private lateinit var canvas: Canvas
    private var surfaceHolder: SurfaceHolder = holder

    private var currentLX: Float = 10f

    private var currentXModifier: Float = 0f
    private var currentYModifier: Float = 0f

    override fun run() {
        while (playing) {
            update()
            draw()
            control()
        }
    }

    private fun update() {
        nyanCat.update(currentXModifier * SPEED_RATIO, currentYModifier * SPEED_RATIO)
    }

    private fun draw() {
        if (surfaceHolder.surface.isValid) {

            canvas = surfaceHolder.lockCanvas()

            setBackground()

            canvas.drawBitmap(
                nyanCat.getBitmap(),
                nyanCat.getX(),
                nyanCat.getY(),
                paint
            )

            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    private fun control() {
        try {
            Thread.sleep(10)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun pause() {
        sensorManager.unregisterListener(this)
        playing = false
        try {
            gameThread.join()
        } catch (e: InterruptedException) {
        }

    }

    fun resume() {
        registerSensors()
        playing = true
        gameThread = Thread(this)
        gameThread.start()
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

    private fun setBackground() {
        if (currentLX < LX_TRESHOLD)
            canvas.drawColor(Color.BLACK)
        else
            canvas.drawColor(Color.BLUE)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_LIGHT)
                currentLX = event.values[0]
            else {
                currentXModifier = event.values[1]
                currentYModifier = event.values[0]
            }
        }
    }


}