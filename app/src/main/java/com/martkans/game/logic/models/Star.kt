package com.martkans.game.logic.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import com.martkans.game.R

class Star(context: Context, screenX: Int, screenY: Int) : Element(screenX, screenY) {

    override var bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.star)
    override var collisionArea: RectF = RectF(x, y, bitmap.width.toFloat(), bitmap.height.toFloat())

}