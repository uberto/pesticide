package com.ubertob.pesticide.examples.googlepage

import com.ubertob.pesticide.DdtActor
import com.ubertob.pesticide.DdtStep
import strikt.api.expectThat
import strikt.assertions.filter
import strikt.assertions.hasSize

data class GoogleUser(override val name: String) : DdtActor<GooglePageDomain>() {
    fun `search for`(searchText: String): DdtStep<GooglePageDomain> = generateStep("searching for $searchText") {
        queryGoogle(searchText)
    }

    fun `can see among results`(expectedText: String): DdtStep<GooglePageDomain> = generateStep {


        val results1 = getSearchResults()
        Thread.sleep(1000)
        val results2 = getSearchResults()

        expectThat(results2).filter { it.contains(expectedText, true) }.hasSize(1)
    }

}