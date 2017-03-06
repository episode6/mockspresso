package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class MockspressoBuilderImpl implements Mockspresso.Builder {

  private final @Nullable Mockspresso mParentMockspresso;
  private final List<Object> mObjectsWithFields = new LinkedList<>();

  public MockspressoBuilderImpl(@Nullable Mockspresso parentMockspresso) {
    mParentMockspresso = parentMockspresso;
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
