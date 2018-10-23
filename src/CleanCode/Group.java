package CleanCode;

import java.util.ArrayList;

public class Group {
	private ArrayList<Integer> nodes;	//Nodes that belong to the group
	private ArrayList<Double> a;		//Coeficiente en la columna para dicho grupo
	private double cost;			//cost of the group
	private double prob;
	
	/**
	 * This method constructs a group
	 * @param nNodes set the nodes in the group 
	 * @par(0, 1, K.getGroups().iterator().ram nCost cost of (expected number of new adopters) the group
	 */
	public Group() {
		nodes=new ArrayList<Integer> ();
		a=new ArrayList<Double> ();
		cost=0.0;
		prob=0.0;
	}
	public Group(ArrayList<Integer> nNodes,ArrayList<Double> a, double nCost, double prob) {//
		this.nodes=nNodes; 
		this.cost=nCost;
		this.a=a;
		this.prob=prob;
	}
	/**
	 * Add node to the set of nodes of the group
	 * @param n node
	 */
	public void addA(double na) {
		a.add(na);
	}
	
	/**
	 * Add node to the set of nodes of the group
	 * @param n node
	 */
	public void addNode(int n) {
		nodes.add(n);
	}
	/**
	 * This method returns the set of nodes of the group
	 * @return nodes
	 */
	public ArrayList<Integer> getNodes() {
		return nodes;
	}
	/**
	 * This method sets the set of nodnodees of the group
	 * @param nodes
	 */
	public void setNodes(ArrayList<Integer> nodes) {
		this.nodes = nodes;
	}
	/**
	 * This method returns the cost of the group
	 * @return cost
	 */
	public double getCost() {
		return cost;
	}
	/**
	 * This method sets the cost of the group
	 * @param cost
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}
	public ArrayList<Double> getA() {
		return a;
	}
	public void setA(ArrayList<Double> a) {
		this.a = a;
	}
	public double getProb() {
		return prob;
	}
	public void setProb(double prob) {
		this.prob = prob;
	}
}

