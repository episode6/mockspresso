package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockerConfig;
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
  private @Nullable MockerConfig mMockerConfig = null;

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

  @Override
  public Mockspresso.Builder mocker(MockerConfig mockerConfig) {
    Preconditions.assertNull(mMockerConfig, "Attempted to set mocker config multiple times.");
    mMockerConfig = mockerConfig;
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
