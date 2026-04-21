package com.example;

/**
 * ================================================================
 *  DATA STRUCTURE: Singly Linked List
 * ================================================================
 *
 *  A singly linked list is a sequence of nodes where each node
 *  holds a piece of data and a pointer to the NEXT node.
 *  The list travels in one direction only: head → tail → null.
 *
 *  Visual:
 *    head
 *     |
 *    [1] → [2] → [3] → null
 *
 *  Key properties:
 *    - O(1) insertion at head
 *    - O(n) search
 *    - O(n) access by index
 *
 * ================================================================
 *  HOW THE WAVES WORK
 * ================================================================
 *  Each wave introduces new methods. Complete every TODO before
 *  moving to the next wave. Run the test file to check your work:
 *
 *    mvn test -Dtest=SinglyLinkedListTest
 *
 *    Wave 1 – Intro       : addFirst, getSize, printList
 *    Wave 2 – Basic       : addLast, removeFirst, contains
 *    Wave 3 – Intermediate: removeByValue, getAt, insertAt
 *    Wave 4 – Advanced    : reverse, findMiddle, hasCycle
 * ================================================================
 */
public class SinglyLinkedList {

    // ----------------------------------------------------------
    //  Inner Node class — do NOT modify
    // ----------------------------------------------------------
    public static class Node {
        public int data;
        public Node next;

        public Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;

    /** Creates an empty Singly Linked List. */
    public SinglyLinkedList() {
        this.head = null;
    }

    // ================================================================
    //  WAVE 1 — INTRO
    //  Goal: Build and inspect a basic list.
    // ================================================================

    /**
     * [Wave 1 – Task 1] addFirst
     * --------------------------
     * Insert a new node containing {@code data} at the FRONT of the list.
     *
     * Before:  head → [2] → [3] → null
     * After addFirst(1):
     *          head → [1] → [2] → [3] → null
     *
     * Steps:
     *   1. Create a new Node(data)
     *   2. Set newNode.next = head
     *   3. Set head = newNode
     *
     * @param data the integer value to insert
     */
    public void addFirst(int data) {
        Node newNode = new Node(data);
        // TODO: connect newNode into the list and update head
    }

    /**
     * [Wave 1 – Task 2] getSize
     * -------------------------
     * Count and return the number of nodes in the list.
     *
     * Example: [1] → [2] → [3] → null  returns 3
     *          (empty list)             returns 0
     *
     * Steps:
     *   1. Start a counter at 0
     *   2. Walk from head to null, incrementing each step
     *   3. Return the counter
     *
     * @return the number of nodes in the list
     */
    public int getSize() {
        int count = 0;
        Node current = head;
        while (current != null) {
            // TODO: increment count, then advance current to current.next
        }
        return count;
    }

    /**
     * [Wave 1 – Task 3] printList
     * ---------------------------
     * Build and return a String of all node values joined by " -> ",
     * ending with "null".
     *
     * Example: [1] → [2] → [3] → null  returns "1 -> 2 -> 3 -> null"
     *          (empty list)             returns "null"
     *
     * @return a String representation of the list
     */
    public String printList() {
        StringBuilder sb = new StringBuilder();
        Node current = head;
        // TODO: while current != null, append current.data + " -> ", then advance
        sb.append("null");
        return sb.toString();
    }

    // ================================================================
    //  WAVE 2 — BASIC
    //  Goal: Add to the end, remove from the front, and search.
    // ================================================================

    /**
     * [Wave 2 – Task 1] addLast
     * -------------------------
     * Append a new node containing {@code data} at the END of the list.
     *
     * Before:  head → [1] → [2] → null
     * After addLast(3):
     *          head → [1] → [2] → [3] → null
     *
     * Edge case: if the list is empty, the new node becomes the head.
     *
     * Steps:
     *   1. Create a new Node(data)
     *   2. If head is null, set head = newNode and return
     *   3. Otherwise walk to the last node (current.next == null)
     *   4. Set lastNode.next = newNode
     *
     * @param data the integer value to append
     */
    public void addLast(int data) {
        Node newNode = new Node(data);
        if (head == null) {
            // TODO: handle empty list — newNode becomes the head
            return;
        }
        Node current = head;
        // TODO: advance current until current.next is null, then link newNode
    }

    /**
     * [Wave 2 – Task 2] removeFirst
     * -----------------------------
     * Remove and return the data value from the FIRST node in the list.
     * Throws {@link java.util.NoSuchElementException} if the list is empty.
     *
     * Before:  head → [1] → [2] → [3] → null
     * After:   head → [2] → [3] → null   (returns 1)
     *
     * @return the data value of the removed head node
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public int removeFirst() {
        if (head == null) {
            throw new java.util.NoSuchElementException("List is empty");
        }
        // TODO: save head.data, advance head to head.next, return the saved value
        return -1; // placeholder
    }

    /**
     * [Wave 2 – Task 3] contains
     * --------------------------
     * Return {@code true} if any node in the list holds the value {@code data}.
     *
     * Example: list = [1] → [2] → [3]
     *   contains(2) → true
     *   contains(5) → false
     *
     * @param data the value to search for
     * @return true if found, false otherwise
     */
    public boolean contains(int data) {
        // TODO: traverse the list; return true as soon as a match is found
        return false; // placeholder
    }

