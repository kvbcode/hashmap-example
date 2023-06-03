package com.cyber.algo;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class CompatibleSet<K> implements Set<K> {
    private final static Object STUB_VALUE = new Object();

    private CompatibleHashMap<K, Object> hashMap = new CompatibleHashMap<>();

    @Override
    public int size() {
        return hashMap.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object obj) {
        return hashMap.get(obj) != null;
    }

    @Override
    public Iterator<K> iterator() {
        return hashMap.keyIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size()];
        int index = 0;
        for (K obj : this) {
            array[index++] = obj;
        }
        return array;
    }

    @Override
    public <T> T[] toArray(T[] array) {
        if (array.length < size()) {
            throw new ArrayIndexOutOfBoundsException("Недостаточный размер массива");
        }
        int index = 0;
        for (K obj : this) {
            array[index++] = (T) obj;
        }
        return array;
    }

    @Override
    public boolean add(K k) {
        return hashMap.put(k, STUB_VALUE) == null;
    }

    @Override
    public boolean remove(Object o) {
        return hashMap.remove(o) != null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) {
            if (!contains(obj)) return false;
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends K> c) {
        for (K obj : c) {
            if (!add(obj)) return false;
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int count = 0;
        for (K obj : this) {
            if (c.contains(obj)) continue;
            remove(obj);
            count++;
        }
        return count > 0;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int count = 0;
        for (Object obj : c) {
            if (remove(obj)) count++;
        }
        return count > 0;
    }

    @Override
    public void clear() {
        hashMap.clear();
    }
}
