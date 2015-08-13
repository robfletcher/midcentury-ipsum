package com.energizedwork.midcenturyipsum

import groovy.json.JsonOutput.toJson
import ratpack.groovy.Groovy.groovyTemplate
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.path.PathTokens
import java.lang.Math.min
import javax.inject.Inject as inject

class IpsumHandler inject constructor(private val generator: IpsumGenerator) : Handler {

  companion object {
    val DEFAULT_PARAGRAPHS: Int = 4
    val MAX_PARAGRAPHS: Int = 25
  }

  override fun handle(context: Context) {
    try {
      val paras = min(context.getPathTokens().asInt("paras", DEFAULT_PARAGRAPHS), MAX_PARAGRAPHS)
      val ipsum = generator.paragraphs(paras)

      context.byContent {
        it.plainText {
          context.getResponse().send(ipsum.join("\n"))
        } json {
          context.getResponse().send(toJson(ipsum))
        } html {
          context.render(groovyTemplate(mapOf(
            "ipsum" to ipsum.map { "<p>$it</p>" }.join(""),
            "paras" to paras
          ), "index.html"))
        }
      }
    } catch (ignored: NumberFormatException) {
      context.clientError(400)
    }
  }

  fun PathTokens.asInt(key: String, default: Int): Int {
    val value: String? = this.get(key)
    return if (value.isNullOrEmpty()) default else value!!.toInt()
  }
}
