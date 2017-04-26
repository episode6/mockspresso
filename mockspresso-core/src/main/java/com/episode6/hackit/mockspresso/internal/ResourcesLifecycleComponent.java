package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;

/**
 *
 */
interface ResourcesLifecycleComponent {
  void setup(Mockspresso mockspresso);
  void teardown();
}
