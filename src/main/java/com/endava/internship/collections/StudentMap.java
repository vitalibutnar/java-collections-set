package com.endava.internship.collections;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

public class StudentMap<K, V> implements Map<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 8;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int size;
    private Node<K, V>[] map;

    @SuppressWarnings("unchecked")
    public StudentMap() {
        map = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int hash = hash(key);
        int index = (map.length - 1) & hash;
        Node<K, V> node = map[index];
        return node != null && node.containsKey(key);
    }

    @Override
    public boolean containsValue(Object o) {
        return Arrays.stream(map).filter(Objects::nonNull).anyMatch(elem -> elem.containsValue(o));
    }

    @Override
    public V get(Object o) {
        int hash = hash(o);
        Node<K, V> node = map[(map.length - 1) & hash];
        if (node != null)
            do {
                if (node.hash == hash && node.key.equals(o))
                    return node.value;
            } while ((node = node.next) != null);
        return null;
    }

    @Override
    public V put(K key, V value) {
        int hash = hash(key);
        int index = (map.length - 1) & hash;
        if (map[index] == null) {
            map[index] = new Node<>(hash, key, value, null);
        } else {
            Node<K, V> node = map[index];
            Node<K, V> temp;
            do {
                if (node.hash == hash && node.key != null && node.key.equals(key)) {
                    V oldVal = node.value;
                    node.value = value;
                    return oldVal;
                }
                temp = node;
                node = node.next;
            } while (node != null);
            temp.next = new Node<>(hash, key, value, null);
        }
        size++;
        resize();
        return null;
    }

    @Override
    public V remove(Object key) {
        int hash = hash(key);
        int index = (map.length - 1) & hash;
        if (map[index] == null)
            return null;
        else {
            return removeFromNode(key, hash, index);
        }
    }

    private V removeFromNode(Object key, int hash, int index) {
        Node<K, V> node = map[index];
        Node<K, V> prev = null;
        do {
            if (node.hash == hash && node.key.equals(key)) {
                if (prev == null)
                    map[index] = (node.next == null) ? null : node.next;
                else
                    prev.next = (node.next == null) ? null : node.next;
                size--;
                return node.value;
            }
            prev = node;
            node = node.next;
        } while (node != null);
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        map.forEach(this::put);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        if (size > 0 && map != null) {
            map = new Node[map.length];
            size = 0;
        }
    }

    @Override
    public Set<K> keySet() {
        return new KeySet();
    }

    @Override
    public Collection<V> values() {
        return new ValueSet();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        //Ignore this for homework
        throw new UnsupportedOperationException();
    }

    private static int hash(Object val) {
        int h;
        return (val == null) ? 0 : (h = val.hashCode()) ^ (h >>> 16);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        if ((float) size / (float) map.length < DEFAULT_LOAD_FACTOR)
            return;
        Node<K, V>[] tmp = new Node[map.length * 2];
        for (Node<K, V> node : map) {
            if (node != null)
                tmp[(map.length * 2 - 1) & node.hash] = node;
        }
        map = tmp;
    }

    @Override
    public String toString() {
        String setStr = Arrays.toString(Arrays.stream(map).filter(Objects::nonNull).toArray());
        return "StudentMap{" + setStr.substring(1, setStr.length() - 1) +
                "}";
    }
    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public boolean containsKey(Object key) {
            if (key == null && this.key == null) return true;
            else if (key == null || this.key == null) return false;
            //compare current elem of the Node with new Object
            boolean eqlThis = (hash(key) == this.hash && key.equals(this.key));
            return next == null
                    ? eqlThis
                    : eqlThis || next.containsKey(key);
        }

        public boolean containsValue(Object value) {
            if (value == null && this.value == null) return true;
            else if (value == null || this.value == null) return false;
            //compare current elem of the Node with new Object
            boolean eqlThis = value.equals(this.value);
            return next == null ?
                    eqlThis
                    : eqlThis || next.containsValue(value);
        }

        @Override
        public String toString() {
            return "{" +
                    "key=" + key +
                    ", value=" + value + "}" +
                    (next == null ? "" : ", " + next);
        }
    }

    private class ValueSet extends AbstractSet<V> {
        @Override
        public Iterator<V> iterator() {
            return new StudentMapValueIterator();
        }

        @Override
        public int size() {
            return size;
        }
    }

    private class KeySet extends AbstractSet<K> {
        @Override
        public Iterator<K> iterator() {
            return new StudentMapKeyIterator();
        }

        @Override
        public int size() {
            return size;
        }

        public boolean contains(Object key) {
            return StudentMap.this.containsKey(key);
        }
    }

    private class StudentMapValueIterator extends StudentMapIterator implements Iterator<V> {
        @Override
        public V next() {
            return nextNode().value;
        }
    }

    private class StudentMapKeyIterator extends StudentMapIterator implements Iterator<K> {
        @Override
        public K next() {
            return nextNode().key;
        }
    }

    private class StudentMapIterator {

        Node<K, V> next = null;
        Node<K, V> current = null;
        int index = 0;

        StudentMapIterator() {
            while (next == null && index < map.length) {
                if (map[index] != null) {
                    next = map[index];
                }
                index++;
            }
        }

        public boolean hasNext() {
            return next != null;
        }

        public Node<K, V> nextNode() {
            if (next == null)
                throw new NoSuchElementException();
            current = next;
            if (current.next != null)
                next = current.next;
            else {
                next = null;
                while (index < map.length && next == null) {
                    if (map[index] != null) {
                        next = map[index];
                    }
                    index++;
                }
            }
            return current;
        }

        public void remove() {
            if (current == null)
                throw new IllegalStateException();
            int hash = current.hash;
            int position = (map.length - 1) & hash;
            if (map[position].next == null) {
                map[position] = null;
                size--;
            } else {
                removeFromNode(current.key, hash, position);
            }
        }
    }
}