package fyi.pauli.prolialize.serialization.types

import kotlinx.io.Buffer

public class IntTag private constructor(override val name: String? = null) : Tag<Int>() {
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
}