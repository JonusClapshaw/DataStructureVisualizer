package com.example;

/**
 * ================================================================
 *  DATA STRUCTURE: Doubly Linked List
 * ================================================================
 *
 *  A doubly linked list is like a singly linked list, but each
 *  node also stores a pointer to the PREVIOUS node. This means
 *  you can traverse in BOTH directions.
 *
 *  Visual (forward):
 *    head                              tail
 *     |                                |
 *  null ← [1] ⇄ [2] ⇄ [3] → null
 *
 *  Each node:  prev ← [data] → next
 *
 *  Key properties:
 *    - O(1) insertion/removal at head OR tail
 *    - O(n) search
 *    - Uses more memory than singly (extra prev pointer per node)
 *
 * ================================================================
 *  HOW THE WAVES WORK
 * ================================================================
 *  Complete each TODO then run:  mvn test -Dtest=DoublyLinkedListTest
 *
 *    Wave 1 – Intro       : addFirst, getSize, printForward
 *    Wave 2 – Basic       : addLast, removeFirst, removeLast
 *    Wave 3 – Intermediate: removeByValue, insertAt, printBackward
 *    Wave 4 – Advanced    : reverse, sortAscending
 * ================================================================
 */
public class DoublyLinkedList {

    // ----------------------------------------------------------
    //  Inner Node class — do NOT modify
    // ----------------------------------------------------------
    public static class Node {
        public int data;
        public Node next;
        public Node prev;

        public Node(int data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private Node head;
    private Node tail;

    /** Creates an empty Doubly Linked List. */
    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
    }

    // ================================================================
    //  WAVE 1 — INTRO
    //  Goal: Build and inspect a basic doubly-linked list.
    // ================================================================

    /**
     * [Wave 1 – Task 1] addFirst
     * --------------------------
     * Insert a new node at the FRONT of the list.
     * Both head AND tail must be kept correct after every call.
     *
     * Empty list case:  the new node becomes both head AND tail.
     *
     * Non-empty steps:
     *   1. Create new Node(data)
     *   2. newNode.next = head
     *   3. head.prev = newNode
     *   4. head = newNode
     *
     * @param data value to insert at front
     */
    public void addFirst(int data) {
        Node newNode = new Node(data);
        if (head == null) {
            // TODO: set both head and tail to newNode
            return;
        }
        // TODO: link newNode before the current head and update head
    }

    /**
     * [Wave 1 – Task 2] getSize
     * -------------------------
     * Traverse from head to tail, counting nodes. Return the count.
     *
     * @return number of nodes in the list
     */
    public int getSize() {
        int count = 0;
        Node current = head;
        // TODO: walk head → null, incrementing count each step
        return count;
    }

    /**
     * [Wave 1 – Task 3] printForward
     * --------------------------------
     * Return a String of all values from head to tail, separated by " <-> ".
     *
     * Example: "1 <-> 2 <-> 3"
     * Empty:   ""
     *
     * Hint: do NOT add " <-> " after the last element.
     *
     * @return forward String representation
     */
    public String printForward() {
        StringBuilder sb = new StringBuilder();
        Node current = head;
        // TODO: walk head → tail; append data between separators (not after last)
        return sb.toString();
    }

    // ================================================================
    //  WAVE 2 — BASIC
    //  Goal: Add to the tail, remove from both ends.
    // ================================================================

    /**
     * [Wave 2 – Task 1] addLast
     * -------------------------
     * Append a new node at the END of the list.
     * Update tail accordingly. Handle the empty list case.
     *
     * Non-empty steps:
     *   1. Create new Node(data)
     *   2. tail.next = newNode
     *   3. newNode.prev = tail
     *   4. tail = newNode
     *
     * @param data value to append
     */
    public void addLast(int data) {
        Node newNode = new Node(data);
        if (tail == null) {
            // TODO: empty list — newNode is both head and tail
            return;
        }
        // TODO: link newNode after the current tail and update tail
    }

    /**
     * [Wave 2 – Task 2] removeFirst
     * -----------------------------
     * Remove the head node. Return its data.
     * Throws {@link java.util.NoSuchElementException} if empty.
     *
     * After removal, set the new head's prev pointer to null.
     * If the list becomes empty, also set tail = null.
     *
     * @return data of the removed head
     */
    public int removeFirst() {
        if (head == null) {
            throw new java.util.NoSuchElementException("List is empty");
        }
        // TODO: save head.data, advance head to head.next, fix head.prev,
        //       handle the case where the list becomes empty
        return -1; // placeholder
    }

