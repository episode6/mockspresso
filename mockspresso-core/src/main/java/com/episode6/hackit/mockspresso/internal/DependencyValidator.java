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
    validateUp(key);
    DependencyValidator newChild = new DependencyValidator(this, key);
    mChildren.add(newChild);
    return newChild;
  }

  public void append(@Nullable DependencyValidator childDependencyValidator) {
    if (childDependencyValidator == null) {
      return;
    }

    if (!childDependencyValidator.mKey.equals(mKey)) {
      throw new RuntimeException(
          String.format(
              "DependencyValidator append mismatch, tried to append %s to %s",
              childDependencyValidator.mKey,
              mKey));
    }

    // TODO: this could be faster since the existing validator has already been validated
    for (DependencyValidator otherChildValidator : childDependencyValidator.mChildren) {
      child(otherChildValidator.mKey).append(otherChildValidator);
    }
  }

  private void validateUp(DependencyKey otherKey) {
    if (mKey.equals(otherKey)) {
      throw new CircularDependencyError();
    }
    if (mParent != null) {
      mParent.validateUp(otherKey);
    }
  }
}
