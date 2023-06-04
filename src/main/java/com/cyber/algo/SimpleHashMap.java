package com.cyber.algo;

import java.util.Arrays;

public class SimpleHashMap<K, V> {
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

    public SimpleHashMap() {
        this(DEFAULT_BUCKET_COUNT, DEFAULT_LOAD_FACTOR);
    }

    public SimpleHashMap(int bucketCount, float loadFactor) {
        this.loadFactor = loadFactor;
        hashTable = (Node<K, V>[]) new Node[bucketCount];
    }

    private int calcHash(K obj) {
        return obj.hashCode();
    }

    private int getBucketIndex(int hash, int targetLength) {
        return hash % targetLength;
    }

    private boolean isKeysEquals(K k1, K k2) {
        return k1.hashCode() == k2.hashCode() && k1.equals(k2);
    }

    private void tryResize() {
        boolean isLoadFactorExceeded = size > hashTable.length * loadFactor;
        if (isLoadFactorExceeded) {
            int newSize = hashTable.length * 2;
            resize(newSize);
        }
    }

    public void put(K key, V value) {
        tryResize();

        int index = getBucketIndex(calcHash(key), hashTable.length);
        Node<K, V> node = hashTable[index];

        if (node == null) {
            hashTable[index] = new Node<>(key, value);
            size++;
            return;
        }

        while (node != null) {
            if (isKeysEquals(node.getKey(), key)) {
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
    }

    public V get(K key) {
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

    public void remove(K key) {
        int index = getBucketIndex(calcHash(key), hashTable.length);
        Node<K, V> node = hashTable[index];

        if (node == null) return;

        if (isKeysEquals(node.getKey(), key)) {
            hashTable[index] = node.getNext();
            size--;
            return;
        }

        while (true) {
            Node<K, V> parentNode = node;
            node = node.getNext();

            if (node == null) break;

            if (isKeysEquals(node.getKey(), key)) {
                parentNode.setNext(node.getNext());
                size--;
                break;
            }
        }
    }

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(hashTable, null);
        size = 0;
    }

    private void resize(int newSize) {
        Node<K, V>[] newHashTable = (Node<K, V>[]) new Node[newSize];
        for (Node<K, V> node : hashTable) {
            while (node != null) {
                Node<K, V> nextNode = node.getNext();
                addNodeWithoutCheck(newHashTable, node);
                node = nextNode;
            }
        }
        hashTable = newHashTable;
    }

    private void addNodeWithoutCheck(Node<K, V>[] targetHashTable, Node<K, V> newNode) {
        int index = getBucketIndex(calcHash(newNode.getKey()), targetHashTable.length);
        Node<K, V> oldHeadNode = targetHashTable[index];
        newNode.setNext(oldHeadNode);
        targetHashTable[index] = newNode;
    }

}
