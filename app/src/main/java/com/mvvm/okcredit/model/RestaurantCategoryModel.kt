package com.mvvm.okcredit.model

data class RestaurantCategoryModel(
    var categoryName: String = "",
    var categoryId: String = ""
) {
    override fun toString(): String {
        return categoryName
    }
}