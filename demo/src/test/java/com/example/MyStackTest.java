package com.example;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * ================================================================
 *  TEST SUITE: MyStack
 * ================================================================
 *  Run:  mvn test -Dtest=MyStackTest
 *
 *    Wave 1 – Intro       : push, peek, isEmpty
 *    Wave 2 – Basic       : pop, size, isFull
 *    Wave 3 – Intermediate: contains, toArray, clear
 *    Wave 4 – Advanced    : reverseString, evaluatePostfix
 * ================================================================
 */
public class MyStackTest {

    private MyStack stack;

    @Before
    public void setUp() {
        stack = new MyStack(10); // capacity 10 for most tests
    }

    // ================================================================
    //  WAVE 1 TESTS — push, peek, isEmpty
    // ================================================================

    @Test
    public void wave1_isEmpty_newStack_returnsTrue() {
        assertTrue(stack.isEmpty());
    }

    @Test
    public void wave1_isEmpty_afterPush_returnsFalse() {
        stack.push(1);
        assertFalse(stack.isEmpty());
    }

    @Test
    public void wave1_push_singleElement_peekReturnsIt() {
        stack.push(42);
        assertEquals(42, stack.peek());
    }

    @Test
    public void wave1_push_threeElements_peekReturnsLast() {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.peek()); // LIFO: last pushed is on top
    }

    @Test
    public void wave1_peek_doesNotRemoveElement() {
        stack.push(7);
        stack.peek();
        assertFalse(stack.isEmpty());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void wave1_peek_onEmptyStack_throwsException() {
        stack.peek();
    }

    @Test(expected = IllegalStateException.class)
    public void wave1_push_beyondCapacity_throwsException() {
        MyStack small = new MyStack(2);
        small.push(1);
        small.push(2);
        small.push(3); // should throw
    }

    // ================================================================
    //  WAVE 2 TESTS — pop, size, isFull
    // ================================================================

    @Test
    public void wave2_pop_returnsTopAndRemovesIt() {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.peek()); // 2 is now on top
    }

    @Test
    public void wave2_pop_untilEmpty_sizeDropsToZero() {
        stack.push(10);
        stack.push(20);
        stack.pop();
        stack.pop();
        assertEquals(0, stack.size());
        assertTrue(stack.isEmpty());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void wave2_pop_onEmptyStack_throwsException() {
        stack.pop();
    }

    @Test
    public void wave2_size_emptyStack_returnsZero() {
        assertEquals(0, stack.size());
    }

    @Test
    public void wave2_size_afterThreePushes_returnsThree() {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.size());
    }

    @Test
    public void wave2_size_decreasesAfterPop() {
        stack.push(1);
        stack.push(2);
        stack.pop();
        assertEquals(1, stack.size());
    }

    @Test
    public void wave2_isFull_notFullByDefault_returnsFalse() {
        stack.push(1);
        assertFalse(stack.isFull());
    }

    @Test
    public void wave2_isFull_whenAtCapacity_returnsTrue() {
        MyStack small = new MyStack(3);
        small.push(1);
        small.push(2);
        small.push(3);
        assertTrue(small.isFull());
    }

    // ================================================================
    //  WAVE 3 TESTS — contains, toArray, clear
    // ================================================================

    @Test
    public void wave3_contains_existingValue_returnsTrue() {
        stack.push(5);
        stack.push(10);
        stack.push(15);
        assertTrue(stack.contains(10));
    }

    @Test
    public void wave3_contains_missingValue_returnsFalse() {
        stack.push(1);
        stack.push(2);
        assertFalse(stack.contains(99));
    }

    @Test
    public void wave3_contains_emptyStack_returnsFalse() {
        assertFalse(stack.contains(1));
    }

    @Test
    public void wave3_toArray_returnsElementsBottomToTop() {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        int[] arr = stack.toArray();
        assertArrayEquals(new int[]{1, 2, 3}, arr);
    }

    @Test
    public void wave3_toArray_emptyStack_returnsEmptyArray() {
        int[] arr = stack.toArray();
        assertEquals(0, arr.length);
    }

    @Test
    public void wave3_clear_removesAllElements() {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.clear();
        assertEquals(0, stack.size());
        assertTrue(stack.isEmpty());
    }

    @Test
    public void wave3_clear_thenPush_worksFine() {
        stack.push(1);
        stack.clear();
        stack.push(99);
        assertEquals(99, stack.peek());
        assertEquals(1, stack.size());
    }

    // ================================================================
    //  WAVE 4 TESTS — reverseString, evaluatePostfix
    // ================================================================

    @Test
    public void wave4_reverseString_multiCharString() {
        MyStack s = new MyStack(10);
        assertEquals("olleh", s.reverseString("hello"));
    }

    @Test
    public void wave4_reverseString_palindrome_unchanged() {
        MyStack s = new MyStack(10);
        assertEquals("racecar", s.reverseString("racecar"));
    }

    @Test
    public void wave4_reverseString_singleChar_unchanged() {
        MyStack s = new MyStack(10);
        assertEquals("a", s.reverseString("a"));
    }

    @Test
    public void wave4_evaluatePostfix_simpleAddition() {
        MyStack s = new MyStack(10);
        assertEquals(7, s.evaluatePostfix("3 4 +"));
    }

    @Test
    public void wave4_evaluatePostfix_multiplication() {
        MyStack s = new MyStack(20);
        assertEquals(14, s.evaluatePostfix("3 4 + 2 *")); // (3+4)*2
    }

    @Test
    public void wave4_evaluatePostfix_complexExpression() {
        MyStack s = new MyStack(20);
        assertEquals(14, s.evaluatePostfix("5 1 2 + 4 * + 3 -")); // 5+((1+2)*4)-3
    }

    @Test
    public void wave4_evaluatePostfix_singleNumber() {
        MyStack s = new MyStack(10);
        assertEquals(42, s.evaluatePostfix("42"));
    }
}
