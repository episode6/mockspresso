package com.episode6.hackit.mockspresso.util;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link CollectionUtil}
 */
@RunWith(DefaultTestRunner.class)
public class CollectionUtilTest {

  @Test
  public void concatWithNoNewItems() {
    ArrayList<String> originalList = new ArrayList<>();
    originalList.add("hi");
    originalList.add("sup");

    List<String> result = CollectionUtil.concat(originalList);

    assertThat(result)
        .containsExactly("hi", "sup");
  }

  @Test
  public void concatWithNewItems() {
    ArrayList<String> originalList = new ArrayList<>();
    originalList.add("hi");
    originalList.add("sup");

    List<String> result = CollectionUtil.concat(originalList, "nice", "dog");

    assertThat(result)
        .containsExactly("hi", "sup", "nice", "dog");
  }

  @Test
  public void concatWithNewItemsAndNulls() {
    ArrayList<String> originalList = new ArrayList<>();
    originalList.add("hi");
    originalList.add("sup");

    List<String> result = CollectionUtil.concat(originalList, "nice", null, "dog", null);

    assertThat(result)
        .containsExactly("hi", "sup", "nice", "dog");
  }
}
