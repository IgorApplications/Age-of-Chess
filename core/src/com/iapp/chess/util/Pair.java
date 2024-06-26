package com.iapp.chess.util;

import java.io.Serializable;

public class Pair<K, V> implements Serializable {

    private K key;
    private V value;

    public Pair() {}

    public Pair(K k, V v) {
        this.key = k;
        this.value = v;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public boolean containsKey(K key) {
        return this.key == key;
    }

    public boolean containsValue(V value) {
        return this.value == value;
    }
}
