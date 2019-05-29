package com.martkans.game.logic.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import com.martkans.game.R


class NyanCat(context: Context, screenX: Int, screenY: Int) {

    companion object {
        private const val START_X = 75f
        private const val MIN_SPEED = 2
    }

    var bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.nyan_cat_small)

    var x: Float = START_X
    var y: Float = (screenY / 2).toFloat()

    private val maxY: Float = (screenY - this.bitmap.height).toFloat()
    private val minY: Float = 0f
    private val maxX: Float = (screenX - this.bitmap.width).toFloat()
    private val minX: Float = 0f

    var collisionArea: RectF = RectF(x, y, bitmap.width.toFloat(), bitmap.height.toFloat())

    fun update(distanceX: Float, distanceY: Float) {

        x += distanceX + MIN_SPEED

        if (x < minX)
            x = minX
        else if (x > maxX)
            x = maxX

        y += distanceY

        if (y < minY)
            y = minY
        else if (y > maxY)
            y = maxY


        collisionArea.left = x
        collisionArea.top = y
        collisionArea.right = x + bitmap.width
        collisionArea.bottom = y + bitmap.height
    }

    fun moveNyanCatToStartPosition() {
        x = START_X
        y = (maxY + bitmap.width) / 2
    }
}