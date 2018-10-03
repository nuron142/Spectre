package `in`.sunil.spectre.spectre.cache

import android.util.Log
import java.util.*

/**
 * Created by Sunil on 9/28/18.
 */

abstract class LruCache<K, V> {

    private val map: HashMap<K, Node<K, V>>
    private val head: Node<K, V>
    private val tail: Node<K, V>

    private val maxCacheEntries: Int
    private val maxCacheSize: Long

    private var currentCacheEntryCount: Int = 0
    private var currentCacheSize: Long = 0

    protected class Node<X, Y> {

        var key: X? = null
        var value: Y? = null
        var size: Long = 0

        @Transient
        var pre: Node<X, Y>? = null
        @Transient
        var next: Node<X, Y>? = null

        constructor(key: X?, value: Y?, size: Long) {

            this.key = key
            this.value = value
            this.size = size

            this.pre = null
            this.next = null
        }
    }

    constructor(maxCacheEntries: Int, maxCacheSize: Long) {

        this.maxCacheEntries = maxCacheEntries
        this.maxCacheSize = maxCacheSize

        map = HashMap()
        head = Node(null, null, 0)
        tail = Node(null, null, 0)
        head.next = tail
        tail.pre = head
    }

    private fun addNode(node: Node<K, V>?) {

        node?.next = head.next
        node?.next?.pre = node
        node?.pre = head
        head.next = node

        currentCacheSize += node?.size ?: 0
    }

    private fun deleteNode(node: Node<K, V>?) {

        node?.pre?.next = node?.next
        node?.next?.pre = node?.pre

        currentCacheSize -= node?.size ?: 0
    }

    private fun entryRemoved(node: Node<K, V>?) {

        Log.d("Sunil", "Testing4 deleteNode " + node?.key)

        deleteNode(node)
        entryRemovedFromCache(node?.key, node?.value)
    }

    private fun entryAdded(node: Node<K, V>?) {

        addNode(node)

        entryAddedToCache(node?.key, node?.value)
    }

    private fun updateEntryValue(node: Node<K, V>?) {

        deleteNode(node)
        entryAdded(node)
    }

    protected fun get(key: K): V? {

        val node = map[key]

        if (node != null) {
            val result = node.value
            updateEntryValue(node)
            return result
        }

        return null
    }

    protected fun set(key: K, value: V, sizeOfObject: Long) {

        var node = map[key]

        if (node != null) {

            node.value = value
            node.size = sizeOfObject

            updateEntryValue(node)

        } else {

            node = Node(key, value, sizeOfObject)
            map[key] = node

            val newSize = currentCacheSize + sizeOfObject

            if (currentCacheEntryCount < maxCacheEntries && newSize <= maxCacheSize) {

                currentCacheEntryCount++
                entryAdded(node)

            } else {

                map.remove(tail.pre?.key)
                entryRemoved(tail.pre)
                entryAdded(node)
            }
        }
    }

    fun contains(key: K): Boolean {

        return map.contains(key)
    }

    protected fun getOrderedEntryList(): List<Node<K, V>> {

        val list = mutableListOf<Node<K, V>>()

        tail.pre?.let { node ->
            list.add(node)
        }

        val tailKey = tail.pre?.key
        val headKey = head.next?.key

        map.forEach { (key, value) ->

            if (key != null && key != tailKey && key != headKey) {
                list.add(value)
            }
        }

        head.next?.let { node ->
            list.remove(node)
            list.add(node)
        }

        return list
    }

    protected abstract fun entryRemovedFromCache(key: K?, value: V?)

    protected abstract fun entryAddedToCache(key: K?, value: V?)

}
