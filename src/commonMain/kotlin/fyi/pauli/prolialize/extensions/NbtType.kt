package fyi.pauli.prolialize.extensions

import fyi.pauli.prolialize.serialization.types.*

/**
 * @author btwonion
 * @since 22/11/2023
 */
public typealias AnyTag = Tag<out Any>

public val AnyTag?.isNbtEnd: Boolean get() = this != null && type == TagType.END
public val AnyTag?.isNbtByte: Boolean get() = this != null && type == TagType.BYTE
public val AnyTag?.isNbtShort: Boolean get() = this != null && type == TagType.SHORT
public val AnyTag?.isNbtInt: Boolean get() = this != null && type == TagType.INT
public val AnyTag?.isNbtLong: Boolean get() = this != null && type == TagType.LONG
public val AnyTag?.isNbtFloat: Boolean get() = this != null && type == TagType.FLOAT
public val AnyTag?.isNbtDouble: Boolean get() = this != null && type == TagType.DOUBLE
public val AnyTag?.isNbtByteArray: Boolean get() = this != null && type == TagType.BYTE_ARRAY
public val AnyTag?.isNbtString: Boolean get() = this != null && type == TagType.STRING
public val AnyTag?.isNbtList: Boolean get() = this != null && type == TagType.LIST
public val AnyTag?.isNbtCompound: Boolean get() = this != null && type == TagType.COMPOUND
public val AnyTag?.isNbtIntArray: Boolean get() = this != null && type == TagType.INT_ARRAY
public val AnyTag?.isNbtLongArray: Boolean get() = this != null && type == TagType.LONG_ARRAY

public val AnyTag?.nbtEnd: EndTag get() = this!!.getAs<EndTag>()
public val AnyTag?.nbtByte: ByteTag get() = this!!.getAs<ByteTag>()
public val AnyTag?.nbtShort: ShortTag get() = this!!.getAs<ShortTag>()
public val AnyTag?.nbtInt: IntTag get() = this!!.getAs<IntTag>()
public val AnyTag?.nbtLong: LongTag get() = this!!.getAs<LongTag>()
public val AnyTag?.nbtFloat: FloatTag get() = this!!.getAs<FloatTag>()
public val AnyTag?.nbtDouble: DoubleTag get() = this!!.getAs<DoubleTag>()
public val AnyTag?.nbtByteArray: ByteArrayTag get() = this!!.getAs<ByteArrayTag>()
public val AnyTag?.nbtString: StringTag get() = this!!.getAs<StringTag>()
public val AnyTag?.nbtList: ListTag get() = this!!.getAs<ListTag>()
public val AnyTag?.nbtCompound: CompoundTag get() = this!!.getAs<CompoundTag>()
public val AnyTag?.nbtIntArray: IntArrayTag get() = this!!.getAs<IntArrayTag>()
public val AnyTag?.nbtLongArray: LongArrayTag get() = this!!.getAs<LongArrayTag>()

public val AnyTag?.byte: Byte get() = nbtByte.value
public val AnyTag?.short: Short get() = nbtShort.value
public val AnyTag?.int: Int get() = nbtInt.value
public val AnyTag?.long: Long get() = nbtLong.value
public val AnyTag?.float: Float get() = nbtFloat.value
public val AnyTag?.double: Double get() = nbtDouble.value
public val AnyTag?.byteArray: ByteArray get() = nbtByteArray.value
public val AnyTag?.string: String get() = nbtString.value
public val AnyTag?.list: ListTagList get() = nbtList.value
public val AnyTag?.compound: CompoundMap get() = nbtCompound.value
public val AnyTag?.intArray: IntArray get() = nbtIntArray.value
public val AnyTag?.longArray: LongArray get() = nbtLongArray.value