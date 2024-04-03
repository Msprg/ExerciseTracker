@file:Suppress("BlockingMethodInNonBlockingContext")

package com.msprg.exerciseTracker.data

import androidx.datastore.core.Serializer
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object PersistUserdataSerializer : Serializer<ExercisesList> {
    override val defaultValue: ExercisesList
        get() = ExercisesList()

    override suspend fun readFrom(input: InputStream): ExercisesList {
        return try {
            Json.decodeFromString(
                deserializer = ExercisesList.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: ExercisesList, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = ExercisesList.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}


@OptIn(ExperimentalSerializationApi::class)
//@Serializer(forClass = PersistentList::class) //Hasn't worked for me
//@kotlinx.serialization.Serializer(forClass = PersistentList::class)   //Works, but warns as redundant
class PersistentListSerializer(
    private val serializer: KSerializer<String>,
) : KSerializer<PersistentList<String>> {

    private class PersistentListDescriptor :
        SerialDescriptor by serialDescriptor<List<String>>() {
        @ExperimentalSerializationApi
        override val serialName: String = "kotlinx.serialization.immutable.persistentList"
    }

    override val descriptor: SerialDescriptor = PersistentListDescriptor()

    override fun serialize(encoder: Encoder, value: PersistentList<String>) {
        return ListSerializer(serializer).serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): PersistentList<String> {
        return ListSerializer(serializer).deserialize(decoder).toPersistentList()
    }
}

//@OptIn(ExperimentalSerializationApi::class)
////@Serializer(forClass = PersistentMap::class)
////@kotlinx.serialization.Serializer(forClass = PersistentMap::class)
//class PersistentMapSerializer(
//    private val keySerializer: KSerializer<String>,
//    private val valueSerializer: KSerializer<PersistentMapSettings>
//) : KSerializer<PersistentMap<String, PersistentMapSettings>> {
//
//    private class PersistentMapDescriptor :
//        SerialDescriptor by serialDescriptor<Map<String, PersistentMapSettings>>() {
//        @ExperimentalSerializationApi
//        override val serialName: String = "kotlinx.serialization.immutable.persistentMap"
//    }
//
//    override val descriptor: SerialDescriptor = PersistentMapDescriptor()
//
//    override fun serialize(encoder: Encoder, value: PersistentMap<String, PersistentMapSettings>) {
//        return MapSerializer(keySerializer, valueSerializer).serialize(encoder, value)
//    }
//
//    override fun deserialize(decoder: Decoder): PersistentMap<String, PersistentMapSettings> {
//        return MapSerializer(keySerializer, valueSerializer).deserialize(decoder).toPersistentMap()
//    }
//
//}


//object ImageVectorSerializer : KSerializer<ImageVector> {
//    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ImageVector", PrimitiveKind.STRING)
//
//    override fun serialize(encoder: Encoder, value: ImageVector) {
//        encoder.encodeString(value.name)
//    }
//
//    override fun deserialize(decoder: Decoder): ImageVector {
//        val name = decoder.decodeString()
//        return Icons.Default::class.java.fields.firstOrNull { it.name == name }?.get(null) as? ImageVector
//            ?: throw IllegalArgumentException("Unknown ImageVector: $name")
//    }
//}

//object ExerciseIconSerializer : KSerializer<ExerciseIcon> {
//    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ExerciseIcon") {
//        element("VectorIcon", ExerciseIcon.VectorIcon.serializer().descriptor)
//        element("RasterIcon", ExerciseIcon.RasterIcon.serializer().descriptor)
//    }
//
//    override fun serialize(encoder: Encoder, value: ExerciseIcon) {
//        when (value) {
//            is ExerciseIcon.VectorIcon -> encoder.encodeSerializableElement(descriptor, 0, ExerciseIcon.VectorIcon.serializer(), value)
//            is ExerciseIcon.RasterIcon -> encoder.encodeSerializableElement(descriptor, 1, ExerciseIcon.RasterIcon.serializer(), value)
//        }
//    }
//
//    override fun deserialize(decoder: Decoder): ExerciseIcon {
//        return when (val index = decoder.decodeElementIndex(descriptor)) {
//            0 -> decoder.decodeSerializableElement(descriptor, 0, ExerciseIcon.VectorIcon.serializer())
//            1 -> decoder.decodeSerializableElement(descriptor, 1, ExerciseIcon.RasterIcon.serializer())
//            CompositeDecoder.DECODE_DONE -> throw SerializationException("Unexpected end of input")
//            else -> throw SerializationException("Unexpected index: $index")
//        }
//    }
//}


