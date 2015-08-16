package com.energizedwork.midcenturyipsum

import com.google.inject.AbstractModule

class IpsumGeneratorModule : AbstractModule() {

  protected override fun configure(): Unit {
    bind(javaClass<IpsumGenerator>()).to(javaClass<MidCenturyIpsumGenerator>())
    bind(javaClass<IpsumHandler>())
  }

}
