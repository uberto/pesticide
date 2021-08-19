package com.ubertob.pesticide.examples.googlepage

import com.ubertob.pesticide.core.DdtActor
import strikt.api.expectThat
import strikt.assertions.isGreaterThanOrEqualTo

data class GoogleUser(override val name: String) : DdtActor<GooglePageActions>() {
    fun `search for #keyword`(searchText: String) = step(searchText) {
        queryGoogle(searchText)
    }

    fun `can see #keyword among results`(expectedText: String) = step(expectedText) {

        val occurrences = getSearchResults().filter { it.contains(expectedText, true) }

        expectThat(occurrences.size).isGreaterThanOrEqualTo(1)
    }

}