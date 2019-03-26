package com.episode6.hackit.mockspresso.api;

import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * A config interface used by mockspresso to handle to mocking of objects using
 * any mocker that supports annotations.
 */
public interface MockerConfig {

  /**
   * Class that creates mocks, used for automatically generating mocks that
   * are not part of the dependency map
   */
  interface MockMaker {
    @NotNull <T> T makeMock(@NotNull TypeToken<T> typeToken);
  }

  /**
   * Class that prepares the mocks on an object (usually an instance of
   * the current test class, but can be any generic pojo with annotated fields)
   */
  interface FieldPreparer {
    void prepareFields(@NotNull Object objectWithMockFields);
  }

  /**
   * @return The MockMaker to be used by this config
   */
  @NotNull MockMaker provideMockMaker();

  /**
   * @return The FieldPreparer to be used by this config.
   */
  @NotNull FieldPreparer provideFieldPreparer();

  /**
   * @return a list of annotations that identify objects as mocks (or spys or whatever).
   * Fields with these annotations should be set my the FieldPreparer, and mockspresso will
   * pull their values and add them to the dependency map.
   */
  @NotNull List<Class<? extends Annotation>> provideMockAnnotations();
}
