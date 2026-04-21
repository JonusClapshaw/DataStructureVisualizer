package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================================
 *  DATA STRUCTURE: Binary Search Tree (BST)
 * ================================================================
 *
 *  A Binary Search Tree is a tree where each node has at most two
 *  children and obeys the BST property:
 *
 *    For every node N:
 *      - Every value in N's LEFT subtree  is LESS THAN    N.data
 *      - Every value in N's RIGHT subtree is GREATER THAN N.data
 *
 *  Visual:
 *              [8]
 *             /   \
 *           [3]   [10]
 *          /   \     \
 *        [1]   [6]  [14]
 *
 *  Key properties:
 *    - Average O(log n) for insert, search, delete
 *    - Worst-case O(n) on a degenerate (unbalanced) tree
 *    - Inorder traversal yields elements in sorted order
 *
 * ================================================================
 *  HOW THE WAVES WORK
 * ================================================================
 *  Complete each TODO then run:  mvn test -Dtest=BinarySearchTreeTest
 *
 *    Wave 1 – Intro       : insert, contains
 *    Wave 2 – Basic       : findMin, findMax, height
 *    Wave 3 – Intermediate: inorder, preorder, postorder
 *    Wave 4 – Advanced    : delete, countNodes, isBalanced
 * ================================================================
 */
public class BinarySearchTree {

    // ----------------------------------------------------------
    //  Inner Node class — do NOT modify
    // ----------------------------------------------------------
    public static class Node {
        public int data;
        public Node left;
        public Node right;

        public Node(int data) {
            this.data  = data;
            this.left  = null;
            this.right = null;
        }
    }

    private Node root;

    /** Creates an empty Binary Search Tree. */
    public BinarySearchTree() {
        this.root = null;
    }

    // ================================================================
    //  WAVE 1 — INTRO
    //  Goal: Build the tree and verify membership.
    // ================================================================

    /**
     * [Wave 1 – Task 1] insert
     * ------------------------
     * Insert {@code value} into the BST while maintaining BST order.
     * Ignore duplicates (if value already exists, do nothing).
     *
     * This method calls the recursive helper insertHelper.
     *
     * Recursive logic:
     *   - node is null           → return new Node(value)   ← base case
     *   - value < node.data      → recurse LEFT
     *   - value > node.data      → recurse RIGHT
     *   - value == node.data     → return node (duplicate, skip)
     *
     * @param value the value to insert
     */
    public void insert(int value) {
        root = insertHelper(root, value);
    }

    private Node insertHelper(Node node, int value) {
        if (node == null) {
            return new Node(value);
        }
        if (value < node.data) {
            // TODO: recurse left — node.left = insertHelper(node.left, value)
            insertHelper(node.left, value);
        } else if (value > node.data) {
            // TODO: recurse right — node.right = insertHelper(node.right, value)
            insertHelper(node.right, value);
        }
        // value == node.data: duplicate, do nothing
        return node;
    }

    /**
     * [Wave 1 – Task 2] contains
     * --------------------------
     * Return true if {@code value} exists in the BST.
     *
     * Recursive logic:
     *   - node is null           → false
     *   - value == node.data     → true
     *   - value < node.data      → search LEFT
     *   - value > node.data      → search RIGHT
     *
     * @param value value to search for
     * @return true if found
     */
    public boolean contains(int value) {
        // TODO: call a recursive helper (or implement iteratively)
        return false; // placeholder
    }

    // ================================================================
    //  WAVE 2 — BASIC
    //  Goal: Find extremes and measure tree depth.
    // ================================================================

    /**
     * [Wave 2 – Task 1] findMin
     * -------------------------
     * Return the minimum value in the BST.
     * In a BST, the minimum is always the LEFTMOST node.
     *
     * Throws {@link java.util.NoSuchElementException} if the tree is empty.
     *
     * Hint: start at root, keep going left until left == null.
     *
     * @return minimum value
     */
    public int findMin() {
        if (root == null) throw new java.util.NoSuchElementException("Tree is empty");
        // TODO: walk left until node.left == null, then return node.data
        return -1; // placeholder
    }

    /**
     * [Wave 2 – Task 2] findMax
     * -------------------------
     * Return the maximum value in the BST.
     * In a BST, the maximum is always the RIGHTMOST node.
     *
     * Throws {@link java.util.NoSuchElementException} if the tree is empty.
     *
     * Hint: start at root, keep going right until right == null.
     *
     * @return maximum value
     */
    public int findMax() {
        if (root == null) throw new java.util.NoSuchElementException("Tree is empty");
        // TODO: walk right until node.right == null, then return node.data
        return -1; // placeholder
    }

