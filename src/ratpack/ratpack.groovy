import com.energizedwork.midcenturyipsum.IpsumGenerator
import com.energizedwork.midcenturyipsum.IpsumGeneratorModule
import org.ratpackframework.groovy.templating.*
import static org.ratpackframework.groovy.RatpackScript.ratpack

ratpack {
    modules {
        register new IpsumGeneratorModule()
        get(TemplatingModule).staticallyCompile = true
    }
    handlers {
        get(":paras?") { TemplateRenderer renderer, IpsumGenerator generator ->
			try {
				int paras = pathTokens.paras?.toInteger() ?: IpsumGenerator.DEFAULT_PARAGRAPHS
				def ipsum = generator.generateText(paras)
				renderer.render "index.html", ipsum: ipsum.collect { "<p>$it</p>" }.join("")
			} catch (NumberFormatException e) {
				clientError 400
			}
        }
        assets "public"
    }
}
