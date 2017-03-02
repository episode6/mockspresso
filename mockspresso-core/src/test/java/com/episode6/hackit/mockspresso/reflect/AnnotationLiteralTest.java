package com.episode6.hackit.mockspresso.reflect;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.annotation.TestQualifierAnnotation;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Some basic tests for {@link AnnotationLiteral}
 * I'm not too worried about testing that code since we didn't write it, but
 * felt that some basic sanity checking was appropriate.
 */
@RunWith(DefaultTestRunner.class)
public class AnnotationLiteralTest {

  public static abstract class TestNamedAnnotationLiteral extends AnnotationLiteral<Named> implements Named {}
  public static class TestQualifierAnnotationLiteral extends AnnotationLiteral<TestQualifierAnnotation> implements TestQualifierAnnotation {}

  @Named("test1") String testProp1;
  @Named String testProp2;
  @TestQualifierAnnotation Integer testProp3;

  @Test
  public void testNamedAnnotationLiterals() {
    Annotation namedTest1Annot = annotationFromProp("testProp1");
    Annotation namedDefaultAnnot = annotationFromProp("testProp2");

    Annotation manualNamedTest1 = new NamedAnnotationLiteral("test1");
    Annotation manualNamedDefault = new NamedAnnotationLiteral();

    assertThat(namedTest1Annot)
        .isEqualTo(manualNamedTest1)
        .isNotEqualTo(manualNamedDefault);

    assertThat(namedDefaultAnnot)
        .isEqualTo(manualNamedDefault)
        .isNotEqualTo(manualNamedTest1);
  }

  @Test
  public void testOnTheFlyAnnotationLiterals() {
    Annotation namedTest1Annot = annotationFromProp("testProp1");
    Annotation namedDefaultAnnot = annotationFromProp("testProp2");
    Annotation testQualiAnnot = annotationFromProp("testProp3");

    Annotation manualNamedTest1 = new TestNamedAnnotationLiteral() {
      @Override
      public String value() {
        return "test1";
      }
    };
    Annotation manualNamedDefault = new TestNamedAnnotationLiteral() {
      @Override
      public String value() {
        return "";
      }
    };
    Annotation manualTestQuali = new TestQualifierAnnotationLiteral();

    assertThat(namedTest1Annot)
        .isEqualTo(manualNamedTest1)
        .isNotEqualTo(manualNamedDefault)
        .isNotEqualTo(manualTestQuali);

    assertThat(namedDefaultAnnot)
        .isEqualTo(manualNamedDefault)
        .isNotEqualTo(manualNamedTest1)
        .isNotEqualTo(manualTestQuali);

    assertThat(testQualiAnnot)
        .isEqualTo(manualTestQuali)
        .isNotEqualTo(manualNamedDefault)
        .isNotEqualTo(manualNamedTest1);
  }

  private Annotation annotationFromProp(String name) {
    try {
      Field field = getClass().getDeclaredField(name);
      return field.getDeclaredAnnotations()[0];
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }
}
