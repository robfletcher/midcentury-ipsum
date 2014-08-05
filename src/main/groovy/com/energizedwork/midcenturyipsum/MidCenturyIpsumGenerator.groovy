package com.energizedwork.midcenturyipsum

import groovy.transform.CompileStatic

@CompileStatic
class MidCenturyIpsumGenerator implements IpsumGenerator {

  public static final IntRange SENTENCES_PER_PARAGRAPH = 2..8
  public static final IntRange WORDS_PER_SENTENCE = 3..9

  private static final List<String> WORD_LIST = MidCenturyIpsumGenerator.getResource("corpus.txt").readLines()
  private static final List<String> PUNCTUATION = [".", ".", "?", "!"]

  private final Random randomizer = new Random()

  final Collection<String> paragraphs(int count) {
    def paragraphs = []
    count.times {
      paragraphs << paragraph()
    }
    return paragraphs
  }

  final String paragraph() {
    sentences randomInt(SENTENCES_PER_PARAGRAPH)
  }

  final String sentences(int count) {
    def buffer = new StringBuilder()
    count.times {
      buffer << sentence() << " "
    }
    return buffer.toString().trim()
  }

  final String sentence() {
    def word = randomWord().capitalize()
    def buffer = new StringBuilder(word) << " "

    if (randomizer.nextBoolean()) {
      randomInt(1..2).times {
        buffer << sentenceFragment() << ", "
      }
    }

    buffer << sentenceFragment() << randomPunctuation()
    return buffer as String
  }

  final String sentenceFragment() {
    words randomInt(WORDS_PER_SENTENCE)
  }

  final String words(int count) {
    def words = []
    while (words.size() < count) {
      def word = randomWord()
      if (!words.contains(word)) {
        words << word
      }
    }
    return words.join(" ")
  }

  final String randomPunctuation() {
    randomElement PUNCTUATION
  }

  final String randomWord() {
    randomElement(WORD_LIST).toLowerCase()
  }

  private String randomElement(List<String> strings) {
    strings[randomInt(0..<strings.size())]
  }

  private int randomInt(IntRange range) {
    randomizer.nextInt(range.size()) + range.fromInt
  }
}
