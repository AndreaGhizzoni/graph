package it.unitn.disi.graph;

import java.util.*;

public class Graph {
    private HashSet<Node> V = new HashSet<>();
    private HashSet<Edge> E = new HashSet<>();

    public void addNode( String name, Object data ){
        V.add( new Node(name, data) );
    }

    public void addEdge( String nameFrom, String nameTo ) throws Exception {
        Node from = getNodeFromName( nameFrom ).orElseThrow(
            () -> new NodeNotFoundException( nameFrom )
        );
        Node to = getNodeFromName( nameTo ).orElseThrow(
            () -> new NodeNotFoundException( nameTo )
        );

        from.addNeighbor( to );
        E.add( new Edge(from, to) );
    }

    public void erdos( String from ) throws Exception {
        Node fromNode = getNodeFromName( from ).orElseThrow(
            () -> new NodeNotFoundException( from )
        );

        LinkedList<Node> queue = new LinkedList<>();
        queue.add( fromNode );
        fromNode.addProperties( Node.ERDOS, 0 );
        fromNode.addProperties( Node.FATHER, null );
        while( !queue.isEmpty() ){
            Node currentNode = queue.remove();
            Integer erdosCN = (Integer)currentNode.getProperties( Node.ERDOS );
            for( Node neighbor : currentNode.getNeighbors() ){
                Integer erdosNeighbor = (Integer) neighbor.getProperties( Node.ERDOS );
                if( erdosNeighbor.equals(-1) ){
                    neighbor.addProperties( Node.ERDOS, erdosCN+1 );
                    neighbor.addProperties( Node.FATHER, currentNode );
                    queue.add( neighbor );
                }
            }
        }
    }

    public Optional<Node> getNodeFromName( String name ){
        Optional<Node> optNodeFound = Optional.empty();
        for( Node v : V ){
            if( !(boolean)v.getProperties(Node.VISITED) ) {
                Optional<Node> opt = getFromProperties( Node.NAME, name, v );
                if( opt.isPresent() ) optNodeFound = opt;
            }
        }

        resetVisited();
        return optNodeFound;
    }

    public Optional<Node> getNodeFromData( Object data ){
        Optional<Node> optNodeFound = Optional.empty();
        for( Node v : V ){
            if( !(boolean)v.getProperties(Node.VISITED) ) {
                Optional<Node> opt = getFromProperties( Node.DATA, data, v );
                if( opt.isPresent() ) optNodeFound = opt;
            }
        }

        resetVisited();
        return optNodeFound;
    }

    private Optional<Node> getFromProperties( String key, Object value, Node start ){
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

    private void resetErdos(){
        V.forEach( v -> {
            v.addProperties(Node.ERDOS, -1);
            v.addProperties(Node.FATHER, null);
        });
    }

    public String getWalk( String start, String end ) throws Exception {
        Node s = getNodeFromName( start ).orElseThrow(
            () -> new NodeNotFoundException( start )
        );
        Node e = getNodeFromName( end ).orElseThrow(
            () -> new NodeNotFoundException( end )
        );
        StringBuilder b = new StringBuilder();
        getWalk( s, e, b );
        return b.toString();
    }

    private void getWalk( Node start, Node end, StringBuilder b ){
        if( start.equals(end) ){
            b.append( start.getName()+" " );
        }else if( end != null && end.getProperties( Node.FATHER ) == null ){
            b.append( "No walk from start to end" );
        }else{
            getWalk( start, (Node)end.getProperties(Node.FATHER), b );
            b.append( end.getName()+" " );
        }
    }

    public Set<Node> getNodes(){
        return Collections.unmodifiableSet( this.V );
    }

    public Set<Edge> getEdges(){
        return Collections.unmodifiableSet( this.E );
    }
}
