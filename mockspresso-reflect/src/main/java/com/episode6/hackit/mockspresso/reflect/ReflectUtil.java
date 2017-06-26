package com.episode6.hackit.mockspresso.reflect;

import com.episode6.hackit.mockspresso.reflect.exception.MultipleQualifierAnnotationException;

import javax.annotation.Nullable;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Java reflect utils
 */
public class ReflectUtil {

  public static List<Field> getAllDeclaredFields(Class<?> clazz) {
    List<Field> fieldList = new LinkedList<>();
    fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
    if (clazz.getSuperclass() != null) {
      fieldList.addAll(getAllDeclaredFields(clazz.getSuperclass()));
    }
    return fieldList;
  }

  public static List<Method> getAllDeclaredMethods(Class<?> clazz) {
    return getAllDeclaredMethodsInternal(clazz, new HashSet<MethodDesc>());
  }

  private static List<Method> getAllDeclaredMethodsInternal(Class<?> clazz, Set<MethodDesc> methodSet) {
    // we want our list to be sorted so that superclass methods come before sub-class methods
    // and we also want to ensure we dont duplicate a method that exists on both the sub and super-class.
    List<Method> methodList = new LinkedList<>();
    for (Method method : clazz.getDeclaredMethods()) {
      MethodDesc methodDesc = new MethodDesc(method);
      if (!methodSet.contains(methodDesc)) {
        methodList.add(method);
        methodSet.add(methodDesc);
      }
    }

    if (clazz.getSuperclass() != null) {
      methodList.addAll(0, getAllDeclaredMethodsInternal(clazz.getSuperclass(), methodSet));
    }
    return methodList;
  }

  public static @Nullable Annotation findQualifierAnnotation(Field field) {
    return findQualifierAnnotation(field.getDeclaredAnnotations(), "field: " + field.getName());
  }

  public static @Nullable Annotation findQualifierAnnotation(Method method) {
    return findQualifierAnnotation(method.getDeclaredAnnotations(), "method: " + method.getName());
  }

  public static @Nullable Annotation findQualifierAnnotation(Annotation[] annotations, String description) {
    Annotation found = null;
    for (Annotation annotation : annotations) {
      Qualifier qualifier = annotation.annotationType().getDeclaredAnnotation(Qualifier.class);
      if (qualifier == null) {
        continue;
      }
      if (found != null) {
        throw new MultipleQualifierAnnotationException(description);
      }
      found = annotation;
    }
    return found;
  }

  public static boolean isAnyAnnotationPresent(Field field, Collection<Class<? extends Annotation>> annotations) {
    for (Class<? extends Annotation> annotation : annotations) {
      if (field.isAnnotationPresent(annotation)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isAnyAnnotationPresent(Method method, Collection<Class<? extends Annotation>> annotations) {
    for (Class<? extends Annotation> annotation : annotations) {
      if (method.isAnnotationPresent(annotation)) {
        return true;
      }
    }
    return false;
  }

  private static class MethodDesc {
    final String methodName;
    final Class<?>[] paramTypes;

    MethodDesc(Method method) {
      methodName = method.getName();
      paramTypes = method.getParameterTypes();
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) return true;
      if (object == null || getClass() != object.getClass()) return false;

      MethodDesc that = (MethodDesc) object;

      if (!methodName.equals(that.methodName)) return false;
      return Arrays.equals(paramTypes, that.paramTypes);
    }

    @Override
    public int hashCode() {
      int result = methodName.hashCode();
      result = 31 * result + Arrays.hashCode(paramTypes);
      return result;
    }
  }
}
