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
