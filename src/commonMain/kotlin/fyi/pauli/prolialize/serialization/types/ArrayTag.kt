package fyi.pauli.prolialize.serialization.types

/**
 * @author btwonion
 * @since 22/11/2023
 */
public abstract class ArrayTag<T : Any> protected constructor(override val name: String? = null) : Tag<T>() {
    internal abstract val arraySize: Int
}