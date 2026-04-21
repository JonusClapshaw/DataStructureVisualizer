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
 * HashMapVisualizer
 *
 * Draws 8 visible buckets (out of 16) as labeled rows.
 * Each bucket shows a horizontal chain of Entry nodes connected by arrows.
 * Operations: Put, Get (highlights bucket + chain), Remove, Random.
 *
 * Hash function displayed: bucketIndex = Math.abs(key % 16)
 */
public class HashMapVisualizer extends BaseVisualizer {

    // Internal simplified entry
    private static class Entry {
        int key; String value;
        Entry(int k, String v) { key = k; value = v; }
    }

    private static final int CAPACITY = 16;
    private static final int VISIBLE_BUCKETS = 10; // show buckets 0-9

    private final List<List<Entry>> buckets = new ArrayList<>();
    private final TextField keyField = new TextField();
    private final TextField valField = new TextField();
    private final Random rng = new Random();
    private MyHashMap studentMap = new MyHashMap();

    private static final double LEFT_X    = 70;
    private static final double BUCKET_H  = 42;
    private static final double ENTRY_W   = 88;
    private static final double START_Y   = 20;

    public HashMapVisualizer() {
        super();
        completeSetup();
        for (int i = 0; i < CAPACITY; i++) buckets.add(new ArrayList<>());
        randomize();
        redraw();
    }

    @Override
    protected HBox buildControls() {
        keyField.setPromptText("key (int)");  keyField.setPrefWidth(80);
        valField.setPromptText("value (str)"); valField.setPrefWidth(90);
        String fs = "-fx-background-color: #313244; -fx-text-fill: #cdd6f4; -fx-background-radius: 6;";
        keyField.setStyle(fs); valField.setStyle(fs);

        Button put       = actionButton("Put",    Color.web("#89b4fa"));
        Button get       = actionButton("Get",    Color.web("#f9e2af"));
        Button remove    = actionButton("Remove", Color.web("#f38ba8"));
        Button randomBtn = actionButton("Random", Color.web("#a6e3a1"));

        put     .setOnAction(e -> animatePut());
        get     .setOnAction(e -> animateGet());
        remove  .setOnAction(e -> animateRemove());
        randomBtn.setOnAction(e -> { randomize(); redraw(); setStatus("Randomized."); });

        return new HBox(8,
            styledLabel("Key:"), keyField,
            styledLabel("Val:"), valField,
            put, get, remove, randomBtn);
    }

    // ── Drawing ────────────────────────────────────────────────────────────

    // nodeViews[bucket][chainIdx] → StackPane
    private final List<List<StackPane>> nodeViews = new ArrayList<>();

    private void redraw() {
        canvas.getChildren().clear();
        nodeViews.clear();

        for (int b = 0; b < VISIBLE_BUCKETS; b++) {
            nodeViews.add(new ArrayList<>());
            double y = START_Y + b * BUCKET_H;

            // Bucket box
            Rectangle box = new Rectangle(LEFT_X, y, 50, 32);
            box.setFill(Color.web("#313244")); box.setStroke(Color.web("#45475a")); box.setArcWidth(4); box.setArcHeight(4);
            Label bucketLbl = new Label("B" + b);
            bucketLbl.setFont(Font.font("Monospaced", FontWeight.BOLD, 11));
            bucketLbl.setTextFill(Color.web("#a6adc8"));
            bucketLbl.setLayoutX(LEFT_X + 8); bucketLbl.setLayoutY(y + 7);
            canvas.getChildren().addAll(box, bucketLbl);

            List<Entry> chain = buckets.get(b);
            for (int c = 0; c < chain.size(); c++) {
                Entry entry = chain.get(c);
                double ex = LEFT_X + 60 + c * ENTRY_W;

                // Arrow from box or previous entry
                double ax = (c == 0) ? LEFT_X + 50 : ex - 8;
                Line arr = new Line(ax, y + 16, ex, y + 16);
                arr.setStroke(ARROW_COLOR); arr.setStrokeWidth(1.5);
                canvas.getChildren().add(arr);

                // Entry rectangle
                Rectangle entryBox = new Rectangle(ex, y + 2, ENTRY_W - 10, 28);
                entryBox.setFill(NODE_FILL); entryBox.setArcWidth(6); entryBox.setArcHeight(6);

                Label entryLbl = new Label(entry.key + ":" + entry.value);
                entryLbl.setFont(Font.font("Monospaced", 10));
                entryLbl.setTextFill(TEXT_COLOR);
                entryLbl.setLayoutX(ex + 4); entryLbl.setLayoutY(y + 8);

                // Wrap in StackPane for highlight support
                StackPane sp = new StackPane(entryBox, entryLbl);
                sp.setLayoutX(ex); sp.setLayoutY(y + 2);
                sp.setPrefSize(ENTRY_W - 10, 28);
                canvas.getChildren().add(sp);
                nodeViews.get(b).add(sp);
            }

            // null at end
            if (chain.isEmpty()) {
                Label nil = new Label("null");
                nil.setTextFill(Color.web("#6c7086")); nil.setFont(Font.font("Monospaced", 11));
                nil.setLayoutX(LEFT_X + 58); nil.setLayoutY(y + 8);
                canvas.getChildren().add(nil);
            }
        }

        // Hash function label
        Label hashLbl = new Label("hash(key) = Math.abs(key % 16)   |   showing buckets 0-9");
        hashLbl.setFont(Font.font("Monospaced", 11));
        hashLbl.setTextFill(Color.web("#6c7086"));
        hashLbl.setLayoutX(LEFT_X);
        hashLbl.setLayoutY(START_Y + VISIBLE_BUCKETS * BUCKET_H + 6);
        canvas.getChildren().add(hashLbl);
    }

