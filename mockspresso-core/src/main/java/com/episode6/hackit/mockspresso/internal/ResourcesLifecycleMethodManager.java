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
class ResourcesLifecycleMethodManager {

  static ResourcesLifecycleMethodManager newInstance(Set<TestResource> testResources) {
    LinkedHashMap<TestResource, List<Method>> beforeMethodMap = new LinkedHashMap<>();
    LinkedHashMap<TestResource, List<Method>> afterMethodMap = new LinkedHashMap<>();

    for (TestResource resource : testResources) {
      if (!resource.isLifecycle()) {
        continue;
      }

      Object obj = resource.getObjectWithResources();
      List<Method> allMethods = ReflectUtil.getAllDeclaredMethods(obj.getClass());
      List<Method> beforeMethods = new LinkedList<>();
      List<Method> afterMethods = new LinkedList<>();

      for (Method method : allMethods) {
        if (isValidBeforeMethod(method)) {
          beforeMethods.add(method);
        } else if (isValidAfterMethod(method)) {
          afterMethods.add(0, method);
        }
      }

      beforeMethodMap.put(resource, beforeMethods);
      afterMethodMap.put(resource, afterMethods);
    }
    return new ResourcesLifecycleMethodManager(beforeMethodMap, afterMethodMap);
  }

  private final Map<TestResource, List<Method>> mBeforeMethods;
  private final Map<TestResource, List<Method>> mAfterMethods;

  ResourcesLifecycleMethodManager(
      Map<TestResource, List<Method>> beforeMethods,
      Map<TestResource, List<Method>> afterMethods) {
    mBeforeMethods = beforeMethods;
    mAfterMethods = afterMethods;
  }

  void callBeforeMethods(Mockspresso mockspresso) throws InvocationTargetException, IllegalAccessException {
    for (Map.Entry<TestResource, List<Method>> entry : mBeforeMethods.entrySet()) {
      for (Method method : entry.getValue()) {
        if (method.getParameterCount() == 0) {
          method.invoke(entry.getKey().getObjectWithResources());
        } else {
          method.invoke(entry.getKey().getObjectWithResources(), mockspresso);
        }
      }
    }
  }

  void callAfterMethods() throws InvocationTargetException, IllegalAccessException {
    ListIterator<Map.Entry<TestResource, List<Method>>> reverseResources =
        new LinkedList<>(mAfterMethods.entrySet()).listIterator(mAfterMethods.size());
    while(reverseResources.hasPrevious()) {
      Map.Entry<TestResource, List<Method>> entry = reverseResources.previous();
      for (Method method : entry.getValue()) {
        method.invoke(entry.getKey().getObjectWithResources());
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
}
