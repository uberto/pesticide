package com.ubertob.pesticide.examples.fables

import com.ubertob.pesticide.DdtProtocol
import com.ubertob.pesticide.DomainUnderTest
import com.ubertob.pesticide.InMemoryHubs

class FablesDomainWrapper : DomainUnderTest<DdtProtocol> {
    override val protocol: DdtProtocol = InMemoryHubs

    override fun isReady(): Boolean = true

    fun aGrandMaLivingAloneIntoTheForest(): FablesDomainWrapper {
        return this
    }


}