    /**
     * [Wave 2 – Task 3] height
     * ------------------------
     * Return the HEIGHT of the tree — the number of edges on the longest
     * path from the root to any leaf.
     *
     * Convention:
     *   - null node     → height = -1
     *   - single node   → height = 0
     *   - root + 1 child → height = 1
     *
     * Recursive formula:
     *   height(node) = 1 + max( height(node.left), height(node.right) )
     *
     * @return height of the BST
     */
    public int height() {
        return heightHelper(root);
    }

    private int heightHelper(Node node) {
        if (node == null) return -1;
        // TODO: return 1 + max(heightHelper(node.left), heightHelper(node.right))
        return 0; // placeholder
    }

    // ================================================================
    //  WAVE 3 — INTERMEDIATE
    //  Goal: Traverse the tree in all three classic orders.
    // ================================================================

    /**
     * [Wave 3 – Task 1] inorder
     * -------------------------
     * Return a List of values visited in INORDER order:
     *   Left → Root → Right
     *
     * For a valid BST, inorder traversal always produces a sorted
     * (ascending) sequence.
     *
     * Example tree:       [4]
     *                    /   \
     *                  [2]   [6]
     *   inorder() → [2, 4, 6]
     *
     * @return list of values in inorder
     */
    public List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private void inorderHelper(Node node, List<Integer> result) {
        // TODO: recurse left, add node.data, recurse right
    }

    /**
     * [Wave 3 – Task 2] preorder
     * --------------------------
     * Return a List of values visited in PREORDER order:
     *   Root → Left → Right
     *
     * Example (same tree above):  preorder() → [4, 2, 6]
     *
     * @return list of values in preorder
     */
    public List<Integer> preorder() {
        List<Integer> result = new ArrayList<>();
        preorderHelper(root, result);
        return result;
    }

    private void preorderHelper(Node node, List<Integer> result) {
        // TODO: add node.data, recurse left, recurse right
    }

    /**
     * [Wave 3 – Task 3] postorder
     * ---------------------------
     * Return a List of values visited in POSTORDER order:
     *   Left → Right → Root
     *
     * Example (same tree above):  postorder() → [2, 6, 4]
     *
     * @return list of values in postorder
     */
    public List<Integer> postorder() {
        List<Integer> result = new ArrayList<>();
        postorderHelper(root, result);
        return result;
    }

    private void postorderHelper(Node node, List<Integer> result) {
        // TODO: recurse left, recurse right, add node.data
    }

    // ================================================================
    //  WAVE 4 — ADVANCED
    //  Goal: Modify the tree structure and analyze balance.
    // ================================================================

    /**
     * [Wave 4 – Task 1] delete
     * ------------------------
     * Remove {@code value} from the BST while maintaining BST order.
     * If value is not found, do nothing.
     *
     * Three cases when deleting a node N:
     *   Case 1: N is a leaf (no children)
     *             → simply remove it (return null)
     *   Case 2: N has ONE child
     *             → replace N with that child
     *   Case 3: N has TWO children
     *             → replace N's data with its INORDER SUCCESSOR
     *               (the smallest value in N's right subtree),
     *               then delete that successor from the right subtree
     *
     * @param value value to delete
     */
    public void delete(int value) {
        root = deleteHelper(root, value);
    }

    private Node deleteHelper(Node node, int value) {
        if (node == null) return null;

        if (value < node.data) {
            // TODO: recurse left — node.left = deleteHelper(node.left, value)
        } else if (value > node.data) {
            // TODO: recurse right — node.right = deleteHelper(node.right, value)
        } else {
            // Found the node — handle all three cases below:
            // TODO Case 1: if node.left == null, return node.right
            // TODO Case 2: if node.right == null, return node.left
            // TODO Case 3: find inorder successor (min of right subtree),
            //              copy its data into node, delete that successor
        }
        return node;
    }

    /**
     * [Wave 4 – Task 2] countNodes
     * ----------------------------
     * Return the total number of nodes in the BST.
     *
     * Recursive formula:
     *   count(null) = 0
     *   count(node) = 1 + count(node.left) + count(node.right)
     *
     * @return total node count
     */
    public int countNodes() {
        // TODO: implement using recursion
        return 0; // placeholder
    }

    /**
     * [Wave 4 – Task 3] isBalanced
     * ----------------------------
     * Return true if the BST is HEIGHT-BALANCED — for every node, the
     * heights of its left and right subtrees differ by AT MOST 1.
     *
     * Efficient approach — write a helper that returns:
     *   - the actual height of the subtree if it is balanced
     *   - a sentinel value of -2 if any subtree is NOT balanced
     *
     * In isBalanced(), return true only if the helper does not return -2.
     *
     * @return true if the entire tree is balanced
     */
    public boolean isBalanced() {
        // TODO: call a helper that returns height or -2 (unbalanced sentinel)
        return false; // placeholder
    }

    // ----------------------------------------------------------
    //  Package-private accessor for tests
    // ----------------------------------------------------------
    Node getRoot() { return root; }
}
