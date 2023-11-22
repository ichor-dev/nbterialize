package fyi.pauli.prolialize.serialization.types

import kotlinx.io.Buffer
import kotlinx.io.readString
import kotlinx.io.writeString

public class StringTag private constructor(override val name: String? = null) : Tag<String>() {
    internal constructor(buffer: Buffer, name: String? = null) : this(name) {
        read(buffer)
    }

    public constructor(value: String, name: String? = null) : this(name) {
        this.value = value
    }

    override val type: TagType = TagType.STRING
    override val size: Int = Short.SIZE_BYTES + value.encodeToByteArray().size

    override fun read(buffer: Buffer) {
        val length = buffer.readShort().toLong()
        value = buffer.readString(length)
    }

    override fun write(buffer: Buffer) {
        buffer.writeShort(value.length.toShort())
        buffer.writeString(value)
    }

    override fun clone(name: String?): Tag<String> {
        return StringTag(value, name)
    }
}