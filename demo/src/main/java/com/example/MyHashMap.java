package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================================
 *  DATA STRUCTURE: Hash Map (int → String, Separate Chaining)
 * ================================================================
 *
 *  A Hash Map stores key-value pairs and provides (near) O(1)
 *  average time for insert, lookup, and delete.
 *
 *  This implementation uses:
 *    - A fixed-size array of "buckets"
 *    - Each bucket is a chain (linked list) of Entry objects
 *      (this collision strategy is called "separate chaining")
 *    - Hash function: index = Math.abs(key % capacity)
 *
 *  Visual (capacity = 4, entries: 1→"a", 5→"b", 9→"c", 3→"d"):
 *
 *    bucket[0]:  null
 *    bucket[1]:  [1→"a"] → [5→"b"] → [9→"c"] → null
 *    bucket[2]:  null
 *    bucket[3]:  [3→"d"] → null
 *
 *  Key properties:
 *    - Average O(1) for put, get, remove
 *    - O(n) worst case when all keys hash to the same bucket
 *
 * ================================================================
 *  HOW THE WAVES WORK
 * ================================================================
 *  Complete each TODO then run:  mvn test -Dtest=MyHashMapTest
 *
 *    Wave 1 – Intro       : put, get, containsKey
 *    Wave 2 – Basic       : remove, size, isEmpty
 *    Wave 3 – Intermediate: keys, values, clear
 *    Wave 4 – Advanced    : getOrDefault, putIfAbsent
 * ================================================================
 */
public class MyHashMap {

    // ----------------------------------------------------------
    //  Inner Entry class — do NOT modify
    // ----------------------------------------------------------
    private static class Entry {
        int    key;
        String value;
        Entry  next;

