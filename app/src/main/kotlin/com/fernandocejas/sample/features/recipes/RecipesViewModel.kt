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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fernandocejas.sample.core.interactor.UseCase.None
import com.fernandocejas.sample.core.platform.BaseViewModel
import com.fernandocejas.sample.features.recipes.GetRecipes
import com.fernandocejas.sample.features.recipes.RecipeView
import javax.inject.Inject

class RecipesViewModel
@Inject constructor(private val getRecipes: GetRecipes) : BaseViewModel() {

    private val _recipes: MutableLiveData<List<RecipeView>> = MutableLiveData()
    val recipes: LiveData<List<RecipeView>> = _recipes

    fun loadRecipes() = getRecipes(None()) { it.fold(::handleFailure, ::handleRecipeList) }

    private fun handleRecipeList(recipes: List<RecipeCartItem>) {
        _recipes.value = recipes.map { RecipeView(it.id, it.name, it.image, it.category, it.label, it.price, it.description) }
    }
}