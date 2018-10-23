package CleanCode;

import java.util.ArrayList;

public class Node {
	private int id;					//node's identifier
	private double pPwLw;			//node's probability
	private double pCS;				//node's probability
	private double pNorm;			//node's probability
	private ArrayList<Integer> N;	//node's neighborhood (neighbors' id)
	private double c;				//consumption/saving
	/**
	 * This method constructs a node object
	 * @param nid 	node's identifier	
	 * @param np 	node's probability
	 * @param nN 	node's neighborhood (neighbors' id)
	 * @param nc	consumption/saving
	 */
	public Node() {

	}
	public Node(int nid,double npPwLw, double npCS, double npNorm, ArrayList<Integer> nN, double nc) {
		this.id=nid;
		this.pPwLw=npPwLw;
		this.pCS=npCS;
		this.pNorm=npNorm;
		this.N=nN;
		this.c=nc;
	}
	/**
	 * This method returns the node's identifier
	 * @return id
	 */
	public int getId() {
		return id;
	}
	/**
	 * This method sets the node's identifier
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * This method returns the node's neighborhood
	 * @return N
	 */
	public ArrayList<Integer> getN() {
		return N;
	}
	/**
	 * This method sets the node's neighborhood
	 * @param N
	 */
	public void setN(ArrayList<Integer> n) {
		N = n;
	}
	/**
	 * This method returns the node's consumption
	 * @return c
	 */
	public double getC() {
		return c;
	}
	/**
	 * This method sets the node's consumption
	 * @param c
	 */
	public void setC(double c) {
		this.c = c;
	}
	public double getpPwLw() {
		return pPwLw;
	}
	public void setpPwLw(double pPwLw) {
		this.pPwLw = pPwLw;
	}
	public double getpCS() {
		return pCS;
	}
	public void setpCS(double pCS) {
		this.pCS = pCS;
	}
	public double getpNor() {
		return pNorm;
	}
	public void setpNor(double pNor) {
		this.pNorm = pNor;
	}	              
}

