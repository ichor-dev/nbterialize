package fyi.pauli.prolialize.serialization.types

import kotlinx.io.Buffer
import kotlinx.io.readDouble
import kotlinx.io.writeDouble

public class DoubleTag private constructor(override val name: String? = null) : Tag<Double>() {
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
}