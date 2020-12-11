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

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fernandocejas.sample.R
import com.fernandocejas.sample.core.extension.inflate
import com.fernandocejas.sample.core.extension.loadFromUrl
import com.fernandocejas.sample.core.navigation.Navigator
import kotlinx.android.synthetic.main.row_recipe.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class RecipesAdapter
@Inject constructor() : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {

    internal var collection: List<RecipeView> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    internal var clickListener: (RecipeView, Navigator.Extras) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(parent.inflate(R.layout.row_recipe))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
            viewHolder.bind(collection[position], clickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(recipeView: RecipeView, clickListener: (RecipeView, Navigator.Extras) -> Unit) {
            itemView.recipeImage.loadFromUrl(recipeView.image)
            itemView.recipe_name.text = recipeView.name
            itemView.recipe_category.text = recipeView.category
            itemView.recipe_price.text = recipeView.price
            itemView.setOnClickListener { clickListener(recipeView, Navigator.Extras(itemView.recipeImage)) }
        }
    }
}
