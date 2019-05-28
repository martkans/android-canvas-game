package com.martkans.game.logic.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import com.martkans.game.R

class Coin(context: Context, screenX: Int, screenY: Int) : Element(screenX, screenY) {

    override var bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.coin)
    override var collisionArea: RectF = RectF(x, y, bitmap.width.toFloat(), bitmap.height.toFloat())

    override fun update(playerSpeed: Float) {

        super.update(playerSpeed)

        collisionArea.left = x
        collisionArea.top = y
        collisionArea.right = x + bitmap.width
        collisionArea.bottom = y + bitmap.height
    }
}