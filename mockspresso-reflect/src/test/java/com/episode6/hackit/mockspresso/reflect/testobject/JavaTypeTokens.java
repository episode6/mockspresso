package com.episode6.hackit.mockspresso.reflect.testobject;

import com.episode6.hackit.mockspresso.reflect.TypeToken;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Some type tokens, declared in java, used by kotlin tests
 */
public class JavaTypeTokens {
  public static TypeToken<String> stringToken = TypeToken.of(String.class);
  public static TypeToken<Integer> intToken = TypeToken.of(Integer.class);

  // maps to List<String> in kotlin
  public static TypeToken<List<? extends String>> stringListToken = new TypeToken<List<? extends String>>() {};

  // maps to Map<String, Int> in kotlin
  public static TypeToken<Map<String, ? extends Integer>> stringIntMapToken = new TypeToken<Map<String, ? extends Integer>>() {};

  // maps to MutableList<String> in kotlin
  public static TypeToken<List<String>> stringMutableListToken = new TypeToken<List<String>>() {};

  // maps to MutableMap<String, Integer> in kotlin
  public static TypeToken<Map<String, Integer>> stringIntMutableMapToken = new TypeToken<Map<String, Integer>>() {};

  public static TypeToken<LinkedList<String>> stringLinkedListToken = new TypeToken<LinkedList<String>>() {};
  public static TypeToken<HashMap<String, Integer>> stringIntHashMapToken = new TypeToken<HashMap<String, Integer>>() {};

  // type  token that is impossible to create/replicate in kotlin due to the `out` keyword
  // on the type variable in TestGenericKtInterface.
  public static TypeToken<TestGenericKtInterface<String>> genericInterfaceIllegalToken = new TypeToken<TestGenericKtInterface<String>>() {};
}
