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
        get { TemplateRenderer renderer, IpsumGenerator generator ->
            renderer.render "index.html", ipsum: generator.generateText()
        }
        assets "public"
    }
}
