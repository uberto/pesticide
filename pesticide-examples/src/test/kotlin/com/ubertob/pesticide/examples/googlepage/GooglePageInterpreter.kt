package com.ubertob.pesticide.examples.googlepage

import com.codeborne.selenide.CollectionCondition.size
import com.codeborne.selenide.Selenide.*
import com.ubertob.pesticide.core.*
import java.net.URL


class GooglePageInterpreter : DomainInterpreter<DdtProtocol> {
    override val protocol: DdtProtocol =
        HtmlUIUsingJS("web")

    override fun prepare(): DomainSetUp {
//        // setup for headless htmlUnit
//        val desiredCapabilities = DesiredCapabilities.htmlUnit()
//        desiredCapabilities.setCapability(HtmlUnitDriver.INVALIDSELECTIONERROR, true)
//        desiredCapabilities.setCapability(HtmlUnitDriver.INVALIDXPATHERROR, false)
//        desiredCapabilities.setJavascriptEnabled(false)
//        val driver = HtmlUnitDriver(desiredCapabilities)
//// associate to selenide
//        WebDriverRunner.setWebDriver(driver)
        return checkInternet()
    }

    fun checkInternet(): DomainSetUp =
        try {
            val url = URL("http://www.google.com")
            val connection = url.openConnection()
            connection.connect()
            Ready
        } catch (e: Exception) {
            NotReady("Internet is not connected, $e")
        }

    fun queryGoogle(search: String) {

        open("https://google.com/ncr") //no country redirect
//        `$`(withText("I agree")).click() //privacy pop up
        `$`("input[name=q]").setValue(search).pressEnter()
    }

    fun getSearchResults(): List<String> {
        return `$$`(".r").shouldHave(size(10)).map { it.text() }
    }

}

