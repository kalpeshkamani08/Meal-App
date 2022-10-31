package com.mvvm.okcredit.ui.home.model


import com.google.gson.annotations.SerializedName

data class FoodClassResponse(
    @SerializedName("meals")
    val meals: List<Meal>
) {
    data class Meal(
        @SerializedName("idMeal")
        val idMeal: String,
        @SerializedName("strMeal")
        val strMeal: String,
        @SerializedName("strMealThumb")
        val strMealThumb: String,
        var isSelected:Boolean=false
    )
}