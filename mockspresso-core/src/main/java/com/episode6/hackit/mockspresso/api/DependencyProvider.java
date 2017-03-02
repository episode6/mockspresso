package com.episode6.hackit.mockspresso.api;

import com.episode6.hackit.mockspresso.reflect.TypeToken;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

/**
 *
 */
public interface DependencyProvider {
  <T> T get(DependencyKey<T> key);
}
