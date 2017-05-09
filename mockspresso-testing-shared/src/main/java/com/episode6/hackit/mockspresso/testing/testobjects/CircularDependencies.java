package com.episode6.hackit.mockspresso.testing.testobjects;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * A depends on B
 * B depends on C
 * C depends on A
 */
public class CircularDependencies {

  public static class A {
    public @Inject B mB;
  }

  public static class B {
    public @Inject C mC;
  }

  public static class C {
    public @Inject A mA;
  }

  public static class AProvider {
    public @Inject Provider<BProvider> mB;
  }

  public static class BProvider {
    public @Inject Provider<CProvider> mC;
  }

  public static class CProvider {
    public @Inject Provider<AProvider> mA;
  }
}
