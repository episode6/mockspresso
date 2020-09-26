package com.episode6.hackit.mockspresso.easymock;

import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;

import static com.episode6.hackit.mockspresso.easymock.Conditions.mockCondition;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link EasyMockMockMaker}
 */
@RunWith(JUnit4.class)
public class EasyMockMockMakerTest {

  private EasyMockMockerConfig mMockMaker;

  @Before
  public void setup() {
    mMockMaker = new EasyMockMockerConfig();
  }

  @Test
  public void testSimpleMock() {
    Runnable runnable = mMockMaker.makeMock(TypeToken.of(Runnable.class));

    assertThat(runnable)
        .isNotNull()
        .is(mockCondition());

    // verify runnable can be run without a replay
    runnable.run();
  }

  @Test
  public void testGenericMock() {
    TypeToken<HashMap<String, Integer>> typeToken = new TypeToken<HashMap<String, Integer>>() {};

    HashMap<String, Integer> hashMap = mMockMaker.makeMock(typeToken);

    assertThat(hashMap)
        .isNotNull()
        .is(mockCondition());

    // verify runnable can be run without a replay and returns a sane default
    Integer retVal = hashMap.get("sup");
    assertThat(retVal).isNull();
  }
}
