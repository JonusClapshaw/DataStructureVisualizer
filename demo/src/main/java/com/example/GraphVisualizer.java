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
 * GraphVisualizer
 *
 * Draws an undirected graph with vertices at random positions on the canvas.
 * Operations: Add Vertex, Add Edge, Remove Edge, BFS (animated), DFS (animated), Random.
 *
 * Vertices are circles labelled with their integer ID.
 * Edges are straight lines between them.
 * BFS/DFS highlights visited nodes one step at a time.
 */
public class GraphVisualizer extends BaseVisualizer {

    private final Map<Integer, double[]> positions = new LinkedHashMap<>(); // vertex → [x, y]
    private final Map<Integer, Set<Integer>> adj    = new LinkedHashMap<>();
    private final TextField vtxField  = new TextField();
    private final TextField edgeField = new TextField();
    private final Random rng = new Random();
    private Graph studentGraph = new Graph();

    public GraphVisualizer() {
        super();
        completeSetup();
        randomize();
        redraw();
    }

    @Override
    protected HBox buildControls() {
        vtxField .setPromptText("vertex");  vtxField .setPrefWidth(60);
        edgeField.setPromptText("v1,v2");   edgeField.setPrefWidth(70);
        String fieldStyle = "-fx-background-color: #313244; -fx-text-fill: #cdd6f4; -fx-background-radius: 6;";
        vtxField.setStyle(fieldStyle); edgeField.setStyle(fieldStyle);

        Button addVtx   = actionButton("Add Vertex", Color.web("#89b4fa"));
        Button addEdge  = actionButton("Add Edge",   Color.web("#89dceb"));
        Button remEdge  = actionButton("Rem Edge",   Color.web("#f38ba8"));
        Button bfs      = actionButton("BFS",        Color.web("#a6e3a1"));
        Button dfs      = actionButton("DFS",        Color.web("#cba6f7"));
        Button randomBtn= actionButton("Random",     Color.web("#f9e2af"));

        addVtx  .setOnAction(e -> addVertex());
        addEdge .setOnAction(e -> addEdge());
        remEdge .setOnAction(e -> removeEdge());
        bfs     .setOnAction(e -> animateBFS());
        dfs     .setOnAction(e -> animateDFS());
        randomBtn.setOnAction(e -> { randomize(); redraw(); setStatus("Randomized graph."); });

        return new HBox(8,
            styledLabel("Vertex:"), vtxField, addVtx,
            styledLabel("Edge:"), edgeField, addEdge, remEdge,
            bfs, dfs, randomBtn);
    }

    // ── Operations ────────────────────────────────────────────────────────

    private void addVertex() {
        try {
            int v = Integer.parseInt(vtxField.getText().trim());
            if (positions.containsKey(v)) { setStatus("Vertex " + v + " already exists."); return; }
            double cx = 80 + rng.nextInt(Math.max(1, (int)canvas.getWidth() - 160));
            double cy = 50 + rng.nextInt(Math.max(1, (int)canvas.getHeight() - 100));
            positions.put(v, new double[]{cx, cy});
            // Call student's addVertex; sync adj from student if successful
            if (!safeCall("addVertex", () -> studentGraph.addVertex(v))) {
                adj.put(v, new LinkedHashSet<>()); // keep shadow working
            } else {
                syncAdjFromStudent();
            }
            redraw();
            setStatus("addVertex(" + v + ")  →  placed at (" + (int)cx + ", " + (int)cy + ")");
        } catch (NumberFormatException e) { setStatus("Enter an integer vertex ID."); }
    }

    private void addEdge() {
        int[] verts = parsePair(edgeField.getText()); if (verts == null) return;
        int u = verts[0], v = verts[1];
        if (!adj.containsKey(u) || !adj.containsKey(v)) { setStatus("Both vertices must exist first."); return; }
        if (!safeCall("addEdge", () -> studentGraph.addEdge(u, v))) {
            adj.get(u).add(v); adj.get(v).add(u); // keep shadow working
        } else {
            syncAdjFromStudent();
        }
        redraw();
        setStatus("addEdge(" + u + ", " + v + ")  →  undirected edge added");
    }

