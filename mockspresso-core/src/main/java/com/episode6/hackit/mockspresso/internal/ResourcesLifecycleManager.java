package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Logic to initialize and tear-down the last mile of a mockspresso config.
 * A list of lifecycle methods is created by the builder including ones for
 * field scanning/injection and for lifecycle method detection/calling.
 *
 * Order is not determined by this class, but this class guarantees that it will
 * make teardown calls in the inverted order it does setup calls
 */
class ResourcesLifecycleManager implements ResourcesLifecycleComponent {
  private final List<ResourcesLifecycleComponent> mLifecycleComponents;

  ResourcesLifecycleManager(List<ResourcesLifecycleComponent> lifecycleComponents) {
    mLifecycleComponents = new LinkedList<>(lifecycleComponents);
  }

  @Override
  public void setup(Mockspresso mockspresso) {
    for (ResourcesLifecycleComponent component : mLifecycleComponents) {
      component.setup(mockspresso);
    }
  }

  @Override
  public void teardown() {
    ListIterator<ResourcesLifecycleComponent> iterator = mLifecycleComponents.listIterator(mLifecycleComponents.size());
    while (iterator.hasPrevious()) {
      ResourcesLifecycleComponent component = iterator.previous();
      component.teardown();
    }
  }
}
