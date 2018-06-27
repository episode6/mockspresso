package com.episode6.hackit.mockspresso.quick;

import com.episode6.hackit.mockspresso.Mockspresso;

/**
 * Old entry-point for quick mockspresso extension. Will be removed in a future version.
 * Use {@link BuildQuickMockspresso} instead
 */
@Deprecated
public class QuickBuildMockspresso {

  /**
   * Old entry-point for quick mockspresso extension. Will be removed in a future version.
   * Use {@link BuildQuickMockspresso#with()} instead
   * @return a new instance of {@link QuickMockspresso.Builder}
   * @deprecated User {@link BuildQuickMockspresso#with()} instead
   */
  @Deprecated
  public static QuickMockspresso.Builder with() {
    return BuildQuickMockspresso.with();
  }

  /**
   * Old entry-point for quick mockspresso extension. Will be removed in a future version.
   * Use {@link BuildQuickMockspresso#upon(Mockspresso)} instead
   * @param mockspresso The {@link Mockspresso} instance to upon
   * @return a new instance of {@link QuickMockspresso.Builder} build upon the supplied
   * {@link Mockspresso} instance
   * @deprecated User {@link BuildQuickMockspresso#upon(Mockspresso)} instead
   */
  @Deprecated
  public static QuickMockspresso.Builder buildUpon(Mockspresso mockspresso) {
    return BuildQuickMockspresso.upon(mockspresso);
  }
}