    private void removeEdge() {
        int[] verts = parsePair(edgeField.getText()); if (verts == null) return;
        int u = verts[0], v = verts[1];
        if (!safeCall("removeEdge", () -> studentGraph.removeEdge(u, v))) {
            // Fall back: remove from shadow directly
            if (adj.containsKey(u)) adj.get(u).remove(v);
            if (adj.containsKey(v)) adj.get(v).remove(u);
        } else {
            syncAdjFromStudent();
        }
        redraw();
        setStatus("removeEdge(" + u + ", " + v + ")  →  edge removed");
    }

    // ── Drawing ────────────────────────────────────────────────────────────

    private final Map<Integer, StackPane> nodeViews = new HashMap<>();

    private void redraw() {
        canvas.getChildren().clear();
        nodeViews.clear();

        // Draw edges first (behind nodes)
        Set<String> drawn = new HashSet<>();
        for (Map.Entry<Integer, Set<Integer>> entry : adj.entrySet()) {
            int u = entry.getKey();
            for (int v : entry.getValue()) {
                String key = Math.min(u,v) + "-" + Math.max(u,v);
                if (drawn.contains(key)) continue;
                drawn.add(key);
                double[] pu = positions.get(u), pv = positions.get(v);
                if (pu == null || pv == null) continue;
                Line edge = new Line(pu[0], pu[1], pv[0], pv[1]);
                edge.setStroke(Color.web("#585b70"));
                edge.setStrokeWidth(2);
                canvas.getChildren().add(edge);
            }
        }

        // Draw vertices
        for (Map.Entry<Integer, double[]> entry : positions.entrySet()) {
            int v = entry.getKey();
            double[] pos = entry.getValue();
            StackPane sp = makeNode(v, NODE_FILL);
            sp.setLayoutX(pos[0] - NODE_RADIUS);
            sp.setLayoutY(pos[1] - NODE_RADIUS);
            canvas.getChildren().add(sp);
            nodeViews.put(v, sp);
        }

        if (positions.isEmpty()) {
            double cy = canvas.getHeight() > 0 ? canvas.getHeight() / 2 : 200;
            Label empty = new Label("[ no vertices — add some or click Random ]");
            empty.setTextFill(Color.web("#6c7086")); empty.setFont(Font.font("System", 14));
            empty.setLayoutX(40); empty.setLayoutY(cy - 10);
            canvas.getChildren().add(empty);
        }
    }

    // ── BFS / DFS ─────────────────────────────────────────────────────────

    private void animateBFS() {
        if (positions.isEmpty()) { setStatus("No vertices."); return; }
        int start = positions.keySet().iterator().next();
        // Try student's bfs first; fall back to shadow if not implemented
        List<Integer> order = null;
        try { order = studentGraph.bfs(start); } catch (Exception ignored) {}
        if (order == null || order.isEmpty()) {
            // shadow BFS
            order = new ArrayList<>();
            Set<Integer> visited = new LinkedHashSet<>();
            Queue<Integer> queue = new ArrayDeque<>();
            queue.add(start); visited.add(start);
            while (!queue.isEmpty()) {
                int cur = queue.poll(); order.add(cur);
                for (int nb : adj.getOrDefault(cur, Collections.emptySet())) {
                    if (!visited.contains(nb)) { visited.add(nb); queue.add(nb); }
                }
            }
        }
        animateTraversal(order, "BFS");
    }

    private void animateDFS() {
        if (positions.isEmpty()) { setStatus("No vertices."); return; }
        int start = positions.keySet().iterator().next();
        // Try student's dfs first; fall back to shadow if not implemented
        List<Integer> order = null;
        try { order = studentGraph.dfs(start); } catch (Exception ignored) {}
        if (order == null || order.isEmpty()) {
            order = new ArrayList<>();
            Set<Integer> visited = new LinkedHashSet<>();
            dfsHelper(start, visited, order);
        }
        animateTraversal(order, "DFS");
    }

