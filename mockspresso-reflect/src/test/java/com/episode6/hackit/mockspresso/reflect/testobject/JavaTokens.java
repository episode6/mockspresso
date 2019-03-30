package com.episode6.hackit.mockspresso.reflect.testobject;

import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.NamedAnnotationLiteral;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Some type tokens and dependency key, declared in java, used by kotlin tests for comparisons
 */
public class JavaTokens {
  public static TypeToken<String> stringToken = TypeToken.of(String.class);
  public static DependencyKey<String> stringKey = DependencyKey.of(stringToken);

  public static TypeToken<Integer> intToken = TypeToken.of(Integer.class);
  public static DependencyKey<Integer> intKey = DependencyKey.of(intToken, new NamedAnnotationLiteral("intKey"));

  // maps to List<String> in kotlin
  public static TypeToken<List<? extends String>> stringListToken = new TypeToken<List<? extends String>>() {};
  public static DependencyKey<List<? extends String>> stringListKey = DependencyKey.of(stringListToken);

  // maps to Map<String, Int> in kotlin
  public static TypeToken<Map<String, ? extends Integer>> stringIntMapToken = new TypeToken<Map<String, ? extends Integer>>() {};
  public static DependencyKey<Map<String, ? extends Integer>> stringIntMapKey = DependencyKey.of(stringIntMapToken);

  // maps to MutableList<String> in kotlin
  public static TypeToken<List<String>> stringMutableListToken = new TypeToken<List<String>>() {};
  public static DependencyKey<List<String>> stringMutableListKey = DependencyKey.of(stringMutableListToken);

  // maps to MutableMap<String, Integer> in kotlin
  public static TypeToken<Map<String, Integer>> stringIntMutableMapToken = new TypeToken<Map<String, Integer>>() {};
  public static DependencyKey<Map<String, Integer>> stringIntMutableMapKey = DependencyKey.of(stringIntMutableMapToken);

  public static TypeToken<LinkedList<String>> stringLinkedListToken = new TypeToken<LinkedList<String>>() {};
  public static DependencyKey<LinkedList<String>> stringLinkedListKey = DependencyKey.of(stringLinkedListToken);

  public static TypeToken<HashMap<String, Integer>> stringIntHashMapToken = new TypeToken<HashMap<String, Integer>>() {};
  public static DependencyKey<HashMap<String, Integer>> stringIntHashMapKey = DependencyKey.of(stringIntHashMapToken);

  // type  token that is impossible to create/replicate in kotlin due to the `out` keyword
  // on the type variable in TestGenericKtInterface.
  public static TypeToken<TestGenericKtInterface<String>> genericInterfaceIllegalToken = new TypeToken<TestGenericKtInterface<String>>() {};
}
