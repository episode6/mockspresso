package com.episode6.hackit.mockspresso.reflect.testobject;

import com.episode6.hackit.mockspresso.reflect.TypeToken;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class JavaTypeTokens {
  public static TypeToken<String> stringToken = TypeToken.of(String.class);
  public static TypeToken<Integer> intToken = TypeToken.of(Integer.class);

  public static TypeToken<List<String>> stringListToken = new TypeToken<List<String>>() {};
  public static TypeToken<Map<String, Integer>> stringIntMapToken = new TypeToken<Map<String, Integer>>() {};

  public static TypeToken<LinkedList<String>> stringLinkedListToken = new TypeToken<LinkedList<String>>() {};
  public static TypeToken<HashMap<String, Integer>> stringIntHashMapToken = new TypeToken<HashMap<String, Integer>>() {};
}
