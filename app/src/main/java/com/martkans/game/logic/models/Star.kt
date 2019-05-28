package com.martkans.game.logic.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.martkans.game.R
import kotlin.random.Random

class Star(context: Context, screenX: Int, screenY: Int) {

    var bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.star)

    var maxX: Float = screenX.toFloat()
    var maxY: Float = screenY.toFloat()

    var x: Float = Random.nextInt(maxX.toInt()).toFloat()
    var y: Float = Random.nextInt(maxY.toInt()).toFloat()
    var speed: Float = Random.nextInt(10).toFloat()

    fun update(playerSpeed: Float) {
        x -= playerSpeed + speed
        if (x < -bitmap.width) {
            x = maxX
            y = Random.nextInt(maxY.toInt()).toFloat()
        }
    }
}