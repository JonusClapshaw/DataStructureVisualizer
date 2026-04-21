package com.example;

import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

/**
 * ================================================================
 *  TEST SUITE: BinarySearchTree
 * ================================================================
 *  Run:  mvn test -Dtest=BinarySearchTreeTest
 *
 *    Wave 1 – Intro       : insert, contains
 *    Wave 2 – Basic       : findMin, findMax, height
 *    Wave 3 – Intermediate: inorder, preorder, postorder
 *    Wave 4 – Advanced    : delete, countNodes, isBalanced
 * ================================================================
 */
public class BinarySearchTreeTest {

    private BinarySearchTree bst;

    @Before
    public void setUp() {
        bst = new BinarySearchTree();
    }

    // ================================================================
    //  WAVE 1 TESTS — insert, contains
    // ================================================================

    @Test
    public void wave1_insert_singleValue_rootIsNotNull() {
        bst.insert(10);
        assertNotNull(bst.getRoot());
        assertEquals(10, bst.getRoot().data);
    }

    @Test
    public void wave1_insert_smallerValueGoesLeft() {
        bst.insert(10);
        bst.insert(5);
        assertEquals(5, bst.getRoot().left.data);
    }

    @Test
    public void wave1_insert_largerValueGoesRight() {
        bst.insert(10);
        bst.insert(15);
        assertEquals(15, bst.getRoot().right.data);
    }

    @Test
    public void wave1_insert_duplicate_ignored() {
        bst.insert(10);
        bst.insert(10);
        // tree still has only one node (no right or left child added)
        assertNull(bst.getRoot().left);
        assertNull(bst.getRoot().right);
    }

    @Test
    public void wave1_contains_insertedValue_returnsTrue() {
        bst.insert(8);
        bst.insert(3);
        bst.insert(10);
        assertTrue(bst.contains(3));
        assertTrue(bst.contains(10));
        assertTrue(bst.contains(8));
    }

    @Test
    public void wave1_contains_missingValue_returnsFalse() {
        bst.insert(8);
        bst.insert(3);
        assertFalse(bst.contains(99));
    }

    @Test
    public void wave1_contains_emptyTree_returnsFalse() {
        assertFalse(bst.contains(1));
    }

    // ================================================================
    //  WAVE 2 TESTS — findMin, findMax, height
    // ================================================================

    @Test
    public void wave2_findMin_returnsSmallestValue() {
        bst.insert(8);
        bst.insert(3);
        bst.insert(10);
        bst.insert(1);
        assertEquals(1, bst.findMin());
    }

