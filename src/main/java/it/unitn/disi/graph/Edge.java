package it.unitn.disi.graph;

public class Edge {
    private Node from;
    private Node to;

    public Edge( Node from, Node to ){
        this.from = from;
        this.to = to;
    }

    public Node getFrom() { return from; }

    public Node getTo() { return to; }

    public void setFrom(Node from) { this.from = from; }

    public void setTo(Node to) { this.to = to; }

    public String toString(){
        StringBuilder b = new StringBuilder();
        b.append(getFrom().getName()).append(" -> ").append(getTo().getName());
        return b.toString();
    }
}
