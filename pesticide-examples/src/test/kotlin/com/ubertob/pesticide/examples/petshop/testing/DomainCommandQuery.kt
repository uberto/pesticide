package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.core.DomainActions


interface DomainAction<D : DomainActions<*>>

interface DomainQuery<D : DomainActions<*>, T> :
    DomainAction<D> {
    val verifyBlock: (T) -> Any?
}

interface DomainCommand<D : DomainActions<*>> :
    DomainAction<D>