    @Test
    public void wave2_findMin_singleNode_returnsRoot() {
        bst.insert(42);
        assertEquals(42, bst.findMin());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void wave2_findMin_emptyTree_throwsException() {
        bst.findMin();
    }

    @Test
    public void wave2_findMax_returnsLargestValue() {
        bst.insert(8);
        bst.insert(3);
        bst.insert(10);
        bst.insert(14);
        assertEquals(14, bst.findMax());
    }

    @Test
    public void wave2_findMax_singleNode_returnsRoot() {
        bst.insert(7);
        assertEquals(7, bst.findMax());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void wave2_findMax_emptyTree_throwsException() {
        bst.findMax();
    }

    @Test
    public void wave2_height_emptyTree_returnsNegativeOne() {
        assertEquals(-1, bst.height());
    }

    @Test
    public void wave2_height_singleNode_returnsZero() {
        bst.insert(5);
        assertEquals(0, bst.height());
    }

    @Test
    public void wave2_height_balancedTree_returnsCorrectHeight() {
        //       [4]
        //      /   \
        //    [2]   [6]
        bst.insert(4);
        bst.insert(2);
        bst.insert(6);
        assertEquals(1, bst.height());
    }

    @Test
    public void wave2_height_skewedTree_returnsCorrectHeight() {
        // [1] → [2] → [3] (all right children)
        bst.insert(1);
        bst.insert(2);
        bst.insert(3);
        assertEquals(2, bst.height());
    }

    // ================================================================
    //  WAVE 3 TESTS — inorder, preorder, postorder
    // ================================================================

    // Tree used for traversal tests:
    //       [4]
    //      /   \
    //    [2]   [6]
    //   /   \
    // [1]   [3]

    private void buildTraversalTree() {
        bst.insert(4);
        bst.insert(2);
        bst.insert(6);
        bst.insert(1);
        bst.insert(3);
    }

    @Test
    public void wave3_inorder_producesSortedOrder() {
        buildTraversalTree();
        List<Integer> result = bst.inorder();
        assertEquals(Arrays.asList(1, 2, 3, 4, 6), result);
    }

    @Test
    public void wave3_inorder_emptyTree_returnsEmptyList() {
        assertTrue(bst.inorder().isEmpty());
    }

    @Test
    public void wave3_preorder_rootFirstThenLeftThenRight() {
        buildTraversalTree();
        List<Integer> result = bst.preorder();
        assertEquals(Arrays.asList(4, 2, 1, 3, 6), result);
    }

    @Test
    public void wave3_preorder_emptyTree_returnsEmptyList() {
        assertTrue(bst.preorder().isEmpty());
    }

    @Test
    public void wave3_postorder_childrenBeforeParent() {
        buildTraversalTree();
        List<Integer> result = bst.postorder();
        assertEquals(Arrays.asList(1, 3, 2, 6, 4), result);
    }

    @Test
    public void wave3_postorder_emptyTree_returnsEmptyList() {
        assertTrue(bst.postorder().isEmpty());
    }

    // ================================================================
    //  WAVE 4 TESTS — delete, countNodes, isBalanced
    // ================================================================

    @Test
    public void wave4_delete_leafNode_removedFromTree() {
        bst.insert(5);
        bst.insert(3);
        bst.insert(7);
        bst.delete(3);
        assertFalse(bst.contains(3));
        assertTrue(bst.contains(5));
        assertTrue(bst.contains(7));
    }

    @Test
    public void wave4_delete_nodeWithOneChild() {
        //   [5]
        //     \
        //     [7]
        //       \
        //       [9]
        bst.insert(5);
        bst.insert(7);
        bst.insert(9);
        bst.delete(7); // has one right child
        assertFalse(bst.contains(7));
        assertTrue(bst.contains(9)); // child should be promoted
    }

    @Test
    public void wave4_delete_nodeWithTwoChildren_maintainsBSTOrder() {
        //       [5]
        //      /   \
        //    [3]   [8]
        //         /   \
        //       [6]   [9]
        bst.insert(5);
        bst.insert(3);
        bst.insert(8);
        bst.insert(6);
        bst.insert(9);
        bst.delete(8); // has two children; inorder successor is 9
        assertFalse(bst.contains(8));
        // BST order must be maintained
        List<Integer> sorted = bst.inorder();
        for (int i = 0; i < sorted.size() - 1; i++) {
            assertTrue(sorted.get(i) < sorted.get(i + 1));
        }
    }

    @Test
    public void wave4_delete_missingValue_treeUnchanged() {
        bst.insert(5);
        bst.insert(3);
        bst.delete(99); // not in tree
        assertTrue(bst.contains(5));
        assertTrue(bst.contains(3));
    }

    @Test
    public void wave4_countNodes_emptyTree_returnsZero() {
        assertEquals(0, bst.countNodes());
    }

    @Test
    public void wave4_countNodes_afterInserts_returnsCorrectCount() {
        bst.insert(4);
        bst.insert(2);
        bst.insert(6);
        bst.insert(1);
        assertEquals(4, bst.countNodes());
    }

    @Test
    public void wave4_countNodes_afterDeleteReducesCount() {
        bst.insert(4);
        bst.insert(2);
        bst.insert(6);
        bst.delete(2);
        assertEquals(2, bst.countNodes());
    }

    @Test
    public void wave4_isBalanced_emptyTree_returnsTrue() {
        assertTrue(bst.isBalanced());
    }

    @Test
    public void wave4_isBalanced_balancedTree_returnsTrue() {
        bst.insert(4);
        bst.insert(2);
        bst.insert(6);
        assertTrue(bst.isBalanced());
    }

    @Test
    public void wave4_isBalanced_skewedTree_returnsFalse() {
        // Skewed right: 1 → 2 → 3 → 4
        bst.insert(1);
        bst.insert(2);
        bst.insert(3);
        bst.insert(4);
        assertFalse(bst.isBalanced());
    }
}
