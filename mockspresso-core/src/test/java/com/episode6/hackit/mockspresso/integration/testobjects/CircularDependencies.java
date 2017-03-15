package com.episode6.hackit.mockspresso.integration.testobjects;

import javax.inject.Inject;

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
}
