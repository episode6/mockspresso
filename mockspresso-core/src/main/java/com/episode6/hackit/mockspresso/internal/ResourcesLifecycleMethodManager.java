package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.reflect.ReflectUtil;
import org.junit.After;
import org.junit.Before;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Handles calling the setup and teardown methods defined in TestResource objects.
 */
class ResourcesLifecycleMethodManager implements ResourcesLifecycleComponent {
  private final List<TestResource> mTestResources;
  private final Map<TestResource, LifecycleMethods> mMethodCache;

  ResourcesLifecycleMethodManager(
      Collection<TestResource> testResources) {
    mTestResources = new LinkedList<>(testResources);
    mMethodCache = new HashMap<TestResource, LifecycleMethods>() {
      @Override
      public LifecycleMethods get(Object key) {
        if (containsKey(key)) {
          return super.get(key);
        }

        TestResource realKey = (TestResource) key;
        LifecycleMethods methods = LifecycleMethods.getFor(realKey);
        put(realKey, methods);
        return methods;
      }
    };
  }

  @Override
  public void setup(Mockspresso mockspresso) {
    try {
      callBeforeMethods(mockspresso);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void teardown() {
    try {
      callAfterMethods();
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private void callBeforeMethods(Mockspresso mockspresso) throws InvocationTargetException, IllegalAccessException {
    for (TestResource resource : mTestResources) {
      Object obj = resource.getObjectWithResources();
      List<Method> beforeMethods = mMethodCache.get(resource).beforeMethods;
      for (Method method : beforeMethods) {
        if (method.getParameterCount() == 0) {
          method.invoke(obj);
        } else {
          method.invoke(obj, mockspresso);
        }
      }
    }
  }

  private void callAfterMethods() throws InvocationTargetException, IllegalAccessException {
    // reverse order test resources.
    ListIterator<TestResource> testIterator = mTestResources.listIterator(mTestResources.size());
    while (testIterator.hasPrevious()) {
      TestResource resource = testIterator.previous();
      Object obj = resource.getObjectWithResources();
      List<Method> afterMethods = mMethodCache.get(resource).afterMethods;
      // after methods are already in reverse order (subclasses first)
      for (Method method : afterMethods) {
        method.invoke(obj);
      }
    }
  }

  private static boolean isValidBeforeMethod(Method method) {
    if (!method.isAnnotationPresent(Before.class)) {
      return false;
    }

    int paramCount = method.getParameterCount();
    return paramCount == 0 || (paramCount == 1 && method.getParameterTypes()[0] == Mockspresso.class);
  }

  private static boolean isValidAfterMethod(Method method) {
    return method.isAnnotationPresent(After.class) && method.getParameterCount() == 0;
  }

  private static class LifecycleMethods {

    private static LifecycleMethods getFor(TestResource testResource) {
      List<Method> beforeMethods = new LinkedList<>();
      List<Method> afterMethods = new LinkedList<>();

      if (!testResource.isLifecycle()) {
        return new LifecycleMethods(beforeMethods, afterMethods);
      }

      List<Method> allMethods = ReflectUtil.getAllDeclaredMethods(testResource.getObjectWithResources().getClass());
      for (Method method : allMethods) {
        if (isValidBeforeMethod(method)) {
          beforeMethods.add(method);
        } else if (isValidAfterMethod(method)) {
          // invert order of after methods so that subclass methods come first
          afterMethods.add(0, method);
        }
      }

      return new LifecycleMethods(beforeMethods, afterMethods);
    }

    final List<Method> beforeMethods;
    final List<Method> afterMethods;

    private LifecycleMethods(List<Method> beforeMethods, List<Method> afterMethods) {
      this.beforeMethods = beforeMethods;
      this.afterMethods = afterMethods;
    }
  }
}
