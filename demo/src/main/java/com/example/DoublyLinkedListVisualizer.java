package com.example;

import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * DoublyLinkedListVisualizer
 *
 * Shows nodes in a horizontal row with bidirectional arrows (←→).
 * Operations: Add First, Add Last, Remove First, Remove Last, Search, Random.
 */
public class DoublyLinkedListVisualizer extends BaseVisualizer {

    private final List<Integer> data = new ArrayList<>();
    private final TextField inputField = new TextField();
    private final Random rng = new Random();
    private DoublyLinkedList studentList = new DoublyLinkedList();

    private static final double START_X = 60;
    private static final double H_GAP   = 100;

    public DoublyLinkedListVisualizer() {
        super();
        completeSetup();
        randomize();
        redraw();
    }

    @Override
    protected HBox buildControls() {
        inputField.setPromptText("value");
        inputField.setPrefWidth(70);
        inputField.setStyle("-fx-background-color: #313244; -fx-text-fill: #cdd6f4; -fx-background-radius: 6;");

        Button addFirst  = actionButton("Add First",   Color.web("#89b4fa"));
        Button addLast   = actionButton("Add Last",    Color.web("#89dceb"));
        Button remFirst  = actionButton("Rem First",   Color.web("#f38ba8"));
        Button remLast   = actionButton("Rem Last",    Color.web("#fab387"));
        Button search    = actionButton("Search",      Color.web("#f9e2af"));
        Button randomBtn = actionButton("Random",      Color.web("#a6e3a1"));

        addFirst .setOnAction(e -> animateAddFirst());
        addLast  .setOnAction(e -> animateAddLast());
        remFirst .setOnAction(e -> animateRemoveFirst());
        remLast  .setOnAction(e -> animateRemoveLast());
        search   .setOnAction(e -> animateSearch());
        randomBtn.setOnAction(e -> { randomize(); redraw(); setStatus("Randomized."); });

        return new HBox(8, styledLabel("Value:"), inputField,
                        addFirst, addLast, remFirst, remLast, search, randomBtn);
    }

    private void redraw() {
        canvas.getChildren().clear();
        double cy = canvasCY();

        for (int i = 0; i < data.size(); i++) {
            double cx = START_X + i * H_GAP;
            StackPane node = makeNode(data.get(i), NODE_FILL);
            node.setLayoutX(cx - NODE_RADIUS);
            node.setLayoutY(cy - NODE_RADIUS);
            canvas.getChildren().add(node);

            if (i < data.size() - 1) {
                double x1 = cx + NODE_RADIUS + 4;
                double x2 = START_X + (i + 1) * H_GAP - NODE_RADIUS - 4;
                // forward arrow (top lane)
                Line fwd = makeArrow(x1, cy - 6, x2, cy - 6);
                Polygon fwdHead = arrowHead(x2, cy - 6, 0);
                // back arrow (bottom lane)
                Line bck = makeArrow(x2, cy + 6, x1, cy + 6);
                Polygon bckHead = arrowHead(x1, cy + 6, Math.PI);
                canvas.getChildren().addAll(fwd, fwdHead, bck, bckHead);
            }
        }

        if (data.isEmpty()) {
            Label empty = new Label("[ empty list ]");
            empty.setTextFill(Color.web("#6c7086"));
            empty.setFont(Font.font("System", 14));
            empty.setLayoutX(40);
            empty.setLayoutY(cy - 10);
            canvas.getChildren().add(empty);
        }
    }

    private void animateAddFirst() {
        int val = parseInput(); if (val == Integer.MIN_VALUE) return;
        if (!safeCall("addFirst", () -> studentList.addFirst(val))) return;
        syncFromStudent();
        redraw();
        StackPane node = getNodeAt(0);
        if (node == null) return;
        node.setTranslateX(-140);
        TranslateTransition t = new TranslateTransition(Duration.millis(stepMs()), node);
        t.setToX(0);
        setStatus("addFirst(" + val + ")  →  inserted at head");
        t.play();
    }