    // ================================================================
    //  WAVE 3 — INTERMEDIATE
    //  Goal: Precise insertion and removal by position or value.
    // ================================================================

    /**
     * [Wave 3 – Task 1] removeByValue
     * --------------------------------
     * Remove the FIRST occurrence of a node whose data equals {@code value}.
     * Return {@code true} if a node was removed, {@code false} if not found.
     *
     * Cases to handle:
     *   - List is empty                    → return false
     *   - The head node matches            → remove head, return true
     *   - A later node matches             → unlink it, return true
     *   - No node matches                  → return false
     *
     * Hint: keep a "prev" pointer one step behind "current" so you can
     *       unlink current by doing prev.next = current.next.
     *
     * @param value the value to remove
     * @return true if removed, false if not found
     */
    public boolean removeByValue(int value) {
        // TODO: implement full traversal with prev/current pointers
        return false; // placeholder
    }

    /**
     * [Wave 3 – Task 2] getAt
     * -----------------------
     * Return the data value of the node at 0-based position {@code index}.
     * Throws {@link IndexOutOfBoundsException} if index is negative or ≥ size.
     *
     * Example: [10] → [20] → [30] → null
     *   getAt(0) → 10
     *   getAt(2) → 30
     *   getAt(3) → IndexOutOfBoundsException
     *
     * @param index 0-based position
     * @return data at that position
     */
    public int getAt(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index cannot be negative: " + index);
        }
        // TODO: walk the list index times; if you fall off the end throw IndexOutOfBoundsException
        return -1; // placeholder
    }

    /**
     * [Wave 3 – Task 3] insertAt
     * --------------------------
     * Insert a new node with {@code data} at 0-based position {@code index}.
     * Throws {@link IndexOutOfBoundsException} if index < 0 or index > size.
     *
     * Example: [1] → [3] → null
     *   insertAt(1, 2) → [1] → [2] → [3] → null
     *
     * Special cases:
     *   - index == 0    → same behavior as addFirst
     *   - index == size → same behavior as addLast
     *
     * @param index 0-based position for the new node
     * @param data  the value to insert
     */
    public void insertAt(int index, int data) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index cannot be negative: " + index);
        }
        if (index == 0) {
            addFirst(data);
            return;
        }
        // TODO: walk to node at (index - 1), then splice in the new node between
        //       that node and its current next
    }

    // ================================================================
    //  WAVE 4 — ADVANCED
    //  Goal: Classic interview-level linked list algorithms.
    // ================================================================

    /**
     * [Wave 4 – Task 1] reverse
     * -------------------------
     * Reverse the list IN-PLACE so the tail becomes the new head.
     *
     * Before: head → [1] → [2] → [3] → null
     * After:  head → [3] → [2] → [1] → null
     *
     * Hint: Use three pointers — prev, current, and next.
     *   While current != null:
     *     1. Save next = current.next
     *     2. Set current.next = prev   (reverse the link)
     *     3. Move prev = current
     *     4. Move current = next
     *   At the end, set head = prev.
     */
    public void reverse() {
        // TODO: implement iterative three-pointer reversal
    }

    /**
     * [Wave 4 – Task 2] findMiddle
     * ----------------------------
     * Return the data value of the MIDDLE node using the fast/slow pointer
     * (tortoise-and-hare) technique — one pass, no size counter needed.
     *
     * For even-length lists, return the SECOND middle node.
     *
     * Example:
     *   [1] → [2] → [3] → null          → returns 2
     *   [1] → [2] → [3] → [4] → null   → returns 3
     *
     * Hint: slow moves 1 step, fast moves 2 steps.
     *       When fast reaches null (or fast.next is null), slow is at the middle.
     *
     * @return data of the middle node
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public int findMiddle() {
        if (head == null) {
            throw new java.util.NoSuchElementException("List is empty");
        }
        // TODO: implement fast/slow pointer technique
        return -1; // placeholder
    }

    /**
     * [Wave 4 – Task 3] hasCycle
     * --------------------------
     * Detect whether the list contains a cycle — a node whose next pointer
     * points back to an earlier node — using Floyd's cycle-detection algorithm.
     *
     * Returns {@code true} if a cycle exists, {@code false} otherwise.
     *
     * Hint: Use a slow pointer (1 step) and fast pointer (2 steps).
     *       If fast ever equals slow (after the start), there is a cycle.
     *       If fast reaches null, there is no cycle.
     *
     * NOTE: Call the package-private helper {@link #createCycleForTesting(int)}
     *       from your test to create a cycle without a setter.
     *
     * @return true if a cycle is detected
     */
    public boolean hasCycle() {
        // TODO: implement Floyd's algorithm
        return false; // placeholder
    }

    // ----------------------------------------------------------
    //  Test Helpers — do NOT use in production code
    // ----------------------------------------------------------

    /** Package-private: creates a cycle for testing hasCycle(). */
    void createCycleForTesting(int targetIndex) {
        if (head == null) return;
        Node tail = head;
        Node target = head;
        int i = 0;
        while (tail.next != null) {
            if (i == targetIndex) target = tail;
            tail = tail.next;
            i++;
        }
        tail.next = target;
    }

    /** Package-private: returns head (for test inspection). */
    Node getHead() {
        return head;
    }
}
