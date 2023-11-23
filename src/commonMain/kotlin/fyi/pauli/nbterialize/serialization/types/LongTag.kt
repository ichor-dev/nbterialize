package fyi.pauli.nbterialize.serialization.types

import kotlinx.io.Buffer

public class LongTag private constructor(override var name: String? = null) : Tag<Long>() {
    internal constructor(buffer: Buffer, name: String? = null) : this(name) {
        read(buffer)
    }

    public constructor(value: Long, name: String? = null) : this(name) {
        this.value = value
    }

    override val type: TagType = TagType.LONG
    override val size: Int = Long.SIZE_BYTES

    override fun read(buffer: Buffer) {
        value = buffer.readLong()
    }

    override fun write(buffer: Buffer) {
        buffer.writeLong(value)
    }

    override fun clone(name: String?): Tag<Long> {
        return LongTag(value, name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as LongTag

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