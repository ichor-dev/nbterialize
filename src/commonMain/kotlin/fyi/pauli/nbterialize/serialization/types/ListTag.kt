package fyi.pauli.nbterialize.serialization.types

import fyi.pauli.nbterialize.extensions.AnyTag
import kotlinx.io.Buffer

public typealias ListTagList = List<AnyTag>
public typealias MutableListTagList = MutableList<AnyTag>

public class ListTag private constructor(override var name: String? = null) : Tag<ListTagList>() {
    private lateinit var elementType: TagType

    internal constructor(buffer: Buffer, name: String? = null) : this(name) {
        read(buffer)
    }

    public constructor(
        elementType: TagType,
        value: ListTagList,
        name: String? = null,
        mustBeSame: Boolean = true
    ) : this(name) {
        var v = value.toMutableList()

        if (elementType == TagType.END) v = mutableListOf()
        else require(!mustBeSame || value.all { it.type == elementType }) { "NbtList elements must be of the same type" }

        this.elementType = elementType
        this.value = v.toList()
    }

    override val type: TagType = TagType.LIST
    override val size: Int
        get() = Byte.SIZE_BYTES + Int.SIZE_BYTES + value.sumOf { it.size }

    override fun read(buffer: Buffer) {
        val elementId = buffer.readByte().toInt()
        if (elementId == TagType.END.id) {
            elementType = TagType.END
            value = emptyList()
            return
        }
        val size = buffer.readInt()

        elementType = TagType.entries.first { it.id == elementId }
        value = List(size) { Companion.read(elementType, buffer) }
    }

    override fun write(buffer: Buffer) {
        if (value.isNotEmpty()) buffer.writeByte(elementType.id.toByte())
        else {
            buffer.writeByte(TagType.END.id.toByte())
            return
        }
        buffer.writeInt(value.size)
        value.forEach {
            it.name = null
            it.write(buffer)
        }
    }

    override fun clone(name: String?): Tag<ListTagList> {
        return ListTag(elementType, value, name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ListTag

        if (name != other.name) return false
        if (elementType != other.elementType) return false
        if (type != other.type) return false
        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + elementType.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + size
        return result
    }
}