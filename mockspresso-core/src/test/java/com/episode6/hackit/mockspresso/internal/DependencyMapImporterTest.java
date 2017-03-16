package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.annotation.TestQualifierAnnotation;
import com.episode6.hackit.mockspresso.reflect.*;
import com.episode6.hackit.mockspresso.testobject.SubclassTestObject;
import com.episode6.hackit.mockspresso.testobject.SuperclassTestObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Named;
import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * Tests {@link DependencyMapImporter}
 */
@RunWith(DefaultTestRunner.class)
public class DependencyMapImporterTest {
  public static class TestQualifierAnnotationLiteral extends AnnotationLiteral<TestQualifierAnnotation> implements TestQualifierAnnotation {}

  DependencyKey<String> key1 = new DependencyKey<>(TypeToken.of(String.class), null);
  DependencyKey<String> key2 = new DependencyKey<>(TypeToken.of(String.class), new TestQualifierAnnotationLiteral());
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
  private DependencyMapImporter mImporter;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mDependencyMap = mock(DependencyMap.class);
    mImporter = new DependencyMapImporter(mDependencyMap);
  }

  @Test
  public void testImportingRealObjects() {
    mImporter.annotatedFields(this, RealObject.class);

    verify(mDependencyMap).put(key1, testObj1);
    verify(mDependencyMap).put(key2, testObj2);
    verify(mDependencyMap).put(key3, testObj3);
    verifyNoMoreInteractions(mDependencyMap);
  }

  @Test
  public void testImportingMocks() {
    mImporter.annotatedFields(this, Mock.class);

    verify(mDependencyMap).put(key5, testObj5);
    verify(mDependencyMap).put(key6, testObj6);
    verify(mDependencyMap).put(key7, testObj7);
    verifyNoMoreInteractions(mDependencyMap);
  }

  @Test
  public void testImportBothMocksAndRealObjects() {
    mImporter.annotatedFields(this, Arrays.asList(Mock.class, RealObject.class));

    verify(mDependencyMap).put(key1, testObj1);
    verify(mDependencyMap).put(key2, testObj2);
    verify(mDependencyMap).put(key3, testObj3);
    // skip 4 because it's null
    verify(mDependencyMap).put(key5, testObj5);
    verify(mDependencyMap).put(key6, testObj6);
    verify(mDependencyMap).put(key7, testObj7);
    verifyNoMoreInteractions(mDependencyMap);
  }

  // TODO: part of this test relies on mockito's functionality to handle all declared fields from super-classes
  // I should write a similar test for easy mock to see if it performs the same way.
  @Test
  public void testImportFromSubAndSuperClasses() {
    SubclassTestObject testObject = new SubclassTestObject();
    DependencyKey<SubclassTestObject.SubClassInnerClass> subClassInnerClassKey = new DependencyKey<>(TypeToken.of(SubclassTestObject.SubClassInnerClass.class), null);
    DependencyKey<SuperclassTestObject.SuperClassInnerClass> superClassInnerClassKey = new DependencyKey<>(TypeToken.of(SuperclassTestObject.SuperClassInnerClass.class), null);

    MockitoAnnotations.initMocks(testObject);
    mImporter.annotatedFields(testObject, Arrays.asList(RealObject.class, Mock.class));

    verify(mDependencyMap).put(key1, "subclass");
    verify(mDependencyMap).put(key2, "superclass");
    verify(mDependencyMap).put(eq(subClassInnerClassKey), any(SubclassTestObject.SubClassInnerClass.class));
    verify(mDependencyMap).put(eq(superClassInnerClassKey), any(SuperclassTestObject.SuperClassInnerClass.class));
    verifyNoMoreInteractions(mDependencyMap);
  }

  public static class TestObject {}
}
