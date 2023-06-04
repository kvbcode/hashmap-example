package com.cyber.algo;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CompatibleHashMap<K, V> implements Map<K, V> {
    private static class Node<K, V> {
        private Node<K, V> next;
        private final K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }

        public void clearNext() {
            setNext(null);
        }

        public boolean hasNext() {
            return next != null;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    private static final int DEFAULT_BUCKET_COUNT = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private final float loadFactor;
    private int size = 0;
    private Node<K, V>[] hashTable;

    public CompatibleHashMap() {
        this(DEFAULT_BUCKET_COUNT, DEFAULT_LOAD_FACTOR);
    }

    public CompatibleHashMap(int bucketCount, float loadFactor) {
        this.loadFactor = loadFactor;
        hashTable = (Node<K, V>[]) new Node[bucketCount];
    }

    private int calcHash(Object obj) {
        return obj.hashCode();
    }

    private int getBucketIndex(int hash, int targetLength) {
        return hash % targetLength;
    }

    private boolean isKeysEquals(Object k1, Object k2) {
        return k1.hashCode() == k2.hashCode() && k1.equals(k2);
    }

    private void tryResize() {
        boolean isLoadFactorExceeded = size > hashTable.length * loadFactor;
        if (isLoadFactorExceeded) {
            int newSize = hashTable.length * 2;
            resize(newSize);
        }
    }

    @Override
    public V put(K key, V value) {
        tryResize();

        int index = getBucketIndex(calcHash(key), hashTable.length);
        Node<K, V> node = hashTable[index];
        V oldValue = null;

        if (node == null) {
            hashTable[index] = new Node<>(key, value);
            size++;
            return oldValue;
        }

        while (node != null) {
            if (isKeysEquals(node.getKey(), key)) {
                oldValue = node.getValue();
                node.setValue(value);
                break;
            }
            if (!node.hasNext()) {
                node.setNext(new Node<>(key, value));
                size++;
                break;
            }
            node = node.getNext();
        }
        return oldValue;
    }

    @Override
    public V get(Object key) {
        int index = getBucketIndex(calcHash(key), hashTable.length);
        Node<K, V> node = hashTable[index];

        while (node != null) {
            if (isKeysEquals(node.getKey(), key)) {
                return node.getValue();
            }
            node = node.getNext();
        }
        return null;
    }

    @Override
    public V remove(Object key) {
        int index = getBucketIndex(calcHash(key), hashTable.length);
        Node<K, V> node = hashTable[index];
        V oldValue = null;

        if (node == null) return oldValue;

        if (isKeysEquals(node.getKey(), key)) {
            oldValue = node.getValue();
            hashTable[index] = node.getNext();
            size--;
            return oldValue;
        }

        while (true) {
            Node<K, V> parentNode = node;
            node = node.getNext();

            if (node == null) break;

            if (isKeysEquals(node.getKey(), key)) {
                oldValue = node.getValue();
                parentNode.setNext(node.getNext());
                size--;
                break;
            }
        }
        return oldValue;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Arrays.fill(hashTable, null);
        size = 0;
    }

    private void resize(int newSize) {
        Node<K, V>[] newHashTable = (Node<K, V>[]) new Node[newSize];
        forEachNode(node -> addNodeWithoutCheck(newHashTable, node));
        hashTable = newHashTable;
    }

    private void forEachNode(Consumer<Node<K, V>> action) {
        for (Node<K, V> node : hashTable) {
            while (node != null) {
                Node<K, V> nextNode = node.getNext();
                action.accept(node);
                node = nextNode;
            }
        }
    }

    private void addNodeWithoutCheck(Node<K, V>[] targetHashTable, Node<K, V> newNode) {
        int index = getBucketIndex(calcHash(newNode.getKey()), targetHashTable.length);
        Node<K, V> oldHeadNode = targetHashTable[index];
        newNode.setNext(oldHeadNode);
        targetHashTable[index] = newNode;
    }

    // Java Collections Map interface implementation

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        forEachNode(node -> action.accept(node.getKey(), node.getValue()));
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Node<K, V> node : hashTable) {
            while (node != null) {
                if (node.getValue().equals(value)) return true;
                node = node.getNext();
            }
        }
        return false;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> sourceMap) {
        sourceMap.forEach(this::put);
    }

    @Override
    public Set<K> keySet() {
        Set<K> resultSet = new HashSet<>();
        forEach((key, value) -> resultSet.add(key));
        return resultSet;
    }

    @Override
    public Collection<V> values() {
        ArrayList<V> resultList = new ArrayList<>(size());
        forEach((key, value) -> resultList.add(value));
        return resultList;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> resultSet = new HashSet<>();
        forEach((key, value) -> resultSet.add(Map.entry(key, value)));
        return resultSet;
    }
}
