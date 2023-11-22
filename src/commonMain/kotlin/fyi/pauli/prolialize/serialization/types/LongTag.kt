package fyi.pauli.prolialize.serialization.types

import kotlinx.io.Buffer

public class LongTag private constructor(override val name: String? = null) : Tag<Long>() {
	internal constructor(buffer: Buffer, name: String? = null) : this(name) {
		read(buffer)
	}

	public constructor(value: Long, name: String? = null) : this(name) {
		this.value = value
	}

	override val type: TagType = TagType.LONG
	override val size: Int = Long.SIZE_BYTES

	override fun read(buffer: Buffer) {
		value = buffer.readLong()
	}

	override fun write(buffer: Buffer) {
		buffer.writeLong(value)
	}

	override fun clone(name: String?): Tag<Long> {
		return LongTag(value, name)
	}
}