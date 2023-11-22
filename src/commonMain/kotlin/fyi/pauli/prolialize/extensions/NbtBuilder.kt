package fyi.pauli.prolialize.extensions

import fyi.pauli.prolialize.serialization.types.*

@DslMarker
internal annotation class NbtBuilderDsl

public inline fun buildListTag(
    name: String? = null,
    builder: ListTagBuilder.() -> Unit,
): ListTag = ListTagBuilder(name).apply(builder).build()

public inline fun buildCompoundTag(
    name: String? = null,
    builder: CompoundTagBuilder.() -> Unit,
): CompoundTag = CompoundTagBuilder(name).apply(builder).build()

@NbtBuilderDsl
public abstract class NbtBuilder<T : AnyTag, U : Any> internal constructor(protected val name: String?) {
    protected abstract val entries: U

    @NbtBuilderDsl
    public abstract fun build(): T

    @PublishedApi
    internal fun List<*>.toListOfByte(): List<ByteTag> = map { ByteTag(it as Byte) }

    @PublishedApi
    internal fun List<*>.toListOfShort(): List<ShortTag> = map { ShortTag(it as Short) }

    @PublishedApi
    internal fun List<*>.toListOfInt(): List<IntTag> = map { IntTag(it as Int) }

    @PublishedApi
    internal fun List<*>.toListOfLong(): List<LongTag> = map { LongTag(it as Long) }

    @PublishedApi
    internal fun List<*>.toListOfFloat(): List<FloatTag> = map { FloatTag(it as Float) }

    @PublishedApi
    internal fun List<*>.toListOfDouble(): List<DoubleTag> = map { DoubleTag(it as Double) }

    @PublishedApi
    internal fun List<*>.toListOfByteArray(): List<ByteArrayTag> = map { ByteArrayTag(it as ByteArray) }

    @PublishedApi
    internal fun List<*>.toListOfString(): List<StringTag> = map { StringTag(it as String) }

    @PublishedApi
    internal fun List<*>.toListOfIntArray(): List<IntArrayTag> = map { IntArrayTag(it as IntArray) }

    @PublishedApi
    internal fun List<*>.toListOfLongArray(): List<LongArrayTag> = map { LongArrayTag(it as LongArray) }

    @Suppress("UNCHECKED_CAST")
    @PublishedApi
    internal fun <T : AnyTag> List<*>.toListOf(): List<T> = map { (it as AnyTag).clone(null) as T }

    @PublishedApi
    internal inline fun <reified T : Any> parseList(value: List<T>): ListTag = when (val clazz = T::class) {
        Byte::class -> ListTag(TagType.BYTE, value.toListOfByte())
        Short::class -> ListTag(TagType.SHORT, value.toListOfShort())
        Int::class -> ListTag(TagType.INT, value.toListOfInt())
        Long::class -> ListTag(TagType.LONG, value.toListOfLong())
        Float::class -> ListTag(TagType.FLOAT, value.toListOfFloat())
        Double::class -> ListTag(TagType.DOUBLE, value.toListOfDouble())
        ByteArray::class -> ListTag(TagType.BYTE_ARRAY, value.toListOfByteArray())
        String::class -> ListTag(TagType.STRING, value.toListOfString())
        IntArray::class -> ListTag(TagType.INT_ARRAY, value.toListOfIntArray())
        LongArray::class -> ListTag(TagType.LONG_ARRAY, value.toListOfLongArray())
        ByteTag::class -> ListTag(TagType.BYTE, value.toListOf<ByteTag>())
        ShortTag::class -> ListTag(TagType.SHORT, value.toListOf<ShortTag>())
        IntTag::class -> ListTag(TagType.INT, value.toListOf<IntTag>())
        LongTag::class -> ListTag(TagType.LONG, value.toListOf<LongTag>())
        FloatTag::class -> ListTag(TagType.FLOAT, value.toListOf<FloatTag>())
        DoubleTag::class -> ListTag(TagType.DOUBLE, value.toListOf<DoubleTag>())
        ByteArrayTag::class -> ListTag(TagType.BYTE_ARRAY, value.toListOf<ByteArrayTag>())
        StringTag::class -> ListTag(TagType.STRING, value.toListOf<StringTag>())
        ListTag::class -> ListTag(TagType.LIST, value.toListOf<ListTag>())
        CompoundTag::class -> ListTag(TagType.COMPOUND, value.toListOf<CompoundTag>())
        IntArrayTag::class -> ListTag(TagType.INT_ARRAY, value.toListOf<IntArrayTag>())
        LongArrayTag::class -> ListTag(TagType.LONG_ARRAY, value.toListOf<LongArrayTag>())
        Any::class -> throw IllegalArgumentException("ListTag elements must be of the same type")
        else -> throw IllegalArgumentException("Unsupported type ${clazz.simpleName} for ListTag")
    }
}

