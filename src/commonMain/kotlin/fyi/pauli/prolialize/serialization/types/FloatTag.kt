package fyi.pauli.prolialize.serialization.types

import kotlinx.io.Buffer
import kotlinx.io.readFloat
import kotlinx.io.writeFloat

public class FloatTag private constructor(override val name: String? = null) : Tag<Float>() {
    internal constructor(buffer: Buffer, name: String? = null) : this(name) {
        read(buffer)
    }

    public constructor(value: Float, name: String? = null) : this(name) {
        this.value = value
    }

    override val type: TagType = TagType.FLOAT
    override val size: Int = Float.SIZE_BYTES

    override fun read(buffer: Buffer) {
        value = buffer.readFloat()
    }

    override fun write(buffer: Buffer) {
        buffer.writeFloat(value)
    }

    override fun clone(name: String?): Tag<Float> {
        return FloatTag(value, name)
    }
}