package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * A container for special object makers. Includes a parent maker (usually
 * another instance of SpecialObjectMakerContainer) that is checked last.
 */
public class SpecialObjectMakerContainer implements SpecialObjectMaker {

  private @Nullable SpecialObjectMaker mParentMaker = null;
  private final List<SpecialObjectMaker> mSpecialObjectMakers = new LinkedList<>();

  public void setParentMaker(SpecialObjectMaker parentMaker) {
    mParentMaker = parentMaker;
  }

  public void add(SpecialObjectMaker specialObjectMaker) {
    mSpecialObjectMakers.add(specialObjectMaker);
  }

  public void addAll(Collection<? extends SpecialObjectMaker> specialObjectMakers) {
    mSpecialObjectMakers.addAll(specialObjectMakers);
  }

  @Override
  public boolean canMakeObject(DependencyKey<?> key) {
    for (SpecialObjectMaker maker : mSpecialObjectMakers) {
      if (maker.canMakeObject(key)) {
        return true;
      }
    }
    return mParentMaker != null && mParentMaker.canMakeObject(key);
  }

  @Override
  public <T> T makeObject(DependencyProvider dependencyProvider, DependencyKey<T> key) {
    for (SpecialObjectMaker maker : mSpecialObjectMakers) {
      if (maker.canMakeObject(key)) {
        return maker.makeObject(dependencyProvider, key);
      }
    }
    return mParentMaker == null ? null : mParentMaker.makeObject(dependencyProvider, key);
  }


}
