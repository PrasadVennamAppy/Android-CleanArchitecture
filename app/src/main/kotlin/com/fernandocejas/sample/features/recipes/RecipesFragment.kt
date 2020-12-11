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

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fernandocejas.sample.R
import com.fernandocejas.sample.core.exception.Failure
import com.fernandocejas.sample.core.exception.Failure.NetworkConnection
import com.fernandocejas.sample.core.exception.Failure.ServerError
import com.fernandocejas.sample.core.extension.*
import com.fernandocejas.sample.core.navigation.Navigator
import com.fernandocejas.sample.core.platform.BaseFragment
import com.fernandocejas.sample.features.recipes.RecipeFailure.ListNotAvailable
import kotlinx.android.synthetic.main.fragment_recipes.*
import javax.inject.Inject

class RecipesFragment : BaseFragment() {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var recipesAdapter: RecipesAdapter

    private lateinit var recipesViewModel: RecipesViewModel

    override fun layoutId() = R.layout.fragment_recipes

    var cartList: ArrayList<RecipeView>? = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        recipesViewModel = viewModel(viewModelFactory) {
            observe(recipes, ::renderRecipesList)
            failure(failure, ::handleFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
        loadRecipesList()
    }


    private fun initializeView() {
        cartList?.clear()
        recipesList.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        recipesList.adapter = recipesAdapter
        recipesAdapter.clickListener = { recipe, navigationExtras ->
            cartList?.add(recipe)
        }
    }

    private fun loadRecipesList() {
        emptyView.invisible()
        recipesList.visible()
        showProgress()
        recipesViewModel.loadRecipes()
    }

    private fun renderRecipesList(recipes: List<RecipeView>?) {
        recipesAdapter.collection = recipes.orEmpty()
        hideProgress()
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is NetworkConnection -> renderFailure(R.string.failure_network_connection)
            is ServerError -> renderFailure(R.string.failure_server_error)
            is ListNotAvailable -> renderFailure(R.string.content_description_empty)
            else -> {

            }
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        recipesList.invisible()
        emptyView.visible()
        hideProgress()
        notifyWithAction(message, R.string.action_refresh, ::loadRecipesList)
    }
}
