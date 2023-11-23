package fyi.pauli.nbterialize.serialization.types

import fyi.pauli.nbterialize.Nbt
import fyi.pauli.nbterialize.extensions.AnyTag
import kotlinx.io.Buffer
import kotlinx.io.readString
import kotlinx.io.readUShort
import kotlinx.io.writeUShort
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeCollection

public typealias CompoundMap = Map<String, AnyTag>
public typealias MutableCompoundMap = MutableMap<String, AnyTag>

@Serializable(with = CompoundTagSerializer::class)
public class CompoundTag private constructor(override var name: String? = null) : Tag<CompoundMap>() {
    internal constructor(buffer: Buffer, name: String? = null) : this(name) {
        read(buffer)
    }

    public constructor(value: CompoundMap, name: String? = null, withoutEnd: Boolean = true) : this(name) {
        require(!withoutEnd || value.values.all { it.type != TagType.END }) { "NbtCompound cannot contain NbtEnd" }

        this.value = value
    }

    override val type: TagType = TagType.COMPOUND
    override val size: Int
        get() = value.entries.sumOf { (name, tag) -> Byte.SIZE_BYTES + (UShort.SIZE_BYTES + name.encodeToByteArray().size + tag.size) } + Byte.SIZE_BYTES

    override fun read(buffer: Buffer) {
        val v: MutableCompoundMap = mutableMapOf()

        var nextId: Byte
        do {
            nextId = buffer.readByte()

            if (nextId == TagType.END.id.toByte()) break

            val nameLength = buffer.readUShort()
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
            buffer.writeUShort(nameBytes.size.toUShort())
            buffer.write(nameBytes)

            tag.write(buffer)
        }

        buffer.writeByte(TagType.END.id.toByte())
    }

    internal fun writeRoot(buffer: Buffer) {
        buffer.writeByte(TagType.COMPOUND.id.toByte())

        write(buffer)
    }

    override fun clone(name: String?): Tag<CompoundMap> {
        return CompoundTag(value, name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CompoundTag

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

public object CompoundTagSerializer : KSerializer<CompoundTag> {
    private object CompoundTagArrayDescription : SerialDescriptor by ListSerializer(Byte.serializer()).descriptor {
        @ExperimentalSerializationApi
        override val serialName: String = "CompoundTag"
    }
    override val descriptor: SerialDescriptor = CompoundTagArrayDescription

    override fun deserialize(decoder: Decoder): CompoundTag {
        val byteArray = ListSerializer(Byte.serializer()).deserialize(decoder).toByteArray()
        return Nbt.decodeToNbt(byteArray) as CompoundTag
    }

    override fun serialize(encoder: Encoder, value: CompoundTag) {
        encoder.encodeCollection(descriptor, Nbt.encodeFromNbt(value).toList()) { index, element ->
            encodeByteElement(descriptor, index, element)
        }
    }
}