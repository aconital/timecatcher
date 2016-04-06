package com.algorithm;


/*
 * directed Arc u->v
 */
public class Arc {
	private final int u;
	private final int v;
	private final int weight;
	
	Arc(final int u, final int v, final int weight) {
		this.u = u;
		this.v = v;
		this.weight = weight;
	}
	
	int getV() {
		return v;
	}
	int getU() {
		return u;
	}
	int getWeight() {
		return weight;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + u;
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
		final Arc other = (Arc) obj;
		return u == other.u && v == other.v && weight == other.weight;
	}
}
