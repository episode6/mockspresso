package com.episode6.hackit.mockspresso.exception;

/**
 * Error thrown when a mockspresso plugin or feature is activated for which the project doesn't
 * contain the underlying dependency for. I.e. building a Mockspresso instance with mocker().easyMock()
 * without declaring a dependency on 'org.easymock:easymock'.
 *
 * This exception will usually wrap an underlying {@link NoClassDefFoundError}
 */
public class MissingDependencyError extends Error {

  public MissingDependencyError(String requiredDependency, Throwable cause) {
    super("Missing Dependency: " + requiredDependency, cause);
  }
}
