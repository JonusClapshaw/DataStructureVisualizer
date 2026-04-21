package com.example;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * ================================================================
 *  TEST SUITE: MyQueue
 * ================================================================
 *  Run:  mvn test -Dtest=MyQueueTest
 *
 *    Wave 1 – Intro       : enqueue, peek, isEmpty
 *    Wave 2 – Basic       : dequeue, size
 *    Wave 3 – Intermediate: contains, clear, toArray
 *    Wave 4 – Advanced    : reverseQueue, interleave
 * ================================================================
 */
public class MyQueueTest {

    private MyQueue queue;

    @Before
    public void setUp() {
        queue = new MyQueue();
    }

    // ================================================================
    //  WAVE 1 TESTS — enqueue, peek, isEmpty
    // ================================================================

    @Test
    public void wave1_isEmpty_newQueue_returnsTrue() {
        assertTrue(queue.isEmpty());
    }

    @Test
    public void wave1_isEmpty_afterEnqueue_returnsFalse() {
        queue.enqueue(1);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void wave1_enqueue_singleElement_peekReturnsIt() {
        queue.enqueue(42);
        assertEquals(42, queue.peek());
    }

    @Test
    public void wave1_enqueue_threeElements_peekReturnsFIFOFront() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(1, queue.peek()); // FIFO: first-in is still at front
    }

    @Test
    public void wave1_peek_doesNotRemoveElement() {
        queue.enqueue(5);
        queue.peek();
        assertFalse(queue.isEmpty());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void wave1_peek_emptyQueue_throwsException() {
        queue.peek();
    }

    // ================================================================
    //  WAVE 2 TESTS — dequeue, size
    // ================================================================

    @Test
    public void wave2_dequeue_returnsFrontValue() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        assertEquals(10, queue.dequeue()); // FIFO
    }

    @Test
    public void wave2_dequeue_removesFromFront() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.dequeue();
        assertEquals(2, queue.peek()); // next front is now 2
    }

    @Test
    public void wave2_dequeue_untilEmpty_isEmptyReturnsTrue() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.dequeue();
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void wave2_dequeue_emptyQueue_throwsException() {
        queue.dequeue();
    }

    @Test
    public void wave2_size_emptyQueue_returnsZero() {
        assertEquals(0, queue.size());
    }

    @Test
    public void wave2_size_afterEnqueueAndDequeue() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(3, queue.size());
        queue.dequeue();
        assertEquals(2, queue.size());
    }

    // ================================================================
    //  WAVE 3 TESTS — contains, clear, toArray
    // ================================================================

    @Test
    public void wave3_contains_existingValue_returnsTrue() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        assertTrue(queue.contains(20));
    }

    @Test
    public void wave3_contains_missingValue_returnsFalse() {
        queue.enqueue(1);
        queue.enqueue(2);
        assertFalse(queue.contains(99));
    }

    @Test
    public void wave3_contains_emptyQueue_returnsFalse() {
        assertFalse(queue.contains(1));
    }

    @Test
    public void wave3_clear_removesAllElements() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.clear();
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void wave3_clear_thenEnqueue_worksFine() {
        queue.enqueue(1);
        queue.clear();
        queue.enqueue(99);
        assertEquals(99, queue.peek());
    }

    @Test
    public void wave3_toArray_returnsElementsFrontToBack() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertArrayEquals(new int[]{1, 2, 3}, queue.toArray());
    }

    @Test
    public void wave3_toArray_emptyQueue_returnsEmptyArray() {
        assertEquals(0, queue.toArray().length);
    }

    // ================================================================
    //  WAVE 4 TESTS — reverseQueue, interleave
    // ================================================================

    @Test
    public void wave4_reverseQueue_threeElements() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.reverseQueue();
        assertArrayEquals(new int[]{3, 2, 1}, queue.toArray());
    }

    @Test
    public void wave4_reverseQueue_singleElement_unchanged() {
        queue.enqueue(42);
        queue.reverseQueue();
        assertEquals(42, queue.peek());
        assertEquals(1, queue.size());
    }

    @Test
    public void wave4_reverseQueue_thenReverseAgain_restoredToOriginal() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.reverseQueue();
        queue.reverseQueue();
        assertArrayEquals(new int[]{1, 2, 3}, queue.toArray());
    }

    @Test
    public void wave4_interleave_sixElements() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        queue.enqueue(5);
        queue.enqueue(6);
        queue.interleave();
        assertArrayEquals(new int[]{1, 4, 2, 5, 3, 6}, queue.toArray());
    }

    @Test
    public void wave4_interleave_twoElements() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.interleave();
        assertArrayEquals(new int[]{1, 2}, queue.toArray());
    }

    @Test(expected = IllegalStateException.class)
    public void wave4_interleave_oddNumberOfElements_throwsException() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.interleave();
    }
}
