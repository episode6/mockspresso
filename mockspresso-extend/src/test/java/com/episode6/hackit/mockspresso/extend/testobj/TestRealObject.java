package com.episode6.hackit.mockspresso.extend.testobj;

/**
 *
 */
public class TestRealObject {

  private final String name;
  private final TestMockObject mockObject;

  public TestRealObject(String name, TestMockObject mockObject) {
    this.name = name;
    this.mockObject = mockObject;
  }

  public String getName() {
    return name;
  }

  public void doThing() {
    mockObject.doSomething();
  }
}
