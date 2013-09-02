package app


import org.ratpackframework.groovy.test.LocalScriptApplicationUnderTest
import org.ratpackframework.groovy.test.TestHttpClient
import spock.lang.Specification


import static com.energizedwork.midcenturyipsum.IpsumHandler.DEFAULT_PARAGRAPHS

class FunctionalSpec extends Specification {

	def aut = new LocalScriptApplicationUnderTest()
	@Delegate TestHttpClient client = aut.httpClient()

	def setup(){
		client.resetRequest()
	}

	def "root default paragraphs"() {
		when:
		get()

		then:
		response.statusCode == 200
		response.header("Content-Type") == "text/plain;charset=UTF-8"
		response.body.asString().findAll("\n").size() == (DEFAULT_PARAGRAPHS - 1) //Subtract one from the default as the final line in plain doesn't have a new line ending
	}

	def "root default paragraphs text/html"() {
		when:
		request.header("Accept", "text/html")
		get()

		then:
		response.statusCode == 200
		response.header("Content-Type") == "text/html;charset=UTF-8"
		response.body.asString().findAll("<p>.*</p>").size() == DEFAULT_PARAGRAPHS
	}

	def "root default paragraphs application/json"() {
		when:
		request.header("Accept", "application/json")
		get()

		then:
		response.statusCode == 200
		response.header("Content-Type") == "application/json"
		response.jsonPath().getList("").size() == DEFAULT_PARAGRAPHS
	}

	def "Bad Param"() {
		when:
		get("3daft")

		then:
		response.statusCode == 400
	}

	def "Change number of Paragraphs"(){
		when:
		request.header("Accept", "application/json")
		get("9")

		then:
		response.statusCode == 200
		response.jsonPath().getList("").size() == 9
	}

	def "Test Max Paragraphs" () {
		when:
		request.header("Accept", "application/json")
		get("100")

		then:
		response.statusCode == 200
		response.jsonPath().getList("").size() == 25
	}

	def cleanup() {
		aut.stop()
	}
}
