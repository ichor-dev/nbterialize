package fyi.pauli.prolialize.serialization.types

import kotlinx.io.Buffer
import kotlinx.io.readByteArray

public class ByteArrayTag private constructor(override val name: String? = null) : ArrayTag<ByteArray>() {
    internal constructor(buffer: Buffer, name: String? = null) : this(name) {
        read(buffer)
    }

    public constructor(value: ByteArray, name: String? = null) : this(name) {
        this.value = value
    }

    override val arraySize: Int = value.size
    override val type: TagType = TagType.BYTE_ARRAY
    override val size: Int = Int.SIZE_BYTES + value.size

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
}