package com.energizedwork.midcenturyipsum

import java.util.Random

class MidCenturyIpsumGenerator : IpsumGenerator {

  companion object {
    val SENTENCES_PER_PARAGRAPH: IntRange = 2..8
    val WORDS_PER_SENTENCE: IntRange = 3..9
    val WORD_LIST: List<String> = javaClass<MidCenturyIpsumGenerator>()
      .getResource("corpus.txt")
      .readText()
      .splitBy("\n")
    val PUNCTUATION: List<String> = listOf(".", ".", "?", "!")
    val randomizer = Random()
  }

  override fun paragraphs(count: Int): Collection<String> {
    return (1..count).map { paragraph() }
  }

  fun paragraph(): String {
    return sentences(SENTENCES_PER_PARAGRAPH.random())
  }

  fun sentences(count: Int): String {
    return (1..count).map { sentence() }.join(" ")
  }

  fun sentence(): String {
    val word = word().capitalize()
    val buffer = StringBuilder(word).append(" ")

    if (randomizer.nextBoolean()) {
      (1..2).random().repeat {
        buffer.append(sentenceFragment()).append(", ")
      }
    }

    buffer.append(sentenceFragment()).append(punctuation())
    return buffer.toString()
  }

  fun sentenceFragment(): String {
    return words(WORDS_PER_SENTENCE.random())
  }

  fun words(count: Int): String {
    return sequence { word() } distinctBy { it } take(count) join(" ")
  }

  fun punctuation(): String {
    return PUNCTUATION.random()
  }

  fun word(): String {
    return WORD_LIST.random().toLowerCase()
  }

  fun Int.repeat(fn: () -> Unit): Unit {
    for (i in 1..this) {
      fn()
    }
  }

  fun List<Any?>.range(): IntRange = 0..size()
  fun <T> List<T>.random(): T = get(range().random())

  fun IntRange.random(): Int = randomizer.nextInt(end - start) + start
}
