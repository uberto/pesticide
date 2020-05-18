package com.ubertob.pesticide.core

/**
 * Base interface for protocols.
 */
interface DdtProtocol {
    val desc: String
}

/**
 * Protocol to use for domain-only tests. Connecting an Hub to the others completely in memory and without technical layers (Json,Db ecc.)
 */
object DomainOnly : DdtProtocol {
    override val desc: String = "DomainOnly"
}

/**
 * Protocol to use for testing one or more Http servers
 */
data class Http(val environmentName: String) : DdtProtocol {
    override val desc: String = "Http $environmentName"
}


/**
 * Protocol to use for testing Html with a headless browser without JavaScript
 */
data class HtmlUINoJS(val environmentName: String) : DdtProtocol {
    override val desc: String = "Html no JS $environmentName"
}

/**
 * Protocol to use for testing Html with a headless browser with JavaScript
 */
data class HtmlUIUsingJS(val environmentName: String) : DdtProtocol {
    override val desc: String = "Html JS $environmentName"
}