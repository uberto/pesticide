package com.ubertob.pesticide.examples.googlepage

import com.codeborne.selenide.Selenide.*
import com.ubertob.pesticide.DdtProtocol
import com.ubertob.pesticide.DomainUnderTest
import com.ubertob.pesticide.PureHttp


class GooglePageDomainWrapper : DomainUnderTest<DdtProtocol> {
    override val protocol: DdtProtocol = PureHttp("web")
    override fun isReady(): Boolean = true

    fun queryGoogle(search: String) {
        open("https://google.com/ncr") //no country redirect
        `$`("input[name=q]").setValue(search).pressEnter()
    }

    fun getSearchResults(): List<String> {
        return `$$`(".r").shouldHaveSize(10).map { it.text() }
    }

}