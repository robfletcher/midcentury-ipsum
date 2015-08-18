/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Robert Fletcher
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.energizedwork.midcenturyipsum

import java.util.Random
import javax.inject.Singleton as singleton

singleton class MidCenturyIpsumGenerator : IpsumGenerator {

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
