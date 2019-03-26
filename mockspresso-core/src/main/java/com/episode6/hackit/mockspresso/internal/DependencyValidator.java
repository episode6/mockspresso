package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.exception.CircularDependencyError;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;

import org.jetbrains.annotations.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * DependencyValidator is essentially a doubly-linked graph of dependency keys used to
 * represent and validate the dependency graph.
 */
class DependencyValidator {

  static DependencyValidator childOrNew(@Nullable DependencyValidator parentValidator, DependencyKey childKey) {
    if (parentValidator == null) {
      return new DependencyValidator(childKey);
    }
    return parentValidator.child(childKey);
  }

  private final Set<DependencyValidator> mParents;
  private final Set<DependencyValidator> mChildren;
  private final DependencyKey mKey;

  DependencyValidator(DependencyKey key) {
    mKey = key;
    mParents = new HashSet<>();
    mChildren = new HashSet<>();
  }

  DependencyValidator child(DependencyKey key) {
    validatePotentialChild(key);
    DependencyValidator newChild = new DependencyValidator(key);

    newChild.mParents.add(this);
    mChildren.add(newChild);

    return newChild;
  }

  void append(@Nullable DependencyValidator childDependencyValidator) {
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

    for (DependencyValidator otherChildValidator : childDependencyValidator.mChildren) {
      otherChildValidator.addParent(this);
    }
  }

  private void addParent(DependencyValidator newParent) {
    validatePotentialParent(newParent);

    mParents.add(newParent);
    newParent.mChildren.add(this);
  }

  private void validatePotentialChild(DependencyKey potentialChildKey) {
    if (mKey.equals(potentialChildKey)) {
      throw new CircularDependencyError(potentialChildKey);
    }
    for (DependencyValidator parent : mParents) {
      parent.validatePotentialChild(potentialChildKey);
    }
  }

  private void validatePotentialParent(DependencyValidator potentialParent) {
    potentialParent.validatePotentialChild(mKey);
    for (DependencyValidator child : mChildren) {
      child.validatePotentialParent(potentialParent);
    }
  }
}
