package com.episode6.hackit.mockspresso.easymock.powermock;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.reflect.ReflectUtil;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.easymock.EasyMock;
import org.easymock.Mock;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.easymock.annotation.MockNice;
import org.powermock.api.easymock.annotation.MockStrict;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * A MockerConfig for PowerMock + EasyMock
 *
 * @deprecated This functionality is now exposed by the kotlin extension method `mockByPowerMock()` and its
 * JavaSupport counterpart {@link MockspressoEasyPowerMockPluginsJavaSupport#mockByPowerMock()}
 *
 * This class will be marked internal/protected in a future release
 */
@Deprecated
public class EasyPowerMockMockerConfig implements MockerConfig, MockerConfig.FieldPreparer, MockerConfig.MockMaker {

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
        org.powermock.api.easymock.annotation.Mock.class,
        MockStrict.class,
        MockNice.class);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T makeMock(TypeToken<T> typeToken) {
    // Using a strict mock by default here doesn't make sense for an auto-mocker
    T obj = PowerMock.<T>createNiceMock((Class<T>)typeToken.getRawType());

    // Since no one holds a reference to these mocks (besides the real object being
    // injected), they should be returned already in replay mode.
    EasyMock.replay(obj);
    return obj;
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
