package fyi.pauli.nbterialize.serialization.types

import kotlinx.io.Buffer
import kotlinx.io.readDouble
import kotlinx.io.writeDouble

public class DoubleTag private constructor(override var name: String? = null) : Tag<Double>() {
    internal constructor(buffer: Buffer, name: String? = null) : this(name) {
        read(buffer)
    }

    public constructor(value: Double, name: String? = null) : this(name) {
        this.value = value
    }

    override val type: TagType = TagType.DOUBLE
    override val size: Int = Double.SIZE_BYTES

    override fun read(buffer: Buffer) {
        value = buffer.readDouble()
    }

    override fun write(buffer: Buffer) {
        buffer.writeDouble(value)
    }

    override fun clone(name: String?): Tag<Double> {
        return DoubleTag(value, name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DoubleTag

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