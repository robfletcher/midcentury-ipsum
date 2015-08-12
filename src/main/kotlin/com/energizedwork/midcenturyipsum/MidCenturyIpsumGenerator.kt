package com.energizedwork.midcenturyipsum

import java.util.Random

class MidCenturyIpsumGenerator : IpsumGenerator {

  companion object {
    public val SENTENCES_PER_PARAGRAPH: IntRange = 2..8
    public val WORDS_PER_SENTENCE: IntRange = 3..9

    private val WORD_LIST: List<String> = javaClass<MidCenturyIpsumGenerator>()
        .getResource("corpus.txt")
        .readText()
        .splitBy("\n")
    private val PUNCTUATION: List<String> = arrayListOf(".", ".", "?", "!")
  }

  private val randomizer = Random()

  override fun paragraphs(count: Int): Collection<String> {
    return (1..count).map { paragraph() }
  }

  fun paragraph(): String {
    return sentences(randomInt(SENTENCES_PER_PARAGRAPH))
  }

  fun sentences(count: Int): String {
    return (1..count).map { sentence() }.join(" ")
  }

  fun sentence(): String {
    val word = randomWord().capitalize()
    val buffer = StringBuilder(word).append(" ")

    if (randomizer.nextBoolean()) {
      for (i in 1..randomInt(1..2)) {
        buffer.append(sentenceFragment()).append(", ")
      }
    }

    buffer.append(sentenceFragment()).append(randomPunctuation())
    return buffer.toString()
  }

  fun sentenceFragment(): String {
    return words(randomInt(WORDS_PER_SENTENCE))
  }

  fun words(count: Int): String {
    val words: MutableList<String> = arrayListOf()
    while (words.size() < count) {
      val word = randomWord()
      if (!words.contains(word)) {
        words.add(word)
      }
    }
    return words.join(" ")
  }

  fun randomPunctuation(): String {
    return randomElement(PUNCTUATION)
  }

  fun randomWord(): String {
    return randomElement(WORD_LIST).toLowerCase()
  }

  fun randomElement(strings: List<String>): String {
    return strings[randomInt(0..strings.size())]
  }

  fun randomInt(range: IntRange): Int {
    return randomizer.nextInt(range.end - range.start) + range.start
  }
}
