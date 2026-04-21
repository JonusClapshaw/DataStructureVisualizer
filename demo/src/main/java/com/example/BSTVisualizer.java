package com.example;

import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.util.Duration;

import java.util.*;

/**
 * BSTVisualizer
 *
 * Draws a binary search tree with parent→child edges.
 * Nodes are positioned using standard BST layout (recursive x-spread).
 * Operations: Insert, Search (BST comparison path highlighted), Find Min,
 *             Find Max, In-Order traversal (highlighted left→root→right), Random.
 */
public class BSTVisualizer extends BaseVisualizer {

    // Simple internal tree node (independent from the student's BinarySearchTree.java)
    private static class TNode {
        int val;
        TNode left, right;
        // layout coords
        double x, y;
        TNode(int v) { val = v; }
    }

    private TNode root = null;
    private final TextField inputField = new TextField();
    private final Random rng = new Random();
    private BinarySearchTree studentBST = new BinarySearchTree();

    private static final double H_SPREAD_ROOT = 200;
    private static final double V_GAP         = 70;
    private static final double CANVAS_CX      = 400;
    private static final double START_Y        = 50;

    public BSTVisualizer() {
        super();
        completeSetup();
        randomize();
        layout(root, CANVAS_CX, START_Y, H_SPREAD_ROOT);
        redraw();
    }

    @Override
    protected HBox buildControls() {
        inputField.setPromptText("value");
        inputField.setPrefWidth(70);
        inputField.setStyle("-fx-background-color: #313244; -fx-text-fill: #cdd6f4; -fx-background-radius: 6;");

        Button insert    = actionButton("Insert",   Color.web("#89b4fa"));
        Button search    = actionButton("Search",   Color.web("#f9e2af"));
        Button findMin   = actionButton("Find Min", Color.web("#89dceb"));
        Button findMax   = actionButton("Find Max", Color.web("#fab387"));
        Button inorder   = actionButton("In-Order", Color.web("#cba6f7"));
        Button randomBtn = actionButton("Random",   Color.web("#a6e3a1"));

        insert  .setOnAction(e -> animateInsert());
        search  .setOnAction(e -> animateSearch());
        findMin .setOnAction(e -> animateFindMin());
        findMax .setOnAction(e -> animateFindMax());
        inorder .setOnAction(e -> animateInorder());
        randomBtn.setOnAction(e -> { randomize(); layoutAndDraw(); setStatus("Tree randomized."); });

        return new HBox(8, styledLabel("Value:"), inputField, insert, search, findMin, findMax, inorder, randomBtn);
    }

    // ── Tree operations ───────────────────────────────────────────────────

    private TNode insert(TNode node, int val) {
        if (node == null) return new TNode(val);
        if (val < node.val) node.left  = insert(node.left,  val);
        else if (val > node.val) node.right = insert(node.right, val);
        return node;
    }

    private void layout(TNode node, double x, double y, double spread) {
        if (node == null) return;
        node.x = x; node.y = y;
        layout(node.left,  x - spread, y + V_GAP, spread / 2);
        layout(node.right, x + spread, y + V_GAP, spread / 2);
    }

    private List<TNode> inorderList(TNode node) {
        if (node == null) return new ArrayList<>();
        List<TNode> list = inorderList(node.left);
        list.add(node);
        list.addAll(inorderList(node.right));
        return list;
    }

    private TNode findMin(TNode node) {
        while (node != null && node.left != null) node = node.left;
        return node;
    }

    private TNode findMax(TNode node) {
        while (node != null && node.right != null) node = node.right;
        return node;
    }

    // ── Drawing ────────────────────────────────────────────────────────────

    // Maps TNode → StackPane for highlight access
    private final Map<TNode, StackPane> nodeViews = new HashMap<>();

    private void redraw() {
        canvas.getChildren().clear();
        nodeViews.clear();
        drawEdges(root);
        drawNodes(root);
    }

