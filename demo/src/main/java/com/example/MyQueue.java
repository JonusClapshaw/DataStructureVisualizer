package com.example;

/**
 * ================================================================
 *  DATA STRUCTURE: Queue (Linked-List Based)
 * ================================================================
 *
 *  A Queue is a First-In, First-Out (FIFO) data structure.
 *  Think of a line at a store — first in, first served.
 *
 *  This implementation uses an internal singly-linked structure
 *  with a "front" pointer (dequeue end) and a "back" pointer
 *  (enqueue end).
 *
 *  Visual:
 *    FRONT                       BACK
 *     |                           |
 *    [A] → [B] → [C] → null
 *     ↑                           ↑
 *   dequeue (remove)          enqueue (add)
 *
 *  Core operations (all O(1)):
 *    enqueue — add to the back
 *    dequeue — remove from the front
 *    peek    — view the front without removing
 *
 * ================================================================
 *  HOW THE WAVES WORK
 * ================================================================
 *  Complete each TODO then run:  mvn test -Dtest=MyQueueTest
 *
 *    Wave 1 – Intro       : enqueue, peek, isEmpty
 *    Wave 2 – Basic       : dequeue, size
 *    Wave 3 – Intermediate: contains, clear, toArray
 *    Wave 4 – Advanced    : reverseQueue, interleave
 * ================================================================
 */
public class MyQueue {

    // ----------------------------------------------------------
    //  Internal Node — do NOT modify
    // ----------------------------------------------------------
    private static class Node {
        int data;
        Node next;
        Node(int data) { this.data = data; this.next = null; }
    }

    private Node front;   // remove (dequeue) from here
    private Node back;    // add (enqueue) here
    private int size;

    /** Creates an empty Queue. */
    public MyQueue() {
        this.front = null;
        this.back  = null;
        this.size  = 0;
    }

    // ================================================================
    //  WAVE 1 — INTRO
    //  Goal: Add to back, view front, check emptiness.
    // ================================================================

    /**
     * [Wave 1 – Task 1] enqueue
     * -------------------------
     * Add a new node with {@code data} at the BACK of the queue.
     *
     * Empty case: front = back = newNode
     * Non-empty:
     *   1. back.next = newNode
     *   2. back = newNode
     * Then increment size.
     *
     * @param data value to add
     */
    public void enqueue(int data) {
        Node newNode = new Node(data);
        if (front == null) {
            // TODO: set both front and back to newNode
        } else {
            // TODO: link newNode after back, then update back
        }
        // TODO: increment size
    }

    /**
     * [Wave 1 – Task 2] peek
     * ----------------------
     * Return the value at the FRONT without removing it.
     * Throws {@link java.util.NoSuchElementException} if empty.
     *
     * @return front value
     */
    public int peek() {
        if (front == null) {
            throw new java.util.NoSuchElementException("Queue is empty");
        }
        // TODO: return front.data
        return -1; // placeholder
    }

    /**
     * [Wave 1 – Task 3] isEmpty
     * -------------------------
     * Return true if the queue has no elements.
     *
     * Hint: check the front pointer or the size field.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        // TODO: check front == null (or size == 0)
        return false; // placeholder
    }

    // ================================================================
    //  WAVE 2 — BASIC
    //  Goal: Remove from front and report the count.
    // ================================================================

    /**
     * [Wave 2 – Task 1] dequeue
     * -------------------------
     * Remove and return the value from the FRONT of the queue.
     * Throws {@link java.util.NoSuchElementException} if empty.
     *
     * Steps:
     *   1. Check empty → throw
     *   2. Save front.data
     *   3. Advance front = front.next
     *   4. If front is now null → also set back = null (list is now empty)
     *   5. Decrement size
     *   6. Return saved value
     *
     * @return the dequeued value
     */
    public int dequeue() {
        if (front == null) {
            throw new java.util.NoSuchElementException("Queue is empty");
        }
        // TODO: implement dequeue using the steps above
        return -1; // placeholder
    }

    /**
     * [Wave 2 – Task 2] size
     * ----------------------
     * Return the number of elements currently in the queue.
     *
     * @return current size
     */
    public int size() {
        // TODO: return the size field
        return 0; // placeholder
    }

    // ================================================================
    //  WAVE 3 — INTERMEDIATE
    //  Goal: Search, reset, and snapshot the queue.
    // ================================================================

    /**
     * [Wave 3 – Task 1] contains
     * --------------------------
     * Return true if any node in the queue holds {@code value}.
     * Traverse from front to back.
     *
     * @param value value to search for
     * @return true if found
     */
    public boolean contains(int value) {
        // TODO: traverse front → back; return true as soon as data matches
        return false; // placeholder
    }

    /**
     * [Wave 3 – Task 2] clear
     * -----------------------
     * Remove all elements from the queue.
     * Reset front, back, and size to their initial values.
     */
    public void clear() {
        // TODO: set front = null, back = null, size = 0
    }

    /**
     * [Wave 3 – Task 3] toArray
     * -------------------------
     * Return a new int[] containing all elements from front to back.
     *
     * Example: queue (front→back) = [1, 2, 3]
     *          toArray()           = {1, 2, 3}
     *
     * @return array snapshot of the queue
     */
    public int[] toArray() {
        // TODO: allocate array of size 'size', then copy elements front → back
        return new int[0]; // placeholder
    }

    // ================================================================
    //  WAVE 4 — ADVANCED
    //  Goal: Apply a queue to solve real algorithmic problems.
    // ================================================================

    /**
     * [Wave 4 – Task 1] reverseQueue
     * --------------------------------
     * Reverse the order of all elements in THIS queue in-place,
     * using a helper {@link MyStack}.
     *
     * Algorithm:
     *   1. Dequeue every element into a MyStack
     *   2. Pop every element from the stack back into this queue
     *
     * Example: front=[1, 2, 3]=back  →  front=[3, 2, 1]=back
     */
    public void reverseQueue() {
        // TODO: use a MyStack to reverse this queue in-place
    }

    /**
     * [Wave 4 – Task 2] interleave
     * ----------------------------
     * Interleave the first half with the second half of this queue.
     * Assumes the queue has an EVEN number of elements.
     * Throws {@link IllegalStateException} if size is odd.
     *
     * Example: [1, 2, 3, 4, 5, 6]  →  [1, 4, 2, 5, 3, 6]
     *
     * Algorithm:
     *   1. Split into two separate MyQueue halves (first n/2, then second n/2)
     *   2. Dequeue one from each half in alternating order into this queue
     *
     * @throws IllegalStateException if the queue does not have an even number of elements
     */
    public void interleave() {
        if (size % 2 != 0) {
            throw new IllegalStateException("Queue must have an even number of elements");
        }
        // TODO: split into firstHalf and secondHalf queues, then alternate merging back
    }

    // ----------------------------------------------------------------
    //  Package-private: used by QueueVisualizer only
    // ----------------------------------------------------------------
    /** Returns elements from front to back without going through TODOs. */
    int[] snapshotForVisualizer() {
        int[] snapshot = new int[size];
        Node cur = front;
        int i = 0;
        while (cur != null) { snapshot[i++] = cur.data; cur = cur.next; }
        return snapshot;
    }
}
