package com.ubertob.pesticide.examples.googlepage

import com.ubertob.pesticide.DdtActor
import com.ubertob.pesticide.DdtStep
import strikt.api.expectThat
import strikt.assertions.filter
import strikt.assertions.hasSize

data class GoogleUser(override val name: String) : DdtActor<GooglePageInterpreter>() {
    fun `search for`(searchText: String): DdtStep<GooglePageInterpreter> = stepWithDesc("searching for $searchText") {
        queryGoogle(searchText)
    }

    fun `can see among results`(expectedText: String): DdtStep<GooglePageInterpreter> = step {

        val results = getSearchResults()

        expectThat(results).filter { it.contains(expectedText, true) }.hasSize(1)
    }

}