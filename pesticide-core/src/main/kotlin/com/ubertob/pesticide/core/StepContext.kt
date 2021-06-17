package com.ubertob.pesticide.core

/**
 * StepContext is the class to get and store the context for a specific user.
 * It uses the user.name as reference, since the user instance and fields can be different between steps.
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
 * see also {@link DdtUserWithContext} and {@link DdtStep}
 *
 */

data class StepContext<C : Any>(val userName: String, private val contextStore: ContextStore) {

    /**
     * get
     *
     * returns the context or throw exception if not present.
     */
    fun get(): C = contextStore.get(userName)

    /**
     * getFrom
     *
     * allow to retrieve information from the context of another user and store it in the current context.
     */
    fun <K : Any> getFrom(anotherUser: DdtUserWithContext<*, K>, block: (K) -> C) =
        (contextStore.get(anotherUser.name) as? K)
            ?.let(block)
            ?.let { contextStore.store(userName, it) }

    /**
     * getOrNull
     *
     * returns the context or null if not present.
     */
    fun getOrNull(): C? = contextStore.getOrNull(userName)

    /**
     * store
     *
     * store the context. if a context was already present it will overwrite it.
     */
    fun store(newContext: C) = contextStore.store(userName, newContext)

    /**
     * delete
     *
     * delete the context for the user.
     */
    fun delete() = contextStore.delete(userName)
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
