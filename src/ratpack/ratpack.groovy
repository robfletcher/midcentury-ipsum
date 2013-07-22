import org.ratpackframework.groovy.templating.*
import static org.ratpackframework.groovy.RatpackScript.ratpack

ratpack {
	modules {
		get(TemplatingModule).staticallyCompile = true
	}
	handlers {
		get {
			get(TemplateRenderer).render "index.html", title: "Mid-Century Ipsum"
		}
		assets "public"
	}
}
