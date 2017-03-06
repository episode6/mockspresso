package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class MockspressoBuilderImpl implements Mockspresso.Builder {

  private @Nullable Mockspresso mParentMockspresso = null;
  private final List<Object> mObjectsWithFields = new LinkedList<>();


  @Override
  public Mockspresso.Builder parent(Mockspresso parentMockspresso) {
    assertNullProperty(mParentMockspresso);
    mParentMockspresso = parentMockspresso;
    return this;
  }

  public Mockspresso.Builder fieldsFrom(Object objectWithFields) {
    mObjectsWithFields.add(objectWithFields);
    return this;
  }

  //TODO: fill in
  public Mockspresso build() {
    return new MockspressoImpl();
  }

  public Mockspresso.Rule buildRule() {
    return new MockspressoRuleImpl(this);
  }

  // some properties must only be allowed to be set exactly once.
  private static void assertNullProperty(Object object) {
    if (object != null) {
      throw new IllegalArgumentException("Argument was already set and cannot be re-set by " + object.toString());
    }
  }
}
