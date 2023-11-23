package fyi.pauli.nbterialize.serialization.types

import kotlinx.io.Buffer

public class ShortTag private constructor(override var name: String? = null) : Tag<Short>() {
    internal constructor(buffer: Buffer, name: String? = null) : this(name) {
        read(buffer)
    }

    public constructor(value: Short, name: String? = null) : this(name) {
        this.value = value
    }

    override val type: TagType = TagType.SHORT
    override val size: Int = Short.SIZE_BYTES

    override fun read(buffer: Buffer) {
        value = buffer.readShort()
    }

    override fun write(buffer: Buffer) {
        buffer.writeShort(value)
    }

    override fun clone(name: String?): Tag<Short> {
        return ShortTag(value, name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ShortTag

        if (name != other.name) return false
        if (type != other.type) return false
        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + type.hashCode()
        result = 31 * result + size
        return result
    }
}