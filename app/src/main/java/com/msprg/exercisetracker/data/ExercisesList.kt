package com.msprg.exerciseTracker.data

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import java.util.UUID


@Serializable
data class ExercisesList(
    @Serializable(with = PersistentListSerializer::class)   //Register the serializer explicitly
    val excList: PersistentList<ExerciseItem> = persistentListOf()
)

@Serializable
data class ExerciseItem(
    val id: String = UUID.randomUUID().toString(),
    val icon: ExerciseIcon = ExerciseIcon.DefaultIcon,
    val exTitle: String = "",
    val exDescription: String = ""
)

@Serializable
sealed class ExerciseIcon {
    @Serializable
    object DefaultIcon : ExerciseIcon()

    @Serializable
    data class RasterIcon(val imageBase64: String) : ExerciseIcon()
}


//val ExerciseIconModule = SerializersModule {
//    polymorphic(ExerciseIcon::class) {
//        subclass(ExerciseIcon.DefaultIcon::class, ExerciseIcon.DefaultIcon.serializer())
//        subclass(ExerciseIcon.RasterIcon::class, ExerciseIcon.RasterIcon.serializer())
//    }
//}


//val json = Json { serializersModule = ExerciseIconModule }