    private void drawEdges(TNode node) {
        if (node == null) return;
        if (node.left != null) {
            canvas.getChildren().add(makeArrow(node.x, node.y + NODE_RADIUS, node.left.x, node.left.y - NODE_RADIUS));
            drawEdges(node.left);
        }
        if (node.right != null) {
            canvas.getChildren().add(makeArrow(node.x, node.y + NODE_RADIUS, node.right.x, node.right.y - NODE_RADIUS));
            drawEdges(node.right);
        }
    }

    private void drawNodes(TNode node) {
        if (node == null) return;
        StackPane sp = makeNode(node.val, NODE_FILL);
        sp.setLayoutX(node.x - NODE_RADIUS);
        sp.setLayoutY(node.y - NODE_RADIUS);
        canvas.getChildren().add(sp);
        nodeViews.put(node, sp);
        drawNodes(node.left);
        drawNodes(node.right);
    }

    // ── Animations ─────────────────────────────────────────────────────────

    private void animateInsert() {
        int val = parseInput(); if (val == Integer.MIN_VALUE) return;
        if (!safeCall("insert", () -> studentBST.insert(val))) return;
        // Sync student tree; fall back to internal insert if student not impl'd
        syncFromStudent();
        layoutAndDraw();

        TNode found = findNode(root, val);
        if (found != null) {
            StackPane sp = nodeViews.get(found);
            if (sp != null) {
                sp.setScaleX(0.1); sp.setScaleY(0.1);
                ScaleTransition st = new ScaleTransition(Duration.millis(stepMs()), sp);
                st.setToX(1); st.setToY(1);
                st.play();
            }
        }
        setStatus("insert(" + val + ")  →  placed per BST property");
    }

    private void animateSearch() {
        int val = parseInput(); if (val == Integer.MIN_VALUE) return;
        // Call student's contains() — result is informational; visual path uses shadow tree
        try { studentBST.contains(val); } catch (Exception ignored) {}
        List<TNode> path = searchPath(root, val);
        redraw();

        SequentialTransition seq = new SequentialTransition();
        for (int i = 0; i < path.size(); i++) {
            final TNode n = path.get(i);
            final boolean isTarget = (n.val == val);
            final int step = i;
            PauseTransition p = pause();
            p.setOnFinished(ev -> {
                StackPane sp = nodeViews.get(n);
                if (sp != null) {
                    Circle c = (Circle) sp.getChildren().get(0);
                    c.setFill(isTarget ? HIGHLIGHT_FILL : COMPARE_FILL);
                }
                if (isTarget) setStatus("Search(" + val + ")  →  FOUND ✓");
                else {
                    String dir = (val < n.val) ? "go LEFT" : "go RIGHT";
                    setStatus("Search(" + val + ")  →  at " + n.val + " → " + dir);
                }
            });
            seq.getChildren().add(p);
        }
        seq.setOnFinished(ev -> { if (path.isEmpty() || path.get(path.size()-1).val != val) setStatus("Search(" + val + ")  →  not found"); });
        seq.play();
    }

    private void animateFindMin() {
        if (root == null) { setStatus("Tree is empty."); return; }
        try { int sMin = studentBST.findMin();
              setStatus("findMin()  →  student returned " + sMin); }
        catch (Exception ignored) {}
        TNode min = findMin(root);
        redraw();
        // Highlight path from root going left
        List<TNode> path = new ArrayList<>();
        TNode cur = root;
        while (cur != null) { path.add(cur); cur = cur.left; }
        SequentialTransition seq = new SequentialTransition();
        for (TNode n : path) {
            PauseTransition p = pause();
            final TNode fn = n;
            p.setOnFinished(ev -> {
                StackPane sp = nodeViews.get(fn);
                if (sp != null) {
                    Circle c = (Circle) sp.getChildren().get(0);
                    c.setFill(fn == min ? HIGHLIGHT_FILL : COMPARE_FILL);
                }
                setStatus("findMin()  →  keep going left … at " + fn.val);
            });
            seq.getChildren().add(p);
        }
        seq.setOnFinished(ev -> setStatus("findMin()  →  minimum is " + min.val + " ✓"));
        seq.play();
    }

