package com.greenrobotdev.linklibrary.bookmarks.export

import com.greenrobotdev.linklibrary.bookmarks.model.Category
import com.greenrobotdev.linklibrary.bookmarks.model.Link

/**
 * Links as a browser bookmarks file (Netscape format): Chrome, Firefox, Safari and most read-later
 * apps import it. Each category is a folder (named by its full path); uncategorized links sit at the
 * top level. Tags go in TAGS and the note in <DD>. Favorite / archived / opened ride along as
 * LOL_* attributes, which other apps ignore, so a later import here can restore them.
 */
fun bookmarksHtml(links: List<Link>, categories: List<Category>, linkCategoryIds: Map<String, String>): String =
    buildString {
        appendLine("<!DOCTYPE NETSCAPE-Bookmark-file-1>")
        appendLine("""<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">""")
        appendLine("<TITLE>Bookmarks</TITLE>")
        appendLine("<H1>Lords of Links</H1>")
        appendLine("<DL><p>")
        val byFolder = links.groupBy { link ->
            linkCategoryIds[link.id]?.let { id -> categories.firstOrNull { it.id == id } }?.path(categories)
        }
        byFolder[null].orEmpty().forEach { appendLink(it, indent = "    ") }
        byFolder.filterKeys { it != null }.toSortedMap(compareBy { it }).forEach { (folder, folderLinks) ->
            appendLine("    <DT><H3>${escape(folder!!)}</H3>")
            appendLine("    <DL><p>")
            folderLinks.forEach { appendLink(it, indent = "        ") }
            appendLine("    </DL><p>")
        }
        appendLine("</DL><p>")
    }

private fun StringBuilder.appendLink(link: Link, indent: String) {
    val attributes = buildList {
        add("""HREF="${escape(link.url)}"""")
        link.createdAt?.let { add("""ADD_DATE="${it / 1000}"""") }
        if (link.tags.isNotEmpty()) add("""TAGS="${escape(link.tags.joinToString(","))}"""")
        if (link.isFavorite) add("""LOL_FAVORITE="1"""")
        if (link.isArchived) add("""LOL_ARCHIVED="1"""")
        if (link.isOpened) add("""LOL_OPENED="1"""")
    }.joinToString(" ")
    appendLine("$indent<DT><A $attributes>${escape(link.title.ifBlank { link.url })}</A>")
    if (link.description.isNotBlank()) appendLine("$indent<DD>${escape(link.description)}")
}

/** Page titles and notes are untrusted text: escape them so they can't break the file. */
internal fun escape(text: String): String =
    text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")
