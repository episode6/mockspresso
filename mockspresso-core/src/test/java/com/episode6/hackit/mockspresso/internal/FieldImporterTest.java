package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.annotation.TestQualifierAnnotation;
import com.episode6.hackit.mockspresso.annotation.Unmapped;
import com.episode6.hackit.mockspresso.reflect.AnnotationLiteral;
import com.episode6.hackit.mockspresso.reflect.AnnotationLiteralTest;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.NamedAnnotationLiteral;
import com.episode6.hackit.mockspresso.testobject.SubclassTestObject;
import com.episode6.hackit.mockspresso.testobject.SuperclassTestObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Tests {@link FieldImporter}
 */
@RunWith(DefaultTestRunner.class)
public class FieldImporterTest {
  public static class TestQualifierAnnotationLiteral extends AnnotationLiteral<TestQualifierAnnotation> implements TestQualifierAnnotation {}

  DependencyKey<String> key1 = DependencyKey.of(String.class);
  DependencyKey<String> key2 = DependencyKey.of(String.class, new TestQualifierAnnotationLiteral());
  DependencyKey<String> key3 = DependencyKey.of(String.class, new NamedAnnotationLiteral("somename"));
  DependencyKey<String> key4 = DependencyKey.of(String.class, new NamedAnnotationLiteral("diffname"));
  DependencyKey<TestObject> key5 = DependencyKey.of(TestObject.class, null);
  DependencyKey<TestObject> key6 = DependencyKey.of(TestObject.class, new AnnotationLiteralTest.TestQualifierAnnotationLiteral());
  DependencyKey<TestObject> key7 = DependencyKey.of(TestObject.class, new NamedAnnotationLiteral("somename"));

  @RealObject String testObj1 = "sup fool";
  @RealObject @TestQualifierAnnotation String testObj2 = "yoo";
  @RealObject @Named("somename") String testObj3 = "cool";
  @RealObject @Named("diffname") String testObj4 = null;
  @Mock TestObject testObj5;
  @Mock @TestQualifierAnnotation TestObject testObj6;
  @Mock @Named("somename") TestObject testObj7;

  @Unmapped @Mock @Named("dontimportme") TestObject testObj8;

  private DependencyMap mDependencyMap;
  private FieldImporter mImporter;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mDependencyMap = mock(DependencyMap.class);
  }

  private void initImporter(Class<? extends Annotation> annotation) {
    initImporter(Collections.<Class<? extends Annotation>>singletonList(annotation));
  }

  private void initImporter(List<Class<? extends Annotation>> annotations) {
    mImporter = new FieldImporter(annotations, mDependencyMap);
  }

  @Test
  public void testImportingRealObjects() {
    initImporter(RealObject.class);
    mImporter.importAnnotatedFields(this);

    verify(mDependencyMap).put(key1, testObj1, null);
    verify(mDependencyMap).put(key2, testObj2, null);
    verify(mDependencyMap).put(key3, testObj3, null);
    verifyNoMoreInteractions(mDependencyMap);
  }

  @Test
  public void testImportingMocks() {
    initImporter(Mock.class);
    mImporter.importAnnotatedFields(this);

    verify(mDependencyMap).put(key5, testObj5, null);
    verify(mDependencyMap).put(key6, testObj6, null);
    verify(mDependencyMap).put(key7, testObj7, null);
    verifyNoMoreInteractions(mDependencyMap);
  }

  @Test
  public void testImportBothMocksAndRealObjects() {
    initImporter(Arrays.asList(Mock.class, RealObject.class));
    mImporter.importAnnotatedFields(this);

    verify(mDependencyMap).put(key1, testObj1, null);
    verify(mDependencyMap).put(key2, testObj2, null);
    verify(mDependencyMap).put(key3, testObj3, null);
    // skip 4 because it's null
    verify(mDependencyMap).put(key5, testObj5, null);
    verify(mDependencyMap).put(key6, testObj6, null);
    verify(mDependencyMap).put(key7, testObj7, null);
    verifyNoMoreInteractions(mDependencyMap);
  }

  // TODO: part of this test relies on mockito's functionality to handle all declared fields from super-classes
  // I should write a similar test for easy mock to see if it performs the same way.
  @Test
  public void testImportFromSubAndSuperClasses() {
    SubclassTestObject testObject = new SubclassTestObject();
    DependencyKey<SubclassTestObject.SubClassInnerClass> subClassInnerClassKey =
        DependencyKey.of(SubclassTestObject.SubClassInnerClass.class);
    DependencyKey<SuperclassTestObject.SuperClassInnerClass> superClassInnerClassKey =
        DependencyKey.of(SuperclassTestObject.SuperClassInnerClass.class);

    MockitoAnnotations.initMocks(testObject);
    initImporter(Arrays.asList(RealObject.class, Mock.class));
    mImporter.importAnnotatedFields(testObject);

    verify(mDependencyMap).put(key1, "subclass", null);
    verify(mDependencyMap).put(key2, "superclass", null);
    verify(mDependencyMap).put(
        eq(subClassInnerClassKey),
        any(SubclassTestObject.SubClassInnerClass.class),
        nullable(DependencyValidator.class));
    verify(mDependencyMap).put(
        eq(superClassInnerClassKey),
        any(SuperclassTestObject.SuperClassInnerClass.class),
        nullable(DependencyValidator.class));
    verifyNoMoreInteractions(mDependencyMap);
  }

  public static class TestObject {}
}
