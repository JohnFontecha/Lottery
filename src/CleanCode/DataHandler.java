package CleanCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;


public class DataHandler {
	private Graph graph=new Graph();
	private ArrayList<Graph> graphs=new ArrayList<Graph>();
	private ArrayList<Integer> label_graphs=new ArrayList<Integer>();
	private PiecewiseFunction fR;
	private ArrayList<Integer> AdoptersSizes; 
	private ArrayList<ArrayList<Integer>> WinnersSizes;
	private int repetitions;
	private int nNodes;
	private int nEdges;
	private Random rnd;
	public static ZipfDistribution pw;
	public static ChiSquaredDistribution cs;
	public static NormalDistribution norm;
	private ArrayList<Double> PwLwprob= new ArrayList<Double>() ;
	private ArrayList<Double> Chiprob=new ArrayList<Double>();
	private ArrayList<Double> Normprob=new ArrayList<Double>();
	private ArrayList<Integer> ids= new ArrayList<Integer>();

	public DataHandler(Random rnd,String path1,String path2) {
		this.rnd=rnd;
		Reader read=new Reader(path1,path2);
		graph=read.getGrafo();
		fR=read.getPiecewise();
		AdoptersSizes=read.getASizes();
		WinnersSizes=read.getSSizes();
		repetitions=read.getRep();
		nNodes=read.getNodes();
		nEdges=read.getEdges();
		pw =new ZipfDistribution(AdoptersSizes.size(),1.5);
		cs =new ChiSquaredDistribution(AdoptersSizes.size());
		norm=new NormalDistribution(10, 1);
		generateprobs();
	}

