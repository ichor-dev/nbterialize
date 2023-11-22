import fyi.pauli.prolialize.Nbt
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author btwonion
 * @since 22/11/2023
 */
class MainTest {
    @Serializable
    data class TestA(val testCompound: TestB, val testList: List<TestC>, val timestamp: Long)

    @Serializable
    data class TestB(val fibonacci: FloatArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as TestB

            return fibonacci.contentEquals(other.fibonacci)
        }

        override fun hashCode(): Int {
            return fibonacci.contentHashCode()
        }
    }

    @Serializable
    data class TestC(val firstString: String, val secondString: String, val justAnInteger: Int)

    @Test
    fun testA() {
        val testA = TestA(
            testCompound = TestB(
                fibonacci = floatArrayOf(1f, 1f, 2f, 3f, 5f, 8f, 13f, 21f)
            ),
            testList = listOf(
                TestC(
                    firstString = "I'm the first String :)",
                    secondString = "I'm the second String, but order is not guaranteed :/",
                    justAnInteger = 1
                )
            ),
            timestamp = 1000L
        )

        val nbt = Nbt

        val tag = nbt.encodeToNbt(TestA.serializer(), testA)
        val array = nbt.encodeToByteArray(testA)

        val arrayToTag = nbt.decodeToNbt(array)
        val tagToClass = nbt.decodeFromNbt(TestA.serializer(), tag)
        val arrayToClass = nbt.decodeFromByteArray<TestA>(array)

        assertEquals(tag, arrayToTag)
        assertEquals(tagToClass, testA)
        assertEquals(arrayToClass, testA)
    }
}