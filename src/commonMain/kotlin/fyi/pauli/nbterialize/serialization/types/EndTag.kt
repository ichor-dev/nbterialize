package fyi.pauli.nbterialize.serialization.types

import kotlinx.io.Buffer

public data object EndTag : Tag<Nothing>() {
    override var name: String? = null
    override val type: TagType = TagType.END
    override val size: Int = 0

    override fun read(buffer: Buffer) {}

    override fun write(buffer: Buffer) {}

    override fun clone(name: String?): Tag<Nothing> {
        return this
    }
}