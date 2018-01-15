package it.unitn.disi.graph;

public class Launcher {
    public static void main( String...args ){
        Graph g = new Graph();

        String[] nodeNames = {"A", "B", "C", "D", "E", "F", "G", "H"};
        for( String name : nodeNames ){
            g.addNode( name, null );
        }

        try {
            g.addNeighbor("A", "H", "F", "G");
            g.addNeighbor("B", "B", "G", "C");
            g.addNeighbor("C", "E", "A");
            g.addNeighbor("D", "F", "H");
            g.addNeighbor("E", "D");
            g.addNeighbor("F", "F");
            g.addNeighbor("G", "A");
            g.addNeighbor("H", "C");
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("List of Nodes:");
        g.getNodes().forEach( node ->
            System.out.println( node.toString() )
        );

        System.out.println("List of Edges:");
        g.getEdges().forEach( edge ->
            System.out.println( edge.toString() )
        );
    }
}
