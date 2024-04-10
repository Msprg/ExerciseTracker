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

object PersistExercisesListSerializer : Serializer<ExercisesList> {
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

object PersistRoutinesListSerializer : Serializer<RoutinesList> {
    override val defaultValue: RoutinesList
        get() = RoutinesList()

    override suspend fun readFrom(input: InputStream): RoutinesList {
        return try {
            Json.decodeFromString(
                deserializer = RoutinesList.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: RoutinesList, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = RoutinesList.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}




