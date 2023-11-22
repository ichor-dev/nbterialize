package fyi.pauli.prolialize

import fyi.pauli.prolialize.extensions.AnyTag
import fyi.pauli.prolialize.serialization.types.Tag
import fyi.pauli.prolialize.serialization.NbtDecoder
import fyi.pauli.prolialize.serialization.NbtEncoder
import fyi.pauli.prolialize.serialization.types.CompoundTag
import fyi.pauli.prolialize.serialization.types.TagType
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import kotlinx.io.readString
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

/**
 * @author btwonion
 * @since 11/11/2023
 *
 * This is a kotlinx.serialization format supporting the Minecraft protocol.
 * For using specific number encoding annotate the specific field with @NumberType.
 * When using enums you can use the @EnumSerial annotation to specify the number, which should be encoded.
 */
public object Nbt : BinaryFormat {
    override val serializersModule: SerializersModule = EmptySerializersModule()

    /**
     * This function is used to decode a serializable object from a Nbt formatted ByteArray.
     * @param deserializer specifies the serialization strategy to decode the object, by default MyObject.serializer()
     * @param bytes the input ByteArray the object should be read from
     * @returns the decoded object
     */
    @InternalSerializationApi
    public override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
        return decodeFromNbt(deserializer, decodeToNbt(bytes))
    }

    public fun <T> decodeFromNbt(deserializer: DeserializationStrategy<T>, value: AnyTag): T {
        val decoder = NbtDecoder(value)
        return decoder.decodeSerializableValue(deserializer)
    }

    public fun decodeToNbt(value: ByteArray): AnyTag {
        val buffer = Buffer().also { it.write(value) }
        val type = TagType.entries.first { it.id == buffer.readByte().toInt() }
        val nameLength = buffer.readShort()
        val name = buffer.readString(nameLength.toLong())

        return Tag.read(type, buffer, name)
    }

    /**
     * This function is used to encode a serializable object to a Nbt formatted ByteArray.
     * @param serializer specifies the serialization strategy to encode the object, by default MyObject.serializer()
     * @param value the object, which should be transformed to a ByteArray
     * @returns the encoded ByteArray
     */
    override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
        val tag = encodeToNbt(serializer, value)
        return if (tag is CompoundTag) {
            val buffer = Buffer()
            tag.writeRoot(buffer)
            buffer.readByteArray()
        } else byteArrayOf()
    }


    public fun <T> encodeToNbt(serializer: SerializationStrategy<T>, value: T): AnyTag {
        lateinit var tag: AnyTag
        val encoder = NbtEncoder { tag = it }
        encoder.encodeSerializableValue(serializer, value)
        return tag
    }
}