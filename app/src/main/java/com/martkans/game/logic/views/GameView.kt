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
import androidx.core.content.ContextCompat
import com.martkans.game.R
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
        private const val STARTING_LX = 10f
        private const val STARTING_NUMBER_OF_LIVES = 3

        private const val SCORE_AND_LIVES_TEXT_SIZE = 50f
        private const val GAME_OVER_TEXT_SIZE = 100f
        private const val PLAY_AGAIN_TEXT_SIZE = 100f

        private const val THREAD_SLEEP_TIME = 10L
    }

    @Volatile
    private var playing: Boolean = true
    private var gameOver: Boolean = false

    private lateinit var gameThread: Thread

    private var nyanCat: NyanCat = NyanCat(contextGame, screenX, screenY)

    private var paint: Paint = Paint()
    private lateinit var canvas: Canvas
    private var surfaceHolder: SurfaceHolder = holder

    private var currentLX: Float = STARTING_LX

    private var currentXModifier: Float = 0f
    private var currentYModifier: Float = 0f

    private var points: Int = 0
    private var lives: Int = STARTING_NUMBER_OF_LIVES

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

    private fun setBackground() {
        if (currentLX < LX_TRESHOLD)
            canvas.drawColor(ContextCompat.getColor(contextGame, R.color.gm_night_canvas))
        else
            canvas.drawColor(ContextCompat.getColor(contextGame, R.color.gm_day_canvas))
    }

    private fun drawElements(elements: ArrayList<Element>) {
        for (e in elements)
            canvas.drawBitmap(e.bitmap, e.x, e.y, paint)
    }

    private fun drawInfoAboutScoreAndLives() {

        paint.textSize = SCORE_AND_LIVES_TEXT_SIZE

        val livesText = context.getString(R.string.gm_lives_num) + lives
        val pointsText = context.getString(R.string.gm_points_num) + points

        canvas.drawText(livesText, 100f, 50f, paint)
        canvas.drawText(pointsText, canvas.width - 150f, 50f, paint)
    }

    private fun drawGameOverMessage() {
        val yCenter = (canvas.height / 2) - ((paint.descent() + paint.ascent()) / 2)

        paint.textSize = GAME_OVER_TEXT_SIZE

        val pointsText = context.getString(R.string.gm_points_game_over) + points

        canvas.drawText(pointsText, canvas.width / 2f, yCenter, paint)

        paint.textSize = PLAY_AGAIN_TEXT_SIZE
        canvas.drawText(context.getString(R.string.gm_play_again), canvas.width / 2f, yCenter + 125f, paint)
    }

    private fun control() {
        try {
            Thread.sleep(THREAD_SLEEP_TIME)
        } catch (e: InterruptedException) {
            e.printStackTrace()
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
            lives = STARTING_NUMBER_OF_LIVES

            resume()
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



}