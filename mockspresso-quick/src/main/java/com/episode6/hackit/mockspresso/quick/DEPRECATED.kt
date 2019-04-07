package com.episode6.hackit.mockspresso.quick

const val QUICK_DEPRECATION_MESSAGE = """
  The :mockspresso-quick module is now deprecated and slated to be removed in a future release.

  Kotlin usage is now the primary focus of mockspresso, and all the functionality here
  can now be found in kotlin extension methods in each plugin's module.

  Java users can still access all of this functionality via the *PluginsJavaSupport classes
  also available in each module.
"""
