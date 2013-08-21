import com.energizedwork.midcenturyipsum.*
import org.ratpackframework.groovy.templating.TemplatingModule
import static org.ratpackframework.groovy.RatpackScript.ratpack

ratpack {
	modules {
		register new IpsumGeneratorModule()
		get(TemplatingModule).staticallyCompile = true
	}
	handlers {
		get ":paras?", registry.get(IpsumHandler)
		assets "public"
	}
}
