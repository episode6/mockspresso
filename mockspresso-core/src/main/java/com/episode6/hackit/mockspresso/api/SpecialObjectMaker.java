package com.episode6.hackit.mockspresso.api;

import com.episode6.hackit.mockspresso.reflect.DependencyKey;

/**
 * An extension of {@link ObjectMaker} intended for special objects
 * that shouldn't be simply mocked by default.
 */
public interface SpecialObjectMaker extends ObjectMaker {
  boolean canMakeObject(DependencyKey<?> key);
}