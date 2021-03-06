package com.episode6.hackit.mockspresso.easymock.powermock;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.reflect.ReflectUtil;
import org.easymock.Mock;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.easymock.annotation.MockNice;
import org.powermock.api.easymock.annotation.MockStrict;

import java.lang.reflect.Field;
import java.util.List;

/**
 * A FieldPreparer for Powermock + EasyMock. Powermock doesn't seem to offer a nice way
 * to init mocks on an arbitrary object that isn't the testClass (and the runner/rule
 * deals with those). So to handle testResources, we use this custom FieldPreparer that
 * expects some mocks/spies to already be initialized (and doesn't touch those).
 *
 * We use {@link PowerMock#createMock(Class)} to create mocks.
 *
 * @deprecated This functionality is internal implementation and shouldn't be exposed
 *
 * This class will be marked internal/protected in a future release
 */
@Deprecated
public class EasyPowerMockFieldPreparer implements MockerConfig.FieldPreparer {
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

      if (field.get(objectWithMockFields) != null) {
        continue;
      }

      if (field.isAnnotationPresent(Mock.class) || field.isAnnotationPresent(org.powermock.api.easymock.annotation.Mock.class)) {
        field.set(objectWithMockFields, PowerMock.createMock(field.getType()));
      } else if (field.isAnnotationPresent(MockNice.class)) {
        field.set(objectWithMockFields, PowerMock.createNiceMock(field.getType()));
      } else if (field.isAnnotationPresent(MockStrict.class)) {
        field.set(objectWithMockFields, PowerMock.createStrictMock(field.getType()));
      }
    }
  }
}
