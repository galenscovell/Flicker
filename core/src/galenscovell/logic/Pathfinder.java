package galenscovell.logic;

import galenscovell.util.Constants;

import java.util.*;

/**
 * PATHFINDER
 * Takes in particular behavior arguments and utilizes A* algorithm for entity movement.
 *
 * @author Galen Scovell
 */

public class Pathfinder {

    private class Node {
        Node parent;
        Tile self;

        public Node(Node p, Tile s) {
            this.parent = p;
            this.self = s;
        }
    }

    public void findPath(Map<Integer, Tile> tiles, Tile start, Tile end) {
        List<Node> open = new ArrayList<Node>();
        List<Tile> closed = new ArrayList<Tile>();
        // Add starting point with no parent to open list
        open.add(new Node(null, start));
        // As long as there are open Nodes in the open list...
        while (!open.isEmpty()) {
            // Choose a node to analyze
            Node a = open.get(0);
            // If node tile is end tile, trace path and finish
            if (a.self == end) {
                tracePath(a);
            } else {
                // Otherwise analyze all adjacent tiles for chosen node
                for (Point point : a.self.getNeighbors()) {
                    Tile neighbor = tiles.get(point.x * Constants.COLUMNS + point.y);
                    // If node tile is unwalkable, ignore it
                    if (neighbor == null || neighbor.isWall() || neighbor.isOccupied()|| neighbor.isBlocking() || neighbor.isWater() || closed.contains(neighbor)) {
                        continue;
                    }
                    // If tile is already in open list, ignore it
                    boolean inOpen = false;
                    for (Node n : open) {
                        if (n.self == neighbor) {
                            inOpen = true;
                        }
                    }
                    // Otherwise add it as new unexplored node with current node as parent
                    if (!inOpen) {
                        open.add(new Node(a, neighbor));
                    }
                }
            }
            // Remove current node from open list and add its tile to closed list
            open.remove(a);
            closed.add(a.self);
        }
    }

    private void tracePath(Node n) {
        // Chase parent of node until start point reached
        while (n != null) {
            n.self.toggleSelected();
            n = n.parent;
        }
    }
}
