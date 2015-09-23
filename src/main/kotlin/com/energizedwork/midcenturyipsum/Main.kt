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

import asset.pipeline.ratpack.AssetPipelineModule
import org.slf4j.LoggerFactory.getLogger
import ratpack.guice.Guice
import ratpack.handlebars.HandlebarsModule
import ratpack.handling.Chain
import ratpack.server.BaseDir
import ratpack.server.RatpackServer

val DEFAULT_PARAGRAPHS: Int = 4
val MAX_PARAGRAPHS: Int = 25

object Main {
  private val log = getLogger(Main::class.java)

  @JvmStatic
  fun main(args: Array<String>): Unit {
    try {
      RatpackServer.of { server ->
        server
          .serverConfig { config ->
            config.baseDir(BaseDir.find())
          }
          .registry(Guice.registry { bindingSpec ->
            bindingSpec
              .module(IpsumGeneratorModule::class.java)
              .module(HandlebarsModule::class.java)
              .module(AssetPipelineModule::class.java)
          })
          .handlers { chain: Chain ->
            chain.get(":paras?", IpsumHandler::class.java)
          }
      }.start()
    } catch (e: Exception) {
      log.error("", e)
    }
  }
}
