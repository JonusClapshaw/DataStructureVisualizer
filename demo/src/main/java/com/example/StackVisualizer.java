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
 * StackVisualizer
 *
 * Renders the stack as a vertical column of nodes, with the TOP at the top.
 * A "TOP →" label points to the most recently pushed element.
 * Operations: Push, Pop, Peek, Random.
 */
public class StackVisualizer extends BaseVisualizer {

    private final List<Integer> stack = new ArrayList<>(); // index 0 = bottom
    private final TextField inputField = new TextField();
    private final Random rng = new Random();
    private MyStack studentStack = new MyStack();

    private static final double CENTER_X = 120;
    private static final double BOTTOM_Y_OFFSET = 60; // px from canvas bottom
    private static final double V_GAP = 64;

    public StackVisualizer() {
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

        Button push      = actionButton("Push",   Color.web("#89b4fa"));
        Button pop       = actionButton("Pop",    Color.web("#f38ba8"));
        Button peek      = actionButton("Peek",   Color.web("#f9e2af"));
        Button randomBtn = actionButton("Random", Color.web("#a6e3a1"));

        push    .setOnAction(e -> animatePush());
        pop     .setOnAction(e -> animatePop());
        peek    .setOnAction(e -> animatePeek());
        randomBtn.setOnAction(e -> { randomize(); redraw(); setStatus("Stack randomized."); });

        return new HBox(8, styledLabel("Value:"), inputField, push, pop, peek, randomBtn);
    }

    private void redraw() {
        canvas.getChildren().clear();
        double canvasH = canvas.getHeight() > 0 ? canvas.getHeight() : 400;
        double baseY = canvasH - BOTTOM_Y_OFFSET;

        // Draw stack frame lines
        double frameL = CENTER_X - NODE_RADIUS - 4;
        double frameR = CENTER_X + NODE_RADIUS + 4;
        double frameTop = baseY - stack.size() * V_GAP - 8;
        Line leftWall  = new Line(frameL, baseY, frameL, frameTop);
        Line rightWall = new Line(frameR, baseY, frameR, frameTop);
        Line base      = new Line(frameL, baseY, frameR, baseY);
        leftWall.setStroke(Color.web("#45475a")); leftWall.setStrokeWidth(2);
        rightWall.setStroke(Color.web("#45475a")); rightWall.setStrokeWidth(2);
        base.setStroke(Color.web("#45475a")); base.setStrokeWidth(2);
        canvas.getChildren().addAll(leftWall, rightWall, base);

        for (int i = 0; i < stack.size(); i++) {
            double cy = baseY - NODE_RADIUS - i * V_GAP - 8;
            Color fill = (i == stack.size() - 1) ? Color.web("#cba6f7") : NODE_FILL;
            StackPane node = makeNode(stack.get(i), fill);
            node.setLayoutX(CENTER_X - NODE_RADIUS);
            node.setLayoutY(cy - NODE_RADIUS);
            canvas.getChildren().add(node);

            // TOP label on the topmost node
            if (i == stack.size() - 1) {
                Label top = new Label("← TOP");
                top.setTextFill(Color.web("#cba6f7"));
                top.setFont(Font.font("Monospaced", FontWeight.BOLD, 12));
                top.setLayoutX(frameR + 8);
                top.setLayoutY(cy - 8);
                canvas.getChildren().add(top);
            }
        }

        if (stack.isEmpty()) {
            Label empty = new Label("[ stack empty ]");
            empty.setTextFill(Color.web("#6c7086"));
            empty.setFont(Font.font("System", 14));
            empty.setLayoutX(40);
            empty.setLayoutY(baseY - 30);
            canvas.getChildren().add(empty);
        }
    }

    private void animatePush() {
        int val = parseInput(); if (val == Integer.MIN_VALUE) return;
        if (!safeCall("push", () -> studentStack.push(val))) return;
        syncFromStudent();
        redraw();
        StackPane top = getTopNode();
        if (top == null) return;
        top.setTranslateY(-80);
        TranslateTransition t = new TranslateTransition(Duration.millis(stepMs()), top);
        t.setToY(0);
        setStatus("push(" + val + ")  →  new element on top of stack");
        t.play();
    }

    private void animatePop() {
        if (stack.isEmpty()) { setStatus("Stack is empty — cannot pop."); return; }
        int val = stack.get(stack.size() - 1);
        StackPane top = getTopNode();
        if (!safeCall("pop", studentStack::pop)) return;
        syncFromStudent();
        if (top == null) { redraw(); return; }
        TranslateTransition t = new TranslateTransition(Duration.millis(stepMs() * 0.7), top);
        t.setToY(-100);
        FadeTransition ft = new FadeTransition(Duration.millis(stepMs() * 0.3), top);
        ft.setToValue(0);
        SequentialTransition seq = new SequentialTransition(t, ft);
        seq.setOnFinished(ev -> { redraw(); setStatus("pop()  →  removed " + val); });
        setStatus("pop()  →  popping top element " + val + " …");
        seq.play();
    }

    private void animatePeek() {
        if (stack.isEmpty()) { setStatus("Stack is empty — nothing to peek."); return; }
        int[] valHolder = { stack.get(stack.size() - 1) };
        try { valHolder[0] = studentStack.peek(); } catch (Exception ignored) {}
        redraw();
        StackPane top = getTopNode();
        if (top != null) {
            Animation flash = flashNode(top, Color.web("#f9e2af"), Color.web("#cba6f7"));
            flash.play();
        }
        setStatus("peek()  →  top element is " + valHolder[0] + " (no removal)");
    }

    private void randomize() {
        studentStack = new MyStack();
        stack.clear();
        int n = 3 + rng.nextInt(5);
        int[] vals = new int[n];
        for (int i = 0; i < n; i++) vals[i] = 1 + rng.nextInt(99);
        for (int v : vals) {
            stack.add(v);
            try { studentStack.push(v); } catch (Exception ignored) {}
        }
        int[] snap = studentStack.snapshotForVisualizer();
        if (snap.length > 0) {
            stack.clear();
            for (int v : snap) stack.add(v);
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
        int[] snap = studentStack.snapshotForVisualizer();
        stack.clear();
        for (int v : snap) stack.add(v);
    }

    private StackPane getTopNode() {
        int count = 0;
        StackPane last = null;
        for (var child : canvas.getChildren()) {
            if (child instanceof StackPane) { last = (StackPane) child; count++; }
        }
        return last;
    }

    private int parseInput() {
        try { return Integer.parseInt(inputField.getText().trim()); }
        catch (NumberFormatException e) { setStatus("Enter a valid integer."); return Integer.MIN_VALUE; }
    }
}
