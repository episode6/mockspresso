package com.episode6.hackit.mockspresso.easymock;

import com.episode6.hackit.mockspresso.api.AbstractMockerConfig;
import org.easymock.Mock;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

/**
 * A MockerConfig for EasyMock
 */
public class EasyMockMockerConfig extends AbstractMockerConfig {

  // This object has no state, so we maintain a static instance of it
  // instead of creating multiple instances on the fly
  private static final EasyMockMockerConfig INSTANCE = new EasyMockMockerConfig();
  public static EasyMockMockerConfig getInstance() {
    return INSTANCE;
  }

  private EasyMockMockerConfig() {
    super(
        new EasyMockMockMaker(),
        new EasyMockFieldPreparer());
  }

  @Override
  public List<Class<? extends Annotation>> provideMockAnnotations() {
    return Collections.<Class<? extends Annotation>>singletonList(Mock.class);
  }
}
