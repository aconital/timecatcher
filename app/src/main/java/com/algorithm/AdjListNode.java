package com.algorithm;

public class AdjListNode {
    private int v;// vertex number 
    private int weight;//the weight of an incoming arc ending with vertex v 
    
    AdjListNode(){}
    AdjListNode(int _v, int _w) { v = _v;  weight = _w; }
    int getVertex() { return v; }
    int getWeight()  { return weight; }
}
