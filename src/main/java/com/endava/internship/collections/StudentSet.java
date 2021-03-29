package com.endava.internship.collections;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class StudentSet<E> implements Set<E> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] set;
    private int size;


    @SuppressWarnings({"unchecked"})
    public StudentSet() {
        set = new Node[DEFAULT_INITIAL_CAPACITY];
        size = 0;
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
    public boolean contains(Object o) {
        int hash = hash(o), i;
        return set[i = ((set.length - 1) & hash)] != null && set[i].contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return new StudentSetIterator();
    }

    @Override
    public Object[] toArray() {
        int q = 0;
        Object[] result = new Object[size];
        for (E e : this) {
            result[q++] = e;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] es) {
        es = es.length >= size ? es : (T[]) Array.newInstance(es.getClass().getComponentType(), size);
        for (int i = 0; i < Math.min(es.length, size); i++) {
            es[i] = (T) iterator().next();
        }
        return es;
    }

    @Override
    public boolean add(E t) {
        int hash = hash(t);
        int index = (set.length - 1) & hash;
        if (set[index] == null) {
            set[index] = new Node<>(hash, t, null);
        } else {
            Node<E> node = set[index];
            Node<E> temp;
            do {
                if (node.hash == hash && node.value != null && node.value.equals(t))
                    return false;
                temp = node;
                node = node.next;
            } while (node != null);
            temp.next = new Node<>(hash, t, null);
        }
        size++;
        resize();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = hash(o);
        int index = (set.length - 1) & hash;
        if (set[index] == null)
            return false;
        else {
            return removeFromNode(o, hash, index);
        }
    }

    private boolean removeFromNode(Object o, int hash, int index) {
        Node<E> node = set[index];
        Node<E> prev = null;
        do {
            if (node.hash == hash && node.value.equals(o)) {
                if (prev == null)
                    set[index] = (node.next == null) ? null : node.next;
                else
                    prev.next = (node.next == null) ? null : node.next;
                size--;
                return true;
            }
            prev = node;
            node = node.next;
        } while (node != null);
        return false;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void clear() {
        if (size > 0 && set != null) {
            set = new Node[set.length];
            size = 0;
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        long count;
        count = collection.stream().filter(this::add).count();
        return count > 0;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        //Ignore this for homework
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        //Ignore this for homework
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        //Ignore this for homework
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        if ((float) size / (float) set.length < DEFAULT_LOAD_FACTOR)
            return;
        Node<E>[] tmp = new Node[set.length * 2];
        for (Node<E> node : set) {
            if (node != null)
                tmp[(set.length * 2 - 1) & node.hash] = node;
        }
        set = tmp;
    }

    static int hash(Object val) {
        int h;
        return (val == null) ? 0 : (h = val.hashCode()) ^ (h >>> 16);
    }

    @Override
    public String toString() {
        String setStr = Arrays.toString(Arrays.stream(set).filter(Objects::nonNull).toArray());
        return "StudentSet{" + setStr.substring(1, setStr.length() - 1) +
                "}";
    }

    private static class Node<V> {
        int hash;
        V value;
        Node<V> next;

        Node(int hash, V value, Node<V> next) {
            this.hash = hash;
            this.value = value;
            this.next = next;
        }

        public boolean contains(Object value) {
            if (value == null && this.value == null) return true;
            else if (value == null || this.value == null) return false;
            //compare current elem of the Node with new Object
            boolean eqlThis = (hash(value) == this.hash && value.equals(this.value));
            return next == null
                    ? eqlThis
                    : eqlThis || next.contains(value);
        }

        @Override
        public String toString() {
            return value.toString() + (next == null ? "" : ", " + next);
        }
    }

    private class StudentSetIterator implements Iterator<E> {

        Node<E> next = null;
        Node<E> current = null;
        int index = 0;

        StudentSetIterator() {
            while (next == null && index < set.length) {
                if (set[index] != null) {
                    next = set[index];
                }
                index++;
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public E next() {
            if (next == null)
                throw new NoSuchElementException();
            current = next;
            if (current.next != null)
                next = current.next;
            else {
                next = null;
                while (index < set.length && next == null) {
                    if (set[index] != null) {
                        next = set[index];
                    }
                    index++;
                }
            }
            return current.value;
        }

        @Override
        public void remove() {
            if (current == null)
                throw new IllegalStateException();
            if (set[index - 1].next == null)
                set[index - 1] = null;
            else {
                removeFromNode(current.value, current.hash, index - 1);
            }
        }

        //not implemented
        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Iterator.super.forEachRemaining(action);
        }
    }
}