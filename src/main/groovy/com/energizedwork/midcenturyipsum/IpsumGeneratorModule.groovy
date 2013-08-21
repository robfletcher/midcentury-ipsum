package com.energizedwork.midcenturyipsum

import com.google.inject.AbstractModule
import groovy.transform.CompileStatic

@CompileStatic
class IpsumGeneratorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MidCenturyIpsumGenerator)
    }

}
