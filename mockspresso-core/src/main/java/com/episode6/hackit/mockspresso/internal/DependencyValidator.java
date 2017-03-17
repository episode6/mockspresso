package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.exception.CircularDependencyError;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class DependencyValidator {

  public static DependencyValidator validatorFor(DependencyKey key) {
    return new DependencyValidator(null, key);
  }

  private final @Nullable DependencyValidator mParent;
  private final Set<DependencyValidator> mChildren;
  private final DependencyKey mKey;

  private DependencyValidator(DependencyValidator parent, DependencyKey key) {
    mParent = parent;
    mKey = key;
    mChildren = new HashSet<>();
  }

  public DependencyValidator child(DependencyKey key) {
    validate(key);
    DependencyValidator newChild = new DependencyValidator(this, key);
    mChildren.add(newChild);
    return newChild;
  }

  private void validate(DependencyKey otherKey) {
    if (mKey.equals(otherKey)) {
      throw new CircularDependencyError();
    }
    if (mParent != null) {
      mParent.validate(otherKey);
    }
  }
}