    private void dfsHelper(int v, Set<Integer> visited, List<Integer> order) {
        visited.add(v); order.add(v);
        for (int nb : adj.getOrDefault(v, Collections.emptySet())) {
            if (!visited.contains(nb)) dfsHelper(nb, visited, order);
        }
    }

    private void animateTraversal(List<Integer> order, String name) {
        redraw();
        SequentialTransition seq = new SequentialTransition();
        StringBuilder sb = new StringBuilder(name + ": ");
        for (Integer v : order) {
            sb.append(v).append(" ");
            final String soFar = sb.toString();
            PauseTransition p = pause();
            p.setOnFinished(ev -> {
                StackPane sp = nodeViews.get(v);
                if (sp != null) ((Circle)sp.getChildren().get(0)).setFill(HIGHLIGHT_FILL);
                setStatus(soFar);
            });
            seq.getChildren().add(p);
        }
        seq.setOnFinished(ev -> setStatus(name + " complete: " + sb.toString().trim()));
        seq.play();
    }

    // ── Random graph ──────────────────────────────────────────────────────

    private void randomize() {
        studentGraph = new Graph();
        positions.clear();
        adj.clear();
        int n = 5 + rng.nextInt(4);
        double canvasW = canvas.getWidth() > 0 ? canvas.getWidth() : 700;
        double canvasH = canvas.getHeight() > 0 ? canvas.getHeight() : 380;
        double margin = 60;

        // Place vertices in a rough circle so edges are readable
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            double rx = (canvasW / 2 - margin) * 0.8;
            double ry = (canvasH / 2 - margin) * 0.8;
            double cx = canvasW / 2 + rx * Math.cos(angle);
            double cy = canvasH / 2 + ry * Math.sin(angle);
            positions.put(i + 1, new double[]{cx, cy});
            adj.put(i + 1, new LinkedHashSet<>());
            try { studentGraph.addVertex(i + 1); } catch (Exception ignored) {}
        }

        // Random edges (connected backbone + extras)
        List<Integer> verts = new ArrayList<>(positions.keySet());
        Collections.shuffle(verts, rng);
        for (int i = 1; i < verts.size(); i++) {
            int u = verts.get(i - 1), v = verts.get(i);
            adj.get(u).add(v); adj.get(v).add(u);
            try { studentGraph.addEdge(u, v); } catch (Exception ignored) {}
        }
        int extras = rng.nextInt(n);
        for (int k = 0; k < extras; k++) {
            int u = verts.get(rng.nextInt(verts.size()));
            int v = verts.get(rng.nextInt(verts.size()));
            if (u != v) {
                adj.get(u).add(v); adj.get(v).add(u);
                try { studentGraph.addEdge(u, v); } catch (Exception ignored) {}
            }
        }
        // If student's addEdge worked, sync shadow from student
        syncAdjFromStudent();
    }

    private boolean safeCall(String opName, Runnable op) {
        try { op.run(); return true; }
        catch (Exception e) {
            setStatus("⚠  " + opName + "() not implemented yet — fill in the TODO!");
            return false;
        }
    }

    /** Syncs the shadow adjacency map from the student's graph (preserves positions). */
    private void syncAdjFromStudent() {
        try {
            Map<Integer, List<Integer>> sAdj = studentGraph.getAdjList();
            if (sAdj.isEmpty()) return; // student hasn't added anything
            adj.clear();
            for (Map.Entry<Integer, List<Integer>> e : sAdj.entrySet()) {
                adj.put(e.getKey(), new LinkedHashSet<>(e.getValue()));
            }
        } catch (Exception ignored) {}
    }

    private int[] parsePair(String text) {
        try {
            String[] parts = text.trim().split(",");
            return new int[]{ Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()) };
        } catch (Exception e) { setStatus("Enter two comma-separated vertex IDs (e.g. 1,2)."); return null; }
    }
}
