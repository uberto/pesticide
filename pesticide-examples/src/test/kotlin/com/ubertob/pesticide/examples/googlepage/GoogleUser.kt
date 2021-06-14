package com.ubertob.pesticide.examples.googlepage

import com.ubertob.pesticide.core.DdtUser
import strikt.api.expectThat
import strikt.assertions.isGreaterThanOrEqualTo

data class GoogleUser(override val name: String) : DdtUser<GooglePageInterpreter>() {
    fun `search for #`(searchText: String) = step(searchText) {
        queryGoogle(searchText)
    }

    fun `can see '#' among results`(expectedText: String) = step(expectedText) {

        val occurrences = getSearchResults().filter { it.contains(expectedText, true) }

        expectThat(occurrences.size).isGreaterThanOrEqualTo(1)
    }

}