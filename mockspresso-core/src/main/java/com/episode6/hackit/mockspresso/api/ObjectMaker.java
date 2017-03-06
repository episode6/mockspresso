package com.episode6.hackit.mockspresso.api;

import com.episode6.hackit.mockspresso.reflect.DependencyKey;

/**
 * An interface for a class that can create objects given a key and a {@link DependencyProvider}
 */
public interface ObjectMaker {
  <T> T makeObject(DependencyProvider dependencyProvider, DependencyKey<T> key);
}
