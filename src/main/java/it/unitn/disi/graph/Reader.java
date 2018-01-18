package it.unitn.disi.graph;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

public class Reader {
    private String fileName;

    private String[] nodeNames = {};
    private ArrayList<String[]> edges = new ArrayList<>();

    public Reader( String fileName ){
        this.fileName = fileName;
    }

    public Graph readMatrix() throws IOException {
        URL urlGraph = getClass().getClassLoader().getResource( this.fileName );
        Path graphPath = Paths.get( urlGraph.getFile() );

        final int[] isFirstLine = {0,0};
        Stream<String> stream = Files.lines(graphPath);
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

        return create();
    }

    public Graph readGraph() throws IOException {
        URL urlGraph = getClass().getClassLoader().getResource( this.fileName );
        Path graphPath = Paths.get( urlGraph.getFile() );

        final boolean[] isFirstLine = {true};
        Stream<String> stream = Files.lines(graphPath);
        stream.parallel()
            .filter( s -> !s.isEmpty() )
            .filter( s -> !s.startsWith( "#" ) )
            .forEach( line ->{
                String[] split = line.split( " " );
                if( isFirstLine[0] ){
                    nodeNames = split;
                    isFirstLine[0] = false;
                }else{
                    edges.add( split );
                }
            });

        return create();
    }

    private Graph create(){
        Graph g = new Graph();

        Random r = new Random(System.currentTimeMillis());
        for( String name : nodeNames )
            g.addNode( name, r.nextInt(11) );

        for( String[] edge: edges )
            g.addEdge( edge[0], edge[1] );

        return g;
    }
}
