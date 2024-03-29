package com.ubertob.pesticide.examples.googlepage

import com.ubertob.pesticide.core.DDT
import com.ubertob.pesticide.core.DomainDrivenTest
import java.time.LocalDate

class GooglePageDDT : DomainDrivenTest<GooglePageActions>(
    setOf(
        GooglePageActions()
    )
) {

    val googleUser by NamedActor(::GoogleUser)

    @DDT
    fun `user can search for a word`() = ddtScenario {

        play(

            googleUser.`search for #keyword`("pesticide github kotlin"),

            googleUser.`can see #keyword among results`("uberto")

        ).wip(LocalDate.of(2100, 12, 31), "Google Cookies popup")
    }
}