    private void animateFindMax() {
        if (root == null) { setStatus("Tree is empty."); return; }
        try { int sMax = studentBST.findMax();
              setStatus("findMax()  →  student returned " + sMax); }
        catch (Exception ignored) {}
        TNode max = findMax(root);
        redraw();
        List<TNode> path = new ArrayList<>();
        TNode cur = root;
        while (cur != null) { path.add(cur); cur = cur.right; }
        SequentialTransition seq = new SequentialTransition();
        for (TNode n : path) {
            PauseTransition p = pause();
            final TNode fn = n;
            p.setOnFinished(ev -> {
                StackPane sp = nodeViews.get(fn);
                if (sp != null) {
                    Circle c = (Circle) sp.getChildren().get(0);
                    c.setFill(fn == max ? HIGHLIGHT_FILL : COMPARE_FILL);
                }
                setStatus("findMax()  →  keep going right … at " + fn.val);
            });
            seq.getChildren().add(p);
        }
        seq.setOnFinished(ev -> setStatus("findMax()  →  maximum is " + max.val + " ✓"));
        seq.play();
    }

    private void animateInorder() {
        if (root == null) { setStatus("Tree is empty."); return; }
        // Call student's inorder() for feedback
        try {
            List<Integer> sOrder = studentBST.inorder();
            setStatus("inorder() returned: " + sOrder);
        } catch (Exception ignored) {}
        List<TNode> order = inorderList(root);
        redraw();
        SequentialTransition seq = new SequentialTransition();
        StringBuilder sb = new StringBuilder("in-order: ");
        for (TNode n : order) {
            sb.append(n.val).append(" ");
            final String soFar = sb.toString();
            final TNode fn = n;
            PauseTransition p = pause();
            p.setOnFinished(ev -> {
                StackPane sp = nodeViews.get(fn);
                if (sp != null) ((Circle)sp.getChildren().get(0)).setFill(HIGHLIGHT_FILL);
                setStatus(soFar);
            });
            seq.getChildren().add(p);
        }
        seq.setOnFinished(ev -> setStatus("In-order traversal complete (sorted order ✓)"));
        seq.play();
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    private void randomize() {
        studentBST = new BinarySearchTree();
        root = null;
        int[] vals = { 40, 20, 60, 10, 30, 50, 70 };
        for (int v : vals) {
            int adjusted = v + rng.nextInt(5) - 2;
            root = insert(root, adjusted); // shadow tree always works
            try { studentBST.insert(adjusted); } catch (Exception ignored) {}
        }
        // If student's insert worked, override shadow with actual tree
        syncFromStudent();
    }

    /** Converts student BinarySearchTree.Node tree into internal TNode tree. */
    private TNode convertNode(BinarySearchTree.Node sn) {
        if (sn == null) return null;
        TNode t = new TNode(sn.data);
        t.left  = convertNode(sn.left);
        t.right = convertNode(sn.right);
        return t;
    }

    private void syncFromStudent() {
        try {
            TNode converted = convertNode(studentBST.getRoot());
            if (converted != null) root = converted;
        } catch (Exception ignored) {}
    }

    private boolean safeCall(String opName, Runnable op) {
        try { op.run(); return true; }
        catch (Exception e) {
            setStatus("⚠  " + opName + "() not implemented yet — fill in the TODO!");
            return false;
        }
    }

    private void layoutAndDraw() {
        layout(root, CANVAS_CX, START_Y, H_SPREAD_ROOT);
        redraw();
    }

    private TNode findNode(TNode node, int val) {
        if (node == null) return null;
        if (val == node.val) return node;
        return val < node.val ? findNode(node.left, val) : findNode(node.right, val);
    }

    private List<TNode> searchPath(TNode node, int val) {
        List<TNode> path = new ArrayList<>();
        TNode cur = node;
        while (cur != null) {
            path.add(cur);
            if (val == cur.val) break;
            cur = (val < cur.val) ? cur.left : cur.right;
        }
        return path;
    }

    private int parseInput() {
        try { return Integer.parseInt(inputField.getText().trim()); }
        catch (NumberFormatException e) { setStatus("Enter a valid integer."); return Integer.MIN_VALUE; }
    }
}
