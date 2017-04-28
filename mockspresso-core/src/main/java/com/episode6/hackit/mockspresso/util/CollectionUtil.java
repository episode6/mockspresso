package com.episode6.hackit.mockspresso.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Static utilities for collections
 */
public class CollectionUtil {

  @SafeVarargs
  public static <T> List<T> concatList(T... newItems) {
    return concatList(null, newItems);
  }

  /**
   * Create a new list, add the collection, then add the newItems. The original collection is not modified.
   * Only non-null newItems are added.
   * @param collection The collection to build from
   * @param newItems The new items to add
   * @param <T> the type of collection
   * @return a new LinkedList containing all items
   */
  @SafeVarargs
  public static <T> List<T> concatList(Collection<T> collection, T... newItems) {
    LinkedList<T> newList = collection == null ? new LinkedList<T>() : new LinkedList<T>(collection);
    for (int i = 0; i<newItems.length; i++) {
      if (newItems[i] != null) {
        newList.add(newItems[i]);
      }
    }
    return newList;
  }
}
