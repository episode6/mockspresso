package com.episode6.hackit.mockspresso.mockito;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.NamedAnnotationLiteral;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests {@link MockitoAutoFactoryMaker}
 */
public class MockitoAutoFactoryMakerTest {

  static final DependencyKey<TestClassFactory> factoryKey = DependencyKey.of(TestClassFactory.class);
  static final DependencyKey<TestClass> testClassKey = DependencyKey.of(TestClass.class);
  static final NamedAnnotationLiteral qualifierAnnotation = new NamedAnnotationLiteral("some_named_annotation");
  static final DependencyKey<TestClassFactory> annotatestFactoryKey = DependencyKey.of(TestClassFactory.class, qualifierAnnotation);
  static final DependencyKey<TestClass> annotatestTestClassKey = DependencyKey.of(TestClass.class, qualifierAnnotation);
  static final DependencyKey<GenericFactory<TestClass>> genericFactoryKey = DependencyKey.of(new TypeToken<GenericFactory<TestClass>>() {});
  static final DependencyKey<VarChangeGenericFactory<TestClass>> varChangeGenericFactoryKey = DependencyKey.of(new TypeToken<VarChangeGenericFactory<TestClass>>() {});
  static final DependencyKey<ComplexVarChangeGeneric<String, TestClass>> complexVarChangeFactoryKey = DependencyKey.of(new TypeToken<ComplexVarChangeGeneric<String, TestClass>>() {});

  interface TestClass {}

  interface TestClassFactory {
    TestClass create(String name);
  }


  interface GenericFactory<V> {
    V createThing(String name);
  }

  interface VarChangeGenericFactory<Q> extends GenericFactory<Q> {}

  interface ComplexVarChangeGeneric<X, Y> extends VarChangeGenericFactory<Y> {}

  @Mock DependencyProvider mDependencyProvider;
  @Mock TestClass mTestClass;

  AutoCloseable mockitoClosable;

  @Before
  public void setup() {
    mockitoClosable = MockitoAnnotations.openMocks(this);
  }

  @After public void teardown() throws Exception {
    mockitoClosable.close();
  }

  @Test
  public void testFactoryMaker() {
    when(mDependencyProvider.get(testClassKey)).thenReturn(mTestClass);
    MockitoAutoFactoryMaker maker = MockitoAutoFactoryMaker.create(TestClassFactory.class);

    boolean canMake = maker.canMakeObject(factoryKey);
    TestClassFactory factory = maker.makeObject(mDependencyProvider, factoryKey);
    TestClass testClass = factory.create("some name");

    verify(mDependencyProvider).get(testClassKey);
    assertThat(canMake).isTrue();
    assertThat(testClass).isEqualTo(mTestClass);
  }

  @Test
  public void testAnnotatedFactoryMaker() {
    when(mDependencyProvider.get(annotatestTestClassKey)).thenReturn(mTestClass);
    MockitoAutoFactoryMaker maker = MockitoAutoFactoryMaker.create(TestClassFactory.class);

    boolean canMake = maker.canMakeObject(DependencyKey.of(TestClassFactory.class));
    TestClassFactory factory = maker.makeObject(mDependencyProvider, annotatestFactoryKey);
    TestClass testClass = factory.create("some name");

    verify(mDependencyProvider).get(annotatestTestClassKey);
    assertThat(canMake).isTrue();
    assertThat(testClass).isEqualTo(mTestClass);
  }

  @Test
  public void testGenericFactoryMaker() {
    when(mDependencyProvider.get(testClassKey)).thenReturn(mTestClass);
    MockitoAutoFactoryMaker maker = MockitoAutoFactoryMaker.create(GenericFactory.class);

    boolean canMake = maker.canMakeObject(genericFactoryKey);
    GenericFactory<TestClass> factory = maker.makeObject(mDependencyProvider, genericFactoryKey);
    TestClass testClass = factory.createThing("some name");

    verify(mDependencyProvider).get(testClassKey);
    assertThat(canMake).isTrue();
    assertThat(testClass).isEqualTo(mTestClass);
  }

  @Test
  public void testVarChangeGenericFactoryMaker() {
    when(mDependencyProvider.get(testClassKey)).thenReturn(mTestClass);
    MockitoAutoFactoryMaker maker = MockitoAutoFactoryMaker.create(VarChangeGenericFactory.class);

    boolean canMake = maker.canMakeObject(varChangeGenericFactoryKey);
    VarChangeGenericFactory<TestClass> factory = maker.makeObject(mDependencyProvider, varChangeGenericFactoryKey);
    TestClass testClass = factory.createThing("some name");

    verify(mDependencyProvider).get(testClassKey);
    assertThat(canMake).isTrue();
    assertThat(testClass).isEqualTo(mTestClass);
  }

  @Test
  public void testComplexVarChangeGenericFactoryMaker() {
    when(mDependencyProvider.get(testClassKey)).thenReturn(mTestClass);
    MockitoAutoFactoryMaker maker = MockitoAutoFactoryMaker.create(ComplexVarChangeGeneric.class);

    boolean canMake = maker.canMakeObject(complexVarChangeFactoryKey);
    ComplexVarChangeGeneric<String, TestClass> factory = maker.makeObject(mDependencyProvider, complexVarChangeFactoryKey);
    TestClass testClass = factory.createThing("some name");

    verify(mDependencyProvider).get(testClassKey);
    assertThat(canMake).isTrue();
    assertThat(testClass).isEqualTo(mTestClass);
  }
}
