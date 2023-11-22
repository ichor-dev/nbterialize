package fyi.pauli.prolialize.serialization.types

import fyi.pauli.prolialize.extensions.AnyTag
import kotlinx.io.Buffer

/**
 * Minecraft's Named Binary Tag file format.
 * @see wiki.vg https://wiki.vg/NBT
 * @author btwonion
 * @since 22/11/2023
 */
public sealed class Tag<T : Any> {
    public abstract val name: String?
    public abstract val type: TagType
    internal lateinit var value: T
    internal abstract val size: Int

    public inline fun <reified T : AnyTag> getAs(): T {
        return this as? T ?: throw IllegalStateException("Tag is not an ${T::class.simpleName}")
    }

    internal abstract fun read(buffer: Buffer)
    internal abstract fun write(buffer: Buffer)
    internal abstract fun clone(name: String? = this.name): Tag<T>

    public companion object {
        public fun read(type: TagType, buffer: Buffer, name: String? = null): AnyTag {
            return when (type) {
                TagType.END -> EndTag
                TagType.BYTE -> ByteTag(buffer, name)
                TagType.SHORT -> ShortTag(buffer, name)
                TagType.INT -> IntTag(buffer, name)
                TagType.LONG -> LongTag(buffer, name)
                TagType.FLOAT -> FloatTag(buffer, name)
                TagType.DOUBLE -> DoubleTag(buffer, name)
                TagType.BYTE_ARRAY -> ByteArrayTag(buffer, name)
                TagType.STRING -> StringTag(buffer, name)
                TagType.LIST -> ListTag(buffer, name)
                TagType.COMPOUND -> CompoundTag(buffer, name)
                TagType.INT_ARRAY -> IntArrayTag(buffer, name)
                TagType.LONG_ARRAY -> LongArrayTag(buffer, name)
            }
        }
    }
}