        Entry(int key, String value) {
            this.key   = key;
            this.value = value;
            this.next  = null;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;

    private Entry[] buckets;
    private int     size;

    /** Creates a MyHashMap with default capacity 16. */
    public MyHashMap() {
        this.buckets = new Entry[DEFAULT_CAPACITY];
        this.size    = 0;
    }

    // ----------------------------------------------------------
    //  Internal hash helper — do NOT modify
    // ----------------------------------------------------------
    private int getBucketIndex(int key) {
        return Math.abs(key % buckets.length);
    }

    // ================================================================
    //  WAVE 1 — INTRO
    //  Goal: Insert, retrieve, and check for keys.
    // ================================================================

    /**
     * [Wave 1 – Task 1] put
     * ---------------------
     * Insert or update the mapping for {@code key} → {@code value}.
     *
     * Steps:
     *   1. Compute index = getBucketIndex(key)
     *   2. Walk the chain at buckets[index]:
     *        - If an entry with the same key is found → update its value, return
     *        - Otherwise → add a new Entry at the FRONT of the chain
     *   3. Increment size only if a NEW entry was added
     *
     * Prepending at the front is easiest:
     *   newEntry.next = buckets[index];
     *   buckets[index] = newEntry;
     *
     * @param key   integer key
     * @param value String value to associate with the key
     */
    public void put(int key, String value) {
        int   index   = getBucketIndex(key);
        Entry current = buckets[index];
        // TODO: walk the chain — if key exists, update value and return
        // TODO: if not found, prepend a new Entry and increment size
    }

    /**
     * [Wave 1 – Task 2] get
     * ---------------------
     * Return the value associated with {@code key}, or null if not found.
     *
     * Steps:
     *   1. Compute index = getBucketIndex(key)
     *   2. Walk the chain; when entry.key == key, return entry.value
     *   3. Return null if the chain ends without a match
     *
     * @param key integer key to look up
     * @return associated String value, or null
     */
    public String get(int key) {
        int   index   = getBucketIndex(key);
        Entry current = buckets[index];
        // TODO: walk the chain and return the matching value
        return null; // placeholder
    }

    /**
     * [Wave 1 – Task 3] containsKey
     * --------------------------------
     * Return true if the map contains a mapping for {@code key}.
     *
     * Hint: you can simply check whether get(key) != null,
     *       OR walk the chain directly.
     *
     * @param key key to check
     * @return true if key is present
     */
    public boolean containsKey(int key) {
        // TODO: return true if the key exists in the map
        return false; // placeholder
    }

    // ================================================================
    //  WAVE 2 — BASIC
    //  Goal: Remove mappings, report count, check emptiness.
    // ================================================================

    /**
     * [Wave 2 – Task 1] remove
     * ------------------------
     * Remove the mapping for {@code key} if it exists.
     * Return the associated value, or null if the key was not found.
     *
     * Steps:
     *   1. Compute bucket index
     *   2. Walk the chain with a "prev" pointer:
     *        - To remove the head: buckets[index] = entry.next
     *        - To remove a non-head: prev.next = entry.next
     *   3. Decrement size; return the removed value
     *
     * @param key key to remove
     * @return the removed value, or null if not found
     */
    public String remove(int key) {
        int   index   = getBucketIndex(key);
        Entry current = buckets[index];
        Entry prev    = null;
        // TODO: walk chain with prev; when key matches, unlink and return value
        return null; // placeholder
    }

    /**
     * [Wave 2 – Task 2] size
     * ----------------------
     * Return the number of key-value pairs in the map.
     *
     * @return current number of mappings
     */
    public int size() {
        // TODO: return size field
        return 0; // placeholder
    }

    /**
     * [Wave 2 – Task 3] isEmpty
     * -------------------------
     * Return true if the map contains no mappings.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        // TODO: check size == 0
        return false; // placeholder
    }

    // ================================================================
    //  WAVE 3 — INTERMEDIATE
    //  Goal: Extract keys/values and reset the entire map.
    // ================================================================

    /**
     * [Wave 3 – Task 1] keys
     * ----------------------
     * Return a List of ALL keys currently in the map.
     * Order is not guaranteed.
     *
     * Steps:
     *   Iterate every bucket (index 0 to buckets.length - 1).
     *   Walk each chain and collect each entry.key.
     *
     * @return list of all keys
     */
    public List<Integer> keys() {
        List<Integer> result = new ArrayList<>();
        // TODO: iterate every bucket, walk each chain, add entry.key to result
        return result;
    }

    /**
     * [Wave 3 – Task 2] values
     * ------------------------
     * Return a List of ALL values currently in the map.
     * Order is not guaranteed.
     *
     * @return list of all values
     */
    public List<String> values() {
        List<String> result = new ArrayList<>();
        // TODO: iterate every bucket, walk each chain, add entry.value to result
        return result;
    }

    /**
     * [Wave 3 – Task 3] clear
     * -----------------------
     * Remove ALL mappings from the map.
     *
     * Steps:
     *   1. Set every bucket to null  (or reallocate the array)
     *   2. Reset size to 0
     */
    public void clear() {
        // TODO: null out all buckets and reset size
    }

    // ================================================================
    //  WAVE 4 — ADVANCED
    //  Goal: Conditional operations on the map.
    // ================================================================

    /**
     * [Wave 4 – Task 1] getOrDefault
     * --------------------------------
     * Return the value associated with {@code key}, or {@code defaultValue}
     * if the key is not present in the map.
     *
     * @param key          key to look up
     * @param defaultValue value to return when key is absent
     * @return the associated value, or defaultValue
     */
    public String getOrDefault(int key, String defaultValue) {
        // TODO: call get(key); if the result is null, return defaultValue instead
        return defaultValue; // placeholder
    }

    /**
     * [Wave 4 – Task 2] putIfAbsent
     * --------------------------------
     * If {@code key} is NOT already in the map, insert key→value and return null.
     * If {@code key} IS already present, do NOT modify the map and return the
     * existing value.
     *
     * @param key   key to insert
     * @param value value to associate if key is absent
     * @return null if inserted, or the existing value if key was already present
     */
    public String putIfAbsent(int key, String value) {
        // TODO: check containsKey; if absent call put and return null;
        //       otherwise return the existing value
        return null; // placeholder
    }

    // ----------------------------------------------------------------
    //  Package-private: used by HashMapVisualizer only
    // ----------------------------------------------------------------
    /**
     * Returns each bucket's chain as a "key:value" comma-separated string.
     * Empty buckets return an empty string. Does NOT go through any TODO methods.
     */
    List<String> getBucketSnapshot() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < buckets.length; i++) {
            StringBuilder sb = new StringBuilder();
            Entry cur = buckets[i];
            while (cur != null) {
                if (sb.length() > 0) sb.append(",");
                sb.append(cur.key).append(":").append(cur.value);
                cur = cur.next;
            }
            result.add(sb.toString());
        }
        return result;
    }
}
