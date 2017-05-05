package com.episode6.hackit.mockspresso.api;

import com.episode6.hackit.mockspresso.reflect.DependencyKey;

/**
 * A class that creates "special" objects, i.e. objects that, by default, should not
 * be simply mocked. An example of this is {@link javax.inject.Provider}, where we'd
 * rather return a real Provider that provides a mock (or a mapped dependency).
 */
public interface SpecialObjectMaker {
  boolean canMakeObject(DependencyKey<?> key);
  <T> T makeObject(DependencyProvider dependencyProvider, DependencyKey<T> key);
}
