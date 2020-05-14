package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.DomainInterpreter


interface DomainAction<D : DomainInterpreter<*>>

interface DomainQuery<D : DomainInterpreter<*>, T> :
    DomainAction<D> {
    val verifyBlock: (T) -> Any?
}

interface DomainCommand<D : DomainInterpreter<*>> :
    DomainAction<D>
