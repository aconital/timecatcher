package com.algorithm;

public class Acr {
	private int u;
	private int v;
	private int weight;
	
	Acr(){}
	Acr(int _v, int _u, int _weight){
		v=_v;
		u=_u;
		weight=_weight;
	}
	
	
	int getV()	{ return v; }
	int getU()  { return u; }
	int getWeight()  {return weight;}
}
