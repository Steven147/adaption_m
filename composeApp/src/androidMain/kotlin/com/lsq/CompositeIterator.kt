package com.lsq

import java.util.Stack


fun main() {

    val subMenu = Menu("Sub menu", arrayListOf(
        MenuItem("sub item 1"), MenuItem("sub item 2")
    ))

    val menu = Menu("All menu", arrayListOf(
        MenuItem("item 1"), subMenu, MenuItem("item 3")
    ))

    val iterator = menu.createIterator()
    while (iterator.hasNext()) {
        iterator.next().print()
    }
}


/**
 * create iterator of component list
 * when one component obtain another component list, create another iterator and add to iterator stack
 */
class CompositeIterator(initIterator: Iterator<MenuComponent>) : Iterator<MenuComponent> {
    private val iteratorStack = Stack<Iterator<MenuComponent>>()

    init {
        iteratorStack.push(initIterator)
    }

    override fun hasNext(): Boolean {
        if (iteratorStack.isEmpty()) return false

        val iterator = iteratorStack.peek()
        if (!iterator.hasNext()) {
            // current iterator empty, move to nex
            iteratorStack.pop()
            return hasNext()
        }
        // current iterator has next
        return true
    }

    override fun next(): MenuComponent {
        if (hasNext()) {
            val iterator = iteratorStack.peek()
            val comp = iterator.next()
            iteratorStack.push(comp.createIterator())
            return comp
        }
        throw NullPointerException()
    }

}

class NullIterator: Iterator<MenuComponent> {
    override fun hasNext(): Boolean = false

    override fun next(): MenuComponent {
        throw NullPointerException()
    }
}


open class MenuComponent(private val name: String) {

    fun print() {
        println("name: $name")
    }

    open fun createIterator(): Iterator<MenuComponent> {
        throw UnsupportedOperationException()
    }
}


class Menu(
    menuName: String, private val menuComponents: ArrayList<MenuComponent>,
): MenuComponent(menuName) {

    override fun createIterator(): Iterator<MenuComponent> {
        return CompositeIterator(menuComponents.iterator())
    }
}

class MenuItem(name: String): MenuComponent(name) {

    override fun createIterator(): Iterator<MenuComponent> = NullIterator()
}