import com.energizedwork.midcenturyipsum.*
import org.ratpackframework.groovy.templating.*
import static groovy.json.JsonOutput.toJson
import static org.ratpackframework.groovy.RatpackScript.ratpack
import static com.energizedwork.midcenturyipsum.IpsumGenerator.DEFAULT_PARAGRAPHS

ratpack {
	modules {
		register new IpsumGeneratorModule()
		get(TemplatingModule).staticallyCompile = true
	}
	handlers {
		get(":paras?") { TemplateRenderer renderer, IpsumGenerator generator ->
			try {
				int paras = pathTokens.paras?.toInteger() ?: DEFAULT_PARAGRAPHS
				def ipsum = generator.generateText(paras)
				accepts.type("text/plain") {
					response.send ipsum.join("\n")
				}
				.type("application/json") {
					response.send toJson(ipsum)
				}
				.type("text/html") {
					renderer.render "index.html", ipsum: ipsum.collect { "<p>$it</p>" }.join("")
				}
				.send()
			} catch (NumberFormatException e) {
				clientError 400
			}
		}
		assets "public"
	}
}
