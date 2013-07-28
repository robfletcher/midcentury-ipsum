package com.energizedwork.midcenturyipsum

import com.google.inject.AbstractModule

class IpsumGeneratorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IpsumGenerator)
    }

}