	public  void generateprobs() {
		int max = Collections.max(AdoptersSizes);
		double []sum=new double[3];
		sum[0]=0.0;
		sum[1]=0.0;
		sum[2]=0.0;
		double []mineffort= new double[3];
		mineffort[0]=0.0;
		mineffort[1]=0.0;
		mineffort[2]=0.0;
		for(int i=0;i<max;i++) {
			double rand=rnd.nextDouble();
			double effort=pw.inverseCumulativeProbability(rand);
			double rand1=rnd.nextDouble();
			double effort1=cs.inverseCumulativeProbability(rand1);
			double rand2=rnd.nextDouble();
			double effort2=norm.inverseCumulativeProbability(rand2);
			PwLwprob.add(effort);
			Chiprob.add(effort1);
			Normprob.add(effort2);

			if(effort<mineffort[0]) {
				mineffort[0]=effort;
			}
			if(effort1<mineffort[1]) {
				mineffort[1]=effort;
			}
			if(effort2<mineffort[2]) {
				mineffort[2]=effort;
			}
		}

		for(int i=0;i<max;i++) {
			PwLwprob.set(i, PwLwprob.get(i)+Math.abs(mineffort[0]));
			Chiprob.set(i, Chiprob.get(i)+Math.abs(mineffort[1]));
			Normprob.set(i, Normprob.get(i)+Math.abs(mineffort[2]));
			sum[0]=sum[0]+PwLwprob.get(i);
			sum[1]=sum[1]+Chiprob.get(i);
			sum[2]=sum[2]+Normprob.get(i);
		}
		double prof=0;
		for(int i=0;i<max;i++) {
			PwLwprob.set(i, PwLwprob.get(i)*1.0/sum[0]);
			Chiprob.set(i, Chiprob.get(i)*1.0/sum[1]);
			Normprob.set(i, Normprob.get(i)*1.0/sum[2]);
			prof=prof+Normprob.get(i);
		}
//		System.out.println("proof: "+prof);
		
		for(int i=0;i<graph.getNodes().size();i++) {
			if(graph.getNodes().get(i).getN().size()>0) {
				ids.add(graph.getNodes().get(i).getId());
				//				System.out.println("--: "+i+" "+graph.getNodes().get(i).getId());
			}
		}

		for(int j=0;j<repetitions;j++) {
			for(int i=0;i<AdoptersSizes.size();i++) {
				ArrayList<Integer> newids=new ArrayList<Integer>();
				ArrayList<Integer> idscopy=new ArrayList<Integer>(ids);
//				System.out.println(ids);
//				System.out.println(idscopy);
				Graph newgraph=Graph.copyGraph(graph);
				sum[0]=0.0;
				sum[1]=0.0;
				sum[2]=0.0;
				for(int k=0;k<AdoptersSizes.get(i);k++) {
					int nextId=rnd.nextInt(idscopy.size()-1);
					int id=idscopy.get(nextId);
					newids.add(id);
					//					System.out.println("adopt size: "+AdoptersSizes.get(i)+" "+idscopy.size()+" "+ids.size()+" "+idscopy.get(nextId));
					idscopy.remove(nextId);
					sum[0]=sum[0]+PwLwprob.get(k);
					sum[1]=sum[1]+Chiprob.get(k);
					sum[2]=sum[2]+Normprob.get(k);	
				}
//				Graph newgraph=new Graph(graph.getNodes(),graph.getAdopters());
				
				//				newgraph=graph;
//				System.out.println(graph.getAdopters().size()+" "+newgraph.getAdopters().size());
				for(int k=0;k<newids.size();k++) {
					newgraph.getNodes().get(newids.get(k)).setpPwLw(PwLwprob.get(k)/sum[0]);
					newgraph.getNodes().get(newids.get(k)).setpCS(Chiprob.get(k)/sum[1]);
					newgraph.getNodes().get(newids.get(k)).setpNor(Normprob.get(k)/sum[2]);
					Node newAdopter=new Node();
					newAdopter=newgraph.getNodes().get(newids.get(k));
					newgraph.getAdopters().add(newAdopter);
//					System.out.println(newids.get(k));
				}

				graphs.add(newgraph);
				label_graphs.add(i);

			}
		}

		//		System.out.println(graphs.size());

	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public PiecewiseFunction getfR() {
		return fR;
	}

	public void setfR(PiecewiseFunction fR) {
		this.fR = fR;
	}

	public ArrayList<Integer> getAdoptersSizes() {
		return AdoptersSizes;
	}

	public void setAdoptersSizes(ArrayList<Integer> adoptersSizes) {
		AdoptersSizes = adoptersSizes;
	}

	public  ArrayList<ArrayList<Integer>> getWinnersSizes() {
		return WinnersSizes;
	}

	public void setWinnersSizes( ArrayList<ArrayList<Integer>> winnersSizes) {
		WinnersSizes = winnersSizes;
	}

	public ArrayList<Double> getPwLwprob() {
		return PwLwprob;
	}

	public void setPwLwprob(ArrayList<Double> pwLwprob) {
		PwLwprob = pwLwprob;
	}

	public ArrayList<Double> getChiprob() {
		return Chiprob;
	}

	public void setChiprob(ArrayList<Double> chiprob) {
		Chiprob = chiprob;
	}

	public ArrayList<Double> getNormprob() {
		return Normprob;
	}

	public void setNormprob(ArrayList<Double> normprob) {
		Normprob = normprob;
	}

	public int getRepetitions() {
		return repetitions;
	}

	public void setRepetitions(int repetitions) {
		this.repetitions = repetitions;
	}

	public int getnNodes() {
		return nNodes;
	}

	public void setnNodes(int nNodes) {
		this.nNodes = nNodes;
	}

	public int getnEdges() {
		return nEdges;
	}

	public void setnEdges(int nEdges) {
		this.nEdges = nEdges;
	}

	public Random getRnd() {
		return rnd;
	}

	public void setRnd(Random rnd) {
		this.rnd = rnd;
	}

	public static ZipfDistribution getPw() {
		return pw;
	}

	public static void setPw(ZipfDistribution pw) {
		DataHandler.pw = pw;
	}

	public static ChiSquaredDistribution getCs() {
		return cs;
	}

	public static void setCs(ChiSquaredDistribution cs) {
		DataHandler.cs = cs;
	}

	public static NormalDistribution getNorm() {
		return norm;
	}

	public static void setNorm(NormalDistribution norm) {
		DataHandler.norm = norm;
	}

	public ArrayList<Graph> getGraphs() {
		return graphs;
	}

	public void setGraphs(ArrayList<Graph> graphs) {
		this.graphs = graphs;
	}

	public ArrayList<Integer> getIds() {
		return ids;
	}

	public void setIds(ArrayList<Integer> ids) {
		this.ids = ids;
	}

	public ArrayList<Integer> getLabel_graphs() {
		return label_graphs;
	}

	public void setLabel_graphs(ArrayList<Integer> label_graphs) {
		this.label_graphs = label_graphs;
	}
}
