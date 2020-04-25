package com.ubertob.pesticide

/*
type class inspired approach...

data class CQDomainApi<D: DomainUnderTest<*>>(
    val execCommand: (DomainCommand<D>) -> D,
    val execQuery: (DomainQuery<D, *>) -> D
) {

  fun D.perform(action: DomainAction<D>) =
      when(action){
          is DomainQuery<D, *> -> execQuery(action)
          is DomainCommand<D> -> execCommand(action)
          else -> this //ignore the rest
      }

   fun DomainAction<D>.bind(anotherAction: action: DomainAction<D>): DomainAction<D> = TODO()

}

*/


interface DomainAction<D : DomainUnderTest<*>>

interface DomainQuery<D : DomainUnderTest<*>, T> : DomainAction<D> {
    val verifyBlock: (T) -> Any?
}

interface DomainCommand<D : DomainUnderTest<*>> : DomainAction<D>
