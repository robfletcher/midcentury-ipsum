/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Robert Fletcher
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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