package com.episode6.hackit.mockspresso.mockito;

import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.HashMap;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests {@link MockitoMockMaker}
 */
@RunWith(JUnit4.class)
public class MockitoMockMakerTest {

  private MockitoMockMaker mMockMaker;

  @Before
  public void setup() {
    mMockMaker = new MockitoMockMaker();
  }

  @Test
  public void testSimpleMock() {
    Runnable runnable = mMockMaker.makeMock(TypeToken.of(Runnable.class));

    assertThat(runnable).isNotNull();
    assertThat(Mockito.mockingDetails(runnable).isMock()).isTrue();

    runnable.run();
    verify(runnable).run();
  }

  @Test
  public void testGenericMock() {
    TypeToken<HashMap<String, Integer>> typeToken = new TypeToken<HashMap<String, Integer>>() {};

    HashMap<String, Integer> hashMap = mMockMaker.makeMock(typeToken);

    assertThat(hashMap).isNotNull();
    assertThat(Mockito.mockingDetails(hashMap).isMock()).isTrue();

    when(hashMap.get("sup")).thenReturn(12);
    Integer retVal = hashMap.get("sup");
    verify(hashMap).get("sup");
    assertThat(retVal).isEqualTo(12);
  }
}
