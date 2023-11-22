package fyi.pauli.prolialize.serialization.types

import kotlinx.io.Buffer

public class ByteTag private constructor(override val name: String? = null) : Tag<Byte>() {
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
}