    private void animateAddLast() {
        int val = parseInput(); if (val == Integer.MIN_VALUE) return;
        if (!safeCall("addLast", () -> studentList.addLast(val))) return;
        syncFromStudent();
        redraw();
        StackPane node = getNodeAt(data.size() - 1);
        if (node == null) return;
        node.setTranslateX(140);
        TranslateTransition t = new TranslateTransition(Duration.millis(stepMs()), node);
        t.setToX(0);
        setStatus("addLast(" + val + ")  →  appended at tail");
        t.play();
    }

    private void animateRemoveFirst() {
        if (data.isEmpty()) { setStatus("List is empty."); return; }
        int removed = data.get(0);
        StackPane node = getNodeAt(0);
        if (!safeCall("removeFirst", studentList::removeFirst)) return;
        syncFromStudent();
        if (node == null) { redraw(); return; }
        FadeTransition ft = new FadeTransition(Duration.millis(stepMs() * 0.6), node);
        ft.setToValue(0);
        ft.setOnFinished(ev -> { redraw(); setStatus("removeFirst()  →  removed " + removed); });
        setStatus("removeFirst()  →  removing " + removed + " …");
        ft.play();
    }

    private void animateRemoveLast() {
        if (data.isEmpty()) { setStatus("List is empty."); return; }
        int lastIdx = data.size() - 1;
        int removed = data.get(lastIdx);
        StackPane node = getNodeAt(lastIdx);
        if (!safeCall("removeLast", studentList::removeLast)) return;
        syncFromStudent();
        if (node == null) { redraw(); return; }
        FadeTransition ft = new FadeTransition(Duration.millis(stepMs() * 0.6), node);
        ft.setToValue(0);
        ft.setOnFinished(ev -> { redraw(); setStatus("removeLast()  →  removed " + removed); });
        setStatus("removeLast()  →  removing " + removed + " …");
        ft.play();
    }

    private void animateSearch() {
        int val = parseInput(); if (val == Integer.MIN_VALUE) return;
        try { studentList.contains(val); } catch (Exception ignored) {}
        redraw();
        SequentialTransition seq = new SequentialTransition();
        boolean[] found = { false };
        for (int i = 0; i < data.size(); i++) {
            final int idx = i, v = data.get(i);
            PauseTransition step = pause();
            step.setOnFinished(ev -> {
                redraw();
                StackPane n = getNodeAt(idx);
                if (n == null) return;
                Circle c = (Circle) n.getChildren().get(0);
                if (v == val) { c.setFill(HIGHLIGHT_FILL); setStatus("Found " + val + " at index " + idx + " ✓"); found[0] = true; }
                else          { c.setFill(COMPARE_FILL);  setStatus("Checking index " + idx + " … not " + val); }
            });
            seq.getChildren().add(step);
        }
        seq.setOnFinished(ev -> { if (!found[0]) setStatus(val + " not found in list"); });
        seq.play();
    }

    private void randomize() {
        studentList = new DoublyLinkedList();
        data.clear();
        int n = 3 + rng.nextInt(4);
        int[] vals = new int[n];
        for (int i = 0; i < n; i++) vals[i] = 1 + rng.nextInt(99);
        for (int v : vals) {
            data.add(v);
            try { studentList.addLast(v); } catch (Exception ignored) {}
        }
        if (studentList.getHead() != null) syncFromStudent();
    }

    private boolean safeCall(String opName, Runnable op) {
        try { op.run(); return true; }
        catch (Exception e) {
            setStatus("⚠  " + opName + "() not implemented yet — fill in the TODO!");
            return false;
        }
    }

    private void syncFromStudent() {
        data.clear();
        try {
            DoublyLinkedList.Node cur = studentList.getHead();
            while (cur != null) { data.add(cur.data); cur = cur.next; }
        } catch (Exception ignored) {}
    }

    private int parseInput() {
        try { return Integer.parseInt(inputField.getText().trim()); }
        catch (NumberFormatException e) { setStatus("Enter a valid integer."); return Integer.MIN_VALUE; }
    }

    private double canvasCY() { double h = canvas.getHeight(); return h > 0 ? h / 2 : 200; }

    private StackPane getNodeAt(int idx) {
        int count = 0;
        for (var child : canvas.getChildren()) {
            if (child instanceof StackPane) { if (count == idx) return (StackPane) child; count++; }
        }
        return null;
    }
}
