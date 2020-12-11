package com.fernandocejas.sample.features.recipes

data class RecipeEntity(private val category: String, private val description: String, private val id: Int, private val image: String, private val label: String, private val name: String, private val price: String) {
    fun toRecipe() = RecipeCartItem(category, description, id, image, label, name, price)
}
