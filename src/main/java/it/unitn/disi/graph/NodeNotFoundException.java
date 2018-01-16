package it.unitn.disi.graph;

public class NodeNotFoundException extends Exception {
    private String name;

    public NodeNotFoundException( String name ){
        this.name = name;
    }

    public String getMessage(){
        return String.format(
            "Node %s not found in graph",
            this.name
        );
    }
}
