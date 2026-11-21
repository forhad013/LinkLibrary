package com.greenrobotdev.lordofthelinks.bookmarks.service

/**
 * The [wanted] attribute of the first `<tag>` whose [key] attribute equals [value] (ignoring case),
 * whatever order the attributes are written in: `<meta property="og:image" content="…">` and
 * `<meta content="…" property="og:image">` both work. "" when there's none.
 */
internal fun findTagAttribute(html: String, tag: String, key: String, value: String, wanted: String): String {
    for (match in Regex("<$tag\\b[^>]*>", RegexOption.IGNORE_CASE).findAll(html)) {
        val attributes = attributesOf(match.value)
        if (attributes[key.lowercase()].equals(value, ignoreCase = true)) {
            return attributes[wanted.lowercase()]?.trim().orEmpty()
        }
    }
    return ""
}

// name="double", name='single' or name=bare
private val ATTRIBUTE = Regex("""([a-zA-Z_:][-a-zA-Z0-9_:.]*)\s*=\s*(?:"([^"]*)"|'([^']*)'|([^\s"'>]+))""")

/** Attributes of one opening tag, names lower-cased. */
internal fun attributesOf(tag: String): Map<String, String> =
    ATTRIBUTE.findAll(tag).associate { m ->
        m.groupValues[1].lowercase() to (m.groups[2] ?: m.groups[3] ?: m.groups[4])?.value.orEmpty()
    }
