package com.energizedwork.midcenturyipsum

import com.google.inject.Inject
import groovy.transform.CompileStatic
import org.ratpackframework.handling.*
import static groovy.json.JsonOutput.toJson
import static org.ratpackframework.groovy.Template.groovyTemplate

@CompileStatic
class IpsumHandler implements Handler {

	public static final int DEFAULT_PARAGRAPHS = 4

	private final IpsumGenerator generator

	@Inject
	IpsumHandler(IpsumGenerator generator) {
		this.generator = generator
	}

	@Override
	void handle(Context context) {
		context.with {
			int paras = getPathTokens().paras?.toInteger() ?: DEFAULT_PARAGRAPHS
			paras = Math.min(paras, 25)
			def ipsum = generator.paragraphs(paras)

			respond getByContent().plainText {
				getResponse().send ipsum.join("\n")
			}
			.json {
				getResponse().send toJson(ipsum)
			}
			.html {
				render groovyTemplate("index.html", ipsum: ipsum.collect { "<p>$it</p>" }.join(""), paras: paras)
			}
		}
	}

}
