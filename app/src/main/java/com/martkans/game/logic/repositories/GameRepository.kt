package com.martkans.game.logic.repositories

import android.content.Context
import com.martkans.game.logic.models.Coin
import com.martkans.game.logic.models.Star
import com.martkans.game.logic.models.Tank

object GameRepository {

    private const val STARS_NUMBER = 20
    private const val ENEMIES_NUMBER = 3
    private const val COINS_NUMBER = 10

    private var stars: ArrayList<Star> = ArrayList()
    private var enemies: ArrayList<Tank> = ArrayList()
    private var coins: ArrayList<Coin> = ArrayList()

    fun createStars(context: Context, screenX: Int, screenY: Int): ArrayList<Star> {

        for (i in 1..STARS_NUMBER)
            stars.add(Star(context, screenX, screenY))

        return stars
    }

    fun updateAllStars(playerSpeed: Float) {
        stars.forEach {
            it.update(playerSpeed)
        }
    }

    fun createEnemies(context: Context, screenX: Int, screenY: Int): ArrayList<Tank> {

        for (i in 1..ENEMIES_NUMBER)
            enemies.add(Tank(context, screenX, screenY))

        return enemies
    }

    fun updateAllEnemies(playerSpeed: Float) {
        enemies.forEach {
            it.update(playerSpeed)
        }
    }

    fun createCoins(context: Context, screenX: Int, screenY: Int): ArrayList<Coin> {

        for (i in 1..COINS_NUMBER)
            coins.add(Coin(context, screenX, screenY))

        return coins
    }

    fun updateAllCoins(playerSpeed: Float) {
        coins.forEach {
            it.update(playerSpeed)
        }
    }
}