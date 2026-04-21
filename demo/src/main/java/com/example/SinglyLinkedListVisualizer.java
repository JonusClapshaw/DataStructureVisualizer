package com.example;

import javafx.animation.*;
import javafx.geometry.Pos;
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
 * SinglyLinkedListVisualizer
 *
 * Displays nodes horizontally with right-pointing arrows.
 * Supported operations:
 *   - Add First   : new node slides in from the left
 *   - Add Last    : new node slides in from the right
 *   - Remove First: head node fades out, list shifts left
 *   - Search      : nodes highlight one-by-one until found
 *   - Reverse     : nodes swap positions with animation
 *   - Random      : populates 5 random values
 */
public class SinglyLinkedListVisualizer extends BaseVisualizer {

    private final List<Integer> data = new ArrayList<>();
    private final TextField inputField = new TextField();
    private final Random rng = new Random();
    private SinglyLinkedList studentList = new SinglyLinkedList();

    private static final double START_X   = 60;
    private static final double NODE_Y    = 0; // relative to canvas centre
    private static final double H_GAP     = 90;

    public SinglyLinkedListVisualizer() {
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
        Button removeFst = actionButton("Remove First",Color.web("#f38ba8"));
        Button search    = actionButton("Search",      Color.web("#f9e2af"));
        Button reverse   = actionButton("Reverse",     Color.web("#cba6f7"));
        Button randomBtn = actionButton("Random",      Color.web("#a6e3a1"));

        addFirst .setOnAction(e -> animateAddFirst());
        addLast  .setOnAction(e -> animateAddLast());
        removeFst.setOnAction(e -> animateRemoveFirst());
        search   .setOnAction(e -> animateSearch());
        reverse  .setOnAction(e -> animateReverse());
        randomBtn.setOnAction(e -> { randomize(); redraw(); setStatus("Randomized list."); });

        return new HBox(8, styledLabel("Value:"), inputField,
                        addFirst, addLast, removeFst, search, reverse, randomBtn);
    }

    // ── Drawing ────────────────────────────────────────────────────────────

