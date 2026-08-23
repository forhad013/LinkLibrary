package com.greenrobotdev.linklibrary.model

import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
data class Link(
    val id: String,
    val title: String,
    val url: String,
    val description: String = "",
    val isFavorite: Boolean = false,
    val createdAt: Instant? = null,
    val tags: List<String> = emptyList()
) {
    companion object {
        val sampleLinks = listOf(
            Link(
                id = "1",
                title = "Kotlin Programming Language",
                url = "https://kotlinlang.org",
                description = "The official Kotlin programming language website",
                isFavorite = true,
                tags = listOf("Kotlin", "Programming")
            ),
            Link(
                id = "2",
                title = "Jetpack Compose",
                url = "https://developer.android.com/jetpack/compose",
                description = "Modern toolkit for building native Android UI",
                tags = listOf("Android", "UI")
            ),
            Link(
                id = "3",
                title = "Kotlin Multiplatform",
                url = "https://kotlinlang.org/lp/multiplatform/",
                description = "Share business logic between platforms",
                tags = listOf("KMM", "Cross-platform")
            )
        )
    }
}