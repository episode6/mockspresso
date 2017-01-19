package com.episode6.hackit.mockspresso.reflect;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests the TypeToken class
 */
@RunWith(DefaultTestRunner.class)
public class TypeTokenTest {

  @Test
  public void testNormalClassesWithConstructors() {
    TypeToken<String> stringToken = new TypeToken<String>() {};
    TypeToken<Integer> intToken = new TypeToken<Integer>() {};


    assertThat(stringToken.getType())
        .isEqualTo(String.class);
    assertThat((Class)stringToken.getRawType())
        .isEqualTo(String.class);
    assertThat(intToken.getType())
        .isEqualTo(Integer.class);
    assertThat((Class)intToken.getRawType())
        .isEqualTo(Integer.class);
  }

  @Test
  public void testNormalClassesWithStaticOfMethod() {
    TypeToken<String> stringToken = TypeToken.of(String.class);
    TypeToken<Integer> intToken = TypeToken.of(Integer.class);


    assertThat(stringToken.getType())
        .isEqualTo(String.class);
    assertThat((Class)stringToken.getRawType())
        .isEqualTo(String.class);
    assertThat(intToken.getType())
        .isEqualTo(Integer.class);
    assertThat((Class)intToken.getRawType())
        .isEqualTo(Integer.class);
  }

  @Test
  public void testSimpleGenerics() {
    TypeToken<List<String>> stringListToken = new TypeToken<List<String>>() {};
    TypeToken<HashMap<String, Integer>> hashmapTypeToken = new TypeToken<HashMap<String, Integer>>() {};

    assertThat(stringListToken.getType()).isInstanceOf(ParameterizedType.class);
    Type[] stringListTypeArgs = ((ParameterizedType)stringListToken.getType()).getActualTypeArguments();
    assertThat(stringListTypeArgs)
        .hasSize(1)
        .containsExactly(String.class);
    assertThat((Class)stringListToken.getRawType())
        .isEqualTo(List.class);

    assertThat(hashmapTypeToken.getType()).isInstanceOf(ParameterizedType.class);
    Type[] hashmapTypeArgs = ((ParameterizedType)hashmapTypeToken.getType()).getActualTypeArguments();
    assertThat(hashmapTypeArgs)
        .hasSize(2)
        .containsExactly(String.class, Integer.class);
    assertThat((Class)hashmapTypeToken.getRawType())
        .isEqualTo(HashMap.class);
  }
}
