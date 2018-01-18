package it.unitn.disi.graph;

import java.io.IOException;
import java.util.Optional;

public class Launcher {
    public static void main( String...args ){
        Reader graphReader = new Reader( "g1.matrix" );
        Graph g = null;
        try{
            g = graphReader.readMatrix();
        }catch( IOException e ){
            System.err.println( e.getMessage() );
            System.exit( 1 );
        }

        if( g.getNodes().size() == 0 ){
            System.err.println( "Node list not found" );
            System.exit( 1 );
        }

        System.out.println("=== List of Nodes:");
        g.getNodes().forEach( node ->
            System.out.println( node.toString() )
        );

        System.out.println("=== List of Edges:");
        g.getEdges().forEach( edge ->
            System.out.println( edge.toString() )
        );

        Object data = 10;
        System.out.println("=== Searching node with data: "+data.toString());
        Optional<Node> searchedNode = g.getNodeFromData( data );
        if( searchedNode.isPresent() ){
            Node n = searchedNode.get();
            System.out.println("Node with selected data: "+n.getName());
        }else{
            System.out.println("No node found");
        }

        String fromNode = "D";
        System.out.println("=== Compute Erdos from: "+fromNode);
        g.erdos( fromNode );
        g.getNodes().forEach( n -> System.out.printf(
            "%s: %d\n",
            n.getName(), (Integer)n.getProperties( Node.ERDOS )
        ));
        for( Node end : g.getNodes() ) {
            System.out.printf("Walk from %s to %s:\n", fromNode, end.getName());
            System.out.println( g.getWalk(fromNode, end.getName()) );
        }

        System.out.println("=== Compute Connected Components");
        Integer ccFound = g.computeConnectedComponents();
        System.out.printf("Found %d connected components:\n",ccFound);
        g.getNodes().forEach( node ->
            System.out.printf(
                "cc(%s) = %d\n",
                node.getName(),
                (Integer)node.getProperties( Node.CC_ID )
            )
        );
    }
}
