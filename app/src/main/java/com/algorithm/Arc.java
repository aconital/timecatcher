package com.algorithm;

public class Arc {
	private int u;
	private int v;
	private int weight;
	
	Arc(int _v, int _u, int _weight){
		v=_v;
		u=_u;
		weight=_weight;
	}
	
	Arc(Arc a){
		v=a.getV();
		u=a.getU();
		weight=a.getWeight();
	}
	
	int getV()	{ return v; }
	int getU()  { return u; }
	int getWeight()  {return weight;}
	
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Arc other = (Arc) obj;
		if (u != other.u)
			return false;
		if (v != other.v)
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}
	
	

}
