package com.cyber.algo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SimpleHashMapTest {

    SimpleHashMap<String, Integer> sut;

    @BeforeEach
    void setUp() {
        sut = new SimpleHashMap<>();
    }

    @AfterEach
    void tearDown() {
        sut = null;
    }

    @Test
    void testManyValues() {
        int firstValue = 1;
        int lastValue = 100_000;
        int expectedSize = lastValue + 1 - firstValue;

        for (int i = firstValue; i < lastValue + 1; i++) {
            sut.put(String.valueOf(i), i * 10);
        }

        assertEquals(sut.size(), expectedSize);

        for (int i = firstValue; i < lastValue + 1; i++) {
            assertEquals(sut.get(String.valueOf(i)), i * 10);
        }
    }


    @Test
    void testPutNew() {
        sut.put("a", 1);
        sut.put("b", 2);
        sut.put("c", 3);
        assertEquals(sut.get("a"), 1);
        assertEquals(sut.get("b"), 2);
        assertEquals(sut.get("c"), 3);
    }

    @Test
    void testPutReplace() {
        int expectedValue = 999;
        sut.put("a", 1);
        sut.put("b", 2);
        sut.put("c", 3);
        sut.put("a", expectedValue);
        assertEquals(sut.get("a"), expectedValue);
    }

    @Test
    void testGetExist() {
        sut.put("a", 1);
        sut.put("b", 2);
        sut.put("c", 3);
        assertEquals(sut.get("a"), 1);
        assertEquals(sut.get("b"), 2);
        assertEquals(sut.get("c"), 3);
    }

    @Test
    void testGetNotExist() {
        String nonExistKey = "z";
        sut.put("a", 1);
        sut.put("b", 2);
        sut.put("c", 3);
        assertNull(sut.get(nonExistKey));
    }

    @Test
    void testRemoveExist() {
        String removedKey = "b";
        sut.put("a", 1);
        sut.put("b", 2);
        sut.put("c", 3);
        sut.remove(removedKey);
        assertNull(sut.get(removedKey));
    }

    @Test
    void testRemoveNotExist() {
        String nonExistKey = "a";
        sut.remove(nonExistKey);
    }

    @Test
    void testSizePut() {
        int expectedValues = 3;
        sut.put("a", 1);
        sut.put("b", 2);
        sut.put("c", 3);
        assertEquals(expectedValues, sut.size());
    }

    @Test
    void testSizePutAndRemove() {
        int expectedValues = 2;
        sut.put("a", 1);
        sut.put("b", 2);
        sut.put("c", 3);
        sut.remove("c");
        assertEquals(expectedValues, sut.size());
    }

    @Test
    void testSizeEmpty() {
        assertEquals(0, sut.size());
    }

    @Test
    void testClearSize() {
        int valuesCount = 50;
        for (int i = 0; i < valuesCount; i++) {
            sut.put(String.valueOf(i), i);
        }
        sut.clear();
        assertEquals(0, sut.size());
    }

    @Test
    void testClearValues() {
        sut.put("a", 1);
        sut.put("b", 2);
        sut.put("c", 3);
        sut.clear();
        assertNull(sut.get("a"));
        assertNull(sut.get("b"));
        assertNull(sut.get("c"));
    }
}