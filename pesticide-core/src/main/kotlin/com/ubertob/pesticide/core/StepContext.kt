package com.ubertob.pesticide.core

/**
 * StepContext is the class to get and store the context for a specific actor.
 * It uses the actor.name as reference, since the actor instance and fields can be different between steps.
 *
 * <pre>
 *     fun `loggin in`() = step(foodName){ ctx ->
 *       ctx.store( getCartId() )
 *     ...
 *     }
 *
 *     fun `checking out`() = step(foodName){ ctx ->
 *       val cartId = ctx.get()
 *     ...
 *     }
 * </pre>
 *
 * see also {@link DdtActorWithContext} and {@link DdtStep}
 *
 */

data class StepContext<C>(val actorName: String, private val contextStore: ContextStore) {

    /**
     * get
     *
     * returns the context or throw exception if not present.
     */
    fun get(): C = contextStore.get(actorName)

    /**
     * getOrNull
     *
     * returns the context or null if not present.
     */
    fun getOrNull(): C? = contextStore.getOrNull(actorName)

    /**
     * store
     *
     * store the context. if a context was already present it will overwrite it.
     */
    fun store(newContext: C) = contextStore.store(actorName, newContext)

    /**
     * delete
     *
     * delete the context for the actor.
     */
    fun delete() = contextStore.delete(actorName)
}


data class ContextStore(private val map: MutableMap<String, Any?> = mutableMapOf()) {

    fun clear() {
        map.clear()
    }

    fun delete(key: String) {
        map[key] = null
    }

    fun <C> store(key: String, newContext: C) {
        map[key] = newContext
    }

    @Suppress("UNCHECKED_CAST")
    fun <C> getOrNull(key: String): C? =
        map[key] as C? //unfortunate... can we do without downcast?

    fun <C> get(key: String): C =
        getOrNull(key) ?: throw ContextNotPresentException("Context for $key not present! Existing for ${allPresent()}")

    private fun allPresent(): List<String> =
        map.entries
            .filter { it.value != null }
            .map { it.key }

}

data class ContextNotPresentException(val msg: String) : Exception()
