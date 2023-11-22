package fyi.pauli.prolialize.serialization.types

import kotlinx.io.Buffer

public class ShortTag private constructor(override val name: String? = null) : Tag<Short>() {
	internal constructor(buffer: Buffer, name: String? = null) : this(name) {
		read(buffer)
	}

	public constructor(value: Short, name: String? = null) : this(name) {
		this.value = value
	}

	override val type: TagType = TagType.SHORT
	override val size: Int = Short.SIZE_BYTES

	override fun read(buffer: Buffer) {
		value = buffer.readShort()
	}

	override fun write(buffer: Buffer) {
		buffer.writeShort(value)
	}

	override fun clone(name: String?): Tag<Short> {
		return ShortTag(value, name)
	}
}