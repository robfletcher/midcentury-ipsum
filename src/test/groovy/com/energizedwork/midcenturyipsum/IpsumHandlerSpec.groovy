package com.energizedwork.midcenturyipsum

import org.ratpackframework.groovy.Template
import org.ratpackframework.path.PathBinding
import spock.lang.*
import static io.netty.handler.codec.rtsp.RtspHeaders.Names.*
import static org.ratpackframework.groovy.test.handling.InvocationBuilder.invoke

@Unroll
class IpsumHandlerSpec extends Specification {

	def generator = Mock(IpsumGenerator)
	def pathBinding = Stub(PathBinding)

	void "by default renders 4 paragraphs"() {
		given:
			pathBinding.getTokens() >> [:]
			def handler = new IpsumHandler(generator)

		when:
			def invocation = invoke(handler) {
				register pathBinding
			}

		then:
			1 * generator.paragraphs(_) >> ["lorem ipsum"]

		and:
			with(invocation) {
				status.code == 200
				bodyText == "lorem ipsum"
			}
	}

	void "renders content type appropriate for accept header of #acceptHeader"() {
		given:
			generator.paragraphs(_) >> ["lorem ipsum"]
			pathBinding.getTokens() >> [:]
			def handler = new IpsumHandler(generator)

		when:
			def invocation = invoke(handler) {
				register pathBinding
				requestHeaders.set(ACCEPT, acceptHeader)
			}

		then:
			with(invocation) {
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
			pathBinding.getTokens() >> [:]
			def handler = new IpsumHandler(generator)

		when:
			def invocation = invoke(handler) {
				register pathBinding
				requestHeaders.set(ACCEPT, "text/html")
			}

		then:
			with(invocation) {
				status.code == 200
				headers.get(CONTENT_TYPE) == "text/html;charset=UTF-8"
				with(rendered(Template)) {
					id == "index.html"
					model == [ipsum: "<p>lorem ipsum</p>", paras: 4]
				}
			}
	}
}
