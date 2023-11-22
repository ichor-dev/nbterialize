package fyi.pauli.prolialize.serialization.types

import fyi.pauli.prolialize.extensions.AnyTag
import kotlinx.io.Buffer
import kotlinx.io.readString

public typealias CompoundMap = Map<String, AnyTag>
public typealias MutableCompoundMap = MutableMap<String, AnyTag>

public class CompoundTag private constructor(override val name: String? = null) : Tag<CompoundMap>() {
    internal constructor(buffer: Buffer, name: String? = null) : this(name) {
        read(buffer)
    }

    public constructor(value: CompoundMap, name: String? = null) : this(name) {
        require(value.values.contains(EndTag)) { "NbtCompound cannot contain NbtEnd" }

        this.value = value
    }

    override val type: TagType = TagType.COMPOUND
    override val size: Int =
        value.entries.sumOf { (name, tag) -> Byte.SIZE_BYTES + (Short.SIZE_BYTES + name.encodeToByteArray().size + tag.size) } + Byte.SIZE_BYTES

    override fun read(buffer: Buffer) {
        val v: MutableCompoundMap = mutableMapOf()

        var nextId: Byte
        do {
            nextId = buffer.readByte()

            if (nextId == TagType.END.id.toByte()) break

            val nameLength = buffer.readShort()
            val nextName = buffer.readString(nameLength.toLong())
            val nextTag = read(TagType.entries.first { it.id == nextId.toInt() }, buffer, nextName)

            v[nextName] = nextTag
        } while (true)

        value = v
    }

    override fun write(buffer: Buffer) {
        value.entries.forEach { (name, tag) ->
            buffer.writeByte(tag.type.id.toByte())
            val nameBytes = name.encodeToByteArray()
            buffer.writeShort(nameBytes.size.toShort())
            buffer.write(nameBytes)

            tag.write(buffer)
        }

        buffer.writeByte(TagType.END.id.toByte())
    }

    internal fun writeRoot(buffer: Buffer) {
        buffer.writeByte(TagType.COMPOUND.id.toByte())
        if (name != null) {
            val nameBytes = name.encodeToByteArray()
            buffer.writeShort(nameBytes.size.toShort())
            buffer.write(nameBytes)
        }

        write(buffer)
    }

    override fun clone(name: String?): Tag<CompoundMap> {
        return CompoundTag(value, name)
    }
}