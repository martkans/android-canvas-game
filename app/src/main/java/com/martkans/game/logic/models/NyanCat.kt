package com.martkans.game.logic.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.martkans.game.R


class NyanCat(context: Context, screenX: Int, screenY: Int) {

    private var bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.nyan_cat_small)

    private var x: Float = 75f
    private var y: Float = (screenY / 2).toFloat()

    private val maxY: Float = (screenY - this.bitmap.height).toFloat()
    private val minY: Float = 0f
    private val maxX: Float = (screenX - this.bitmap.width).toFloat()
    private val minX: Float = 0f


    fun update(distanceX: Float, distanceY: Float) {

        x += distanceX

        if (x < minX)
            x = minX
        else if (x > maxX)
            x = maxX

        y += distanceY

        if (y < minY)
            y = minY
        else if (y > maxY)
            y = maxY

    }

    fun getX(): Float {
        return x
    }

    fun getY(): Float {
        return y
    }

    fun getBitmap(): Bitmap {
        return bitmap
    }
}