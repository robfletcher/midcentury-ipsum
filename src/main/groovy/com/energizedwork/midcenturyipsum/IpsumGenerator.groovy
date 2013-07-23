package com.energizedwork.midcenturyipsum

import groovy.transform.CompileStatic

@CompileStatic
class IpsumGenerator {

	public static final int DEFAULT_PARAGRAPHS = 4
	public static final int SENTENCES_PER_PARAGRAPH = 7
	public static final IntRange WORDS_PER_SENTENCE = 4..8

	private static final List<String> WORD_LIST = [
			"abstract",
			"acetate",
			"art department",
			"Atompunk",
			"Bakelite",
			"beatnik",
			"bourbon",
			"bowling alley",
			"broad",
			"Brylcreem",
			"candy-ass",
			"carport",
			"cocktail",
			"copasetic",
			"creative",
			"Dieter Rams",
			"diner",
			"doo-wop",
			"drive-in",
			"Eames chair",
			"executive",
			"floating staircase",
			"Formica",
			"Frutiger",
			"geometric",
			"googie",
			"Helvetica",
			"Herman Miller",
			"highway",
			"hostess trolley",
			"jazz",
			"jet age",
			"Jetsons",
			"keen",
			"mid-century",
			"modern",
			"Palm Springs",
			"Pan Am",
			"pocket square",
			"populuxe",
			"ring-a-ding",
			"roadside",
			"rye",
			"Saul Bass",
			"skinny lapel",
			"sleek",
			"son of a bitch",
			"space age",
			"starburst",
			"suburban",
			"swingin'",
			"tailfin",
			"tiki",
			"Tomorrowland",
			"upswept",
			"whiskey",
			"zephyr"
	]

	private final Random randomizer = new Random()

	String generateText(int numParas = DEFAULT_PARAGRAPHS) {
		def paras = []
		numParas.times {
			def sentences = []
			SENTENCES_PER_PARAGRAPH.times {
				int numWords = randomInt(WORDS_PER_SENTENCE)
				def words = []
				numWords.times {
					words << WORD_LIST[randomInt(0..<WORD_LIST.size())]
				}
				sentences << words.join(" ").capitalize()
			}
			paras << sentences.join(". ") + "."
		}

		def buffer = new StringBuffer()
		for (para in paras) {
			buffer << "<p>" << para << "</p>"
		}
		println "ipsum..."
		println buffer as String
		return buffer as String
	}

	private int randomInt(IntRange range) {
		randomizer.nextInt(range.size()) + range.fromInt
	}
}
