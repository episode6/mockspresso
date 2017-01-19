package com.episode6.hackit.mockspresso.reflect;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

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
}
