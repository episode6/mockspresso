package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;

/**
 * Interface representing an internal implementation of Mockspresso that includes
 * retrievable objects so that we may create child instances of Mockspresso
 */
public interface MockspressoInternal extends Mockspresso {

  MockerConfig getMockerConfig();
  InjectionConfig getInjectionConfig();
  DependencyMap getDependencyMap();
  SpecialObjectMaker getSpecialObjectMaker();
}
