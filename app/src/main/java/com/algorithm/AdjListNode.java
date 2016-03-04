package com.algorithm;

public class AdjListNode {
    private int v;// vertex number 
    private int weight;//the weight of an incoming arc ending with vertex v 
    
    AdjListNode(int _v, int _w) { v = _v;  weight = _w; }
    int getVertex() { return v; }
    int getWeight()  { return weight; }
    
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AdjListNode other = (AdjListNode) obj;
		if (v != other.v)
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}
    
    
}
