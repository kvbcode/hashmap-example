package com.cyber.algo;

public class App {

    public static void main(String[] args) {
        SimpleHashMap<String, String> map = new SimpleHashMap<>();

        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        map.remove("key2");

        System.out.println("Значений в таблице: " + map.size());

        System.out.println("key1: " + map.get("key1"));
        System.out.println("key2: " + map.get("key2"));
        System.out.println("key3: " + map.get("key3"));

    }

}
