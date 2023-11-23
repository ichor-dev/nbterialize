package fyi.pauli.nbterialize.serialization.types

import kotlinx.io.*

public class StringTag private constructor(override var name: String? = null) : Tag<String>() {
    internal constructor(buffer: Buffer, name: String? = null) : this(name) {
        read(buffer)
    }

    public constructor(value: String, name: String? = null) : this(name) {
        this.value = value
    }

    override val type: TagType = TagType.STRING
    override val size: Int
        get() = UShort.SIZE_BYTES + value.encodeToByteArray().size

    override fun read(buffer: Buffer) {
        val length = buffer.readUShort().toLong()
        value = buffer.readString(length)
    }

    override fun write(buffer: Buffer) {
        buffer.writeUShort(value.length.toUShort())
        buffer.writeString(value)
    }

    override fun clone(name: String?): Tag<String> {
        return StringTag(value, name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as StringTag

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