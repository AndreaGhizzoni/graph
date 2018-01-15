package it.unitn.disi.graph;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {
    private String name;
    private ArrayList<Node> neighbors = new ArrayList<>();

    public static final String DATA="data";
    public static final String VISITED="visited";
    private HashMap<String, Object> properties = new HashMap<>();

    Node( String name, Object data ){
        this.setName( name );
        this.addProperties(DATA, data);
        this.addProperties(VISITED, false);
    }

    void setName(String name) { this.name = name; }

    public void addNeighbor( Node node ){
        this.neighbors.add( node );
    }

    public void addProperties( String key, Object value ){
        this.properties.put( key, value );
    }

    public Object getProperites( String key ){
        return this.properties.getOrDefault( key, new Object() );
    }

    public String getName() { return name; }

    public ArrayList<Node> getNeighbors() { return neighbors; }

    @Override
    public String toString(){
        StringBuilder b = new StringBuilder();
        b.append("- ").append(this.name).append(" -> [ ");
        for( Node n : neighbors )
            b.append(n.name).append(" ");
        b.append("]");
        return b.toString();
    }
}
