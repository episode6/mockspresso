package com.episode6.hackit.mockspresso.mockito.powermock;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.reflect.ReflectUtil;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;

import java.lang.reflect.Field;
import java.util.List;

/**
 * A FieldPreparer for Powermock + Mockito. Powermock doesn't seem to offer a nice way
 * to init mocks on an arbitrary object that isn't the testClass (and the runner/rule
 * deals with those). So to handle testResources, we use this custom FieldPreparer that
 * expects some mocks/spies to already be initialized (and doesn't touch those).
 *
 * We use {@link PowerMockito#mock(Class)} to create mocks and {@link Mockito#spy(Object)}/{@link Mockito#spy(Class)}
 * to create spies.
 */
public class PowerMockitoFieldPreparer implements MockerConfig.FieldPreparer {

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
