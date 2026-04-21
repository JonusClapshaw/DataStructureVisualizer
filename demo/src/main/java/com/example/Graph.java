package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * ================================================================
 *  DATA STRUCTURE: Graph (Undirected, Unweighted — Adjacency List)
 * ================================================================
 *
 *  A Graph is a collection of VERTICES (nodes) connected by EDGES.
 *
 *  This implementation is:
 *    - Undirected: edge(A, B) means A ↔ B (travels both ways)
 *    - Unweighted: edges have no associated cost
 *    - Stored as an adjacency list: HashMap<Integer, List<Integer>>
 *
 *  Visual:
 *    Vertices: {1, 2, 3, 4}
 *    Edges:    1-2, 1-3, 2-4
 *
 *    1 → [2, 3]
 *    2 → [1, 4]
 *    3 → [1]
 *    4 → [2]
 *
 *  Key properties:
 *    - O(1) average addVertex / addEdge
 *    - O(V + E) for BFS and DFS
 *    - Good for sparse graphs
 *
 * ================================================================
 *  HOW THE WAVES WORK
 * ================================================================
 *  Complete each TODO then run:  mvn test -Dtest=GraphTest
 *
 *    Wave 1 – Intro       : addVertex, addEdge, hasVertex
 *    Wave 2 – Basic       : hasEdge, getNeighbors, removeEdge
 *    Wave 3 – Intermediate: bfs, dfs
 *    Wave 4 – Advanced    : hasCycle, connectedComponents, shortestPath
 * ================================================================
 */
public class Graph {

    // adjacency list: vertex label → list of neighbor labels
    private Map<Integer, List<Integer>> adjList;

    /** Creates an empty undirected Graph. */
    public Graph() {
        this.adjList = new HashMap<>();
    }

    // ================================================================
    //  WAVE 1 — INTRO
    //  Goal: Add vertices, connect them, and verify existence.
    // ================================================================

    /**
     * [Wave 1 – Task 1] addVertex
     * ---------------------------
     * Add vertex {@code v} to the graph.
     * If the vertex already exists, do nothing.
     *
     * Steps:
     *   1. Check adjList.containsKey(v) — skip if already present
     *   2. Put a new empty ArrayList for key v in adjList
     *
     * @param v the integer vertex label to add
     */
    public void addVertex(int v) {
        // TODO: if v is not already in adjList, add it with an empty ArrayList
    }

    /**
     * [Wave 1 – Task 2] addEdge
     * -------------------------
     * Add an undirected edge between vertices {@code u} and {@code v}.
     * Both vertices must already exist — throw {@link IllegalArgumentException}
     * if either is missing.
     * Avoid duplicate edges.
     *
     * Steps (since the graph is undirected):
     *   1. Validate both u and v exist
     *   2. If v is not already in u's neighbor list, add v
     *   3. If u is not already in v's neighbor list, add u
     *
     * @param u first vertex
     * @param v second vertex
     * @throws IllegalArgumentException if either vertex does not exist
     */
    public void addEdge(int u, int v) {
        if (!adjList.containsKey(u) || !adjList.containsKey(v)) {
            throw new IllegalArgumentException("Both vertices must exist before adding an edge");
        }
        // TODO: add v to u's neighbor list (if not already there)
        // TODO: add u to v's neighbor list (if not already there)
    }

    /**
     * [Wave 1 – Task 3] hasVertex
     * ---------------------------
     * Return true if vertex {@code v} exists in the graph.
     *
     * @param v vertex to check
     * @return true if present
     */
    public boolean hasVertex(int v) {
        // TODO: check adjList for key v
        return false; // placeholder
    }

    // ================================================================
    //  WAVE 2 — BASIC
    //  Goal: Query and remove edges.
    // ================================================================

    /**
     * [Wave 2 – Task 1] hasEdge
     * -------------------------
     * Return true if there is an edge between {@code u} and {@code v}.
     *
     * Hint: vertex u must exist AND v must appear in u's neighbor list.
     *
     * @param u first vertex
     * @param v second vertex
     * @return true if the edge exists
     */
    public boolean hasEdge(int u, int v) {
        // TODO: check adjList.containsKey(u) and that v is in u's neighbor list
        return false; // placeholder
    }

    /**
     * [Wave 2 – Task 2] getNeighbors
     * --------------------------------
     * Return an unmodifiable list of all neighbors of vertex {@code v}.
     * Throws {@link IllegalArgumentException} if v does not exist.
     *
     * @param v the vertex
     * @return unmodifiable list of neighbor labels
     * @throws IllegalArgumentException if v is not in the graph
     */
    public List<Integer> getNeighbors(int v) {
        if (!adjList.containsKey(v)) {
            throw new IllegalArgumentException("Vertex " + v + " does not exist");
        }
        // TODO: return Collections.unmodifiableList(adjList.get(v))
        return null; // placeholder
    }

