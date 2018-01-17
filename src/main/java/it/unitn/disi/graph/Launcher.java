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
    static String[] nodeNames;
    static ArrayList<String[]> edges = new ArrayList<>();
    static Graph g;

    public void readGraph(){
        URL urlGraph = getClass().getClassLoader().getResource( "g1.graph" );
        Path graphPath = Paths.get( urlGraph.getFile() );

        try( Stream<String> stream = Files.lines(graphPath) ){
            final int[] lines = {0};
            stream.forEach( line ->{
                if( lines[0] == 0 ){
                    nodeNames = line.split( " " );
                }else{
                    edges.add( line.split(" ") );
                }

                lines[0]++;
            } );
        }catch( IOException e ){
            e.printStackTrace();
        }
    }

    public static void Tmain( String...args ){
        Launcher l = new Launcher();
        l.readGraph();

        for( String s : nodeNames )
            System.out.printf("%s ",s );
        System.out.println();

        for( String[] edge: edges ){
            String from = edge[0];
            String to = edge[1];
            System.out.printf("%s -> %s\n", from, to);
        }
    }

    public static void main( String...args ){
        Launcher l = new Launcher();
        l.readGraph();

        g = new Graph();
//        String[] nodeNames = {"A", "B", "C", "D", "E", "F", "G", "H"};
//        String[][] edges = {
//            {"A", "H"},
//            {"A", "F"},
//            {"A", "G"},
//            {"B", "D"},
//            {"B", "H"},
//            {"C", "E"},
//            {"C", "A"},
//            {"D", "F"},
//            {"D", "H"},
//            {"E", "D"},
//            {"F", "F"},
//            {"G", "A"},
//            {"H", "C"},
//        };

        Random r = new Random(System.currentTimeMillis());
        for( String name : nodeNames ) {
            if( name.equals("A") )
                g.addNode(name, 10);
            else{
                g.addNode(name, r.nextInt(10));
            }
        }

        for( String[] edge: edges ){
            String from = edge[0];
            String to = edge[1];
            try {
                g.addEdge( from, to );
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        System.out.println("=== Searching for data: "+data.toString());
        Optional<Node> searchedNode = g.getNodeFromData( data );
        if( searchedNode.isPresent() ){
            System.out.println("Node with searched data: "+searchedNode.get().getName());
        }else{
            System.out.println("No node found");
        }

        String fromNode = "D";
        System.out.println("=== Compute Erdos from: "+fromNode);
        try {
            g.erdos( fromNode );

            g.getNodes().forEach( n -> System.out.printf(
                "%s: %d\n",
                n.getName(), (Integer)n.getProperties( Node.ERDOS )
            ));

            for( String end : nodeNames ) {
                System.out.printf("Walk from %s to %s:\n", fromNode, end);
                System.out.println(g.getWalk(fromNode, end));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
