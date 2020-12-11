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

import com.fernandocejas.sample.UnitTest
import com.fernandocejas.sample.core.exception.Failure.NetworkConnection
import com.fernandocejas.sample.core.exception.Failure.ServerError
import com.fernandocejas.sample.core.functional.Either
import com.fernandocejas.sample.core.functional.Either.Right
import com.fernandocejas.sample.core.platform.NetworkHandler
import com.fernandocejas.sample.features.recipes.RecipeService
import com.fernandocejas.sample.features.recipes.RecipesRepository
import io.mockk.Called
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class RecipeRepositoryTest : UnitTest() {

    private lateinit var networkRepository: RecipesRepository.Network

    @MockK
    private lateinit var networkHandler: NetworkHandler

    @MockK
    private lateinit var service: RecipeService

    @MockK
    private lateinit var recipesCall: Call<List<RecipeEntity>>

    @MockK
    private lateinit var recipesResponse: Response<List<RecipeEntity>>

    @Before
    fun setUp() {
        networkRepository = RecipesRepository.Network(networkHandler, service)
    }

    @Test
    fun `should return empty list by default`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { recipesResponse.body() } returns null
        every { recipesResponse.isSuccessful } returns true
        every { recipesCall.execute() } returns recipesResponse
        every { service.recipes() } returns recipesCall

        val recipes = networkRepository.recipes()

        recipes shouldEqual Right(emptyList<RecipeCartItem>())
        verify(exactly = 1) { service.recipes() }
    }

    @Test
    fun `should get recipe list from service`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { recipesResponse.body() } returns listOf(RecipeEntity("appetizer", "Assorted chillies from Guntur", 1, "", "", "Vadonut", "90"))
        every { recipesResponse.isSuccessful } returns true
        every { recipesCall.execute() } returns recipesResponse
        every { service.recipes() } returns recipesCall

        val recipes = networkRepository.recipes()

        recipes shouldEqual Right(listOf(RecipeEntity("appetizer", "Assorted chillies from Guntur", 1, "", "", "Vadonut", "90")))
        verify(exactly = 1) { service.recipes() }
    }

    @Test
    fun `recipes service should return network failure when no connection`() {
        every { networkHandler.isNetworkAvailable() } returns false

        val recipes = networkRepository.recipes()

        recipes shouldBeInstanceOf Either::class.java
        recipes.isLeft shouldEqual true
        recipes.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java }, {})
        verify { service wasNot Called }
    }

    @Test
    fun `recipes service should return server error if no successful response`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { recipesResponse.isSuccessful } returns false
        every { recipesCall.execute() } returns recipesResponse
        every { service.recipes() } returns recipesCall

        val recipes = networkRepository.recipes()

        recipes shouldBeInstanceOf Either::class.java
        recipes.isLeft shouldEqual true
        recipes.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }

    @Test
    fun `recipes request should catch exceptions`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { recipesCall.execute() } returns recipesResponse
        every { service.recipes() } returns recipesCall

        val recipes = networkRepository.recipes()

        recipes shouldBeInstanceOf Either::class.java
        recipes.isLeft shouldEqual true
        recipes.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }
}