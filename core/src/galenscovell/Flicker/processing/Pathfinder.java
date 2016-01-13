package galenscovell.flicker.processing;

import galenscovell.flicker.world.Tile;

import java.util.*;

public class Pathfinder {

    public Stack<Point> findPath(Tile startTile, Tile endTile, Repository repo) {
        List<Node> openList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();
        Node startNode = new Node(startTile);
        startNode.setCost(0);
        Node endNode = new Node(endTile);
        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node current = getBestNode(openList, endNode);

            if (current.getTile() == endNode.getTile()) {
                return tracePath(current);
            }

            openList.remove(current);
            closedList.add(current);

            for (Point point : current.getTile().getNeighbors()) {
                Tile neighborTile = repo.findTile(point.x, point.y);

                if (neighborTile != null && !neighborTile.isWater() && !neighborTile.isBlocking()) {
                    Node neighborNode = new Node(neighborTile);

                    if (inList(neighborTile, openList) || inList(neighborTile, closedList)) {
                        continue;
                    } else {
                        openList.add(neighborNode);
                    }
                    // If this is a new path or shorter than current, keep it
                    if (current.getCost() + 1 < neighborNode.getCost()) {
                        neighborNode.setParent(current);
                        neighborNode.setCost(current.getCost() + 1);
                    }
                }
            }
        }
        return null;
    }

    private boolean inList(Tile nodeTile, List<Node> nodeList) {
        for (Node node : nodeList) {
            if (node.getTile() == nodeTile) {
                return true;
            }
        }
        return false;
    }

    private Node getBestNode(List<Node> openList, Node endNode) {
        double minCost = Double.POSITIVE_INFINITY;
        Node bestNode = null;

        for (Node node : openList) {
            double costFromStart = node.getCost();
            double costToEnd = estimateDistance(node, endNode);
            double totalCoat = costFromStart + costToEnd;
            if (minCost > totalCoat) {
                minCost = totalCoat;
                bestNode = node;
            }
        }
        return bestNode;
    }

    private double estimateDistance(Node start, Node end) {
        // Euclidean distance between nodes
        double xs = (start.getTile().x - end.getTile().x) * (start.getTile().x - end.getTile().x);
        double ys = (start.getTile().y - end.getTile().y) * (start.getTile().y - end.getTile().y);
        return Math.sqrt(xs + ys);
    }

    private Stack<Point> tracePath(Node node) {
        // Returns ordered stack of points along movement path
        Stack<Point> path = new Stack<Point>();
        // Chase parent of node until start point reached
        while (node.getParent() != null) {
            path.push(new Point(node.getTile().x, node.getTile().y));
            node = node.getParent();
        }
        return path;
    }
}
