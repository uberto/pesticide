package com.ubertob.pesticide

/*
type class inspired approach, to be validated...


data class CQDomainApi<D : DomainUnderTest<*>>(
    val execCommand: (DomainCommand<D>) -> D,
    val execQuery: (DomainQuery<D, *>) -> D
) {

    fun D.perform(action: DomainAction<D>): D =
        when (action) {
            is DomainQuery<D, *> -> execQuery(action)
            is DomainCommand<D> -> execCommand(action)
            else -> this //ignore the rest
        }

    fun D.fold(actions: Iterable<DomainAction<D>>): D = TODO()
}
*/


interface DomainAction<D : DomainUnderTest<*>>

interface DomainQuery<D : DomainUnderTest<*>, T> : DomainAction<D> {
    val verifyBlock: (T) -> Any?
}

interface DomainCommand<D : DomainUnderTest<*>> : DomainAction<D>
