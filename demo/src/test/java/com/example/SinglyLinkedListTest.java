package com.example;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * ================================================================
 *  TEST SUITE: SinglyLinkedList
 * ================================================================
 *
 *  Each test verifies a specific method in SinglyLinkedList.java.
 *  Tests are grouped by wave — complete Wave 1 before Wave 2, etc.
 *
 *  Run all tests for this structure:
 *    mvn test -Dtest=SinglyLinkedListTest
 *
 *  Run a single test by name:
 *    mvn test -Dtest=SinglyLinkedListTest#wave1_addFirst_singleElement
 *
 *  A PASSING test = green. A FAILING test = your TODO still needs work.
 * ================================================================
 */
public class SinglyLinkedListTest {

    private SinglyLinkedList list;

    @Before
    public void setUp() {
        list = new SinglyLinkedList();
    }

    // ================================================================
    //  WAVE 1 TESTS — addFirst, getSize, printList
    // ================================================================

    @Test
    public void wave1_addFirst_singleElement_sizeIsOne() {
        list.addFirst(10);
        assertEquals(1, list.getSize());
    }

    @Test
    public void wave1_addFirst_threeElements_buildsInReverseOrder() {
        list.addFirst(3);
        list.addFirst(2);
        list.addFirst(1);
        // Each addFirst prepends, so result should be 1 -> 2 -> 3
        assertEquals("1 -> 2 -> 3 -> null", list.printList());
    }

    @Test
    public void wave1_getSize_emptyList_returnsZero() {
        assertEquals(0, list.getSize());
    }

    @Test
    public void wave1_getSize_afterFiveAdds_returnsFive() {
        for (int i = 1; i <= 5; i++) {
            list.addFirst(i);
        }
        assertEquals(5, list.getSize());
    }

    @Test
    public void wave1_printList_emptyList_returnsNull() {
        assertEquals("null", list.printList());
    }

    @Test
    public void wave1_printList_singleElement_formattedCorrectly() {
        list.addFirst(42);
        assertEquals("42 -> null", list.printList());
    }

    // ================================================================
    //  WAVE 2 TESTS — addLast, removeFirst, contains
    // ================================================================

    @Test
    public void wave2_addLast_appendsToEnd() {
        list.addFirst(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals("1 -> 2 -> 3 -> null", list.printList());
    }

    @Test
    public void wave2_addLast_onEmptyList_becomesOnlyElement() {
        list.addLast(99);
        assertEquals("99 -> null", list.printList());
        assertEquals(1, list.getSize());
    }

    @Test
    public void wave2_removeFirst_returnsHeadValueAndShortenslist() {
        list.addFirst(3);
        list.addFirst(2);
        list.addFirst(1);
        int removed = list.removeFirst();
        assertEquals(1, removed);
        assertEquals("2 -> 3 -> null", list.printList());
    }

    @Test
    public void wave2_removeFirst_untilEmpty_sizeDropsToZero() {
        list.addFirst(1);
        list.addFirst(2);
        list.removeFirst();
        list.removeFirst();
        assertEquals(0, list.getSize());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void wave2_removeFirst_onEmptyList_throwsException() {
        list.removeFirst();
    }

    @Test
    public void wave2_contains_existingValue_returnsTrue() {
        list.addFirst(3);
        list.addFirst(2);
        list.addFirst(1);
        assertTrue(list.contains(2));
    }

    @Test
    public void wave2_contains_missingValue_returnsFalse() {
        list.addFirst(1);
        list.addFirst(2);
        assertFalse(list.contains(99));
    }

    @Test
    public void wave2_contains_emptyList_returnsFalse() {
        assertFalse(list.contains(1));
    }

    // ================================================================
    //  WAVE 3 TESTS — removeByValue, getAt, insertAt
    // ================================================================

    @Test
    public void wave3_removeByValue_removesHeadNode() {
        list.addFirst(3);
        list.addFirst(2);
        list.addFirst(1);
        assertTrue(list.removeByValue(1));
        assertEquals("2 -> 3 -> null", list.printList());
    }

    @Test
    public void wave3_removeByValue_removesMiddleNode() {
        list.addFirst(3);
        list.addFirst(2);
        list.addFirst(1);
        assertTrue(list.removeByValue(2));
        assertEquals("1 -> 3 -> null", list.printList());
    }

    @Test
    public void wave3_removeByValue_removesTailNode() {
        list.addFirst(3);
        list.addFirst(2);
        list.addFirst(1);
        assertTrue(list.removeByValue(3));
        assertEquals("1 -> 2 -> null", list.printList());
    }

    @Test
    public void wave3_removeByValue_valueNotFound_returnsFalse() {
        list.addFirst(1);
        assertFalse(list.removeByValue(99));
    }

    @Test
    public void wave3_removeByValue_emptyList_returnsFalse() {
        assertFalse(list.removeByValue(5));
    }

    @Test
    public void wave3_getAt_returnsCorrectElementsAtEachIndex() {
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        assertEquals(10, list.getAt(0));
        assertEquals(20, list.getAt(1));
        assertEquals(30, list.getAt(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void wave3_getAt_indexTooLarge_throwsException() {
        list.addFirst(1);
        list.getAt(5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void wave3_getAt_negativeIndex_throwsException() {
        list.addFirst(1);
        list.getAt(-1);
    }

    @Test
    public void wave3_insertAt_insertsInMiddle() {
        list.addLast(1);
        list.addLast(3);
        list.insertAt(1, 2);
        assertEquals("1 -> 2 -> 3 -> null", list.printList());
    }

    @Test
    public void wave3_insertAt_atIndexZero_behavesLikeAddFirst() {
        list.addLast(2);
        list.addLast(3);
        list.insertAt(0, 1);
        assertEquals("1 -> 2 -> 3 -> null", list.printList());
    }

    @Test
    public void wave3_insertAt_atTailIndex_behavesLikeAddLast() {
        list.addLast(1);
        list.addLast(2);
        list.insertAt(2, 3);
        assertEquals("1 -> 2 -> 3 -> null", list.printList());
    }

    // ================================================================
    //  WAVE 4 TESTS — reverse, findMiddle, hasCycle
    // ================================================================

    @Test
    public void wave4_reverse_oddLengthList() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.reverse();
        assertEquals("3 -> 2 -> 1 -> null", list.printList());
    }

    @Test
    public void wave4_reverse_evenLengthList() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.addLast(4);
        list.reverse();
        assertEquals("4 -> 3 -> 2 -> 1 -> null", list.printList());
    }

    @Test
    public void wave4_reverse_singleElement_noChange() {
        list.addFirst(42);
        list.reverse();
        assertEquals("42 -> null", list.printList());
    }

    @Test
    public void wave4_findMiddle_oddLengthList_returnsCenterNode() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(2, list.findMiddle());
    }

    @Test
    public void wave4_findMiddle_evenLengthList_returnsSecondMiddle() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.addLast(4);
        assertEquals(3, list.findMiddle()); // second of the two middles
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void wave4_findMiddle_emptyList_throwsException() {
        list.findMiddle();
    }

    @Test
    public void wave4_hasCycle_noCycle_returnsFalse() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertFalse(list.hasCycle());
    }

    @Test
    public void wave4_hasCycle_cycleAtHead_returnsTrue() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.createCycleForTesting(0); // tail points back to node[0]
        assertTrue(list.hasCycle());
    }

    @Test
    public void wave4_hasCycle_cycleInMiddle_returnsTrue() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.addLast(4);
        list.createCycleForTesting(1); // tail points back to node[1]
        assertTrue(list.hasCycle());
    }
}
