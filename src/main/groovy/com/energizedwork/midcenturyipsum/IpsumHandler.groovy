package com.energizedwork.midcenturyipsum

import com.google.common.base.Strings
import com.google.inject.Inject
import ratpack.groovy.handling.GroovyContext
import ratpack.groovy.handling.GroovyHandler

import static com.google.common.base.Strings.emptyToNull
import static groovy.json.JsonOutput.toJson
import static ratpack.groovy.Groovy.groovyTemplate

class IpsumHandler extends GroovyHandler {

  public static final int DEFAULT_PARAGRAPHS = 4
  public static final int MAX_PARAGRAPHS = 25

  private final IpsumGenerator generator

  @Inject
  IpsumHandler(IpsumGenerator generator) {
    this.generator = generator
  }

  @Override
  void handle(GroovyContext context) {
    context.with {
      try {
        def paras = emptyToNull(pathTokens.get("paras"))?.toInteger() ?: DEFAULT_PARAGRAPHS
        paras = [paras, MAX_PARAGRAPHS].min()

        def ipsum = generator.paragraphs(paras)

        byContent {
          plainText {
            response.send ipsum.join("\n")
          } json {
            response.send toJson(ipsum)
          } html {
            render groovyTemplate("index.html", ipsum: ipsum.collect { "<p>$it</p>" }.join(""), paras: paras)
          }
        }
      } catch (NumberFormatException ignored) {
        clientError 400
      }
    }
  }

}
