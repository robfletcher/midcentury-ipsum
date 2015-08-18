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

import groovy.json.JsonOutput.toJson
import ratpack.handlebars.Template.handlebarsTemplate
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.path.PathTokens
import java.lang.Math.min
import javax.inject.Inject as inject
import javax.inject.Singleton as singleton

singleton class IpsumHandler
inject constructor(private val generator: IpsumGenerator)
: Handler {

  override fun handle(context: Context) {
    try {
      val paras = min(
        context.getPathTokens().asInt("paras", DEFAULT_PARAGRAPHS),
        MAX_PARAGRAPHS)
      val ipsum = generator.paragraphs(paras)

      context.byContent {
        it.plainText {
          context.getResponse().send(ipsum.join("\n"))
        } json {
          context.getResponse().send(toJson(ipsum))
        } html {
          context.render(
            handlebarsTemplate("index.html",
                               mapOf("ipsum" to ipsum, "paras" to paras)))
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