    // ── Animations ─────────────────────────────────────────────────────────

    private void animatePut() {
        int key = parseKey(); if (key == Integer.MIN_VALUE) return;
        String val = valField.getText().trim();
        if (val.isEmpty()) val = "v" + key;
        int bi = Math.abs(key % CAPACITY);
        final String finalVal = val;
        if (!safeCall("put", () -> studentMap.put(key, finalVal))) {
            // Shadow fallback: update or insert directly
            List<Entry> chain = buckets.get(bi);
            for (Entry e : chain) { if (e.key == key) { e.value = finalVal; redraw(); highlight(bi, chain.indexOf(e), HIGHLIGHT_FILL); setStatus("put(" + key + ", " + finalVal + ")  →  updated existing entry in bucket " + bi); return; } }
            chain.add(new Entry(key, finalVal));
        } else {
            syncFromStudent();
        }
        redraw();
        if (bi < VISIBLE_BUCKETS && !nodeViews.get(bi).isEmpty()) {
            StackPane sp = nodeViews.get(bi).get(nodeViews.get(bi).size() - 1);
            sp.setScaleX(0.1); sp.setScaleY(0.1);
            ScaleTransition st = new ScaleTransition(Duration.millis(stepMs()), sp);
            st.setToX(1); st.setToY(1); st.play();
        }
        setStatus("put(" + key + ", " + val + ")  →  hash=" + bi + "  entry added/updated");
    }

    private void animateGet() {
        int key = parseKey(); if (key == Integer.MIN_VALUE) return;
        int bi = Math.abs(key % CAPACITY);
        List<Entry> chain = buckets.get(bi);
        redraw();
        // Try student's get (informational)
        String studentResult = null;
        try { studentResult = studentMap.get(key); } catch (Exception ignored) {}
        final String sResult = studentResult;
        setStatus("get(" + key + ")  →  checking bucket " + bi + " …");

        SequentialTransition seq = new SequentialTransition();
        boolean[] found = {false};
        for (int c = 0; c < chain.size(); c++) {
            final int ci = c; final Entry entry = chain.get(c);
            PauseTransition p = pause();
            p.setOnFinished(ev -> {
                Color col = (entry.key == key) ? HIGHLIGHT_FILL : COMPARE_FILL;
                highlight(bi, ci, col);
                if (entry.key == key) {
                    String result = sResult != null ? sResult : entry.value;
                    setStatus("get(" + key + ")  →  FOUND → \"" + result + "\" ✓"); found[0] = true;
                } else setStatus("get(" + key + ")  →  key " + entry.key + " ≠ " + key + ", follow next …");
            });
            seq.getChildren().add(p);
        }
        seq.setOnFinished(ev -> { if (!found[0]) setStatus("get(" + key + ")  →  not found (returns null)"); });
        seq.play();
    }

    private void animateRemove() {
        int key = parseKey(); if (key == Integer.MIN_VALUE) return;
        int bi = Math.abs(key % CAPACITY);
        if (!safeCall("remove", () -> studentMap.remove(key))) {
            buckets.get(bi).removeIf(e -> e.key == key);
            redraw();
            setStatus("remove(" + key + ")  →  entry removed from shadow (student TODO pending)");
            return;
        }
        syncFromStudent();
        redraw();
        setStatus("remove(" + key + ")  →  entry removed from bucket " + bi);
    }

    private void highlight(int bucket, int chainIdx, Color color) {
        if (bucket >= VISIBLE_BUCKETS) return;
        List<StackPane> row = nodeViews.get(bucket);
        if (row == null || chainIdx >= row.size()) return;
        StackPane sp = row.get(chainIdx);
        for (var child : sp.getChildren()) {
            if (child instanceof Rectangle) { ((Rectangle) child).setFill(color); break; }
        }
    }

    private void randomize() {
        studentMap = new MyHashMap();
        for (List<Entry> b : buckets) b.clear();
        String[] words = {"cat","dog","sun","map","key","fox","car","ant","bee","cup"};
        int n = 6 + rng.nextInt(6);
        Set<Integer> used = new HashSet<>();
        while (used.size() < n) {
            int k = rng.nextInt(50);
            if (used.add(k)) {
                String word = words[rng.nextInt(words.length)];
                int bi = Math.abs(k % CAPACITY);
                buckets.get(bi).add(new Entry(k, word));
                try { studentMap.put(k, word); } catch (Exception ignored) {}
            }
        }
        syncFromStudent();
    }

    private boolean safeCall(String opName, Runnable op) {
        try { op.run(); return true; }
        catch (Exception e) {
            setStatus("⚠  " + opName + "() not implemented yet — fill in the TODO!");
            return false;
        }
    }

    /** Syncs the shadow buckets from student's getBucketSnapshot(). */
    private void syncFromStudent() {
        try {
            List<String> snapshot = studentMap.getBucketSnapshot();
            if (snapshot.isEmpty()) return;
            for (int i = 0; i < Math.min(snapshot.size(), buckets.size()); i++) {
                buckets.get(i).clear();
                String chain = snapshot.get(i);
                if (!chain.isEmpty()) {
                    for (String pair : chain.split(",")) {
                        String[] kv = pair.split(":");
                        if (kv.length == 2) {
                            try { buckets.get(i).add(new Entry(Integer.parseInt(kv[0].trim()), kv[1].trim())); }
                            catch (NumberFormatException ignored2) {}
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    private int parseKey() {
        try { return Integer.parseInt(keyField.getText().trim()); }
        catch (NumberFormatException e) { setStatus("Enter an integer key."); return Integer.MIN_VALUE; }
    }
}
