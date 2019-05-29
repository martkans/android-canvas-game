package com.martkans.game.logic.models

import android.graphics.Bitmap
import android.graphics.RectF
import kotlin.random.Random

abstract class Element(screenX: Int, screenY: Int) {

    companion object {
        private const val MIN_SPEED = 3f
        private const val RANDOM_LIMIT = 10
    }

    abstract var bitmap: Bitmap

    var maxX: Float = screenX.toFloat()
    var maxY: Float = screenY.toFloat()

    var x: Float = maxX
    var y: Float = Random.nextInt(maxY.toInt()).toFloat()
    private var speed: Float = Random.nextInt(RANDOM_LIMIT).toFloat() + MIN_SPEED

    abstract var collisionArea: RectF

    open fun update(playerSpeed: Float) {
        x -= playerSpeed + speed
        if (x < -bitmap.width) {
            x = maxX
            y = Random.nextInt(maxY.toInt()).toFloat()
            speed = Random.nextInt(RANDOM_LIMIT).toFloat() + MIN_SPEED
        }
    }
}