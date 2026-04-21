package com.example;

import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

/**
 * ================================================================
 *  TEST SUITE: Graph
 * ================================================================
 *  Run:  mvn test -Dtest=GraphTest
 *
 *    Wave 1 – Intro       : addVertex, addEdge, hasVertex
 *    Wave 2 – Basic       : hasEdge, getNeighbors, removeEdge
 *    Wave 3 – Intermediate: bfs, dfs
 *    Wave 4 – Advanced    : hasCycle, connectedComponents, shortestPath
 * ================================================================
 */
public class GraphTest {

    private Graph graph;

    @Before
    public void setUp() {
        graph = new Graph();
    }

    // ================================================================
    //  WAVE 1 TESTS — addVertex, addEdge, hasVertex
    // ================================================================

    @Test
    public void wave1_addVertex_newVertex_appearsInAdjList() {
        graph.addVertex(1);
        assertTrue(graph.getAdjList().containsKey(1));
    }

    @Test
    public void wave1_addVertex_duplicate_doesNotAddTwice() {
        graph.addVertex(1);
        graph.addVertex(1);
        assertEquals(1, graph.getAdjList().size());
    }

    @Test
    public void wave1_addEdge_bothVerticesHaveEachOtherAsNeighbor() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);
        assertTrue(graph.getAdjList().get(1).contains(2));
        assertTrue(graph.getAdjList().get(2).contains(1));
    }

    @Test
    public void wave1_addEdge_duplicateEdge_notAddedTwice() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);
        graph.addEdge(1, 2); // second call should be ignored
        assertEquals(1, graph.getAdjList().get(1).size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void wave1_addEdge_missingVertex_throwsException() {
        graph.addVertex(1);
        graph.addEdge(1, 99); // 99 does not exist
    }

    @Test
    public void wave1_hasVertex_existingVertex_returnsTrue() {
        graph.addVertex(5);
        assertTrue(graph.hasVertex(5));
    }

    @Test
    public void wave1_hasVertex_missingVertex_returnsFalse() {
        assertFalse(graph.hasVertex(42));
    }

    // ================================================================
    //  WAVE 2 TESTS — hasEdge, getNeighbors, removeEdge
    // ================================================================

    @Test
    public void wave2_hasEdge_existingEdge_returnsTrue() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);
        assertTrue(graph.hasEdge(1, 2));
        assertTrue(graph.hasEdge(2, 1)); // undirected
    }

    @Test
    public void wave2_hasEdge_missingEdge_returnsFalse() {
        graph.addVertex(1);
        graph.addVertex(2);
        assertFalse(graph.hasEdge(1, 2));
    }

    @Test
    public void wave2_getNeighbors_returnsCorrectNeighborList() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        List<Integer> neighbors = graph.getNeighbors(1);
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(2));
        assertTrue(neighbors.contains(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void wave2_getNeighbors_missingVertex_throwsException() {
        graph.getNeighbors(99);
    }

    @Test
    public void wave2_removeEdge_removesFromBothSides() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);
        graph.removeEdge(1, 2);
        assertFalse(graph.hasEdge(1, 2));
        assertFalse(graph.hasEdge(2, 1));
    }

    @Test
    public void wave2_removeEdge_nonExistentEdge_doesNothing() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.removeEdge(1, 2); // no edge to remove — should not throw
        assertFalse(graph.hasEdge(1, 2));
    }

    // ================================================================
    //  WAVE 3 TESTS — bfs, dfs
    // ================================================================

    // Graph used for traversal tests:
    //   1 - 2 - 4
    //   |
    //   3
    private void buildTraversalGraph() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
    }

    @Test
    public void wave3_bfs_visitsAllReachableVertices() {
        buildTraversalGraph();
        List<Integer> result = graph.bfs(1);
        assertEquals(4, result.size());
        assertTrue(result.containsAll(Arrays.asList(1, 2, 3, 4)));
    }

    @Test
    public void wave3_bfs_firstElementIsStartVertex() {
        buildTraversalGraph();
        List<Integer> result = graph.bfs(1);
        assertEquals(Integer.valueOf(1), result.get(0));
    }

    @Test
    public void wave3_bfs_neighborsVisitedBeforeTheirChildren() {
        buildTraversalGraph();
        List<Integer> result = graph.bfs(1);
        // 2 and 3 (direct neighbors of 1) must appear before 4 (neighbor of 2)
        int idx2 = result.indexOf(2);
        int idx3 = result.indexOf(3);
        int idx4 = result.indexOf(4);
        assertTrue(idx2 < idx4);
        assertTrue(idx3 < idx4 || idx2 < idx4); // level-order guarantee
    }

    @Test(expected = IllegalArgumentException.class)
    public void wave3_bfs_missingStartVertex_throwsException() {
        graph.bfs(99);
    }

    @Test
    public void wave3_dfs_visitsAllReachableVertices() {
        buildTraversalGraph();
        List<Integer> result = graph.dfs(1);
        assertEquals(4, result.size());
        assertTrue(result.containsAll(Arrays.asList(1, 2, 3, 4)));
    }

    @Test
    public void wave3_dfs_firstElementIsStartVertex() {
        buildTraversalGraph();
        List<Integer> result = graph.dfs(1);
        assertEquals(Integer.valueOf(1), result.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void wave3_dfs_missingStartVertex_throwsException() {
        graph.dfs(99);
    }

    // ================================================================
    //  WAVE 4 TESTS — hasCycle, connectedComponents, shortestPath
    // ================================================================

    @Test
    public void wave4_hasCycle_graphWithCycle_returnsTrue() {
        // Triangle: 1 - 2 - 3 - 1
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);
        assertTrue(graph.hasCycle());
    }

    @Test
    public void wave4_hasCycle_treeGraph_noCycle_returnsFalse() {
        // Tree (no cycle): 1 - 2, 1 - 3
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        assertFalse(graph.hasCycle());
    }

    @Test
    public void wave4_hasCycle_emptyGraph_returnsFalse() {
        assertFalse(graph.hasCycle());
    }

    @Test
    public void wave4_connectedComponents_singleComponent() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        assertEquals(1, graph.connectedComponents());
    }

    @Test
    public void wave4_connectedComponents_twoIsolatedComponents() {
        // Component A: 1-2  |  Component B: 3-4
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addEdge(1, 2);
        graph.addEdge(3, 4);
        assertEquals(2, graph.connectedComponents());
    }

    @Test
    public void wave4_connectedComponents_allIsolatedVertices() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        assertEquals(3, graph.connectedComponents());
    }

    @Test
    public void wave4_shortestPath_directNeighbors() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);
        List<Integer> path = graph.shortestPath(1, 2);
        assertEquals(Arrays.asList(1, 2), path);
    }

    @Test
    public void wave4_shortestPath_longerPath() {
        // 1 - 2 - 3 - 4
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        List<Integer> path = graph.shortestPath(1, 4);
        assertEquals(Arrays.asList(1, 2, 3, 4), path);
    }

    @Test
    public void wave4_shortestPath_noPath_returnsEmptyList() {
        graph.addVertex(1);
        graph.addVertex(2); // disconnected
        List<Integer> path = graph.shortestPath(1, 2);
        assertTrue(path.isEmpty());
    }

    @Test
    public void wave4_shortestPath_sameStartAndEnd() {
        graph.addVertex(1);
        List<Integer> path = graph.shortestPath(1, 1);
        assertEquals(Arrays.asList(1), path);
    }
}
