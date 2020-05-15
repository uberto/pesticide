package com.ubertob.pesticide.examples.googlepage

import com.ubertob.pesticide.core.DdtActor
import strikt.api.expectThat
import strikt.assertions.isGreaterThanOrEqualTo

data class GoogleUser(override val name: String) : DdtActor<GooglePageInterpreter>() {
    fun `search for`(searchText: String) = stepWithDesc("searching for $searchText") {
        queryGoogle(searchText)
    }

    fun `can see among results`(expectedText: String) = step {

        val occurrences = getSearchResults().filter { it.contains(expectedText, true) }

        expectThat(occurrences.size).isGreaterThanOrEqualTo(1)
    }

}