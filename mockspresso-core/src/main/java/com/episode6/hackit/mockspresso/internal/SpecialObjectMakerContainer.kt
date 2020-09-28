package com.episode6.hackit.mockspresso.internal

import com.episode6.hackit.mockspresso.api.DependencyProvider
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker
import com.episode6.hackit.mockspresso.reflect.DependencyKey
import java.util.*

/**
 * A container for special object makers. Includes a parent maker (usually
 * another instance of SpecialObjectMakerContainer) that is checked last.
 */
internal class SpecialObjectMakerContainer : SpecialObjectMaker {
  private var parent: SpecialObjectMaker? = null
  private val makers: MutableList<SpecialObjectMaker> = LinkedList()

  fun deepCopy(): SpecialObjectMakerContainer = SpecialObjectMakerContainer().also {
    it.setParentMaker(parent)
    it.addAll(makers)
  }

  fun setParentMaker(parentMaker: SpecialObjectMaker?) {
    parent = parentMaker
  }

  fun add(specialObjectMaker: SpecialObjectMaker) {
    makers.add(specialObjectMaker)
  }

  fun addAll(specialObjectMakers: Collection<SpecialObjectMaker>) {
    makers.addAll(specialObjectMakers)
  }

  override fun canMakeObject(key: DependencyKey<*>): Boolean = when {
    findMakerFor(key) != null -> true
    else                      -> parent?.canMakeObject(key) ?: false
  }

  override fun makeObject(dependencyProvider: DependencyProvider, key: DependencyKey<*>): Any? =
      (findMakerFor(key) ?: parent)?.makeObject(dependencyProvider, key)

  private fun findMakerFor(key: DependencyKey<*>) = (makers.firstOrNull { it.canMakeObject(key) })
}
