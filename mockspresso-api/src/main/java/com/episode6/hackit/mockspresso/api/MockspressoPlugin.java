package com.episode6.hackit.mockspresso.api;

import com.episode6.hackit.mockspresso.Mockspresso;
import org.jetbrains.annotations.NotNull;

/**
 * An interface that can encapsulate multiple related mockspresso components into a single plugin.
 */
public interface MockspressoPlugin {
  @NotNull Mockspresso.Builder apply(@NotNull Mockspresso.Builder builder);
}