    private void redraw() {
        canvas.getChildren().clear();
        double cy = canvas.getHeight() / 2;
        if (cy == 0) cy = 200;

        for (int i = 0; i < data.size(); i++) {
            double cx = START_X + i * H_GAP;
            StackPane node = makeNode(data.get(i), NODE_FILL);
            node.setLayoutX(cx - NODE_RADIUS);
            node.setLayoutY(cy - NODE_RADIUS);
            canvas.getChildren().add(node);

            if (i < data.size() - 1) {
                double ax1 = cx + NODE_RADIUS;
                double ax2 = START_X + (i + 1) * H_GAP - NODE_RADIUS;
                double ay  = cy;
                Line arrow = makeArrow(ax1, ay, ax2, ay);
                double angle = 0; // pointing right
                Polygon head = arrowHead(ax2, ay, angle);
                canvas.getChildren().addAll(arrow, head);
            }

            // NULL pointer at tail
            if (i == data.size() - 1) {
                Label nil = new Label("null");
                nil.setTextFill(Color.web("#6c7086"));
                nil.setFont(Font.font("Monospaced", 12));
                nil.setLayoutX(cx + NODE_RADIUS + 8);
                nil.setLayoutY(cy - 8);
                canvas.getChildren().add(nil);
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

    // ── Animations ─────────────────────────────────────────────────────────

    private void animateAddFirst() {
        int val = parseInput();
        if (val == Integer.MIN_VALUE) return;
        if (!safeCall("addFirst", () -> studentList.addFirst(val))) return;
        syncFromStudent();
        redraw();

        StackPane first = getNodeAt(0);
        if (first == null) return;
        first.setTranslateX(-120);
        TranslateTransition slide = new TranslateTransition(Duration.millis(stepMs()), first);
        slide.setToX(0);
        setStatus("addFirst(" + val + ")  →  new node slides to head");
        slide.play();
    }

    private void animateAddLast() {
        int val = parseInput();
        if (val == Integer.MIN_VALUE) return;
        if (!safeCall("addLast", () -> studentList.addLast(val))) return;
        syncFromStudent();
        redraw();

        StackPane last = getNodeAt(data.size() - 1);
        if (last == null) return;
        last.setTranslateX(120);
        TranslateTransition slide = new TranslateTransition(Duration.millis(stepMs()), last);
        slide.setToX(0);
        setStatus("addLast(" + val + ")  →  new node appended at tail");
        slide.play();
    }

    private void animateRemoveFirst() {
        if (data.isEmpty()) { setStatus("List is empty — nothing to remove."); return; }
        int[] removedHolder = { Integer.MIN_VALUE };
        StackPane oldHead = getNodeAt(0);
        if (!safeCall("removeFirst", () -> removedHolder[0] = studentList.removeFirst())) return;
        syncFromStudent();

        if (oldHead == null) { redraw(); return; }
        FadeTransition fade = new FadeTransition(Duration.millis(stepMs() * 0.6), oldHead);
        fade.setToValue(0);
        fade.setOnFinished(ev -> {
            redraw();
            setStatus("removeFirst()  →  removed " + removedHolder[0]);
        });
        setStatus("removeFirst()  →  removing head …");
        fade.play();
    }

    private void animateSearch() {
        int val = parseInput();
        if (val == Integer.MIN_VALUE) return;
        if (data.isEmpty()) { setStatus("List is empty."); return; }
        // Try calling student's contains() for accuracy; visual walk continues either way
        boolean[] studentResult = { false };
        try { studentResult[0] = studentList.contains(val); }
        catch (Exception ignored) { /* not implemented yet — visual still shows the walk */ }

        redraw();
        SequentialTransition seq = new SequentialTransition();
        boolean[] found = { false };

        for (int i = 0; i < data.size(); i++) {
            final int idx = i;
            final int v   = data.get(i);
            PauseTransition step = pause();
            step.setOnFinished(ev -> {
                redraw();
                StackPane node = getNodeAt(idx);
                if (node == null) return;
                Circle c = (Circle) node.getChildren().get(0);
                if (v == val) {
                    c.setFill(HIGHLIGHT_FILL);
                    setStatus("Search(" + val + ")  →  FOUND at index " + idx + " ✓");
                    found[0] = true;
                } else {
                    c.setFill(COMPARE_FILL);
                    setStatus("Search(" + val + ")  →  checking index " + idx + " … not a match");
                }
            });
            seq.getChildren().add(step);
        }
        seq.setOnFinished(ev -> {
            if (!found[0]) setStatus("Search(" + val + ")  →  value not found in list");
        });
        seq.play();
    }

    private void animateReverse() {
        if (data.size() < 2) { setStatus("Need at least 2 nodes to reverse."); return; }
        if (!safeCall("reverse", studentList::reverse)) return;
        syncFromStudent();

        setStatus("reverse()  →  reversing list …");
        int n = data.size();
        SequentialTransition seq = new SequentialTransition();

        for (int i = 0; i < n / 2; i++) {
            final int lo = i, hi = n - 1 - i;
            PauseTransition step = pause();
            step.setOnFinished(ev -> {
                redraw();
                StackPane a = getNodeAt(lo), b = getNodeAt(hi);
                if (a != null) ((Circle)a.getChildren().get(0)).setFill(HIGHLIGHT_FILL);
                if (b != null) ((Circle)b.getChildren().get(0)).setFill(HIGHLIGHT_FILL);
            });
            seq.getChildren().add(step);
        }
        seq.setOnFinished(ev -> setStatus("reverse()  →  done ✓"));
        seq.play();
    }

    // ── Utilities ──────────────────────────────────────────────────────────

    private void randomize() {
        studentList = new SinglyLinkedList();
        data.clear();
        int count = 4 + rng.nextInt(4);
        int[] vals = new int[count];
        for (int i = 0; i < count; i++) vals[i] = 1 + rng.nextInt(99);
        // Populate shadow list always; also try student's addLast
        for (int v : vals) {
            data.add(v);
            try { studentList.addLast(v); } catch (Exception ignored) {}
        }
        // If student's addLast worked, override shadow with actual state
        if (studentList.getHead() != null) syncFromStudent();
    }

    // ── Student wiring helpers ──────────────────────────────────────────────

    /** Runs op, shows warning on any exception. Returns true on success. */
    private boolean safeCall(String opName, Runnable op) {
        try { op.run(); return true; }
        catch (Exception e) {
            setStatus("⚠  " + opName + "() not implemented yet — fill in the TODO!");
            return false;
        }
    }

    /** Rebuilds the shadow {@code data} list from the student's actual linked list. */
    private void syncFromStudent() {
        data.clear();
        try {
            SinglyLinkedList.Node cur = studentList.getHead();
            while (cur != null) { data.add(cur.data); cur = cur.next; }
        } catch (Exception ignored) {}
    }

    private int parseInput() {
        try {
            return Integer.parseInt(inputField.getText().trim());
        } catch (NumberFormatException e) {
            setStatus("Please enter a valid integer.");
            return Integer.MIN_VALUE;
        }
    }

    private double canvasCY() {
        double h = canvas.getHeight();
        return h > 0 ? h / 2 : 200;
    }

    /** Returns the StackPane node at position idx in the canvas child list. */
    private StackPane getNodeAt(int idx) {
        // Nodes are added first; arrows follow — node at data index idx is child idx * (2 or 1)
        // We rebuild via layout position search — simpler: store references
        int nodeCount = 0;
        for (var child : canvas.getChildren()) {
            if (child instanceof StackPane) {
                if (nodeCount == idx) return (StackPane) child;
                nodeCount++;
            }
        }
        return null;
    }
}
