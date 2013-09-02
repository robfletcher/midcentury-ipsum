package com.energizedwork.midcenturyipsum

import com.google.inject.Inject
import org.ratpackframework.handling.*
import static groovy.json.JsonOutput.toJson
import static org.ratpackframework.groovy.Template.groovyTemplate

class IpsumHandler implements Handler {

	public static final int DEFAULT_PARAGRAPHS = 4

	private final IpsumGenerator generator

	@Inject
	IpsumHandler(IpsumGenerator generator) {
		this.generator = generator
	}

	@Override
	void handle(Context context) {
		try {
			context.with {
				int paras = pathTokens.asInt("paras") ?: DEFAULT_PARAGRAPHS
				paras = Math.min(paras, 25)
				def ipsum = generator.paragraphs(paras)

				respond byContent.plainText {
					response.send ipsum.join("\n")
				}
				.json {
					response.send toJson(ipsum)
				}
				.html {
					render groovyTemplate("index.html", ipsum: ipsum.collect { "<p>$it</p>" }.join(""), paras: paras)
				}
			}
		} catch (NumberFormatException e) {
			context.clientError 400
		}
	}

}
