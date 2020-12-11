package com.fernandocejas.sample.features.recipes


import com.fernandocejas.sample.core.extension.empty
import com.google.gson.annotations.SerializedName

data class RecipeCartItem(
        @SerializedName("category")
        val category: String, // appetizer
        @SerializedName("description")
        val description: String, // Cooked Toenails of various animals
        @SerializedName("id")
        val id: Int, // 9
        @SerializedName("image")
        val image: String, // https://i.imgur.com/IpG3YOT.jpg
        @SerializedName("label")
        val label: String, // weird
        @SerializedName("name")
        val name: String, // Toenail Zingy
        @SerializedName("price")
        val price: String // 0.99
) {
    companion object {
        val empty = RecipeCartItem(String.empty(), String.empty(), 0, String.empty(), String.empty(), String.empty(), String.empty())
    }
}