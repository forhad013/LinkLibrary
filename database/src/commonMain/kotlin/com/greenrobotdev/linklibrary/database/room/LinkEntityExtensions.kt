//package com.greenrobotdev.linklibrary.database.room
//
//import com.greenrobotdev.linklibrary.model.Link
//import kotlinx.datetime.Instant
//import kotlin.time.ExperimentalTime
//
///**
// * Extension functions to convert between Link (domain model) and LinkEntity (Room entity).
// */
//
//@OptIn(ExperimentalTime::class)
//fun Link.toEntity(): LinkEntity {
//    return LinkEntity(
//        id = id,
//        title = title,
//        url = url,
//        description = description,
//        isFavorite = isFavorite,
//        createdAt = createdAt?.toEpochMilliseconds(),
//        tags = tags
//    )
//}
//
//@OptIn(ExperimentalTime::class)
//fun LinkEntity.toDomain(): Link {
//    return Link(
//        id = id,
//        title = title,
//        url = url,
//        description = description,
//        isFavorite = isFavorite,
//        createdAt = createdAt?.let { Instant.fromEpochMilliseconds(it) },
//        tags = tags
//    )
//}
