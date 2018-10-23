package CleanCode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Reader {
	private BufferedReader in;
	private Graph grafo=new Graph();
	private PiecewiseFunction piecewise=new PiecewiseFunction();
	private int nodes; 
	private int edges;
	private int maxid;
	private int rep;
	private int As;
	private int Ss;
	private ArrayList<Integer> ASizes=new ArrayList<Integer>();
	private ArrayList<ArrayList<Integer>> SSizes=new ArrayList<ArrayList<Integer>>();


//	private DataHandler data=new DataHandler();
//	public static ZipfDistribution
	/**
	 * This method reads the data and generates the graph
	 * @param path1 to the file
	 */
	public Reader(String path1,String path2){
		try{
			int id1;
			int id2;
			int i=0;			
			in = new BufferedReader(new FileReader(path1));
			String line;
			i=0;
			while((line=in.readLine())!=null){
				String [] list= line.split(" ");
				if(i==0){
					nodes=Integer.parseInt(list[0]);
					edges=Integer.parseInt(list[1]);
					maxid=Integer.parseInt(list[2])+1;			
					for(int j=0;j<maxid;j++) {				
						ArrayList<Integer> neigbors=new ArrayList<Integer>();
						Node h=new Node(j,0,0,0,neigbors,0);
						grafo.getNodes().add(h);
					}
					i++;
				} else if(i==1){ 
					rep=Integer.parseInt(list[0]);
					As=Integer.parseInt(list[1]);
					Ss=Integer.parseInt(list[2]);
					for(int j=0;j<As;j++){
						ASizes.add(Integer.parseInt(list[3+j]));
						ArrayList<Integer> sizes=new ArrayList<Integer>();
						for(int k=0;k<Ss;k++) {
							sizes.add(Integer.parseInt(list[3+As+Ss*j+k]));
						}
						SSizes.add(sizes);
					}
					i++;
				} else{
					id1=Integer.parseInt(list[0]);
					id2=Integer.parseInt(list[1]);	
					grafo.getNodes().get(id1).getN().add(id2);
					grafo.getNodes().get(id2).getN().add(id1);
					i++;
				}
			}

			for(int j=0;j<grafo.getNodes().size();j++) {
				Set<Integer> hs=new HashSet<Integer>();
				hs.addAll(grafo.getNodes().get(j).getN());
				//				System.out.println(hs);
				grafo.getNodes().get(j).getN().clear();
				grafo.getNodes().get(j).getN().addAll(hs);
			}


			in = new BufferedReader(new FileReader(path2));
			i=0;
			double m;
			double b;
			while((line=in.readLine())!=null){
				String [] list= line.split(" ");
				Line linea=new Line();
				m=Double.parseDouble(list[0]);
				b=Double.parseDouble(list[1]);
				linea.setM(m);
				linea.setB(b);
				piecewise.addLine(linea);
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * This method returns the graph
	 * @return graph
	 */
	public Graph getGrafo() {
		return grafo;
	}
	/**
	 * This method sets the graph
	 * @param graph
	 */
	public void setGrafo(Graph grafo) {
		this.grafo = grafo;
	}
	public PiecewiseFunction getPiecewise() {
		return piecewise;
	}
	public void setPiecewise(PiecewiseFunction piecewise) {
		this.piecewise = piecewise;
	}
	public BufferedReader getIn() {
		return in;
	}
	public void setIn(BufferedReader in) {
		this.in = in;
	}
	public int getNodes() {
		return nodes;
	}
	public void setNodes(int nodes) {
		this.nodes = nodes;
	}
	public int getEdges() {
		return edges;
	}
	public void setEdges(int edges) {
		this.edges = edges;
	}
	public int getMaxid() {
		return maxid;
	}
	public void setMaxid(int maxid) {
		this.maxid = maxid;
	}
	public int getRep() {
		return rep;
	}
	public void setRep(int rep) {
		this.rep = rep;
	}
	public int getAs() {
		return As;
	}
	public void setAs(int as) {
		As = as;
	}
	public int getSs() {
		return Ss;
	}
	public void setSs(int ss) {
		Ss = ss;
	}
	public ArrayList<Integer> getASizes() {
		return ASizes;
	}
	public void setASizes(ArrayList<Integer> aSizes) {
		ASizes = aSizes;
	}
	public ArrayList<ArrayList<Integer>> getSSizes() {
		return SSizes;
	}
	public void setSSizes(ArrayList<ArrayList<Integer>> sSizes) {
		SSizes = sSizes;
	}
}