    /**
     * [Wave 2 – Task 3] removeEdge
     * ----------------------------
     * Remove the undirected edge between {@code u} and {@code v}.
     * If the edge does not exist, do nothing.
     * Throws {@link IllegalArgumentException} if either vertex is missing.
     *
     * Hint: List.remove(Object) removes by value;
     *       use Integer.valueOf(v) to remove the integer v, not index v.
     *
     * @param u first vertex
     * @param v second vertex
     */
    public void removeEdge(int u, int v) {
        if (!adjList.containsKey(u) || !adjList.containsKey(v)) {
            throw new IllegalArgumentException("Both vertices must exist");
        }
        // TODO: remove Integer.valueOf(v) from u's list
        // TODO: remove Integer.valueOf(u) from v's list
    }

    // ================================================================
    //  WAVE 3 — INTERMEDIATE
    //  Goal: Traverse the graph systematically.
    // ================================================================

    /**
     * [Wave 3 – Task 1] bfs
     * ---------------------
     * Perform Breadth-First Search starting at {@code start}.
     * Return the vertices in the ORDER they were first visited.
     *
     * Algorithm:
     *   1. Create a Set<Integer> visited and a Queue<Integer>
     *   2. Mark start as visited; add start to queue
     *   3. While queue is not empty:
     *        a. Poll (dequeue) a vertex → add to result
     *        b. For each neighbor: if not in visited, mark and enqueue it
     *
     * BFS explores level by level — closest vertices first.
     *
     * @param start vertex to begin from
     * @return list of vertices in BFS order
     * @throws IllegalArgumentException if start does not exist
     */
    public List<Integer> bfs(int start) {
        if (!adjList.containsKey(start)) {
            throw new IllegalArgumentException("Start vertex does not exist");
        }
        List<Integer> result  = new ArrayList<>();
        Set<Integer>  visited = new HashSet<>();
        Queue<Integer> queue  = new LinkedList<>();
        // TODO: implement BFS — mark start, enqueue, then process queue
        return result;
    }

    /**
     * [Wave 3 – Task 2] dfs
     * ---------------------
     * Perform Depth-First Search starting at {@code start}.
     * Return the vertices in the order they were first visited.
     *
     * You may implement this either:
     *   - Recursively (elegant, uses the call stack)
     *   - Iteratively (using a java.util.Stack / ArrayDeque)
     *
     * DFS explores as far as possible before backtracking.
     *
     * @param start vertex to begin from
     * @return list of vertices in DFS order
     * @throws IllegalArgumentException if start does not exist
     */
    public List<Integer> dfs(int start) {
        if (!adjList.containsKey(start)) {
            throw new IllegalArgumentException("Start vertex does not exist");
        }
        List<Integer> result  = new ArrayList<>();
        Set<Integer>  visited = new HashSet<>();
        // TODO: implement DFS (recursive helper or iterative stack)
        return result;
    }

    // ================================================================
    //  WAVE 4 — ADVANCED
    //  Goal: Detect cycles, count components, find shortest paths.
    // ================================================================

    /**
     * [Wave 4 – Task 1] hasCycle
     * --------------------------
     * Return true if the undirected graph contains at least one cycle.
     *
     * Algorithm (DFS with parent tracking):
     *   For each unvisited vertex, run DFS.
     *   During DFS, if you reach a visited neighbor that is NOT the parent
     *   you came from, a cycle exists.
     *
     * Handle disconnected graphs: run DFS from every unvisited vertex.
     *
     * @return true if any cycle is detected
     */
    public boolean hasCycle() {
        // TODO: implement cycle detection with a visited set and parent tracking
        return false; // placeholder
    }

    /**
     * [Wave 4 – Task 2] connectedComponents
     * ----------------------------------------
     * Return the NUMBER of connected components in the graph.
     *
     * A connected component is a maximal set of vertices where every
     * vertex is reachable from every other vertex in that component.
     *
     * Algorithm:
     *   Keep a visited set.
     *   For each vertex not yet in visited, run BFS/DFS and increment a counter.
     *
     * @return number of connected components
     */
    public int connectedComponents() {
        // TODO: iterate all vertices; each fresh BFS/DFS start = 1 new component
        return 0; // placeholder
    }

    /**
     * [Wave 4 – Task 3] shortestPath
     * --------------------------------
     * Return the shortest path (fewest edges) from {@code start} to {@code end}
     * as an ordered List of vertex labels including both endpoints.
     * Returns an empty list if no path exists.
     *
     * Algorithm (BFS with parent map):
     *   1. BFS from start, tracking Map<Integer, Integer> parent (child → parent)
     *   2. When end is reached, reconstruct the path by following parent links
     *      back to start, then reverse the list
     *
     * @param start source vertex
     * @param end   target vertex
     * @return ordered list of vertices on the shortest path, or empty list
     */
    public List<Integer> shortestPath(int start, int end) {
        // TODO: BFS with parent map, then reconstruct path from end → start, then reverse
        return new ArrayList<>(); // placeholder
    }

    // ----------------------------------------------------------
    //  Package-private accessor for tests
    // ----------------------------------------------------------
    Map<Integer, List<Integer>> getAdjList() { return adjList; }
}
