package com.greenrobotdev.linklibrary.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable

/**
 * Service for fetching URL metadata (title, description, etc.)
 * Uses multiple strategies to extract metadata from web pages
 */
class MetadataFetchService(private val httpClient: HttpClient) {

    /**
     * Fetches metadata from the given URL
     * Returns Flow<Result> to support coroutines and error handling
     */
    suspend fun fetchMetadata(url: String): Flow<Result<LinkMetadata>> = flow {
        if (url.isBlank()) {
            emit(Result.failure(IllegalArgumentException("URL cannot be blank")))
            return@flow
        }

        // Validate URL format
        if (!isValidUrl(url)) {
            emit(Result.failure(IllegalArgumentException("Invalid URL format")))
            return@flow
        }

        try {
            val response: HttpResponse = httpClient.get(url) {
                // Set user agent to avoid being blocked
                header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                // Follow redirects
                header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            }

            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.MovedPermanently, HttpStatusCode.Found -> {
                    val htmlContent = response.body<String>()
                    val metadata = extractMetadata(htmlContent, url)
                    emit(Result.success(metadata))
                }
                else -> {
                    emit(Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}")))
                }
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Extracts metadata from HTML content
     * Uses multiple strategies: Open Graph, Twitter Cards, standard HTML meta tags, and fallback to title/content
     */
    private fun extractMetadata(html: String, url: String): LinkMetadata {
        val title = extractTitle(html)
        val description = extractDescription(html)
        val imageUrl = extractImageUrl(html)
        val faviconUrl = extractFavicon(html, url)

        return LinkMetadata(
            url = url,
            title = title,
            description = description,
            imageUrl = imageUrl,
            faviconUrl = faviconUrl,
            siteName = extractSiteName(html)
        )
    }

    /**
     * Extracts title using Open Graph, Twitter Card, or fallback to HTML title
     */
    private fun extractTitle(html: String): String {
        // Try Open Graph title first
        val ogTitle = extractMetaContent(html, "property", "og:title")
        if (ogTitle.isNotBlank()) return ogTitle

        // Try Twitter Card title
        val twitterTitle = extractMetaContent(html, "name", "twitter:title")
        if (twitterTitle.isNotBlank()) return twitterTitle

        // Fallback to HTML title tag
        val titleTag = Regex("<title[^>]*>(.*?)</title>", RegexOption.IGNORE_CASE)
            .find(html)?.groupValues?.get(1)?.trim()

        return titleTag?.substring(0, minOf(200, titleTag.length)) ?: ""
    }

    /**
     * Extracts description using Open Graph, Twitter Card, or meta description
     */
    private fun extractDescription(html: String): String {
        // Try Open Graph description first
        val ogDescription = extractMetaContent(html, "property", "og:description")
        if (ogDescription.isNotBlank()) return ogDescription

        // Try Twitter Card description
        val twitterDescription = extractMetaContent(html, "name", "twitter:description")
        if (twitterDescription.isNotBlank()) return twitterDescription

        // Fallback to standard meta description
        val metaDescription = extractMetaContent(html, "name", "description")
        if (metaDescription.isNotBlank()) return metaDescription

        // Last resort: extract first paragraph
        val firstParagraph = Regex("<p[^>]*>(.*?)</p>", RegexOption.IGNORE_CASE)
            .find(html)?.groupValues?.get(1)?.stripHtml()?.trim()

        return firstParagraph?.substring(0, minOf(300, firstParagraph.length)) ?: ""
    }

    /**
     * Extracts image URL using Open Graph, Twitter Card, or favicon
     */
    private fun extractImageUrl(html: String): String? {
        // Try Open Graph image first
        val ogImage = extractMetaContent(html, "property", "og:image")
        if (ogImage.isNotBlank()) return ogImage

        // Try Twitter Card image
        val twitterImage = extractMetaContent(html, "name", "twitter:image")
        if (twitterImage.isNotBlank()) return twitterImage

        // Try image_src link
        val imageSrc = extractLinkHref(html, "image_src")
        if (imageSrc.isNotBlank()) return imageSrc

        return null
    }

    /**
     * Extracts favicon URL
     */
    private fun extractFavicon(html: String, baseUrl: String): String? {
        // Try shortcut icon
        val shortcutIcon = extractLinkHref(html, "shortcut icon")
        if (shortcutIcon.isNotBlank()) return makeAbsoluteUrl(shortcutIcon, baseUrl)

        // Try icon
        val icon = extractLinkHref(html, "icon")
        if (icon.isNotBlank()) return makeAbsoluteUrl(icon, baseUrl)

        // Try apple-touch-icon
        val appleIcon = extractLinkHref(html, "apple-touch-icon")
        if (appleIcon.isNotBlank()) return makeAbsoluteUrl(appleIcon, baseUrl)

        return null
    }

    /**
     * Extracts site name using Open Graph or fallback
     */
    private fun extractSiteName(html: String): String? {
        val ogSiteName = extractMetaContent(html, "property", "og:site_name")
        if (ogSiteName.isNotBlank()) return ogSiteName

        val applicationName = extractMetaContent(html, "name", "application-name")
        if (applicationName.isNotBlank()) return applicationName

        return null
    }

    /**
     * Helper to extract meta tag content by attribute and value
     */
    private fun extractMetaContent(html: String, attribute: String, value: String): String {
        val pattern = Regex("<meta[^>]*$attribute\\s*=\\s*['\"]*$value['\"]*[^>]*content\\s*=\\s*['\"]([^'\"]*)['\"][^>]*>", RegexOption.IGNORE_CASE)
        return pattern.find(html)?.groupValues?.get(1)?.trim() ?: ""
    }

    /**
     * Helper to extract link href by rel attribute
     */
    private fun extractLinkHref(html: String, rel: String): String {
        val pattern = Regex("<link[^>]*rel\\s*=\\s*['\"]*$rel['\"]*[^>]*href\\s*=\\s*['\"]([^'\"]*)['\"][^>]*>", RegexOption.IGNORE_CASE)
        return pattern.find(html)?.groupValues?.get(1)?.trim() ?: ""
    }

    /**
     * Converts relative URLs to absolute URLs
     */
    private fun makeAbsoluteUrl(url: String, baseUrl: String): String {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url
        }

        val base = baseUrl.substringBeforeLast("/")
        return when {
            url.startsWith("//") -> "https:$url"
            url.startsWith("/") -> base.substringBefore("/") + url
            else -> "$base/$url"
        }
    }

    /**
     * Strips HTML tags from text
     */
    private fun String.stripHtml(): String {
        return this.replace(Regex("<[^>]+>"), "").trim()
    }

    /**
     * Validates URL format
     */
    private fun isValidUrl(url: String): Boolean {
        return url.matches(Regex("^https?://[\\w\\-._~:/?#[\\]@!$&'()*+,;=]+$"))
    }
}

/**
 * Data class representing extracted metadata from a URL
 */

@Serializable
data class LinkMetadata(
    val url: String,
    val title: String,
    val description: String,
    val imageUrl: String? = null,
    val faviconUrl: String? = null,
    val siteName: String? = null
) {
    companion object {
        /**
         * Creates empty metadata for fallback scenarios
         */
        fun empty(url: String) = LinkMetadata(
            url = url,
            title = "",
            description = "",
            imageUrl = null,
            faviconUrl = null,
            siteName = null
        )
    }
}