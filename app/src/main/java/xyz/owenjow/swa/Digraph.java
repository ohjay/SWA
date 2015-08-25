package xyz.owenjow.swa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * A directed graph providing support for edge addition and depth-first search.
 * @author Owen Jow
 */
public class Digraph {
    private int numVertices;
    private ArrayList<Integer>[] adjacencyList;

    /**
     * Constructs a directed graph with NUM_VERTICES vertices.
     * Initializes a list of outgoing edges for each vertex.
     * @param numVertices the number of vertices in the graph
     */
    public Digraph(int numVertices) {
        this.numVertices = numVertices; // this value should not be negative
        this.adjacencyList = (ArrayList<Integer>[]) new ArrayList[numVertices];

        for (int i = 0; i < numVertices; i++) {
            adjacencyList[i] = new ArrayList<Integer>(); // these will contain outgoing edge IDs
        }
    }

    /**
     * Creates an edge pointing from vertex V_1 to vertex V_2.
     * @param v1 the identifier for the origin vertex (should be >= 0 and < numVertices)
     * @param v2 the identifier for the destination vertex (should be >= 0 and < numVertices)
     */
    public void addEdge(int v1, int v2) {
        adjacencyList[v1].add(v2);
    }

    /**
     * Returns the set of all vertices that can be reached by starting at the locations
     * specified by SOURCE_IDS. This is accomplished by running a depth-first traversal
     * through the graph.
     * @param sourceIDs origin vertex IDs (numbers between 0 and numVertices - 1)
     */
    public Set<Integer> reachableVertices(Set<Integer> sourceIDs) {
        boolean[] visited = new boolean[numVertices];
        for (int s : sourceIDs) {
            if (!visited[s]) {
                dfsVisited(s, visited);
            }
        }

        HashSet<Integer> reachableSet = new HashSet<Integer>();
        for (int i = 0; i < visited.length; i++) {
            if (visited[i]) { reachableSet.add(i); }
        }

        return reachableSet;
    }

    /**
     * Returns the array of all vertices visited on a depth-first search
     * originating at vertex S.
     * @param s the starting vertex
     * @param visited the array to be updated (should start out as empty)
     * @return the filled-in VISITED array
     */
    private boolean[] dfsVisited(int s, boolean[] visited) {
        LinkedList<Integer> stack = new LinkedList<Integer>();
        stack.push(s);

        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited[v]) {
                visited[v] = true;
                for (int w : adjacencyList[v]) {
                    if (!visited[w]) {
                        stack.push(w);
                    }
                }
            }
        }

        return visited;
    }
}
