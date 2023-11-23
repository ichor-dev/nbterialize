package fyi.pauli.nbterialize.serialization.types

import kotlinx.io.Buffer

public class ByteTag private constructor(override var name: String? = null) : Tag<Byte>() {
    internal constructor(buffer: Buffer, name: String? = null) : this(name) {
        read(buffer)
    }

    public constructor(value: Byte, name: String? = null) : this(name) {
        this.value = value
    }

    override val type: TagType = TagType.BYTE
    override val size: Int = Byte.SIZE_BYTES

    override fun read(buffer: Buffer) {
        value = buffer.readByte()
    }

    override fun write(buffer: Buffer) {
        buffer.writeByte(value)
    }

    override fun clone(name: String?): Tag<Byte> {
        return ByteTag(value, name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ByteTag

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