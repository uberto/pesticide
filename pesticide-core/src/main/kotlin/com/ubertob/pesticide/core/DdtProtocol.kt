package com.ubertob.pesticide.core

interface DdtProtocol {
    val desc: String
}

object DomainOnly : DdtProtocol {
    override val desc: String = "DomainOnly"
}

data class Http(val environmentName: String) : DdtProtocol {
    override val desc: String = "Http $environmentName"
}

data class HtmlUINoJS(val environmentName: String) : DdtProtocol {
    override val desc: String = "Html no JS $environmentName"
}

data class HtmlUIUsingJS(val environmentName: String) : DdtProtocol {
    override val desc: String = "Html JS $environmentName"
}