public class CompoundTagBuilder @PublishedApi internal constructor(name: String?) :
    NbtBuilder<CompoundTag, MutableCompoundMap>(name) {

    override val entries: MutableCompoundMap = mutableMapOf()

    override fun build(): CompoundTag = CompoundTag(entries, name)

    public fun put(name: String, tag: AnyTag) {
        if (tag is EndTag) throw IllegalArgumentException("Cannot add an NbtEnd to an NbtCompound")
        else entries[name] = tag
    }

    public fun put(name: String, byte: Byte): Unit = put(name, ByteTag(byte, name))
    public fun put(name: String, short: Short): Unit = put(name, ShortTag(short, name))
    public fun put(name: String, int: Int): Unit = put(name, IntTag(int, name))
    public fun put(name: String, long: Long): Unit = put(name, LongTag(long, name))
    public fun put(name: String, float: Float): Unit = put(name, FloatTag(float, name))
    public fun put(name: String, double: Double): Unit = put(name, DoubleTag(double, name))
    public fun put(name: String, byteArray: ByteArray): Unit = put(name, ByteArrayTag(byteArray, name))
    public fun put(name: String, string: String): Unit = put(name, StringTag(string, name))
    public fun put(name: String, intArray: IntArray): Unit = put(name, IntArrayTag(intArray, name))
    public fun put(name: String, longArray: LongArray): Unit = put(name, LongArrayTag(longArray, name))

    public fun putNbtList(name: String, builder: ListTagBuilder.() -> Unit): Unit =
        put(name, ListTagBuilder(name).apply(builder).build())

    public fun putNbtCompound(name: String, builder: CompoundTagBuilder.() -> Unit): Unit =
        put(name, CompoundTagBuilder(null).apply(builder).build())

    public inline fun <reified T : Any> put(name: String, value: List<T>): Unit = put(name, parseList(value))
}

public class ListTagBuilder @PublishedApi internal constructor(name: String?) : NbtBuilder<ListTag, MutableListTagList>(name) {

    override val entries: MutableListTagList = mutableListOf()

    private var elementsType = entries.firstOrNull()?.type

    override fun build(): ListTag = ListTag(elementsType ?: TagType.END, entries, name)

    public fun add(tag: AnyTag) {
        if (elementsType == null) elementsType = tag.type
        else if (elementsType != tag.type) throw IllegalArgumentException("NbtList elements must be of the same type")

        entries.add(tag)
    }

    public fun add(byte: Byte): Unit = add(ByteTag(byte))
    public fun add(short: Short): Unit = add(ShortTag(short))
    public fun add(int: Int): Unit = add(IntTag(int))
    public fun add(long: Long): Unit = add(LongTag(long))
    public fun add(float: Float): Unit = add(FloatTag(float))
    public fun add(double: Double): Unit = add(DoubleTag(double))
    public fun add(byteArray: ByteArray): Unit = add(ByteArrayTag(byteArray))
    public fun add(string: String): Unit = add(StringTag(string))
    public fun add(intArray: IntArray): Unit = add(IntArrayTag(intArray))
    public fun add(longArray: LongArray): Unit = add(LongArrayTag(longArray))

    public fun addNbtList(builder: ListTagBuilder.() -> Unit): Unit = add(ListTagBuilder(null).apply(builder).build())
    public fun addNbtCompound(builder: CompoundTagBuilder.() -> Unit): Unit =
        add(CompoundTagBuilder(null).apply(builder).build())

    public inline fun <reified T : Any> add(value: List<T>): Unit = add(parseList(value))
}