package com.episode6.hackit.mockspresso.mockito.powermock;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import org.mockito.Mock;
import org.mockito.Spy;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * A MockerConfig for Powermock + Mockito
 *
 * @deprecated This functionality is now exposed by the kotlin extension method `mockByPowerMockito()` and its
 * JavaSupport counterpart {@link MockspressoPowerMockitoPluginsJavaSupport#mockByPowerMockito()}
 *
 * This class will be marked internal/protected in a future release
 */
@Deprecated
public class PowerMockitoConfig implements MockerConfig {

  private final MockMaker mMockMaker = new PowerMockitoMockMaker();
  private final FieldPreparer mFieldPreparer = new PowerMockitoFieldPreparer();

  @Override
  public MockMaker provideMockMaker() {
    return mMockMaker;
  }

  @Override
  public FieldPreparer provideFieldPreparer() {
    return mFieldPreparer;
  }

  @Override
  public List<Class<? extends Annotation>> provideMockAnnotations() {
    return Arrays.asList(
        Mock.class,
        Spy.class);
  }
}
