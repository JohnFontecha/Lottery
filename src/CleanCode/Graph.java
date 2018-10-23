package CleanCode;

import java.util.ArrayList;

public class Graph {
	//	private String label;
	private ArrayList<Node> nodes; 		//total nodes
	private ArrayList<Node> adopters;	//only adopters
	/**
	 * This method constructs the graph
	 * @param nNodes set of nodes
	 * @param nAdopters Set of adopters
	 */
	public Graph() {
		nodes=new ArrayList<Node>();
		adopters=new ArrayList<Node>();
		//		label="Na";
	}

	public Graph(ArrayList<Node> nNodes, ArrayList<Node> nAdopters) {
		this.nodes=nNodes;
		this.adopters=nAdopters;
		//		this.label=lab;
	}
	/**
	 * This method adds a node to the set of nodes
	 * @param n
	 */
	public void addNode(Node n) {
		nodes.add(n);
	}
	/**
	 * This method adds an adpoter to the set of nodes and to the set of adopters
	 * @param n
	 */
	public void addAdopter(Node n) {
		adopters.add(n);
	}
	/**
	 * This method returns the set of nodes
	 * @return nodes
	 */
	public ArrayList<Node> getNodes() {
		return nodes;
	}
	/**
	 * This method sets the set of nodes
	 * @param nodes
	 */
	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}
	/**
	 * This method returns the set of adopters
	 * @return adopters
	 */
	public ArrayList<Node> getAdopters() {
		return adopters;
	}
	/** 
	 * This method sets the set of adopters
	 * @param adopters
	 */
	public void setAdopters(ArrayList<Node> adopters) {
		this.adopters = adopters;
	}

	public static Graph copyGraph (Graph g){
		Graph f = new Graph();
		for(int i=0;i<g.getNodes().size();i++) {
			Node node=new Node(g.getNodes().get(i).getId(),g.getNodes().get(i).getpPwLw(),g.getNodes().get(i).getpCS(),g.getNodes().get(i).getpNor(),g.getNodes().get(i).getN(),g.getNodes().get(i).getC());
			f.addNode(node);
		}
		return f;
	}
}

