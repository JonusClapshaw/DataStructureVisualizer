package com.example;

import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.util.Duration;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * QueueVisualizer
 *
 * Renders the queue horizontally. FRONT is on the left, BACK is on the right.
 * Labels show FRONT → and ← BACK at each end.
 * Operations: Enqueue (enters from right), Dequeue (exits from left), Peek, Random.
 */
public class QueueVisualizer extends BaseVisualizer {

    private final List<Integer> queue = new ArrayList<>(); // index 0 = front
    private final TextField inputField = new TextField();
    private final Random rng = new Random();
    private MyQueue studentQueue = new MyQueue();

    private static final double START_X = 80;
    private static final double H_GAP   = 84;

    public QueueVisualizer() {
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

        Button enqueue   = actionButton("Enqueue", Color.web("#89b4fa"));
        Button dequeue   = actionButton("Dequeue", Color.web("#f38ba8"));
        Button peek      = actionButton("Peek",    Color.web("#f9e2af"));
        Button randomBtn = actionButton("Random",  Color.web("#a6e3a1"));

        enqueue .setOnAction(e -> animateEnqueue());
        dequeue .setOnAction(e -> animateDequeue());
        peek    .setOnAction(e -> animatePeek());
        randomBtn.setOnAction(e -> { randomize(); redraw(); setStatus("Randomized."); });

        return new HBox(8, styledLabel("Value:"), inputField, enqueue, dequeue, peek, randomBtn);
    }

    private void redraw() {
        canvas.getChildren().clear();
        double cy = canvasCY();

        // Draw queue tunnel lines
        if (!queue.isEmpty()) {
            double x1 = START_X - NODE_RADIUS - 4;
            double x2 = START_X + (queue.size() - 1) * H_GAP + NODE_RADIUS + 4;
            Line top    = new Line(x1, cy - NODE_RADIUS - 4, x2, cy - NODE_RADIUS - 4);
            Line bottom = new Line(x1, cy + NODE_RADIUS + 4, x2, cy + NODE_RADIUS + 4);
            top   .setStroke(Color.web("#45475a")); top   .setStrokeWidth(2);
            bottom.setStroke(Color.web("#45475a")); bottom.setStrokeWidth(2);
            canvas.getChildren().addAll(top, bottom);
        }

        for (int i = 0; i < queue.size(); i++) {
            double cx = START_X + i * H_GAP;
            Color fill = NODE_FILL;
            if (i == 0)                  fill = Color.web("#a6e3a1"); // front
            if (i == queue.size() - 1)   fill = Color.web("#cba6f7"); // back
            StackPane node = makeNode(queue.get(i), fill);
            node.setLayoutX(cx - NODE_RADIUS);
            node.setLayoutY(cy - NODE_RADIUS);
            canvas.getChildren().add(node);
        }

        // FRONT / BACK labels
        if (!queue.isEmpty()) {
            double cy2 = cy;
            Label front = new Label("FRONT →");
            front.setTextFill(Color.web("#a6e3a1"));
            front.setFont(Font.font("Monospaced", FontWeight.BOLD, 11));
            front.setLayoutX(START_X - NODE_RADIUS - 80);
            front.setLayoutY(cy2 - 8);

            double backX = START_X + (queue.size() - 1) * H_GAP;
            Label back = new Label("← BACK");
            back.setTextFill(Color.web("#cba6f7"));
            back.setFont(Font.font("Monospaced", FontWeight.BOLD, 11));
            back.setLayoutX(backX + NODE_RADIUS + 8);
            back.setLayoutY(cy2 - 8);
            canvas.getChildren().addAll(front, back);
        }

        if (queue.isEmpty()) {
            Label empty = new Label("[ queue empty ]");
            empty.setTextFill(Color.web("#6c7086"));
            empty.setFont(Font.font("System", 14));
            empty.setLayoutX(40);
            empty.setLayoutY(cy - 10);
            canvas.getChildren().add(empty);
        }
    }

    private void animateEnqueue() {
        int val = parseInput(); if (val == Integer.MIN_VALUE) return;
        if (!safeCall("enqueue", () -> studentQueue.enqueue(val))) return;
        syncFromStudent();
        redraw();
        StackPane back = getNodeAt(queue.size() - 1);
        if (back == null) return;
        back.setTranslateX(120);
        back.setOpacity(0);
        TranslateTransition tt = new TranslateTransition(Duration.millis(stepMs()), back);
        tt.setToX(0);
        FadeTransition ft = new FadeTransition(Duration.millis(stepMs()), back);
        ft.setToValue(1);
        new ParallelTransition(tt, ft).play();
        setStatus("enqueue(" + val + ")  →  added to back of queue");
    }

    private void animateDequeue() {
        if (queue.isEmpty()) { setStatus("Queue is empty — cannot dequeue."); return; }
        int val = queue.get(0);
        StackPane front = getNodeAt(0);
        if (!safeCall("dequeue", studentQueue::dequeue)) return;
        syncFromStudent();
        if (front == null) { redraw(); return; }
        TranslateTransition tt = new TranslateTransition(Duration.millis(stepMs() * 0.7), front);
        tt.setToX(-120);
        FadeTransition ft = new FadeTransition(Duration.millis(stepMs() * 0.5), front);
        ft.setToValue(0);
        ParallelTransition pt = new ParallelTransition(tt, ft);
        pt.setOnFinished(ev -> { redraw(); setStatus("dequeue()  →  removed front " + val); });
        setStatus("dequeue()  →  removing " + val + " from front …");
        pt.play();
    }

    private void animatePeek() {
        if (queue.isEmpty()) { setStatus("Queue is empty."); return; }
        int[] valHolder = { queue.get(0) };
        try { valHolder[0] = studentQueue.peek(); } catch (Exception ignored) {}
        redraw();
        StackPane front = getNodeAt(0);
        if (front != null) flashNode(front, Color.web("#f9e2af"), Color.web("#a6e3a1")).play();
        setStatus("peek()  →  front is " + valHolder[0] + " (no removal)");
    }

    private void randomize() {
        studentQueue = new MyQueue();
        queue.clear();
        int n = 3 + rng.nextInt(5);
        int[] vals = new int[n];
        for (int i = 0; i < n; i++) vals[i] = 1 + rng.nextInt(99);
        for (int v : vals) {
            queue.add(v);
            try { studentQueue.enqueue(v); } catch (Exception ignored) {}
        }
        int[] snap = studentQueue.snapshotForVisualizer();
        if (snap.length > 0) {
            queue.clear();
            for (int v : snap) queue.add(v);
        }
    }

    private boolean safeCall(String opName, Runnable op) {
        try { op.run(); return true; }
        catch (Exception e) {
            setStatus("⚠  " + opName + "() not implemented yet — fill in the TODO!");
            return false;
        }
    }

    private void syncFromStudent() {
        int[] snap = studentQueue.snapshotForVisualizer();
        queue.clear();
        for (int v : snap) queue.add(v);
    }

    private double canvasCY() { double h = canvas.getHeight(); return h > 0 ? h / 2 : 200; }

    private StackPane getNodeAt(int idx) {
        int count = 0;
        for (var child : canvas.getChildren()) {
            if (child instanceof StackPane) { if (count == idx) return (StackPane) child; count++; }
        }
        return null;
    }

    private int parseInput() {
        try { return Integer.parseInt(inputField.getText().trim()); }
        catch (NumberFormatException e) { setStatus("Enter a valid integer."); return Integer.MIN_VALUE; }
    }
}
