package com.episode6.hackit.mockspresso.api;

import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A interface for a class that can provide dependencies for an object.
 * Its get method will never return null.
 */
public interface DependencyProvider {
  @Nullable <T> T get(@NotNull DependencyKey<T> key);
}
