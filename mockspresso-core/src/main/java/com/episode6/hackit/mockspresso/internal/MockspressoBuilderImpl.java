package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import com.episode6.hackit.mockspresso.util.Preconditions;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class MockspressoBuilderImpl implements Mockspresso.Builder {

  private final @Nullable MockspressoInternal mParentMockspresso;
  private final DependencyMap mDependencyMap;
  private final SpecialObjectMakerContainer mSpecialObjectMakers;
  private final List<Object> mObjectsWithFields;

  private @Nullable MockerConfig mMockerConfig;

  public MockspressoBuilderImpl() {
    this(null);
  }

  MockspressoBuilderImpl(@Nullable MockspressoInternal parentMockspresso) {
    mParentMockspresso = parentMockspresso;
    mDependencyMap = new DependencyMap(
        parentMockspresso == null ? null : parentMockspresso.getDependencyMap());
    mSpecialObjectMakers = new SpecialObjectMakerContainer(
        parentMockspresso == null ? null : parentMockspresso.getSpecialObjectMaker());
    mObjectsWithFields = new LinkedList<>();

    mMockerConfig = parentMockspresso == null ? null : parentMockspresso.getMockerConfig();
  }

  public Mockspresso.Builder fieldsFrom(Object objectWithFields) {
    mObjectsWithFields.add(objectWithFields);
    return this;
  }

  @Override
  public Mockspresso.Builder mockerConfig(MockerConfig mockerConfig) {
    mMockerConfig = mockerConfig;
    return this;
  }

  @Override
  public Mockspresso.Builder specialObjectMaker(SpecialObjectMaker specialObjectMaker) {
    mSpecialObjectMakers.add(specialObjectMaker);
    return this;
  }

  @Override
  public Mockspresso.Builder specialObjectMakers(List<SpecialObjectMaker> specialObjectMakers) {
    mSpecialObjectMakers.addAll(specialObjectMakers);
    return this;
  }

  @Override
  public <T> Mockspresso.Builder dependency(Class<T> clazz, T value) {
    return dependency(TypeToken.of(clazz), null, value);
  }

  @Override
  public <T> Mockspresso.Builder dependency(TypeToken<T> typeToken, T value) {
    return dependency(typeToken, null, value);
  }

  @Override
  public <T> Mockspresso.Builder dependency(Class<T> clazz, Annotation annotation, T value) {
    return dependency(TypeToken.of(clazz), annotation, value);
  }

  @Override
  public <T> Mockspresso.Builder dependency(TypeToken<T> typeToken, Annotation annotation, T value) {
    mDependencyMap.put(new DependencyKey<T>(typeToken, annotation), value);
    return this;
  }

  private void validate() {
    Preconditions.assertNotNull(mMockerConfig, "MockerConfig missing from mockspresso builder");
  }

  //TODO: fill in
  public Mockspresso build() {
    validate();

    MockerConfig.FieldPreparer mockFieldPreparer = mMockerConfig.provideFieldPreparer();

    for (Object o : mObjectsWithFields) {
      // prepare mock fields
      mockFieldPreparer.prepareFields(o);

      //import mocks and non-null real objects into dependency map
      mDependencyMap.importFrom()
          .annotatedFields(o, mMockerConfig.provideMockAnnotations())
          .annotatedFields(o, RealObject.class);

      // inflate null realObjects
      // import previously null realObjects
    }

    return new MockspressoImpl(
        mMockerConfig,
        mDependencyMap,
        mSpecialObjectMakers);
  }

  public Mockspresso.Rule buildRule() {
    return new MockspressoRuleImpl(build());
  }
}
