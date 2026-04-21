package com.example;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * ================================================================
 *  TEST SUITE: DoublyLinkedList
 * ================================================================
 *  Run:  mvn test -Dtest=DoublyLinkedListTest
 *
 *    Wave 1 – Intro       : addFirst, getSize, printForward
 *    Wave 2 – Basic       : addLast, removeFirst, removeLast
 *    Wave 3 – Intermediate: removeByValue, insertAt, printBackward
 *    Wave 4 – Advanced    : reverse, sortAscending
 * ================================================================
 */
public class DoublyLinkedListTest {

    private DoublyLinkedList list;

    @Before
    public void setUp() {
        list = new DoublyLinkedList();
    }

    // ================================================================
    //  WAVE 1 TESTS — addFirst, getSize, printForward
    // ================================================================

    @Test
    public void wave1_addFirst_singleElement_sizeIsOne() {
        list.addFirst(10);
        assertEquals(1, list.getSize());
    }

    @Test
    public void wave1_addFirst_setsHeadAndTailOnEmptyList() {
        list.addFirst(5);
        assertNotNull(list.getHead());
        assertNotNull(list.getTail());
        assertEquals(list.getHead(), list.getTail());
    }

    @Test
    public void wave1_addFirst_threeElements_headPointsToFirst() {
        list.addFirst(3);
        list.addFirst(2);
        list.addFirst(1);
        assertEquals(1, list.getHead().data);
        assertEquals(3, list.getTail().data);
    }

    @Test
    public void wave1_getSize_emptyList_returnsZero() {
        assertEquals(0, list.getSize());
    }

    @Test
    public void wave1_getSize_afterMultipleAdds() {
        list.addFirst(1);
        list.addFirst(2);
        list.addFirst(3);
        assertEquals(3, list.getSize());
    }

    @Test
    public void wave1_printForward_emptyList_returnsEmptyString() {
        assertEquals("", list.printForward());
    }

    @Test
    public void wave1_printForward_singleElement() {
        list.addFirst(7);
        assertEquals("7", list.printForward());
    }

    @Test
    public void wave1_printForward_multipleElements() {
        list.addFirst(3);
        list.addFirst(2);
        list.addFirst(1);
        assertEquals("1 <-> 2 <-> 3", list.printForward());
    }

    // ================================================================
    //  WAVE 2 TESTS — addLast, removeFirst, removeLast
    // ================================================================

    @Test
    public void wave2_addLast_appendsToEnd() {
        list.addFirst(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals("1 <-> 2 <-> 3", list.printForward());
    }

    @Test
    public void wave2_addLast_onEmptyList_setsHeadAndTail() {
        list.addLast(42);
        assertEquals(list.getHead(), list.getTail());
        assertEquals(42, list.getHead().data);
    }

    @Test
    public void wave2_addLast_tailPrevPointsToNewSecondToLast() {
        list.addLast(1);
        list.addLast(2);
        assertNotNull(list.getTail().prev);
        assertEquals(1, list.getTail().prev.data);
    }

    @Test
    public void wave2_removeFirst_returnsValueAndUpdatesHead() {
        list.addFirst(3);
        list.addFirst(2);
        list.addFirst(1);
        int removed = list.removeFirst();
        assertEquals(1, removed);
        assertEquals(2, list.getHead().data);
        assertNull(list.getHead().prev);  // new head's prev must be null
    }

    @Test
    public void wave2_removeFirst_singleElement_listBecomesEmpty() {
        list.addFirst(5);
        list.removeFirst();
        assertNull(list.getHead());
        assertNull(list.getTail());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void wave2_removeFirst_emptyList_throwsException() {
        list.removeFirst();
    }

    @Test
    public void wave2_removeLast_returnsValueAndUpdatesTail() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        int removed = list.removeLast();
        assertEquals(3, removed);
        assertEquals(2, list.getTail().data);
        assertNull(list.getTail().next);  // new tail's next must be null
    }

    @Test
    public void wave2_removeLast_singleElement_listBecomesEmpty() {
        list.addFirst(9);
        list.removeLast();
        assertNull(list.getHead());
        assertNull(list.getTail());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void wave2_removeLast_emptyList_throwsException() {
        list.removeLast();
    }

    // ================================================================
    //  WAVE 3 TESTS — removeByValue, insertAt, printBackward
    // ================================================================

    @Test
    public void wave3_removeByValue_removesHead() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertTrue(list.removeByValue(1));
        assertEquals("2 <-> 3", list.printForward());
    }

    @Test
    public void wave3_removeByValue_removesTail() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertTrue(list.removeByValue(3));
        assertEquals("1 <-> 2", list.printForward());
    }

    @Test
    public void wave3_removeByValue_removesMiddle() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertTrue(list.removeByValue(2));
        assertEquals("1 <-> 3", list.printForward());
    }

    @Test
    public void wave3_removeByValue_notFound_returnsFalse() {
        list.addFirst(1);
        assertFalse(list.removeByValue(99));
    }

    @Test
    public void wave3_insertAt_insertsAtMiddle() {
        list.addLast(1);
        list.addLast(3);
        list.insertAt(1, 2);
        assertEquals("1 <-> 2 <-> 3", list.printForward());
    }

    @Test
    public void wave3_insertAt_atIndexZero_behavesLikeAddFirst() {
        list.addLast(2);
        list.addLast(3);
        list.insertAt(0, 1);
        assertEquals("1 <-> 2 <-> 3", list.printForward());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void wave3_insertAt_negativeIndex_throwsException() {
        list.insertAt(-1, 5);
    }

    @Test
    public void wave3_printBackward_multipleElements() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals("3 <-> 2 <-> 1", list.printBackward());
    }

    @Test
    public void wave3_printBackward_singleElement() {
        list.addFirst(42);
        assertEquals("42", list.printBackward());
    }

    @Test
    public void wave3_printBackward_emptyList_returnsEmptyString() {
        assertEquals("", list.printBackward());
    }

    // ================================================================
    //  WAVE 4 TESTS — reverse, sortAscending
    // ================================================================

    @Test
    public void wave4_reverse_threeElementList() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.reverse();
        assertEquals("3 <-> 2 <-> 1", list.printForward());
        assertEquals(3, list.getHead().data);
        assertEquals(1, list.getTail().data);
    }

    @Test
    public void wave4_reverse_singleElement_noChange() {
        list.addFirst(7);
        list.reverse();
        assertEquals("7", list.printForward());
    }

    @Test
    public void wave4_reverse_prevPointersCorrectAfterReversal() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.reverse();
        // Walk backward from tail to head — should equal forward order before reversal
        assertEquals("1 <-> 2 <-> 3", list.printBackward());
    }

    @Test
    public void wave4_sortAscending_unsortedList_sortedCorrectly() {
        list.addLast(3);
        list.addLast(1);
        list.addLast(4);
        list.addLast(2);
        list.sortAscending();
        assertEquals("1 <-> 2 <-> 3 <-> 4", list.printForward());
    }

    @Test
    public void wave4_sortAscending_alreadySorted_unchanged() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.sortAscending();
        assertEquals("1 <-> 2 <-> 3", list.printForward());
    }

    @Test
    public void wave4_sortAscending_singleElement_unchanged() {
        list.addFirst(5);
        list.sortAscending();
        assertEquals("5", list.printForward());
    }
}
