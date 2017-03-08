package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.util.Preconditions;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class MockspressoBuilderImpl implements Mockspresso.Builder {

  private final @Nullable MockspressoInternal mParentMockspresso;
  private final DependencyMap mCustomDependencyMap = new DependencyMap();
  private final List<Object> mObjectsWithFields = new LinkedList<>();
  private final SpecialObjectMakerContainer mSpecialObjectMakers;

  private @Nullable MockerConfig mMockerConfig = null;

  public MockspressoBuilderImpl() {
    this(null);
  }

  MockspressoBuilderImpl(@Nullable MockspressoInternal parentMockspresso) {
    mParentMockspresso = parentMockspresso;
    mSpecialObjectMakers = new SpecialObjectMakerContainer(
        mParentMockspresso == null ? null : mParentMockspresso.getSpecialObjectMaker());
  }

  public Mockspresso.Builder fieldsFrom(Object objectWithFields) {
    mObjectsWithFields.add(objectWithFields);
    return this;
  }

  @Override
  public Mockspresso.Builder mockerConfig(MockerConfig mockerConfig) {
    Preconditions.assertNull(mMockerConfig, "Attempted to set mockerConfig multiple times.");
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

  private void validate() {
    assertParentOr(mMockerConfig, "mockerConfig");
  }

  //TODO: fill in
  public Mockspresso build() {
    validate();

    DependencyMap dependencyMap = new DependencyMap();

    MockerConfig mockerConfig = mMockerConfig != null ? mMockerConfig : mParentMockspresso.getMockerConfig();
    MockerConfig.FieldPreparer mockFieldPreparer = mockerConfig.provideFieldPreparer();
    MockerConfig.MockMaker mockMaker = mockerConfig.provideMockMaker();


    for (Object o : mObjectsWithFields) {
      // prepare mock fields
      mockFieldPreparer.prepareFields(o);

      //import mocks and non-null real objects into dependency map
      dependencyMap.importFrom()
          .annotatedFields(o, mockerConfig.provideMockAnnotations())
          .annotatedFields(o, RealObject.class);

      // inflate null realObjects
      // import previously null realObjects
    }

    dependencyMap.importFrom().dependencyMap(mCustomDependencyMap);


    return new MockspressoImpl(mockerConfig, mSpecialObjectMakers);
  }

  public Mockspresso.Rule buildRule() {
    validate();
    return new MockspressoRuleImpl(this);
  }

  private void assertParentOr(@Nullable Object object, String name) {
    Preconditions.assertTrue(
        mParentMockspresso != null || object != null,
        String.format("Mockspresso builder must include a %s or inherit one from a parent Mockspresso instance", name));
  }
}
