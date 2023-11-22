package fyi.pauli.prolialize.serialization.types

import kotlinx.io.Buffer

public class LongArrayTag private constructor(override val name: String? = null) : ArrayTag<LongArray>() {
	internal constructor(buffer: Buffer, name: String? = null) : this(name) {
		read(buffer)
	}

	public constructor(value: LongArray, name: String? = null) : this(name) {
		this.value = value
	}

	override val arraySize: Int = value.size
	override val type: TagType = TagType.LONG_ARRAY
	override val size: Int = Int.SIZE_BYTES + value.size * Long.SIZE_BYTES

	override fun read(buffer: Buffer) {
		val length = buffer.readInt()

		value = LongArray(length) { buffer.readLong() }
	}

	override fun write(buffer: Buffer) {
		buffer.writeInt(value.size)
		value.forEach { buffer.writeLong(it) }
	}

	override fun clone(name: String?): Tag<LongArray> {
		return LongArrayTag(value, name)
	}
}