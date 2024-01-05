package com.payway.paywaytransactions.domainCore

import android.graphics.Color
object ColorProvider {
    //Provider to deal with repeated colors
    val colorOptions  = listOf(
        Color.BLUE, Color.GREEN, Color.RED, Color.CYAN,
        Color.MAGENTA,
        -1752325,     // Vivid Orange
        -746171,      // Vivid Yellow
        -14349113,    // Teal
        -16724992,    // Dodger Blue
        -11558159,    // Amethyst
        -14866005,    // Alizarin Crimson
        -1417668,     // Turquoise
        -913536,      // Orange
        -10011977,    // Belize Hole
        -913564,      // Wisteria
        -7403521,     // Pomegranate
        -13755792,    // Nephritis
        -5618656,     // Pumpkin
        -16777216,    // Midnight Blue
        -196576,      // Sunflower Yellow
        -14866005,    // Light Coral
        -16724992,    // Dodger Blue (2nd)
        -16453611,    // Carrot Orange
        -15000705,    // Emerald Green
        -14066048,    // Amethyst (2nd)
        -7453525,     // Yellow Orange
        -13581421,    // Green Sea
        -14866005,    // Indian Red
        -16724992,    // Azure
        -1417669,     // Shamrock Green
        -9845137,     // Medium Purple
        -746117,      // Mahogany
        -4613387,     // Squash
        -14128111,    // Medium Turquoise
        -1842205      // Marigold Yellow
    )

    // Shuffle the list of colors to always get a fresh set
    private val shuffledColors = colorOptions.shuffled()

    // Use a cyclic index to iterate through the shuffled colors
    private var currentIndex = 0
    fun getNextColor(): Int {
        val color = shuffledColors[currentIndex]
        currentIndex = (currentIndex + 1) % shuffledColors.size//prevents out of bounds
        return color
    }

    fun getColors(count: Int): List<Int> {
        val resultColors = mutableListOf<Int>()
        repeat(count) {
            resultColors.add(getNextColor())
        }
        return resultColors
    }
}

