package com.example;

/**
 * ================================================================
 *  DATA STRUCTURE: Stack (Array-Based)
 * ================================================================
 *
 *  A Stack is a Last-In, First-Out (LIFO) data structure.
 *  Think of a stack of plates — you always add and remove from the TOP.
 *
 *  This implementation uses a fixed-size array internally.
 *
 *  Visual:
 *    index → [0][1][2][3] ...
 *    top   =  2  (points to the most recently pushed element)
 *
 *    TOP → [C]   ← push here / pop from here
 *          [B]
 *          [A]   ← bottom (index 0)
 *
 *  Core operations (all O(1)):
 *    push  — add an element on top
 *    pop   — remove and return the top element
 *    peek  — view the top element without removing
 *
 * ================================================================
 *  HOW THE WAVES WORK
 * ================================================================
 *  Complete each TODO then run:  mvn test -Dtest=MyStackTest
 *
 *    Wave 1 – Intro       : push, peek, isEmpty
 *    Wave 2 – Basic       : pop, size, isFull
 *    Wave 3 – Intermediate: contains, toArray, clear
 *    Wave 4 – Advanced    : reverseString, evaluatePostfix
 * ================================================================
 */
public class MyStack {

    private static final int DEFAULT_CAPACITY = 16;
    private int[] data;   // internal storage array
    private int top;      // index of the current top element (-1 when empty)

    /** Creates a stack with default capacity 16. */
    public MyStack() {
        this.data = new int[DEFAULT_CAPACITY];
        this.top = -1;
    }

    /** Creates a stack with a custom maximum capacity. */
    public MyStack(int capacity) {
        this.data = new int[capacity];
        this.top = -1;
    }

    // ================================================================
    //  WAVE 1 — INTRO
    //  Goal: Add, view, and check emptiness.
    // ================================================================

    /**
     * [Wave 1 – Task 1] push
     * ----------------------
     * Push {@code value} onto the top of the stack.
     * Throws {@link IllegalStateException} if the stack is already full.
     *
     * Steps:
     *   1. If top == data.length - 1 → throw IllegalStateException("Stack is full")
     *   2. Increment top
     *   3. Set data[top] = value
     *
     * @param value the value to push
     */
    public void push(int value) {
        if (top == data.length - 1) {
            throw new IllegalStateException("Stack is full");
        }
        // TODO: increment top and store value at data[top]
    }

    /**
     * [Wave 1 – Task 2] peek
     * ----------------------
     * Return the value at the TOP without removing it.
     * Throws {@link java.util.NoSuchElementException} if the stack is empty.
     *
     * Hint: the top element is stored at data[top].
     *
     * @return the top value
     */
    public int peek() {
        if (top == -1) {
            throw new java.util.NoSuchElementException("Stack is empty");
        }
        // TODO: return the element at the top index
        return -1; // placeholder
    }

    /**
     * [Wave 1 – Task 3] isEmpty
     * -------------------------
     * Return true if the stack has no elements.
     *
     * Hint: the stack is empty when top == -1.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        // TODO: check whether top indicates an empty stack
        return false; // placeholder
    }

    // ================================================================
    //  WAVE 2 — BASIC
    //  Goal: Remove from top, report count, check capacity.
    // ================================================================

    /**
     * [Wave 2 – Task 1] pop
     * ---------------------
     * Remove and return the top value.
     * Throws {@link java.util.NoSuchElementException} if the stack is empty.
     *
     * Steps:
     *   1. If top == -1 → throw NoSuchElementException("Stack is empty")
     *   2. Save data[top]
     *   3. Decrement top
     *   4. Return the saved value
     *
     * @return the removed top value
     */
    public int pop() {
        if (top == -1) {
            throw new java.util.NoSuchElementException("Stack is empty");
        }
        // TODO: retrieve top value, decrement top, and return the value
        return -1; // placeholder
    }

    /**
     * [Wave 2 – Task 2] size
     * ----------------------
     * Return the number of elements currently in the stack.
     *
     * Hint: since top is 0-indexed and starts at -1, size = top + 1.
     *
     * @return current element count
     */
    public int size() {
        // TODO: derive the size from the top index
        return 0; // placeholder
    }

