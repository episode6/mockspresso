package com.episode6.hackit.mockspresso.basic.plugin.javax;

import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.reflect.Constructor;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link JavaxInjectionConfig}
 */
@SuppressWarnings("unchecked")
public class JavaxInjectionConfigTest {

  private final JavaxInjectionConfig mInjectionConfig = new JavaxInjectionConfig();

  @Test
  public void testHasOnlyInjectAnnotation() {
    assertThat(mInjectionConfig.provideInjectableFieldAnnotations()).containsOnly(Inject.class);
  }

  @Test
  public void testFindInjectConstructor() {
    Constructor<TestClassWithMultipleConstructorsButOnlyOneInject> constructor = (Constructor<TestClassWithMultipleConstructorsButOnlyOneInject>) mInjectionConfig
        .chooseConstructor(TypeToken.of(TestClassWithMultipleConstructorsButOnlyOneInject.class));

    assertThat(constructor).isNotNull();
    assertThat(constructor.isAnnotationPresent(Inject.class)).isTrue();
    assertThat(constructor.getParameterCount()).isEqualTo(2);
  }

  @Test(expected = MultipleInjectConstructorException.class)
  public void testMultipleInjectConstructors() {
    Constructor<TestClassWithTwoInjectAnnotations> constructor = (Constructor<TestClassWithTwoInjectAnnotations>) mInjectionConfig
        .chooseConstructor(TypeToken.of(TestClassWithTwoInjectAnnotations.class));
  }

  @Test
  public void testFindFallbackConstructor() {
    Constructor<TestClassWithEmptyFallback> constructor = (Constructor<TestClassWithEmptyFallback>) mInjectionConfig
        .chooseConstructor(TypeToken.of(TestClassWithEmptyFallback.class));

    assertThat(constructor).isNotNull();
    assertThat(constructor.isAnnotationPresent(Inject.class)).isFalse();
    assertThat(constructor.getParameterCount()).isEqualTo(0);
  }

  @Test
  public void testNoValidConstructor() {
    Constructor<TestClassWithNoValidConstructor> constructor = (Constructor<TestClassWithNoValidConstructor>) mInjectionConfig
        .chooseConstructor(TypeToken.of(TestClassWithNoValidConstructor.class));

    assertThat(constructor).isNull();
  }

  @Test
  public void testFindFallbackConstructorWhenNoneDeclared() {
    Constructor<TestClassWithNoDefinedConstructors> constructor = (Constructor<TestClassWithNoDefinedConstructors>) mInjectionConfig
        .chooseConstructor(TypeToken.of(TestClassWithNoDefinedConstructors.class));

    assertThat(constructor).isNotNull();
    assertThat(constructor.isAnnotationPresent(Inject.class)).isFalse();
    assertThat(constructor.getParameterCount()).isEqualTo(0);
  }

  public static class TestClassWithMultipleConstructorsButOnlyOneInject {

    public TestClassWithMultipleConstructorsButOnlyOneInject() {}
    public TestClassWithMultipleConstructorsButOnlyOneInject(String testString) {}
    public TestClassWithMultipleConstructorsButOnlyOneInject(Runnable runnable) {}

    @Inject
    public TestClassWithMultipleConstructorsButOnlyOneInject(Runnable runnable, Provider<Runnable> runnableProvider) {}
  }

  public static class TestClassWithTwoInjectAnnotations {
    @Inject public TestClassWithTwoInjectAnnotations() {}
    @Inject public TestClassWithTwoInjectAnnotations(Runnable runnable) {}
  }

  public static class TestClassWithEmptyFallback {
    public TestClassWithEmptyFallback(Runnable runnable) {}
    public TestClassWithEmptyFallback() {}
    public TestClassWithEmptyFallback(Runnable runnable, String string) {}
  }

  public static class TestClassWithNoValidConstructor {
    public TestClassWithNoValidConstructor(Runnable runnable) {}
    public TestClassWithNoValidConstructor(Runnable runnable, String string) {}
    public TestClassWithNoValidConstructor(Runnable runnable, String string, Provider<String> stringProvider) {}
  }

  public static class TestClassWithNoDefinedConstructors {}
}
