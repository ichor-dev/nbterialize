package fyi.pauli.prolialize.serialization.types

import fyi.pauli.prolialize.extensions.AnyTag
import kotlinx.io.Buffer

public typealias ListTagList = List<AnyTag>
public typealias MutableListTagList = MutableList<AnyTag>

public class ListTag private constructor(override val name: String? = null) : Tag<ListTagList>() {
    private lateinit var elementType: TagType

    internal constructor(buffer: Buffer, name: String? = null) : this(name) {
        read(buffer)
    }

    public constructor(elementType: TagType, value: ListTagList, name: String? = null) : this(name) {
        var v = value.toMutableList()

        if (elementType == TagType.END) v = mutableListOf()
        else require(value.all { it.type == elementType }) { "NbtList elements must be of the same type" }

        this.elementType = elementType
        this.value = v.toList()
    }

    override val type: TagType = TagType.LIST
    override val size: Int = Byte.SIZE_BYTES + Int.SIZE_BYTES + value.sumOf { it.size }

    override fun read(buffer: Buffer) {
        val elementId = buffer.readByte().toInt()
        val size = buffer.readInt()

        elementType = TagType.entries.first { it.id == elementId }
        value = List(size) { Companion.read(type, buffer) }
    }

    override fun write(buffer: Buffer) {
        buffer.writeByte(elementType.id.toByte())
        buffer.writeInt(value.size)
        value.forEach { it.write(buffer) }
    }

    override fun clone(name: String?): Tag<ListTagList> {
        return ListTag(elementType, value, name)
    }
}