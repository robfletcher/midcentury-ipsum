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

import ratpack.handlebars.Template
import spock.lang.Specification
import spock.lang.Unroll
import static com.energizedwork.midcenturyipsum.MidcenturyipsumPackage.DEFAULT_PARAGRAPHS
import static io.netty.handler.codec.rtsp.RtspHeaderNames.ACCEPT
import static io.netty.handler.codec.rtsp.RtspHeaderNames.CONTENT_TYPE
import static ratpack.test.handling.RequestFixture.requestFixture

@Unroll
class IpsumHandlerSpec extends Specification {

  def generator = Mock(IpsumGenerator)

  def "by default renders 4 paragraphs"() {
    given:
    def handler = new IpsumHandler(generator)

    when:
    def result = requestFixture().pathBinding(paras: "").handle(handler)

    then:
    1 * generator.paragraphs(DEFAULT_PARAGRAPHS) >> ["lorem ipsum"]

    and:
    with(result) {
      status.code == 200
      bodyText == "lorem ipsum"
    }
  }

  def "renders specified number of paragraphs"() {
    given:
    def handler = new IpsumHandler(generator)

    when:
    def result = requestFixture().pathBinding(paras: "1").handle(handler)

    then:
    1 * generator.paragraphs(1) >> ["lorem ipsum"]

    and:
    with(result) {
      status.code == 200
      bodyText == "lorem ipsum"
    }
  }

  def "renders content type appropriate for accept header of #acceptHeader"() {
    given:
    generator.paragraphs(_) >> ["lorem ipsum"]
    def handler = new IpsumHandler(generator)

    when:
    def result = requestFixture().header(ACCEPT, acceptHeader).handle(handler)

    then:
    with(result) {
      status.code == 200
      headers.get(CONTENT_TYPE) == contentType
      bodyText == responseContent
    }

    where:
    acceptHeader       | contentType        | responseContent
    "application/json" | "application/json" | '["lorem ipsum"]'
    "text/plain"       | "text/plain"       | "lorem ipsum"
  }

  def "renders content type appropriate for accept header of text/html"() {
    given:
    generator.paragraphs(_) >> ["lorem ipsum"]
    def handler = new IpsumHandler(generator)

    when:
    def result = requestFixture().header(ACCEPT, "text/html").handle(handler)

    then:
    with(result) {
      status.code == 200
      headers.get(CONTENT_TYPE) == "text/html"
      with(rendered(Template)) {
        name == "index.html"
        model == [ipsum: ["lorem ipsum"], paras: 4]
      }
    }
  }
}
