package com.energizedwork.midcenturyipsum

import ratpack.groovy.templating.Template
import spock.lang.Specification
import spock.lang.Unroll

import static com.energizedwork.midcenturyipsum.IpsumHandler.DEFAULT_PARAGRAPHS
import static io.netty.handler.codec.rtsp.RtspHeaders.Names.ACCEPT
import static io.netty.handler.codec.rtsp.RtspHeaders.Names.CONTENT_TYPE
import static ratpack.test.UnitTest.requestFixture

@Unroll
class IpsumHandlerSpec extends Specification {

  def generator = Mock(IpsumGenerator)

  void "by default renders 4 paragraphs"() {
    given:
    def handler = new IpsumHandler(generator)

    when:
    def result = requestFixture().handle(handler)

    then:
    1 * generator.paragraphs(DEFAULT_PARAGRAPHS) >> ["lorem ipsum"]

    and:
    with(result) {
      status.code == 200
      bodyText == "lorem ipsum"
    }
  }

  void "renders specified number of paragraphs"() {
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

  void "renders content type appropriate for accept header of #acceptHeader"() {
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
    acceptHeader       | contentType                | responseContent
    "application/json" | "application/json"         | '["lorem ipsum"]'
    "text/plain"       | "text/plain;charset=UTF-8" | "lorem ipsum"
  }

  void "renders content type appropriate for accept header of text/html"() {
    given:
    generator.paragraphs(_) >> ["lorem ipsum"]
    def handler = new IpsumHandler(generator)

    when:
    def result = requestFixture().header(ACCEPT, "text/html").handle(handler)

    then:
    with(result) {
      status.code == 200
      headers.get(CONTENT_TYPE) == "text/html;charset=UTF-8"
      with(rendered(Template)) {
        id == "index.html"
        model == [ipsum: "<p>lorem ipsum</p>", paras: 4]
      }
    }
  }
}