    /**
     * [Wave 2 – Task 3] isFull
     * ------------------------
     * Return true if the stack has reached its maximum capacity and
     * cannot accept another push.
     *
     * Hint: isFull when top == data.length - 1.
     *
     * @return true if full
     */
    public boolean isFull() {
        // TODO: compare top to the last valid index
        return false; // placeholder
    }

    // ================================================================
    //  WAVE 3 — INTERMEDIATE
    //  Goal: Search, snapshot, and reset.
    // ================================================================

    /**
     * [Wave 3 – Task 1] contains
     * --------------------------
     * Return true if {@code value} exists anywhere in the stack.
     * Search from the bottom (index 0) to the top (index top).
     *
     * @param value the value to search for
     * @return true if found
     */
    public boolean contains(int value) {
        // TODO: loop from 0 to top (inclusive); return true if data[i] == value
        return false; // placeholder
    }

    /**
     * [Wave 3 – Task 2] toArray
     * -------------------------
     * Return a new int[] containing all stack elements from bottom to top.
     *
     * Example: stack (bottom→top) = [1, 2, 3]
     *          toArray()           = {1, 2, 3}
     *
     * @return a copy of the stack contents in bottom-to-top order
     */
    public int[] toArray() {
        // TODO: create an array of size (top + 1), copy data[0..top] into it
        return new int[0]; // placeholder
    }

    /**
     * [Wave 3 – Task 3] clear
     * -----------------------
     * Remove all elements from the stack by resetting the top index.
     *
     * You do NOT need to zero out the array — resetting top to -1 is sufficient.
     */
    public void clear() {
        // TODO: set top = -1
    }

    // ================================================================
    //  WAVE 4 — ADVANCED
    //  Goal: Apply a stack to solve real algorithmic problems.
    // ================================================================

    /**
     * [Wave 4 – Task 1] reverseString
     * ---------------------------------
     * Use a stack to reverse the characters of {@code input} and return
     * the reversed String.
     *
     * Algorithm:
     *   1. Create a local MyStack sized to input.length()
     *   2. Push the ASCII value of each character onto the stack
     *   3. Pop each value and cast back to char to build the reversed result
     *
     * Example: "hello" → "olleh"
     *
     * @param input the String to reverse
     * @return the reversed String
     */
    public String reverseString(String input) {
        // TODO: push each char's ASCII int, then pop to build reversed string
        return ""; // placeholder
    }

    /**
     * [Wave 4 – Task 2] evaluatePostfix
     * -----------------------------------
     * Evaluate a postfix (Reverse Polish Notation) expression.
     * Return the integer result.
     *
     * The input is a String of space-separated tokens.
     * Each token is either an integer OR one of: +  -  *  /
     *
     * Algorithm — for each token:
     *   - If it's a number  → push it
     *   - If it's an operator → pop b (top), pop a, compute (a OP b), push result
     * After processing all tokens, the stack holds exactly one value — the answer.
     *
     * Example:
     *   "3 4 +"      → 3 + 4 = 7
     *   "3 4 + 2 *"  → (3 + 4) * 2 = 14
     *   "5 1 2 + 4 * + 3 -"  → 14
     *
     * @param expression the postfix expression string
     * @return the computed integer result
     */
    public int evaluatePostfix(String expression) {
        MyStack stack = new MyStack(expression.length());
        String[] tokens = expression.split(" ");
        // TODO: process each token:
        //         - if numeric (try Integer.parseInt), push it
        //         - if operator, pop b then a, apply operator, push result
        return 0; // placeholder
    }

    // ----------------------------------------------------------------
    //  Package-private: used by StackVisualizer only
    // ----------------------------------------------------------------
    /** Returns a copy of elements from bottom (index 0) to top, without going through TODOs. */
    int[] snapshotForVisualizer() {
        if (top < 0) return new int[0];
        int[] snapshot = new int[top + 1];
        System.arraycopy(data, 0, snapshot, 0, top + 1);
        return snapshot;
    }
}
