package it.unitn.disi.graph;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {
    private ArrayList<Node> neighbors = new ArrayList<>();

    public static final String DATA="data";
    public static final String VISITED="visited";
    public static final String NAME="name";
    private HashMap<String, Object> properties = new HashMap<>();

    Node( String name, Object data ){
        this.setName( name );
        this.addProperties(DATA, data);
        this.addProperties(VISITED, false);
    }

    void setName(String name) { this.addProperties(NAME, name); }

    public void addNeighbor( Node node ){
        this.neighbors.add( node );
    }

    public void addProperties( String key, Object value ){
        this.properties.put( key, value );
    }

    public Object getProperties(String key ){
        return this.properties.getOrDefault( key, new Object() );
    }

    public String getName() { return (String)getProperties(NAME); }

    public ArrayList<Node> getNeighbors() { return neighbors; }

    @Override
    public String toString(){
        StringBuilder b = new StringBuilder();
        b.append("- ").append( getProperties(NAME) );
        b.append("(").append(String.format("%5s", getProperties(DATA))).append(")");

        b.append(" -> [ ");
        for( Node n : neighbors )
            b.append( n.getProperties(NAME) ).append(" ");
        b.append("]");
        return b.toString();
    }
}
