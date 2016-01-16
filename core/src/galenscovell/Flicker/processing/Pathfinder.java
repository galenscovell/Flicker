package galenscovell.flicker.processing;

import galenscovell.flicker.util.Constants;
import galenscovell.flicker.world.Tile;

import java.util.*;

public class Pathfinder {

    public Stack<Point> findPath(Tile startTile, Tile endTile, Repository repo) {
        List<Node> openList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();
        Node startNode = new Node(startTile);
        Node endNode = new Node(endTile);

        startNode.setCostFromStart(0);
        startNode.setTotalCost(startNode.getCostFromStart() + heuristic(startNode, endNode));
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

                // Force taxicab/manhattan pathfinding
                if (!Constants.DIAGONAL_MOVEMENT) {
                    Tile currentTile = current.getTile();
                    int diffX = Math.abs(currentTile.x - neighborTile.x);
                    int diffY = Math.abs(currentTile.y - neighborTile.y);

                    if (diffX > 0 && diffY > 0) {
                        continue;
                    }
                }

                if (neighborTile != null && !neighborTile.isWater() && !neighborTile.isBlocking()) {
                    Node neighborNode = new Node(neighborTile);

                    if (!inList(neighborTile, closedList)) {
                        neighborNode.setTotalCost(current.getCostFromStart() + heuristic(neighborNode, endNode));

                        if (!inList(neighborTile, openList)) {
                            neighborNode.setParent(current);
                            openList.add(neighborNode);
                        } else {
                            if (neighborNode.getCostFromStart() < current.getCostFromStart()) {
                                neighborNode.setCostFromStart(neighborNode.getCostFromStart());
                                neighborNode.setParent(neighborNode.getParent());
                            }
                        }
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
            double totalCost = node.getCostFromStart() + heuristic(node, endNode);
            if (minCost > totalCost) {
                minCost = totalCost;
                bestNode = node;
            }
        }
        return bestNode;
    }

    private double heuristic(Node start, Node end) {
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
