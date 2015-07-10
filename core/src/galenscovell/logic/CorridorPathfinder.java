package galenscovell.logic;

import galenscovell.util.Constants;

import java.util.*;

/**
 * CORRIDOR PATHFINDER
 * Digs connecting corridors between rooms during level construction.
 *
 * @author Galen Scovell
 */

public class CorridorPathfinder {
    private Tile[][] grid;
    private int columns, rows;

    public CorridorPathfinder(Tile[][] grid, int columns, int rows) {
        this.grid = grid;
        this.columns = columns;
        this.rows = rows;
    }

    private class Node {
        Node parent;
        Tile self;
        double cost;

        public Node(Tile s) {
            this.self = s;
            this.cost = Double.POSITIVE_INFINITY;
        }
    }

    public Stack<Tile> findPath(Tile start, Tile end) {
        List<Node> open = new ArrayList<Node>();
        List<Tile> closed = new ArrayList<Tile>();
        Node startNode = new Node(start);
        startNode.cost = 0;
        Node endNode = new Node(end);
        open.add(startNode);

        while (!open.isEmpty()) {
            // Consider node with best score in open list
            Node a = chooseNode(open, endNode);
            // If node tile is end tile, trace path and finish
            if (a.self == end) {
                return tracePath(a);
            } else {
                // Don't repeat ourselves
                open.remove(a);
                closed.add(a.self);

                // Consider current node's neighbors
                for (Tile neighbor : getCardinalNeighbors(a.self)) {
                    // Ignore nulls and walls
                    if (neighbor == null || closed.contains(neighbor)) {
                        continue;
                    }
                    // If tile is already in open list, ignore it
                    boolean inOpen = false;
                    for (Node n : open) {
                        if (n.self == neighbor) {
                            inOpen = true;
                        }
                    }
                    Node adjacent = new Node(neighbor);
                    // Otherwise add it as new unexplored node
                    if (!inOpen) {
                        open.add(adjacent);
                    }
                    // If this is a new path or shorter than current, keep it
                    if (a.cost + 1 < adjacent.cost) {
                        adjacent.parent = a;
                        adjacent.cost = a.cost + 1;
                    }
                }
            }
        }
        return null;
    }

    private Node chooseNode(List<Node> open, Node end) {
        double minCost = Double.POSITIVE_INFINITY;
        Node bestNode = null;

        for (Node n : open) {
            double costFromStart = n.cost;
            double costToEnd = estimateDistance(n, end);
            double totalCost = costFromStart + costToEnd;
            if (minCost > totalCost) {
                minCost = totalCost;
                bestNode = n;
            }
        }
        return bestNode;
    }

    private double estimateDistance(Node n, Node end) {
        // Calculates Manhattan distance between nodes
        double xs = (n.self.x - end.self.x) * (n.self.x - end.self.x);
        double ys = (n.self.y - end.self.y) * (n.self.y - end.self.y);
        return Math.sqrt(xs + ys);
    }

    private Stack<Tile> tracePath(Node n) {
        // Returns ordered stack of points along movement path
        Stack<Tile> path = new Stack<Tile>();
        // Chase parent of node until start point reached
        while (n.parent != null) {
            path.push(n.self);
            n = n.parent;
        }
        return path;
    }

    private List<Tile> getCardinalNeighbors(Tile tile) {
        List<Tile> neighbors = new ArrayList<Tile>();
        for (int dx = -1; dx <= 1; dx += 2) {
            if (isOutOfBounds(tile.x + dx, tile.y)) {
                continue;
            }
            neighbors.add(grid[tile.y][tile.x + dx]);
        }
        for (int dy = -1; dy <= 1; dy += 2) {
            if (isOutOfBounds(tile.x, tile.y + dy)) {
                continue;
            }
            neighbors.add(grid[tile.y + dy][tile.x]);
        }
        return neighbors;
    }

    private boolean isOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= columns || y >= rows);
    }
}

