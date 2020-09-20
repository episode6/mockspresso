package com.episode6.hackit.mockspresso.mockito.integration

import org.junit.After
import org.junit.Before

internal class LifecycleTestResources {
  var isSetup: Boolean = false
  var isTornDown: Boolean = false

  @Before
  fun setup() {
    isSetup = true
  }

  @After
  fun teardown() {
    isTornDown = true
  }
}
