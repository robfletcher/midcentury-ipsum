package com.energizedwork.midcenturyipsum

import groovy.transform.CompileStatic

@CompileStatic
class IpsumGenerator {

	public static final int DEFAULT_PARAGRAPHS = 4
	public static final int SENTENCES_PER_PARAGRAPH = 7
	public static final IntRange WORDS_PER_SENTENCE = 4..8

	private static final List<String> WORD_LIST = IpsumGenerator.getResource("corpus.txt").readLines()

	private final Random randomizer = new Random()

	Collection<String> generateText(int numParas = DEFAULT_PARAGRAPHS) {
		def paras = []
		numParas.times {
			def sentences = []
			SENTENCES_PER_PARAGRAPH.times {
				int numWords = randomInt(WORDS_PER_SENTENCE)
				def words = []
				while (words.size() < numWords) {
					def word = WORD_LIST[randomInt(0..<WORD_LIST.size())]
					if (!words.contains(word)) words << word.toLowerCase()
				}
				sentences << words.join(" ").capitalize()
			}
			paras << sentences.join(". ") + "."
		}
		return paras
	}

	private int randomInt(IntRange range) {
		randomizer.nextInt(range.size()) + range.fromInt
	}
}
