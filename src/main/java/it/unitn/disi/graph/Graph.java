package it.unitn.disi.graph;

import java.util.*;

public class Graph {
    private HashSet<Node> V = new HashSet<>();
    private HashSet<Edge> E = new HashSet<>();

    public void addNode( String name, Object data ){
        V.add( new Node(name, data) );
    }

    public void addEdge( String nameFrom, String nameTo ) throws Exception {
        Node from = searchForName( nameFrom ).orElseThrow(
            () -> new NodeNotFoundException( nameFrom )
        );
        Node to = searchForName( nameTo ).orElseThrow(
            () -> new NodeNotFoundException( nameTo )
        );

        from.addNeighbor( to );
        E.add( new Edge(from, to) );
    }

    public Optional<Node> searchForName( String name ){
        Optional<Node> optNodeFound = Optional.empty();
        for( Node v : V ){
            if( !(boolean)v.getProperties(Node.VISITED) ) {
                Optional<Node> opt = searchForProperties( Node.NAME, name, v );
                if( opt.isPresent() ) optNodeFound = opt;
            }
        }

        resetVisited();
        return optNodeFound;
    }

    public Optional<Node> searchForData( Object data ){
        Optional<Node> optNodeFound = Optional.empty();
        for( Node v : V ){
            if( !(boolean)v.getProperties(Node.VISITED) ) {
                Optional<Node> opt = searchForProperties( Node.DATA, data, v );
                if( opt.isPresent() ) optNodeFound = opt;
            }
        }

        resetVisited();
        return optNodeFound;
    }

    private Optional<Node> searchForProperties( String key, Object value, Node start ){
        Optional<Node> optNodeFound = Optional.empty();

        LinkedList<Node> queue = new LinkedList<>();
        start.addProperties( Node.VISITED, true );
        queue.add( start );
        while( !queue.isEmpty() && !optNodeFound.isPresent() ){
            Node currentNode = queue.remove();
            Object currentNodeValue = currentNode.getProperties( key );
            if( currentNodeValue != null  && currentNodeValue.equals(value) ) {
                optNodeFound = Optional.of( currentNode );
            }else{
                for( Node neighbor : currentNode.getNeighbors() ){
                    if( !(boolean)neighbor.getProperties(Node.VISITED) ) {
                        neighbor.addProperties( Node.VISITED, true );
                        queue.add(neighbor);
                    }
                }
            }
        }

        return optNodeFound;
    }

    private void resetVisited(){
        V.forEach( v -> v.addProperties(Node.VISITED, false) );
    }

    public Set<Node> getNodes(){
        return Collections.unmodifiableSet( this.V );
    }

    public Set<Edge> getEdges(){
        return Collections.unmodifiableSet( this.E );
    }
}
