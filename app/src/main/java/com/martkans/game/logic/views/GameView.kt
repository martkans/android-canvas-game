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
import com.martkans.game.logic.models.Element
import com.martkans.game.logic.models.NyanCat
import com.martkans.game.logic.repositories.GameRepository


class GameView(
    private val contextGame: Context, private var sensorManager: SensorManager, private val screenX: Int,
    private val screenY: Int
) : SurfaceView(contextGame), Runnable, SensorEventListener {

    companion object {
        private const val LX_TRESHOLD = 5
        private const val SPEED_RATIO = 5
    }

    @Volatile
    private var playing: Boolean = true
    private var gameOver: Boolean = false

    private lateinit var gameThread: Thread

    private var nyanCat: NyanCat = NyanCat(contextGame, screenX, screenY)

    private var paint: Paint = Paint()
    private lateinit var canvas: Canvas
    private var surfaceHolder: SurfaceHolder = holder

    private var currentLX: Float = 10f

    private var currentXModifier: Float = 0f
    private var currentYModifier: Float = 0f

    private var points: Int = 0
    private var lives: Int = 3

    init {

        GameRepository.createCoins(contextGame, screenX, screenY)
        GameRepository.createEnemies(contextGame, screenX, screenY)
        GameRepository.createStars(contextGame, screenX, screenY)

        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER
    }

    override fun run() {
        while (playing) {
            update()
            draw()
            control()
        }
    }

    private fun update() {
        nyanCat.update(currentXModifier * SPEED_RATIO, currentYModifier * SPEED_RATIO)

        GameRepository.updateAllStars(currentXModifier)

        if (GameRepository.updateAllCoins(currentXModifier, nyanCat.collisionArea))
            points++

        if (GameRepository.updateAllEnemies(currentXModifier, nyanCat.collisionArea))
            lives--

        if (lives == 0) {
            gameOver = true
            playing = false

            sensorManager.unregisterListener(this)
        }

    }

    private fun draw() {
        if (surfaceHolder.surface.isValid) {

            canvas = surfaceHolder.lockCanvas()

            setBackground()

            drawElements(GameRepository.stars)
            drawElements(GameRepository.enemies)
            drawElements(GameRepository.coins)

            drawInfoAboutScoreAndLives()

            canvas.drawBitmap(nyanCat.bitmap, nyanCat.x, nyanCat.y, paint)

            if (gameOver) {
                drawGameOverMessage()
            }

            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    private fun drawElements(elements: ArrayList<Element>) {
        for (e in elements)
            canvas.drawBitmap(e.bitmap, e.x, e.y, paint)
    }

    private fun drawInfoAboutScoreAndLives() {

        paint.textSize = 50f
        canvas.drawText("Lives: $lives", 100f, 50f, paint)
        canvas.drawText("Points: $points", canvas.width - 150f, 50f, paint)
    }

    private fun drawGameOverMessage() {
        val yCenter = ((canvas.height / 2) - ((paint.descent() + paint.ascent()) / 2))

        paint.textSize = 100f
        canvas.drawText("Your points: $points", canvas.width / 2f, yCenter, paint)

        paint.textSize = 75f
        canvas.drawText("Tap to play again", canvas.width / 2f, yCenter + 125f, paint)
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
            if (event.sensor.type == Sensor.TYPE_LIGHT) {
                currentLX = event.values[0]
            } else {
                currentXModifier = event.values[1]
                currentYModifier = event.values[0]
            }
        }
    }

    fun initGame() {

        if (gameOver) {
            currentXModifier = 0f
            currentYModifier = 0f

            nyanCat.moveNyanCatToStartPosition()

            GameRepository.createCoins(contextGame, screenX, screenY)
            GameRepository.createEnemies(contextGame, screenX, screenY)
            GameRepository.createStars(contextGame, screenX, screenY)

            gameOver = false

            points = 0
            lives = 3

            resume()
        }

    }

}