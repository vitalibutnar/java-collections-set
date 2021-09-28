package com.endava.internship.collections;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentSet<E> implements Set<E> {
    private final StudentMap<E, Integer> map;
    private static final Integer DEFAULT_VALUE = -1;

    public StudentSet() {
        map = new StudentMap<>();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public Object[] toArray() {
        int q = 0;
        Object[] result = new Object[map.size()];
        for (E e : this) result[q++] = e;
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] es) {
        es = es.length >= map.size() ? es : (T[]) Array.newInstance(es.getClass().getComponentType(), map.size());
        for (int i = 0; i < Math.min(es.length, map.size()); i++) {
            es[i] = (T) iterator().next();
        }
        return es;
    }

    @Override
    public boolean add(E t) {
        return map.put(t, DEFAULT_VALUE) == null;
    }

    @Override
    public boolean remove(Object o) {
        return DEFAULT_VALUE.equals(map.remove(o));
    }

    @Override
    public void clear() {
        map.clear();
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

    @Override
    public String toString() {
        String setStr = map.keySet().stream().filter(Objects::nonNull).collect(Collectors.toList()).toString();
        return "StudentSet{" + setStr.substring(1, setStr.length() - 1) + "}";
    }
}