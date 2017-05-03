package com.episode6.hackit.mockspresso;

/**
 * Contains static methods to create new {@link Mockspresso.Builder}s
 */
public class BuildMockspresso {

  /**
   * @return an empty {@link Mockspresso.Builder} with no configuration applied.
   */
  public static Mockspresso.Builder empty() {
    return com.episode6.hackit.mockspresso.internal.MockspressoBuilderImpl.PROVIDER.get();
  }

  /**
   * Start building an instance of Mockspresso designed to create POJOs with no DI.
   * @return a basic instance of {@link Mockspresso.Builder} with the
   * {@link com.episode6.hackit.mockspresso.plugin.simple.SimpleInjectMockspressoPlugin} applied
   */
  public static Mockspresso.Builder simple() {
    return empty()
        .plugin(com.episode6.hackit.mockspresso.plugin.simple.SimpleInjectMockspressoPlugin.getInstance());
  }

  /**
   * Start building an instance of Mockspresso designed to create javax.inject compatible DI objects.
   * @return an instance of {@link Mockspresso.Builder} with the
   * {@link com.episode6.hackit.mockspresso.plugin.javax.JavaxInjectMockspressoPlugin} applied.
   */
  public static Mockspresso.Builder javaxInjection() {
    return empty()
        .plugin(com.episode6.hackit.mockspresso.plugin.javax.JavaxInjectMockspressoPlugin.getInstance());
  }
}
