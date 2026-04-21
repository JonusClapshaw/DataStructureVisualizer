package com.example;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

/**
 * ================================================================
 *  TEST SUITE: MyHashMap
 * ================================================================
 *  Run:  mvn test -Dtest=MyHashMapTest
 *
 *    Wave 1 – Intro       : put, get, containsKey
 *    Wave 2 – Basic       : remove, size, isEmpty
 *    Wave 3 – Intermediate: keys, values, clear
 *    Wave 4 – Advanced    : getOrDefault, putIfAbsent
 * ================================================================
 */
public class MyHashMapTest {

    private MyHashMap map;

    @Before
    public void setUp() {
        map = new MyHashMap();
    }

    // ================================================================
    //  WAVE 1 TESTS — put, get, containsKey
    // ================================================================

    @Test
    public void wave1_put_singleEntry_getReturnsValue() {
        map.put(1, "apple");
        assertEquals("apple", map.get(1));
    }

    @Test
    public void wave1_put_updateExistingKey_valueIsOverwritten() {
        map.put(1, "apple");
        map.put(1, "banana");
        assertEquals("banana", map.get(1));
    }

    @Test
    public void wave1_put_collisionKeys_bothRetrievable() {
        // Keys 1 and 17 both map to bucket index 1 (capacity 16)
        map.put(1, "one");
        map.put(17, "seventeen");
        assertEquals("one",       map.get(1));
        assertEquals("seventeen", map.get(17));
    }

    @Test
    public void wave1_get_missingKey_returnsNull() {
        map.put(1, "apple");
        assertNull(map.get(99));
    }

    @Test
    public void wave1_get_emptyMap_returnsNull() {
        assertNull(map.get(5));
    }

    @Test
    public void wave1_containsKey_existingKey_returnsTrue() {
        map.put(3, "three");
        assertTrue(map.containsKey(3));
    }

    @Test
    public void wave1_containsKey_missingKey_returnsFalse() {
        map.put(1, "one");
        assertFalse(map.containsKey(99));
    }

    @Test
    public void wave1_containsKey_emptyMap_returnsFalse() {
        assertFalse(map.containsKey(1));
    }

    // ================================================================
    //  WAVE 2 TESTS — remove, size, isEmpty
    // ================================================================

    @Test
    public void wave2_remove_existingKey_returnsValueAndRemovesIt() {
        map.put(1, "one");
        String removed = map.remove(1);
        assertEquals("one", removed);
        assertFalse(map.containsKey(1));
    }

    @Test
    public void wave2_remove_missingKey_returnsNull() {
        assertNull(map.remove(99));
    }

    @Test
    public void wave2_remove_headOfChain_nextEntryStillReachable() {
        // Both 1 and 17 hash to bucket 1; remove head of the chain
        map.put(1,  "one");
        map.put(17, "seventeen");
        map.remove(1);
        assertEquals("seventeen", map.get(17));
    }

    @Test
    public void wave2_size_emptyMap_returnsZero() {
        assertEquals(0, map.size());
    }

    @Test
    public void wave2_size_afterPuts_returnsCorrectCount() {
        map.put(1, "a");
        map.put(2, "b");
        map.put(3, "c");
        assertEquals(3, map.size());
    }

    @Test
    public void wave2_size_updateDoesNotIncreaseSize() {
        map.put(1, "a");
        map.put(1, "b"); // update, not insert
        assertEquals(1, map.size());
    }

    @Test
    public void wave2_size_decreasesAfterRemove() {
        map.put(1, "a");
        map.put(2, "b");
        map.remove(1);
        assertEquals(1, map.size());
    }

    @Test
    public void wave2_isEmpty_newMap_returnsTrue() {
        assertTrue(map.isEmpty());
    }

    @Test
    public void wave2_isEmpty_afterPut_returnsFalse() {
        map.put(1, "x");
        assertFalse(map.isEmpty());
    }

    @Test
    public void wave2_isEmpty_afterRemoveLastEntry_returnsTrue() {
        map.put(1, "x");
        map.remove(1);
        assertTrue(map.isEmpty());
    }

    // ================================================================
    //  WAVE 3 TESTS — keys, values, clear
    // ================================================================

    @Test
    public void wave3_keys_returnsAllKeys() {
        map.put(1, "a");
        map.put(2, "b");
        map.put(3, "c");
        List<Integer> keys = map.keys();
        assertEquals(3, keys.size());
        assertTrue(keys.contains(1));
        assertTrue(keys.contains(2));
        assertTrue(keys.contains(3));
    }

    @Test
    public void wave3_keys_emptyMap_returnsEmptyList() {
        assertTrue(map.keys().isEmpty());
    }

    @Test
    public void wave3_values_returnsAllValues() {
        map.put(1, "alpha");
        map.put(2, "beta");
        map.put(3, "gamma");
        List<String> vals = map.values();
        assertEquals(3, vals.size());
        assertTrue(vals.contains("alpha"));
        assertTrue(vals.contains("beta"));
        assertTrue(vals.contains("gamma"));
    }

    @Test
    public void wave3_values_emptyMap_returnsEmptyList() {
        assertTrue(map.values().isEmpty());
    }

    @Test
    public void wave3_clear_removesAllMappings() {
        map.put(1, "a");
        map.put(2, "b");
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertNull(map.get(1));
        assertNull(map.get(2));
    }

    @Test
    public void wave3_clear_thenPut_worksFine() {
        map.put(1, "a");
        map.clear();
        map.put(2, "b");
        assertEquals("b", map.get(2));
        assertEquals(1, map.size());
    }

    // ================================================================
    //  WAVE 4 TESTS — getOrDefault, putIfAbsent
    // ================================================================

    @Test
    public void wave4_getOrDefault_existingKey_returnsActualValue() {
        map.put(1, "hello");
        assertEquals("hello", map.getOrDefault(1, "default"));
    }

    @Test
    public void wave4_getOrDefault_missingKey_returnsDefaultValue() {
        assertEquals("fallback", map.getOrDefault(99, "fallback"));
    }

    @Test
    public void wave4_putIfAbsent_newKey_insertsAndReturnsNull() {
        String result = map.putIfAbsent(1, "first");
        assertNull(result);
        assertEquals("first", map.get(1));
    }

    @Test
    public void wave4_putIfAbsent_existingKey_doesNotOverwriteAndReturnsExistingValue() {
        map.put(1, "original");
        String result = map.putIfAbsent(1, "new");
        assertEquals("original", result);           // returns existing value
        assertEquals("original", map.get(1));        // map is unchanged
    }

    @Test
    public void wave4_putIfAbsent_doesNotChangeSizeWhenKeyExists() {
        map.put(1, "a");
        map.putIfAbsent(1, "b");
        assertEquals(1, map.size());
    }
}
