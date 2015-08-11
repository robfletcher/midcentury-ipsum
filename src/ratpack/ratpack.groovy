import com.energizedwork.midcenturyipsum.IpsumGenerator
import com.energizedwork.midcenturyipsum.IpsumGeneratorModule
import com.energizedwork.midcenturyipsum.IpsumHandler
import ratpack.groovy.template.TextTemplateModule
import static ratpack.groovy.Groovy.ratpack

ratpack {
  bindings {
    module IpsumGeneratorModule
    module TextTemplateModule
  }
  handlers { IpsumGenerator ipsumGenerator ->
    get ":paras?", new IpsumHandler(ipsumGenerator)
    fileSystem("public") { f ->
      f.files()
    }
  }
}
