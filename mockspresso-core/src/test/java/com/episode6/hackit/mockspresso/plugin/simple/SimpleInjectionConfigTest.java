package com.episode6.hackit.mockspresso.plugin.simple;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link SimpleInjectionConfig}
 */
@RunWith(DefaultTestRunner.class)
public class SimpleInjectionConfigTest {

  private SimpleInjectionConfig mSimpleInjectionConfig = SimpleInjectionConfig.getInstance();

  @Test
  public void testFindSmalledConstructor() {
    Constructor<TestClass1> constructor1 = mSimpleInjectionConfig.provideConstructorSelector()
        .chooseConstructor(TypeToken.of(TestClass1.class));
    Constructor<TestClass2> constructor2 = mSimpleInjectionConfig.provideConstructorSelector()
        .chooseConstructor(TypeToken.of(TestClass2.class));

    assertThat(constructor1.getParameterCount()).isEqualTo(1);
    assertThat(constructor2.getParameterCount()).isEqualTo(0);
  }

  @Test
  public void testNoAnnotationsForSimpleInject() {
    List<Class<? extends Annotation>> annotationList = mSimpleInjectionConfig.provideInjectableFieldAnnotations();

    assertThat(annotationList).isEmpty();
  }

  @Test
  public void testFailWithNoConstructor() {
    Constructor<TestInterface> constructor = mSimpleInjectionConfig.provideConstructorSelector()
        .chooseConstructor(TypeToken.of(TestInterface.class));

    assertThat(constructor).isNull();
  }

  public static class TestClass1 {
    public TestClass1(String param1, String param2, String param3) {}
    public TestClass1(String param1, String param2) {}
    public TestClass1(String param1) {}
  }

  public static class TestClass2 {
    public TestClass2() {}
    public TestClass2(String param1, String param2) {}
    public TestClass2(String param1, String param2, String param3) {}
  }

  public interface TestInterface {}
}