    /**
     * [Wave 2 – Task 3] removeLast
     * ----------------------------
     * Remove the tail node. Return its data.
     * Throws {@link java.util.NoSuchElementException} if empty.
     *
     * After removal, set the new tail's next pointer to null.
     * If the list becomes empty, also set head = null.
     *
     * @return data of the removed tail
     */
    public int removeLast() {
        if (tail == null) {
            throw new java.util.NoSuchElementException("List is empty");
        }
        // TODO: save tail.data, move tail = tail.prev, fix tail.next,
        //       handle the case where the list becomes empty
        return -1; // placeholder
    }

    // ================================================================
    //  WAVE 3 — INTERMEDIATE
    //  Goal: Remove by value, insert at position, traverse backward.
    // ================================================================

    /**
     * [Wave 3 – Task 1] removeByValue
     * ---------------------------------
     * Remove the FIRST node whose data equals {@code value}.
     * Return true if removed, false if not found.
     *
     * Because each node knows its own prev, you can unlink it directly:
     *   node.prev.next = node.next   (skip over node going forward)
     *   node.next.prev = node.prev   (skip over node going backward)
     *
     * Edge cases:
     *   - Removing the head → delegate to removeFirst()
     *   - Removing the tail → delegate to removeLast()
     *   - Removing a middle node → fix both neighbors' pointers
     *
     * @param value value to remove
     * @return true if a node was removed
     */
    public boolean removeByValue(int value) {
        // TODO: traverse from head; when data matches, unlink that node
        return false; // placeholder
    }

    /**
     * [Wave 3 – Task 2] insertAt
     * --------------------------
     * Insert a new node with {@code data} at 0-based position {@code index}.
     * Throws {@link IndexOutOfBoundsException} if index < 0 or index > size.
     *
     * Hint:
     *   - index == 0         → addFirst(data)
     *   - index == getSize() → addLast(data)
     *   - otherwise: walk to node at (index), splice the new node BEFORE it
     *     (update both next and prev pointers of the surrounding nodes)
     *
     * @param index 0-based insertion position
     * @param data  value to insert
     */
    public void insertAt(int index, int data) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index cannot be negative: " + index);
        }
        if (index == 0) {
            addFirst(data);
            return;
        }
        // TODO: walk to position index; splice in new node before that position
        //       Remember: update both .next AND .prev on all affected nodes
    }

    /**
     * [Wave 3 – Task 3] printBackward
     * --------------------------------
     * Return a String of all values from tail to head, separated by " <-> ".
     *
     * Example (list = 1 <-> 2 <-> 3): "3 <-> 2 <-> 1"
     * Empty: ""
     *
     * Hint: start at tail, walk using .prev pointers.
     *
     * @return backward String representation
     */
    public String printBackward() {
        StringBuilder sb = new StringBuilder();
        Node current = tail;
        // TODO: walk tail → head using .prev; append data between separators
        return sb.toString();
    }

    // ================================================================
    //  WAVE 4 — ADVANCED
    //  Goal: Classic doubly-linked list algorithms.
    // ================================================================

    /**
     * [Wave 4 – Task 1] reverse
     * -------------------------
     * Reverse the list IN-PLACE.
     * Every node's next and prev pointers must be swapped.
     * After reversal, head becomes the old tail and vice versa.
     *
     * Algorithm:
     *   Walk every node. For each node, swap node.next and node.prev.
     *   After the loop, swap head and tail.
     *
     * Example:
     *   Before: 1 <-> 2 <-> 3   (head=1, tail=3)
     *   After:  3 <-> 2 <-> 1   (head=3, tail=1)
     */
    public void reverse() {
        // TODO: iterate through all nodes swapping .next and .prev,
        //       then swap head and tail references
    }

    /**
     * [Wave 4 – Task 2] sortAscending
     * --------------------------------
     * Sort the list in ascending order IN-PLACE.
     *
     * Do NOT create new nodes — only swap data values between existing nodes.
     * A simple bubble sort is perfectly acceptable here.
     *
     * Bubble sort reminder:
     *   Repeat (size - 1) passes:
     *     Walk adjacent pairs; if left.data > right.data, swap their data values.
     *
     * Example: 3 <-> 1 <-> 2  →  1 <-> 2 <-> 3
     */
    public void sortAscending() {
        // TODO: implement bubble sort by swapping .data values (not nodes)
    }

    // ----------------------------------------------------------
    //  Package-private accessors for tests — do NOT modify
    // ----------------------------------------------------------
    Node getHead() { return head; }
    Node getTail() { return tail; }
}
