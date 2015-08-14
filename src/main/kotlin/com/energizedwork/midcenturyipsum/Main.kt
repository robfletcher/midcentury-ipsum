package com.energizedwork.midcenturyipsum

import asset.pipeline.ratpack.AssetPipelineModule
import ratpack.groovy.template.TextTemplateModule
import ratpack.guice.Guice
import ratpack.handling.Chain
import ratpack.server.BaseDir
import ratpack.server.RatpackServer

val DEFAULT_PARAGRAPHS: Int = 4
val MAX_PARAGRAPHS: Int = 25

fun main(args: Array<String>) {
  RatpackServer.start { server ->
    server
      .serverConfig { config ->
        config.baseDir(BaseDir.find())
      }
      .registry(Guice.registry { bindingSpec ->
        bindingSpec
          .module(javaClass<IpsumGeneratorModule>())
          .module(javaClass<TextTemplateModule>())
          .module(javaClass<AssetPipelineModule>())
      })
      .handlers { chain: Chain ->
        val ipsumGenerator = chain
          .getRegistry()
          .get(javaClass<IpsumGenerator>())
        chain.get(":paras?", IpsumHandler(ipsumGenerator))
      }
  }
}
