package com.algorithm;

public class AdjListNode {
    // vertex number
    private final int v;

    //the weight of an incoming arc ending with vertex v
    private final int weight;

    AdjListNode(final int v, final int w) {
        this.v = v;
        this.weight = w;
    }

    int getVertex() {
        return v;
    }

    int getWeight() {
        return weight;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + v;
        result = prime * result + weight;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AdjListNode other = (AdjListNode) obj;
        return v == other.v && weight == other.weight;
    }
}
