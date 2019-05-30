package com.martkans.game.logic.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import com.martkans.game.R

class Tank(context: Context, screenX: Int, screenY: Int) : Element(screenX, screenY) {

    companion object {
        private const val COLLISION_AREA_MODIFIER = 30f
    }

    override var bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.tank)
    override var collisionArea: RectF = RectF(
        x - COLLISION_AREA_MODIFIER,
        y - COLLISION_AREA_MODIFIER,
        bitmap.width.toFloat() - COLLISION_AREA_MODIFIER,
        bitmap.height.toFloat() - COLLISION_AREA_MODIFIER
    )

    override fun update(playerSpeed: Float) {

        super.update(playerSpeed)

        collisionArea.left = x - COLLISION_AREA_MODIFIER
        collisionArea.top = y - COLLISION_AREA_MODIFIER
        collisionArea.right = x + bitmap.width - COLLISION_AREA_MODIFIER
        collisionArea.bottom = y + bitmap.height - COLLISION_AREA_MODIFIER
    }
}