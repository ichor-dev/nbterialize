package fyi.pauli.nbterialize.serialization.types

import kotlinx.io.Buffer

public class IntTag private constructor(override var name: String? = null) : Tag<Int>() {
    internal constructor(buffer: Buffer, name: String? = null) : this(name) {
        read(buffer)
    }

    public constructor(value: Int, name: String? = null) : this(name) {
        this.value = value
    }

    override val type: TagType = TagType.INT
    override val size: Int = Int.SIZE_BYTES

    override fun read(buffer: Buffer) {
        value = buffer.readInt()
    }

    override fun write(buffer: Buffer) {
        buffer.writeInt(value)
    }

    override fun clone(name: String?): Tag<Int> {
        return IntTag(value, name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as IntTag

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