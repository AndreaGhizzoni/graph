package it.unitn.disi.graph;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public class Launcher {
    private static String[] nodeNames;
    private static ArrayList<String[]> edges = new ArrayList<>();

    private void readMatrix(){
        URL urlGraph = getClass().getClassLoader().getResource( "g1.matrix" );
        Path graphPath = Paths.get( urlGraph.getFile() );

        final int[] isFirstLine = {0,0};
        try( Stream<String> stream = Files.lines(graphPath) ){
            stream.parallel()
                .filter( s -> !s.isEmpty() )
                .filter( s -> !s.startsWith( "#" ) )
                .forEach( line ->{
                    String[] split = line.split( " " );
                    if( isFirstLine[0] == 0 ){
                        nodeNames = split;
                        isFirstLine[0] = 1;
                    }else{
                        String from = nodeNames[isFirstLine[1]];
                        for( int i=0; i<split.length; i++ ){
                            if( split[i].equals("1") ){
                                edges.add( new String[]{from, nodeNames[i]} );
                            }
                        }
                        isFirstLine[1]++;
                    }
                });
        }catch( IOException e ){
            e.printStackTrace();
        }
    }

    private void readGraph(){
        URL urlGraph = getClass().getClassLoader().getResource( "g1.graph" );
        Path graphPath = Paths.get( urlGraph.getFile() );

        final boolean[] isFirstLine = {true};
        try( Stream<String> stream = Files.lines(graphPath) ){
            stream.parallel()
                .filter( s -> !s.isEmpty() )
                .filter( s -> !s.startsWith( "#" ) )
                .forEach( line ->{
                    if( isFirstLine[0] ){
                        nodeNames = line.split( " " );
                        isFirstLine[0] = false;
                    }else{
                        edges.add( line.split( " " ) );
                    }
                });
        }catch( IOException e ){
            e.printStackTrace();
        }
    }

    public static void main( String...args ){
        new Launcher().readMatrix();
        if( nodeNames.length == 0 ){
            System.err.println( "Node list not found" );
            System.exit( 1 );
        }

        Graph g = new Graph();

        Random r = new Random(System.currentTimeMillis());
        for( String name : nodeNames )
            g.addNode( name, r.nextInt(11) );

        for( String[] edge: edges )
            g.addEdge( edge[0], edge[1] );


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
        for( String end : nodeNames ) {
            System.out.printf("Walk from %s to %s:\n", fromNode, end);
            System.out.println( g.getWalk(fromNode, end) );
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
