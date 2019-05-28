package com.martkans.game.logic.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import com.martkans.game.R

class Tank(context: Context, screenX: Int, screenY: Int) : Element(screenX, screenY) {

    override var bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.tank)
    override var collisionArea: RectF =
        RectF(x - 10f, y - 10f, bitmap.width.toFloat() - 10f, bitmap.height.toFloat() - 10f)

    override fun update(playerSpeed: Float) {

        super.update(playerSpeed)

        collisionArea.left = x - 10f
        collisionArea.top = y - 10f
        collisionArea.right = x + bitmap.width - 10f
        collisionArea.bottom = y + bitmap.height - 10f
    }
}