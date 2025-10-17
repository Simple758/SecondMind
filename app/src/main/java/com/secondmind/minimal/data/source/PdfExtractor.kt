// ============================================
// FILE: data/source/PdfExtractor.kt
// ============================================

package com.secondmind.minimal.data.source

import android.content.Context
import android.net.Uri
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import com.secondmind.minimal.data.model.Audiobook
import com.secondmind.minimal.data.model.Chapter
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Locale
import java.util.regex.Pattern

/**
 * Extracts text from a PDF and splits it into chapters using a set of heuristics.
 * This uses iText (itextg-5.x for Android).
 */
class PdfExtractor {

    private val chapterHeaderPattern = Pattern.compile(
        "^(Chapter\\s+\\d+|Cap[ií]tulo\\s+\\d+|CHAPTER\\s+\\d+|Parte\\s+\\d+|Parte\\s+I+|Secci[oó]n\\s+\\d+)\\b",
        Pattern.CASE_INSENSITIVE or Pattern.MULTILINE
    )

    fun extractToAudiobook(context: Context, uri: Uri): Audiobook {
        val bytes = readAllBytes(context, uri)
        val reader = PdfReader(bytes)
        val pageCount = reader.numberOfPages

        val pageTexts = (1..pageCount).map { page ->
            PdfTextExtractor.getTextFromPage(reader, page).trim()
        }
        reader.close()

        val allText = pageTexts.joinToString("\n\n---PAGE BREAK---\n\n")
        val titleGuess = guessTitle(pageTexts)
        val authorGuess = guessAuthor(pageTexts)

        val chapters = splitIntoChapters(pageTexts)

        return Audiobook(
            id = "ab_" + System.currentTimeMillis().toString(16),
            title = titleGuess,
            author = authorGuess,
            chapters = chapters
        )
    }

    private fun readAllBytes(context: Context, uri: Uri): ByteArray {
        context.contentResolver.openInputStream(uri).use { input ->
            requireNotNull(input) { "Unable to open input stream for: $uri" }
            val buffer = ByteArrayOutputStream()
            val data = ByteArray(16 * 1024)
            var n: Int
            while (true) {
                n = input.read(data)
                if (n <= 0) break
                buffer.write(data, 0, n)
            }
            return buffer.toByteArray()
        }
    }

    private fun guessTitle(pages: List<String>): String {
        // Heuristic: use the biggest uppercase line in first pages or fallback
        val first = pages.take(3).joinToString("\n")
        val candidates = first.lines().map { it.trim() }
            .filter { it.length in 4..80 && it == it.uppercase(Locale.getDefault()) }
        return candidates.firstOrNull() ?: "Untitled Audiobook"
    }

    private fun guessAuthor(pages: List<String>): String? {
        val first = pages.take(3).joinToString("\n")
        val lower = first.lowercase(Locale.getDefault())
        val markers = listOf("by ", "por ")
        for (m in markers) {
            val idx = lower.indexOf(m)
            if (idx != -1) {
                val rest = first.substring(idx + m.length).lineSequence().firstOrNull()?.trim()
                if (!rest.isNullOrBlank() && rest.length < 80) return rest
            }
        }
        return null
    }

    private fun splitIntoChapters(pageTexts: List<String>): List<Chapter> {
        // Join with page markers to later map back
        val pageJoin = pageTexts.mapIndexed { i, t -> "<<<PAGE:${i+1}>>>\n$t" }.joinToString("\n")
        val matcher = chapterHeaderPattern.matcher(pageJoin)

        val starts = mutableListOf<Int>()
        while (matcher.find()) {
            starts += matcher.start()
        }
        if (starts.isEmpty()) {
            // Fallback: create one chapter
            val all = pageTexts.joinToString("\n\n")
            return listOf(
                Chapter(
                    index = 0,
                    title = "Chapter 1",
                    startPage = 1,
                    endPage = pageTexts.size,
                    text = all
                )
            )
        }

        val chapters = mutableListOf<Chapter>()
        val indices = (starts + pageJoin.length).windowed(2, 1) { (a, b) -> a to b }
        var chapterIdx = 0
        for ((start, end) in indices) {
            val chunk = pageJoin.substring(start, end)
            val titleLine = chunk.lineSequence().firstOrNull()?.trim().takeUnless { it.isNullOrBlank() } ?: "Chapter ${chapterIdx+1}"
            val pagesCovered = Regex(r"<<<PAGE:(\d+?)>>>").findAll(chunk).map { it.groupValues[1].toInt() }.toList()
            val startPage = pagesCovered.minOrNull() ?: 1
            val endPage = pagesCovered.maxOrNull() ?: startPage

            // Remove the <<<PAGE:n>>> markers
            val cleanText = chunk.replace(Regex(r"<<<PAGE:\d+>>>"), "").trim()

            chapters += Chapter(
                index = chapterIdx,
                title = titleLine,
                startPage = startPage,
                endPage = endPage,
                text = cleanText
            )
            chapterIdx++
        }
        return chapters
    }
}
