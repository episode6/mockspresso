package com.episode6.hackit.mockspresso.internal;

/**
 * Holds on to an object that acts as a test resource, as well as information about it.
 */
class TestResource {
  private final Object mObjectWithResources;
  private final boolean mLifecycle;

  TestResource(Object objectWithResources, boolean lifecycle) {
    mObjectWithResources = objectWithResources;
    mLifecycle = lifecycle;
  }

  public Object getObjectWithResources() {
    return mObjectWithResources;
  }

  public boolean isLifecycle() {
    return mLifecycle;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TestResource that = (TestResource) o;

    return mObjectWithResources.equals(that.mObjectWithResources);
  }

  @Override
  public int hashCode() {
    return mObjectWithResources.hashCode();
  }
}
