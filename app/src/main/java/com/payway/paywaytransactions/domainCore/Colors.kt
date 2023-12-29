package com.payway.paywaytransactions.domainCore

import android.graphics.Color
object ColorProvider {
    //Provider to deal with repeated colors
    val colorOptions  = listOf(
        Color.BLUE, Color.GREEN, Color.RED, Color.CYAN,
        Color.MAGENTA,
        Color.parseColor("#FF5733"), // Vivid Orange
        Color.parseColor("#FFC300"), // Vivid Yellow
        Color.parseColor("#36D7B7"), // Teal
        Color.parseColor("#3498DB"), // Dodger Blue
        Color.parseColor("#9B59B6"), // Amethyst
        Color.parseColor("#E74C3C"), // Alizarin Crimson
        Color.parseColor("#1ABC9C"), // Turquoise
        Color.parseColor("#F39C12"), // Orange
        Color.parseColor("#2980B9"), // Belize Hole
        Color.parseColor("#8E44AD"), // Wisteria
        Color.parseColor("#C0392B"), // Pomegranate
        Color.parseColor("#27AE60"), // Nephritis
        Color.parseColor("#D35400"), // Pumpkin
        Color.parseColor("#2C3E50"), // Midnight Blue
        Color.parseColor("#F1C40F"), // Sunflower Yellow
        Color.parseColor("#E74C3C"), // Light Coral
        Color.parseColor("#3498DB"), // Dodger Blue (2nd)
        Color.parseColor("#E67E22"), // Carrot Orange
        Color.parseColor("#2ECC71"), // Emerald Green
        Color.parseColor("#8E44AD"), // Amethyst (2nd)
        Color.parseColor("#F39C12"), // Yellow Orange
        Color.parseColor("#16A085"), // Green Sea
        Color.parseColor("#E74C3C"), // Indian Red
        Color.parseColor("#3498DB"), // Azure
        Color.parseColor("#27AE60"), // Shamrock Green
        Color.parseColor("#9B59B6"), // Medium Purple
        Color.parseColor("#C0392B"), // Mahogany
        Color.parseColor("#E67E22"), // Squash
        Color.parseColor("#1ABC9C"), // Medium Turquoise
        Color.parseColor("#F1C40F")  // Marigold Yellow
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

