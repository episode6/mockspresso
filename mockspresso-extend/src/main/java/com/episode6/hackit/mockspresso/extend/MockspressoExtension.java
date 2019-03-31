package com.episode6.hackit.mockspresso.extend;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.*;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;

/**
 * Extend these 3 interfaces to build your own Mockspresso extension and own your api.
 * {@link MockspressoExtension}
 * {@link MockspressoExtension.Rule}
 * {@link MockspressoExtension.Builder}
 *
 * Each interface/class is generically typed which is what allows you to extend them whithout needing to override
 * all the default methods.
 *
 * Note: if you're working in a 100% kotlin codebase, this is totally unnecessary and you're better off simply
 * adding extension methods to {@link Mockspresso.Builder}. These interfaces/abstract classes are only needed
 * for java.
 *
 * Usually you will only want to add custom methods to your extension of {@link MockspressoExtension.Builder}
 *
 * @param <BLDR> Should point to your custom extension of {@link MockspressoExtension.Builder}
 */
public interface MockspressoExtension<BLDR extends MockspressoExtension.Builder> extends Mockspresso {

  @Override
  @NotNull BLDR buildUpon();

  /**
   * Custom extension of {@link MockspressoExtension.Rule}
   *
   * @param <BLDR> Should point to your custom extension of {@link MockspressoExtension.Builder}
   */
  interface Rule<BLDR extends MockspressoExtension.Builder> extends Mockspresso.Rule {

    @Override
    @NotNull BLDR buildUpon();
  }

  /**
   * Custom extension of {@link MockspressoExtension.Builder}
   *
   * Extend this interface and add your custom methods to create your own Mockspresso extension.
   *
   * @param <EXT> Should point to your custom extension of {@link MockspressoExtension}
   * @param <RULE> Should point to your custom extension of {@link MockspressoExtension.Rule}
   * @param <BLDR> Should point to your custom extension of {@link MockspressoExtension.Builder}
   */
  interface Builder<
      EXT extends MockspressoExtension,
      RULE extends MockspressoExtension.Rule,
      BLDR extends MockspressoExtension.Builder> extends Mockspresso.Builder {

    @Override
    @NotNull BLDR plugin(@NotNull MockspressoPlugin plugin);

    @Override
    @NotNull BLDR outerRule(@NotNull TestRule testRule);

    @Override
    @NotNull BLDR outerRule(@NotNull MethodRule methodRule);

    @Override
    @NotNull BLDR innerRule(@NotNull TestRule testRule);

    @Override
    @NotNull BLDR innerRule(@NotNull MethodRule methodRule);

    @Override
    @NotNull BLDR testResources(@NotNull Object objectWithResources);

    @Override
    @NotNull BLDR testResourcesWithoutLifecycle(@NotNull Object objectWithResources);

    @Override
    @NotNull BLDR mocker(@NotNull MockerConfig mockerConfig);

    @Override
    @NotNull BLDR injector(@NotNull InjectionConfig injectionConfig);

    @Override
    @NotNull BLDR specialObjectMaker(@NotNull SpecialObjectMaker specialObjectMaker);

    @Override
    @NotNull <T, V extends T> BLDR dependency(@NotNull Class<T> clazz, @Nullable V value);

    @Override
    @NotNull <T, V extends T> BLDR dependency(@NotNull TypeToken<T> typeToken, @Nullable V value);

    @Override
    @NotNull <T, V extends T> BLDR dependency(@NotNull DependencyKey<T> key, @Nullable V value);

    @Override
    @NotNull <T, V extends T> BLDR dependencyProvider(@NotNull Class<T> clazz, @NotNull ObjectProvider<V> value);

    @Override
    @NotNull <T, V extends T> BLDR dependencyProvider(@NotNull TypeToken<T> typeToken, @NotNull ObjectProvider<V> value);

    @Override
    @NotNull <T, V extends T> BLDR dependencyProvider(@NotNull DependencyKey<T> key, @NotNull ObjectProvider<V> value);

    @Override
    @NotNull <T> BLDR realObject(@NotNull Class<T> objectClass);

    @Override
    @NotNull <T> BLDR realObject(@NotNull TypeToken<T> objectToken);

    @Override
    @NotNull <T> BLDR realObject(@NotNull DependencyKey<T> keyAndImplementation);

    @Override
    @NotNull<T> BLDR realObject(@NotNull DependencyKey<T> key, @NotNull Class<? extends T> implementationClass);

    @Override
    @NotNull<T> BLDR realObject(@NotNull DependencyKey<T> key, @NotNull TypeToken<? extends T> implementationToken);

    @Override
    @NotNull EXT build();

    @Override
    @NotNull RULE buildRule();
  }
}
