package com.example.steamapp.quiz_feature.presentation.home

import androidx.annotation.DrawableRes
import com.example.steamapp.R

data class TabItem(
    val title: String,
    @DrawableRes val unselectedIcon: Int,
    @DrawableRes val selectedIcon: Int,
){
    companion object{
        val tabItemsList= listOf(
            TabItem(
                title = "My Quizzes",
                unselectedIcon = R.drawable.myquizzes_outlined,
                selectedIcon = R.drawable.myquizzes_filled
            ),
            TabItem(
                title = "Pi Quizzes",
                unselectedIcon = R.drawable.raspberry_pi_outlined,
                selectedIcon = R.drawable.raspberry_pi_filled
            )
        )

        val materialTabItemsList= listOf(
            TabItem(
                title = "My Materials",
                unselectedIcon = R.drawable.myquizzes_outlined,
                selectedIcon = R.drawable.myquizzes_filled
            ),
            TabItem(
                title = "Pi Materials",
                unselectedIcon = R.drawable.raspberry_pi_outlined,
                selectedIcon = R.drawable.raspberry_pi_filled
            )
        )
    }
}