package com.energizedwork.midcenturyipsum

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.jetbrains.spek.api.Spek
import ratpack.http.client.ReceivedResponse
import ratpack.test.http.TestHttpClient
import ratpack.test.http.TestHttpClient.testHttpClient
import kotlin.test.assertEquals

class EndToEndSpecs : Spek() {
  companion object {
    val OK = 200
    val BAD_REQUEST = 400
  }

  init {
    given("the application is running") {
      val app = MainFunctionApplicationUnderTest(::main)
      val client = testHttpClient(app)

      afterOn { app.stop() }

      on("a get request to /") {
        val response = client.get()

        it("should return OK") {
          assertEquals(OK, response.getStatusCode())
        }

        it("should return plain text") {
          assertEquals("text/plain", response.getContentType())
        }

        it("should return 4 paragraphs") {
          assertEquals(4, response.asText().splitBy("\n").size())
        }
      }

      on("a get request to / with Accept text/html") {
        val response = client.accept("text/html").get()

        it("should return OK") {
          assertEquals(OK, response.getStatusCode())
        }

        it("should return HTML") {
          assertEquals("text/html", response.getContentType())
        }

        it("should return 4 paragraphs") {
          assertEquals(4, response.asText().countMatches("<p>.*</p>"))
        }
      }

      on("a get request to / with Accept application/json") {
        val response = client.accept("application/json").get()

        it("should return OK") {
          assertEquals(OK, response.getStatusCode())
        }

        it("should return JSON") {
          assertEquals("application/json", response.getContentType())
        }

        it("should return 4 paragraphs") {
          assertEquals(4, response.asJson().size())
        }
      }

      on("a get request with an invalid parameter") {
        val response = client.get("3daft")

        it("should return Bad Request") {
          assertEquals(BAD_REQUEST, response.getStatusCode())
        }
      }

      for (i in 1..10) {
        on("a request for $i paragraphs") {
          val response = client.accept("application/json").get("$i")

          it("should return OK") {
            assertEquals(OK, response.getStatusCode())
          }

          it("should return $i paragraphs") {
            assertEquals(i, response.asJson().size())
          }
        }
      }

      on("a request for too many paragraphs") {
        val response = client.accept("application/json").get("26")

        it("should return OK") {
          assertEquals(OK, response.getStatusCode())
        }

        it("should return the maximum number of paragraphs") {
          assertEquals(25, response.asJson().size())
        }
      }
    }
  }

  fun ReceivedResponse.getContentType(): String = getHeaders().get("Content-Type")

  fun TestHttpClient.accept(mimeType: String): TestHttpClient = requestSpec {
    it.getHeaders().add("Accept", mimeType)
  }

  fun ReceivedResponse.asJson(): JsonNode {
    return ObjectMapper().readTree(getBody().getBytes())
  }

  fun ReceivedResponse.asText(): String {
    return getBody().getText()
  }

  fun String.countMatches(regex: String): Int = regex.toRegex().matchAll(this).count()
}
