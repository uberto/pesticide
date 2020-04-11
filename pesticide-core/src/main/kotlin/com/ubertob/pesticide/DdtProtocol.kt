package com.ubertob.pesticide

interface DdtProtocol {
    val desc: String
}

object InMemoryHubs : DdtProtocol {
    override val desc: String = "InMemory"
}

data class PureHttp(val environmentName: String) : DdtProtocol {
    override val desc: String = "Http $environmentName"
}

data class HtmlUINoJS(val environmentName: String) : DdtProtocol {
    override val desc: String = "Html no JS $environmentName"
}

data class HtmlUIUsingJS(val environmentName: String) : DdtProtocol {
    override val desc: String = "Html JS $environmentName"
}