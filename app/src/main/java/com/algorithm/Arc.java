package com.algorithm;

public class Arc {
	private int u;
	private int v;
	private int weight;
	
	Arc(){}
	Arc(int _v, int _u, int _weight){
		v=_v;
		u=_u;
		weight=_weight;
	}
	
	
	int getV()	{ return v; }
	int getU()  { return u; }
	int getWeight()  {return weight;}
}
