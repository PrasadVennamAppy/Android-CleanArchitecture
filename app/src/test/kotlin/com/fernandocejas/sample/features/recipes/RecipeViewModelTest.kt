/**
 * Copyright (C) 2020 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.sample.features.recipes

import com.fernandocejas.sample.AndroidTest
import com.fernandocejas.sample.core.functional.Either.Right
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqualTo
import org.junit.Before
import org.junit.Test

class RecipeViewModelTest : AndroidTest() {

    private lateinit var recipesViewModel: RecipesViewModel

    @MockK
    private lateinit var getRecipes: GetRecipes

    @Before
    fun setUp() {
        recipesViewModel = RecipesViewModel(getRecipes)
    }

    @Test
    fun `loading recipes should update live data`() {
        val recipesList = listOf(RecipeCartItem("appetizer", "Assorted chillies from Guntur", 1, "", "", "Vadonut", "90"))
        coEvery { getRecipes.run(any()) } returns Right(recipesList)

        recipesViewModel.recipes.observeForever {
            it.size shouldEqualTo 1
            it[0].id shouldEqualTo 0
            it[0].category shouldEqualTo "appetizer"
            it[0].description shouldEqualTo "Assorted chillies from Guntur"
            it[0].image shouldEqualTo ""
            it[0].label shouldEqualTo ""
            it[0].name shouldEqualTo "something"
            it[1].price shouldEqualTo "90"
        }

        runBlocking { recipesViewModel.loadRecipes() }
    }
}