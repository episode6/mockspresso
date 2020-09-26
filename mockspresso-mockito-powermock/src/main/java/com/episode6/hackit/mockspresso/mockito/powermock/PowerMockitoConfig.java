package com.episode6.hackit.mockspresso.mockito.powermock;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.reflect.ReflectUtil;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
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
public class PowerMockitoConfig implements MockerConfig, MockerConfig.FieldPreparer, MockerConfig.MockMaker {

  @Override
  public MockMaker provideMockMaker() {
    return this;
  }

  @Override
  public FieldPreparer provideFieldPreparer() {
    return this;
  }

  @Override
  public List<Class<? extends Annotation>> provideMockAnnotations() {
    return Arrays.asList(
        Mock.class,
        Spy.class);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T makeMock(TypeToken<T> typeToken) {
    return PowerMockito.mock((Class<T>) typeToken.getRawType());
  }

  @Override
  public void prepareFields(Object objectWithMockFields) {
    try {
      prepareFieldsInternal(objectWithMockFields);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private void prepareFieldsInternal(Object objectWithMockFields) throws IllegalAccessException {
    List<Field> allFields = ReflectUtil.getAllDeclaredFields(objectWithMockFields.getClass());
    for (Field field : allFields) {
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }

      Object value = field.get(objectWithMockFields);
      if (value == null && field.isAnnotationPresent(Mock.class)) {
        field.set(objectWithMockFields, PowerMockito.mock(field.getType()));
      } else if (field.isAnnotationPresent(Spy.class)) {
        if (value == null) {
          field.set(objectWithMockFields, Mockito.spy(field.getType()));
        } else if (!Mockito.mockingDetails(value).isSpy()) {
          field.set(objectWithMockFields, Mockito.spy(value));
        }
      }
    }
  }
}
