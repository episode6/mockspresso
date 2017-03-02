package com.episode6.hackit.mockspresso;

import com.episode6.hackit.mockspresso.internal.MockspressoImpl;
import com.episode6.hackit.mockspresso.internal.MockspressoRuleImpl;

import java.util.LinkedList;
import java.util.List;

/**
 * A builder for Mockspresso and MockspressoRule
 */
public class MockspressoBuilder {

  public static MockspressoBuilder fromTest(Object testObject) {
    return new MockspressoBuilder().fieldsFrom(testObject);
  }

  private final List<Object> mObjectsWithFields = new LinkedList<>();

  private MockspressoBuilder() {}

  public MockspressoBuilder fieldsFrom(Object objectWithFields) {
    mObjectsWithFields.add(objectWithFields);
    return this;
  }

  public Mockspresso build() {
    return new MockspressoImpl();
  }

  public MockspressoRule buildRule() {
    return new MockspressoRuleImpl(this);
  }
}
