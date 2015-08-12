package com.energizedwork.midcenturyipsum

import ratpack.groovy.template.TextTemplateModule
import ratpack.guice.Guice
import ratpack.handling.Chain
import ratpack.server.BaseDir
import ratpack.server.RatpackServer

fun main(args: Array<String>) {
  RatpackServer.start { server ->
    server
      .serverConfig { config ->
        config
          .baseDir(BaseDir.find())
      }
      .registry(Guice.registry { bindingSpec ->
        bindingSpec
          .module(javaClass<IpsumGeneratorModule>())
          .module(javaClass<TextTemplateModule>())
      })
      .handlers { chain: Chain ->
        val ipsumGenerator = chain.getRegistry().get(javaClass<IpsumGenerator>())
        chain
          .get(":paras?", IpsumHandler(ipsumGenerator))
          .fileSystem("public") { f ->
            f.files()
          }
      }
  }
}
