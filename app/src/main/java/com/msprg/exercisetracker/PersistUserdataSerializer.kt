package com.msprg.exerciseTracker

import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object PersistUserdataSerializer : Serializer<ExerciseItemData> {
    override val defaultValue: ExerciseItemData
        get() = ExerciseItemData("UNSPECIFIEDtit", "UNSPECIFIEDdesc")

    override suspend fun readFrom(input: InputStream): ExerciseItemData {
        return try {
            Json.decodeFromString(
                deserializer = ExerciseItemData.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: ExerciseItemData, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = ExerciseItemData.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}