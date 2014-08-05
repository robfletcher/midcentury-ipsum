import com.energizedwork.midcenturyipsum.IpsumGeneratorModule
import com.energizedwork.midcenturyipsum.IpsumHandler
import ratpack.groovy.templating.TemplatingModule

import static ratpack.groovy.Groovy.ratpack

ratpack {
  bindings {
    add new IpsumGeneratorModule()
    add new TemplatingModule(staticallyCompile: true)
  }
  handlers {
    handler ":paras?", registry.get(IpsumHandler)
    assets "public"
  }
}
