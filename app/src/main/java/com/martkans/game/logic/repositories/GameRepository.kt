package com.martkans.game.logic.repositories

import android.content.Context
import android.graphics.RectF
import com.martkans.game.logic.models.Coin
import com.martkans.game.logic.models.Element
import com.martkans.game.logic.models.Star
import com.martkans.game.logic.models.Tank

object GameRepository {

    private const val STARS_NUMBER = 50
    private const val ENEMIES_NUMBER = 3
    private const val COINS_NUMBER = 10

    var stars: ArrayList<Element> = ArrayList()
    var enemies: ArrayList<Element> = ArrayList()
    var coins: ArrayList<Element> = ArrayList()

    fun createStars(context: Context, screenX: Int, screenY: Int): ArrayList<Element> {

        stars.clear()

        for (i in 1..STARS_NUMBER)
            stars.add(Star(context, screenX, screenY))

        return stars
    }

    fun updateAllStars(playerSpeed: Float) {
        stars.forEach {
            it.update(playerSpeed)
        }
    }

    fun createEnemies(context: Context, screenX: Int, screenY: Int): ArrayList<Element> {

        enemies.clear()

        for (i in 1..ENEMIES_NUMBER)
            enemies.add(Tank(context, screenX, screenY))

        return enemies
    }

    fun updateAllEnemies(playerSpeed: Float, playerCollisionArea: RectF): Boolean {
        enemies.forEach {
            it.update(playerSpeed)
            if (RectF.intersects(it.collisionArea, playerCollisionArea)) {
                it.x = -it.bitmap.width.toFloat()
                return true
            }
        }

        return false
    }

    fun createCoins(context: Context, screenX: Int, screenY: Int): ArrayList<Element> {

        coins.clear()

        for (i in 1..COINS_NUMBER)
            coins.add(Coin(context, screenX, screenY))

        return coins
    }

    fun updateAllCoins(playerSpeed: Float, playerCollisionArea: RectF): Boolean {
        coins.forEach {
            it.update(playerSpeed)
            if (RectF.intersects(it.collisionArea, playerCollisionArea)) {
                it.x = -it.bitmap.width.toFloat()
                return true
            }
        }

        return false
    }
}