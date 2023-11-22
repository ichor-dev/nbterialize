package fyi.pauli.prolialize.serialization

import fyi.pauli.prolialize.extensions.AnyTag
import fyi.pauli.prolialize.serialization.types.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.internal.NamedValueEncoder
import kotlinx.serialization.modules.EmptySerializersModule

/**
 * @author btwonion
 * @since 11/11/2023
 *
 * Encoder for the Minecraft Nbt format.
 */
@OptIn(InternalSerializationApi::class)
internal open class NbtEncoder(private val tagConsumer: (AnyTag) -> Unit) : NamedValueEncoder() {
    private val compoundMap: MutableCompoundMap = mutableMapOf()

    override val serializersModule = EmptySerializersModule()

    open fun currentNbtTag(): AnyTag = CompoundTag(compoundMap)

    open fun putTag(key: String, tag: AnyTag) {
        compoundMap[key] = tag
    }

    // Fully supported types -> standard encoding
    override fun encodeTaggedByte(tag: String, value: Byte) = putTag(tag, ByteTag(value, tag))
    override fun encodeTaggedShort(tag: String, value: Short) = putTag(tag, ShortTag(value, tag))
    override fun encodeTaggedInt(tag: String, value: Int) = putTag(tag, IntTag(value, tag))
    override fun encodeTaggedLong(tag: String, value: Long) = putTag(tag, LongTag(value, tag))
    override fun encodeTaggedFloat(tag: String, value: Float) = putTag(tag, FloatTag(value, tag))
    override fun encodeTaggedDouble(tag: String, value: Double) = putTag(tag, DoubleTag(value, tag))
    override fun encodeTaggedString(tag: String, value: String) = putTag(tag, StringTag(value, tag))
    override fun encodeTaggedBoolean(tag: String, value: Boolean) = putTag(tag, ByteTag((if (value) 1 else 0).toByte()))
    override fun encodeTaggedChar(tag: String, value: Char) = putTag(tag, ByteTag(value.code.toByte()))

    @OptIn(ExperimentalSerializationApi::class)
    override fun beginStructure(descriptor: SerialDescriptor): NbtEncoder {
        val consumer = if (currentTagOrNull == null) tagConsumer
        else { tag -> putTag(popTag(), tag) }

        val serialName = descriptor.serialName
        return when (descriptor.kind) {
            StructureKind.CLASS -> NbtEncoder(consumer)
            StructureKind.LIST -> when {
                serialName.endsWith("Array") -> PrimitiveArrayNbtEncoder(serialName.substringAfterLast('.'), consumer)
                serialName.endsWith("List") -> ListNbtEncoder(consumer)
                else -> throw IllegalArgumentException("NBT encoding for $serialName not implemented")
            }

            else -> this
        }
    }

    override fun endEncode(descriptor: SerialDescriptor) {
        tagConsumer(currentNbtTag())
    }
}

internal class PrimitiveArrayNbtEncoder(private val serialName: String, tagConsumer: (AnyTag) -> Unit) :
    NbtEncoder(tagConsumer) {
    private val list = mutableListOf<Any>()

    private fun addPrimitive(primitive: Any) {
        list += primitive
    }

    override fun encodeTaggedByte(tag: String, value: Byte) = addPrimitive(value)
    override fun encodeTaggedInt(tag: String, value: Int) = addPrimitive(value)
    override fun encodeTaggedLong(tag: String, value: Long) = addPrimitive(value)
    override fun encodeTaggedShort(tag: String, value: Short) = addPrimitive(value)
    override fun encodeTaggedFloat(tag: String, value: Float) = addPrimitive(value)
    override fun encodeTaggedDouble(tag: String, value: Double) = addPrimitive(value)
    override fun encodeTaggedBoolean(tag: String, value: Boolean) = addPrimitive(value)
    override fun encodeTaggedChar(tag: String, value: Char) = addPrimitive(value)

    @Suppress("UNCHECKED_CAST")
    override fun currentNbtTag() = when (serialName) {
        "ByteArray" -> ByteArrayTag((list as List<Byte>).toByteArray())
        "IntArray" -> IntArrayTag((list as List<Int>).toIntArray())
        "LongArray" -> LongArrayTag((list as List<Long>).toLongArray())

        "ShortArray" -> IntArrayTag((list as List<Short>).map { it.toInt() }.toIntArray())
        "FloatArray" -> ListTag(TagType.FLOAT, (list as List<Float>).map(::FloatTag))
        "DoubleArray" -> ListTag(TagType.DOUBLE, (list as List<Double>).map(::DoubleTag))

        "BooleanArray" -> ByteArrayTag((list as List<Boolean>).map { (if (it) 1 else 0).toByte() }.toByteArray())
        "CharArray" -> ByteArrayTag((list as List<Char>).map { it.code.toByte() }.toByteArray())
        else -> throw IllegalArgumentException("NBT encoding for $serialName not implemented")
    }
}

internal class ListNbtEncoder(tagConsumer: (AnyTag) -> Unit) : NbtEncoder(tagConsumer) {
    private val list = mutableListOf<AnyTag>()

    override fun currentNbtTag() = ListTag(list.first().type, list)

    override fun putTag(key: String, tag: AnyTag) {
        list += tag
    }
}