package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.util.Preconditions;

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
    Preconditions.assertNull(mParentMockspresso, "Attempted to set parent mockspresso instance multiple times.");
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
}
