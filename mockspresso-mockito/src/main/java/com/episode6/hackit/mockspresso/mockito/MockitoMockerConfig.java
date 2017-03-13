package com.episode6.hackit.mockspresso.mockito;

import com.episode6.hackit.mockspresso.api.AbstractMockerConfig;
import org.mockito.Mock;
import org.mockito.Spy;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * A MockerConfig for Mockito.
 */
public class MockitoMockerConfig extends AbstractMockerConfig {

  // This object has no state, so we maintain a static instance of it
  // instead of creating multiple instances on the fly
  private static final MockitoMockerConfig INSTANCE = new MockitoMockerConfig();
  public static MockitoMockerConfig getInstance() {
    return INSTANCE;
  }

  private MockitoMockerConfig() {
    super(
        new MockitoMockMaker(),
        new MockitoFieldPreparer());
  }

  @Override
  public List<Class<? extends Annotation>> provideMockAnnotations() {
    return Arrays.asList(
        Mock.class,
        Spy.class);
  }

}
