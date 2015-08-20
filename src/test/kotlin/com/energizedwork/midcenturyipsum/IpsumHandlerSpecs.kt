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

import io.netty.handler.codec.rtsp.RtspHeaderNames.ACCEPT
import io.netty.handler.codec.rtsp.RtspHeaderNames.CONTENT_TYPE
import org.jetbrains.spek.api.Spek
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.internal.stubbing.answers.Returns
import ratpack.handlebars.Template
import ratpack.test.handling.RequestFixture.requestFixture
import java.util.Objects
import kotlin.test.assertEquals

class IpsumHandlerSpecs : Spek() {
  companion object {
    val OK = 200
    val STUB_RESPONSE = "lorem ipsum"
  }

  init {
    given("a handler instance") {
      on("receiving a request with no 'paras' key") {
        val generator = mock(javaClass<IpsumGenerator>(),
                             Returns(listOf(STUB_RESPONSE)))
        val handler = IpsumHandler(generator)
        val result = requestFixture()
          .pathBinding(mapOf("paras" to ""))
          .handle(handler)

        it("responds successfully") {
          assertEquals(OK, result.getStatus().getCode())
        }

        it("renders 4 paragraphs by default") {
          println("verifying ${Objects.hashCode(generator)}")
          verify(generator).paragraphs(DEFAULT_PARAGRAPHS)
        }
      }

      on("receiving a request with a 'paras' key") {
        val generator = mock(javaClass<IpsumGenerator>(),
                             Returns(listOf(STUB_RESPONSE)))
        val handler = IpsumHandler(generator)
        val result = requestFixture()
          .pathBinding(mapOf("paras" to "1"))
          .handle(handler)

        it("responds successfully") {
          assertEquals(OK, result.getStatus().getCode())
        }

        it("renders the requested number of paragraphs") {
          verify(generator).paragraphs(1)
        }
      }

      for ((mimeType, text) in mapOf(
        "application/json" to "[\"$STUB_RESPONSE\"]",
        "text/plain" to STUB_RESPONSE
      )) {
        on("receiving a request with an 'Accept' header of '$mimeType'") {
          val generator = mock(javaClass<IpsumGenerator>(),
                               Returns(listOf(STUB_RESPONSE)))
          val handler = IpsumHandler(generator)
          val result = requestFixture()
            .header(ACCEPT, mimeType)
            .handle(handler)

          it("responds successfully") {
            assertEquals(OK, result.getStatus().getCode())
          }

          it("renders the requested content type") {
            assertEquals(mimeType, result.getHeaders().get(CONTENT_TYPE))
          }

          it("renders content in the correct format") {
            assertEquals(text, result.getBodyText())
          }
        }
      }

      on("receiving a request with an 'Accept' header of 'text/html'") {
        val generator = mock(javaClass<IpsumGenerator>(),
                             Returns(listOf(STUB_RESPONSE)))
        val handler = IpsumHandler(generator)
        val result = requestFixture()
          .header(ACCEPT, "text/html")
          .handle(handler)

        it("responds successfully") {
          assertEquals(OK, result.getStatus().getCode())
        }

        it("renders the requested content type") {
          assertEquals("text/html", result.getHeaders().get(CONTENT_TYPE))
        }

        it("renders an HTML template") {
          val template = result.rendered(javaClass<Template>())
          assertEquals("index.html", template.getName())
          assertEquals(
            mapOf("ipsum" to listOf(STUB_RESPONSE), "paras" to 4),
            template.getModel())
        }
      }
    }
  }
}
