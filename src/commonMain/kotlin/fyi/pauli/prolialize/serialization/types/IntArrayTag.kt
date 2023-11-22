package fyi.pauli.prolialize.serialization.types

import kotlinx.io.Buffer

public class IntArrayTag private constructor(override val name: String? = null) : ArrayTag<IntArray>() {
	internal constructor(buffer: Buffer, name: String? = null) : this(name) {
		read(buffer)
	}

	public constructor(value: IntArray, name: String? = null) : this(name) {
		this.value = value
	}

	override val arraySize: Int = value.size
	override val type: TagType = TagType.INT_ARRAY
	override val size: Int = Int.SIZE_BYTES + value.size * Int.SIZE_BYTES

	override fun read(buffer: Buffer) {
		val length = buffer.readInt()

		value = IntArray(length) { buffer.readInt() }
	}

	override fun write(buffer: Buffer) {
		buffer.writeInt(value.size)
		value.forEach { buffer.writeInt(it) }
	}

	override fun clone(name: String?): Tag<IntArray> {
		return IntArrayTag(value, name)
	}
}