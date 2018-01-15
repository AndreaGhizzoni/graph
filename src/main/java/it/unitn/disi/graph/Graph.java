package it.unitn.disi.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Graph {
    private ArrayList<Node> V = new ArrayList<>();
    private ArrayList<Edge> E = new ArrayList<>();

    public void addNode( String name, Object data ){
        V.add( new Node(name, data) );
    }

    public void addNeighbor( String name, String...neighbors ) throws Exception {
        Node node = searchNodeForNameOrThrow( name );

        ArrayList<Node> neighborsNode = new ArrayList<>();
        for( String neighbor : neighbors ){
            neighborsNode.add( searchNodeForNameOrThrow( neighbor ) );
        }

        for( Node n : neighborsNode ){
            node.addNeighbor( n );
            E.add( new Edge(node, n) );
        }
    }

    private Node searchNodeForNameOrThrow( String name ) throws Exception {
        Optional<Node> optNode = V.stream()
            .filter(n -> n.getName().equals(name))
            .findFirst();

        if( !optNode.isPresent() ) {
            throw new Exception(
                String.format("Neighbor %s not found in graph", name)
            );
        }
        return optNode.get();
    }

    public List<Node> getNodes(){
        return Collections.unmodifiableList( this.V );
    }

    public List<Edge> getEdges(){
        return Collections.unmodifiableList( this.E );
    }
}
