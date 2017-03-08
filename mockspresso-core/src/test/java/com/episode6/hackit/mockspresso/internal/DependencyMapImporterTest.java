package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.annotation.TestQualifierAnnotation;
import com.episode6.hackit.mockspresso.reflect.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Named;

import java.util.Arrays;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link DependencyMapImporter}
 */
@RunWith(DefaultTestRunner.class)
public class DependencyMapImporterTest {
  public static class TestQualifierAnnotationLiteral extends AnnotationLiteral<TestQualifierAnnotation> implements TestQualifierAnnotation {}

  DependencyKey<String> key1 = new DependencyKey<>(TypeToken.of(String.class), null);
  DependencyKey<String> key2 = new DependencyKey<>(TypeToken.of(String.class), new AnnotationLiteralTest.TestQualifierAnnotationLiteral());
  DependencyKey<String> key3 = new DependencyKey<>(TypeToken.of(String.class), new NamedAnnotationLiteral("somename"));
  DependencyKey<String> key4 = new DependencyKey<>(TypeToken.of(String.class), new NamedAnnotationLiteral("diffname"));
  DependencyKey<TestObject> key5 = new DependencyKey<>(TypeToken.of(TestObject.class), null);
  DependencyKey<TestObject> key6 = new DependencyKey<>(TypeToken.of(TestObject.class), new AnnotationLiteralTest.TestQualifierAnnotationLiteral());
  DependencyKey<TestObject> key7 = new DependencyKey<>(TypeToken.of(TestObject.class), new NamedAnnotationLiteral("somename"));

  @RealObject String testObj1 = "sup fool";
  @RealObject @TestQualifierAnnotation String testObj2 = "yoo";
  @RealObject @Named("somename") String testObj3 = "cool";
  @RealObject @Named("diffname") String testObj4 = null;
  @Mock TestObject testObj5;
  @Mock @TestQualifierAnnotation TestObject testObj6;
  @Mock @Named("somename") TestObject testObj7;

  private DependencyMap mDependencyMap;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mDependencyMap = new DependencyMap();
  }

  @Test
  public void testImportingRealObjects() {
    mDependencyMap.importFrom().annotatedFields(this, RealObject.class);

    assertThat(mDependencyMap.get(key1)).isEqualTo(testObj1);
    assertThat(mDependencyMap.get(key2)).isEqualTo(testObj2);
    assertThat(mDependencyMap.get(key3)).isEqualTo(testObj3);
    assertThat(mDependencyMap.containsKey(key4)).isFalse();
    assertThat(mDependencyMap.containsKey(key5)).isFalse();
    assertThat(mDependencyMap.containsKey(key6)).isFalse();
    assertThat(mDependencyMap.containsKey(key7)).isFalse();
  }

  @Test
  public void testImportingMocks() {
    mDependencyMap.importFrom().annotatedFields(this, Mock.class);

    assertThat(mDependencyMap.containsKey(key1)).isFalse();
    assertThat(mDependencyMap.containsKey(key2)).isFalse();
    assertThat(mDependencyMap.containsKey(key3)).isFalse();
    assertThat(mDependencyMap.containsKey(key4)).isFalse();
    assertThat(mDependencyMap.get(key5)).isEqualTo(testObj5);
    assertThat(mDependencyMap.get(key6)).isEqualTo(testObj6);
    assertThat(mDependencyMap.get(key7)).isEqualTo(testObj7);
  }

  @Test
  public void testImportBothMocksAndRealObjects() {
    mDependencyMap.importFrom().annotatedFields(this, Arrays.asList(Mock.class, RealObject.class));

    assertThat(mDependencyMap.get(key1)).isEqualTo(testObj1);
    assertThat(mDependencyMap.get(key2)).isEqualTo(testObj2);
    assertThat(mDependencyMap.get(key3)).isEqualTo(testObj3);
    assertThat(mDependencyMap.containsKey(key4)).isFalse();
    assertThat(mDependencyMap.get(key5)).isEqualTo(testObj5);
    assertThat(mDependencyMap.get(key6)).isEqualTo(testObj6);
    assertThat(mDependencyMap.get(key7)).isEqualTo(testObj7);
  }

  public static class TestObject {}
}
