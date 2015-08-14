package com.energizedwork.midcenturyipsum

import ratpack.registry.Registry
import ratpack.server.RatpackServer
import ratpack.server.internal.ServerCapturer.Overrides
import ratpack.server.internal.ServerCapturer.capture
import ratpack.test.ServerBackedApplicationUnderTest
import java.lang.reflect.InvocationTargetException

open class MainFunctionApplicationUnderTest(val mainFun: (Array<String>) -> Unit) : ServerBackedApplicationUnderTest() {
  protected open fun createOverrides(serverRegistry: Registry): Registry = Registry.empty()

  override fun createServer(): RatpackServer =
    capture(
      Overrides()
        .port(0)
        .development(true)
        .registry { registry -> createOverrides(registry) }
    ) {
      try {
        mainFun(arrayOf<String>())
      } catch (e: IllegalAccessException) {
        throw IllegalStateException("Could not invoke $mainFun", e)
      } catch (e: IllegalArgumentException) {
        throw IllegalStateException("Could not invoke $mainFun", e)
      } catch (e: InvocationTargetException) {
        throw IllegalStateException("Could not invoke $mainFun", e)
      }
    } orElseThrow { ->
      IllegalStateException("$mainFun did not start a Ratpack server")
    }
}