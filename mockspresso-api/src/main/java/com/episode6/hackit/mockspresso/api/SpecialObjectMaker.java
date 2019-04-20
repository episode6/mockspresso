package com.episode6.hackit.mockspresso.api;

import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A class that creates "special" objects, i.e. objects that, by default, should not
 * be simply mocked. An example of this is {@link javax.inject.Provider}, where we'd
 * rather return a real instance that provides a mock (or a mapped dependency).
 */
public interface SpecialObjectMaker {
  boolean canMakeObject(@NotNull DependencyKey<?> key);
  @Nullable <T> T makeObject(@NotNull DependencyProvider dependencyProvider, @NotNull DependencyKey<T> key);
}
