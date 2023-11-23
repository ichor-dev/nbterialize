package fyi.pauli.nbterialize.serialization.types

import kotlinx.io.Buffer
import kotlinx.io.readByteArray

public class ByteArrayTag private constructor(override var name: String? = null) : ArrayTag<ByteArray>() {
    internal constructor(buffer: Buffer, name: String? = null) : this(name) {
        read(buffer)
    }

    public constructor(value: ByteArray, name: String? = null) : this(name) {
        this.value = value
    }

    override val arraySize: Int
        get() = value.size
    override val type: TagType = TagType.BYTE_ARRAY
    override val size: Int
        get() = Int.SIZE_BYTES + value.size

    override fun read(buffer: Buffer) {
        val length = buffer.readInt()

        value = buffer.readByteArray(length)
    }

    override fun write(buffer: Buffer) {
        buffer.writeInt(value.size)
        buffer.write(value)
    }

    override fun clone(name: String?): Tag<ByteArray> {
        return ByteArrayTag(value, name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ByteArrayTag

        if (name != other.name) return false
        if (arraySize != other.arraySize) return false
        if (type != other.type) return false
        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + arraySize
        result = 31 * result + type.hashCode()
        result = 31 * result + size
        return result
    }
}