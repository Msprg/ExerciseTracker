package com.msprg.exerciseTracker.data

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


@Serializable
data class ExercisesList(
    @Serializable(with = PersistentListSerializer::class)   //Register the serializer explicitly
    val excList: PersistentList<ExerciseItem> = persistentListOf()
)

@Serializable
data class ExerciseItem(
    val icon: ExerciseIcon = ExerciseIcon.VectorIcon(),
    val exTitle: String = "UNSPECIFIEDtit",
    val exDescription: String = "UNSPECIFIEDdesc"
)

//sealed class ExerciseIcon {
//    //    @Serializable(with = ImageVectorSerializer::class)
////    data class VectorIcon(val imageVector: ImageVector = Icons.Default.FitnessCenter) : ExerciseIcon()
//    @Serializable
//    data class VectorIcon(val iconName: String = "FitnessCenter") : ExerciseIcon()
//
//    @Serializable
//    data class RasterIcon(val imageBase64: String) : ExerciseIcon()
//}

//@Serializable(with = ExerciseIconSerializer::class)
//sealed class ExerciseIcon {
//    @Serializable
//    data class VectorIcon(val iconName: String = "FitnessCenter") : ExerciseIcon()
//
//    @Serializable
//    data class RasterIcon(val imageBase64: String) : ExerciseIcon()
//}


@Serializable
sealed class ExerciseIcon {
    @Serializable
    data class VectorIcon(val iconName: String = "FitnessCenter") : ExerciseIcon()

    @Serializable
    data class RasterIcon(val imageBase64: String) : ExerciseIcon()
}

//@Serializable
//data class ExerciseItem(
//    val icon: ExerciseIcon = ExerciseIcon.VectorIcon(),
//    val exTitle: String = "UNSPECIFIEDtit",
//    val exDescription: String = "UNSPECIFIEDdesc"
//)

val ExerciseIconModule = SerializersModule {
    polymorphic(ExerciseIcon::class) {
        subclass(ExerciseIcon.VectorIcon::class, ExerciseIcon.VectorIcon.serializer())
        subclass(ExerciseIcon.RasterIcon::class, ExerciseIcon.RasterIcon.serializer())
    }
}

//val json = Json { serializersModule = ExerciseIconModule }
