package app

import groovy.json.JsonSlurper
import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.http.client.RequestSpec
import ratpack.test.http.TestHttpClient
import spock.lang.Specification
import static com.energizedwork.midcenturyipsum.IpsumHandler.DEFAULT_PARAGRAPHS

class FunctionalSpec extends Specification {

  def aut = new GroovyRatpackMainApplicationUnderTest()
  @Delegate TestHttpClient client = testHttpClient(aut)
  def json = new JsonSlurper()

  def setup() {
    client.resetRequest()
  }

  def "root default paragraphs"() {
    when:
    get()

    then:
    response.statusCode == 200
    response.headers.get("Content-Type") == "text/plain"
    response.body.text.findAll("\n").size() == (DEFAULT_PARAGRAPHS - 1) //Subtract one from the default as the final line in plain doesn't have a new line ending
  }

  def "root default paragraphs text/html"() {
    when:
    requestSpec { RequestSpec request ->
      request.headers.add("Accept", "text/html")
    }
    get()

    then:
    response.statusCode == 200
    response.headers.get("Content-Type") == "text/html"
    response.body.text.findAll("<p>.*</p>").size() == DEFAULT_PARAGRAPHS
  }

  def "root default paragraphs application/json"() {
    when:
    requestSpec { RequestSpec request ->
      request.headers.add("Accept", "application/json")
    }
    get()

    then:
    response.statusCode == 200
    response.headers.get("Content-Type") == "application/json"
    json.parseText(response.body.text).size() == DEFAULT_PARAGRAPHS
  }

  def "Bad Param"() {
    when:
    get("3daft")

    then:
    response.statusCode == 400
  }

  def "Change number of Paragraphs"() {
    when:
    requestSpec { RequestSpec request ->
      request.headers.add("Accept", "application/json")
    }
    get("9")

    then:
    response.statusCode == 200
    json.parseText(response.body.text).size() == 9
  }

  def "Test Max Paragraphs"() {
    when:
    requestSpec { RequestSpec request ->
      request.headers.add("Accept", "application/json")
    }
    get("100")

    then:
    response.statusCode == 200
    json.parseText(response.body.text).size() == 25
  }

  def cleanup() {
    aut.stop()
  }
}
