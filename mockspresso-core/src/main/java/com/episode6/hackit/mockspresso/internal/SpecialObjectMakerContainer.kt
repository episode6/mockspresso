package com.episode6.hackit.mockspresso.internal

import com.episode6.hackit.mockspresso.api.DependencyProvider
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker
import com.episode6.hackit.mockspresso.exception.BrokenSpecialObjectMakerException
import com.episode6.hackit.mockspresso.reflect.DependencyKey
import java.util.*

/**
 * A container for special object makers. Includes a parent maker (usually
 * another instance of SpecialObjectMakerContainer) that is checked last.
 */
internal class SpecialObjectMakerContainer {
  private var parent: SpecialObjectMakerContainer? = null
  private val makers: MutableList<SpecialObjectMaker> = LinkedList()

  fun deepCopy(): SpecialObjectMakerContainer = SpecialObjectMakerContainer().also {
    it.setParentMaker(parent)
    it.addAll(makers)
  }

  fun setParentMaker(parentMaker: SpecialObjectMakerContainer?) {
    parent = parentMaker
  }

  fun add(specialObjectMaker: SpecialObjectMaker) {
    makers.add(specialObjectMaker)
  }

  fun addAll(specialObjectMakers: Collection<SpecialObjectMaker>) {
    makers.addAll(specialObjectMakers)
  }

  fun canMakeObject(key: DependencyKey<*>): Boolean = when {
    findMakerFor(key) != null -> true
    else                      -> parent?.canMakeObject(key) ?: false
  }

  fun <T : Any> makeObject(dependencyProvider: DependencyProvider, key: DependencyKey<T>): T? = when (val maker = findMakerFor(key)) {
    null -> parent?.makeObject(dependencyProvider, key)
    else -> maker.makeOrThrow(dependencyProvider, key)
  }

  private fun findMakerFor(key: DependencyKey<*>) = (makers.firstOrNull { it.canMakeObject(key) })

  @Suppress("UNCHECKED_CAST")
  private fun <T : Any> SpecialObjectMaker.makeOrThrow(dependencyProvider: DependencyProvider, key: DependencyKey<T>): T? {
    val value: Any? = makeObject(dependencyProvider, key)
    if (value != null && !key.typeToken.rawType.isAssignableFrom(value.javaClass)) {
      throw BrokenSpecialObjectMakerException(this, key, value)
    }
    return value as T?
  }
}
