package com.episode6.hackit.mockspresso.reflect;

import com.episode6.hackit.mockspresso.reflect.testobject.TestGenericKtInterface;
import com.episode6.hackit.mockspresso.reflect.testobject.TestJavaObjectWithKtGeneric;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests the TypeToken class
 */
public class TypeTokenTest {

  public static class TestClass {
    List<String> stringList;
    HashMap<String, Integer> stringIntHashmap;
  }

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

  @Test
  public void testGenericsFromField() throws NoSuchFieldException {
    TypeToken stringListToken = TypeToken.of(TestClass.class.getDeclaredField("stringList").getGenericType());
    TypeToken hashmapTypeToken = TypeToken.of(TestClass.class.getDeclaredField("stringIntHashmap").getGenericType());

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

    TypeToken<List<String>> manualStringListToken = new TypeToken<List<String>>() {};
    TypeToken<HashMap<String, Integer>> manualHashmapTypeToken = new TypeToken<HashMap<String, Integer>>() {};
    assertThat(stringListToken).isEqualTo(manualStringListToken);
    assertThat(hashmapTypeToken).isEqualTo(manualHashmapTypeToken);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testKotlinGeneric() {
    TestJavaObjectWithKtGeneric testObj = new TestJavaObjectWithKtGeneric();
    TypeToken<TestGenericKtInterface<String>> manualToken = new TypeToken<TestGenericKtInterface<String>>() {};
    TypeToken fieldToken = TypeToken.of(testObj.getGenericIfaceField().getGenericType());

    assertThat(manualToken).isEqualTo(fieldToken);
  }
}
