import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import CleanCode.ColumnGeneration;
import CleanCode.Combination;
import CleanCode.DataHandler;
import CleanCode.Permutation;
import CleanCode.Simulation;

public class Solution {
	//	public static int [] Instances=new int [] {1,2,3,4,5,6,7,8,9,10};
	public static String [] Distributions=new String [] {"PwLw","CS","Norm"};
	public static String Path="./data/instances/";
	public static String Lines1="./data/Lines/I_Lines_1.dat";
	public static Random randomNumbers=new Random(13);
	private static PrintWriter writer;
	private static Combination comb;
	private static Permutation perm;
	private static Simulation sim;
	private static ColumnGeneration col;
	/**
	 * This is the main
	 * @param args
	 */
	public static void main(String[] args) {
		int myi=Integer.parseInt(args[0]);
		int myj=Integer.parseInt(args[1]);

//		int myi=5;
//		int myj=1;
		
		//		for(int i=0;i<Instances.length;i++) {//Instances.length
		DataHandler data=new DataHandler(randomNumbers, Path+myi+".dat", Lines1);
		String rutaF = "./out/Final/";
		File directorioFacturas = new File(rutaF);
		if(!directorioFacturas.exists())
			directorioFacturas.mkdirs();

		File file = new File(rutaF+"Instance_"+myi+"_"+Distributions[myj]+".dat");

		try {
			writer = new PrintWriter(new FileWriter(file,false));
		} catch (IOException e) {
			e.printStackTrace();
		}		

		for(int k=0;k<data.getGraphs().size();k++) {
			for(int l=0;l<data.getWinnersSizes().get(data.getLabel_graphs().get(k)).size();l++) {
				//					for(int j=0;j<Distributions.length;j++) {
				writer.println();
				writer.print(myi+" "+data.getGraphs().get(k).getNodes().size()+" "+data.getGraphs().get(k).getAdopters().size()+" "+data.getWinnersSizes().get(data.getLabel_graphs().get(k)).get(l)+" "+Distributions[myj]+" ");						
				//						System.out.println();
			//	System.out.print(myi+" "+data.getGraphs().get(k).getNodes().size()+" "+data.getGraphs().get(k).getAdopters().size()+" "+data.getWinnersSizes().get(data.getLabel_graphs().get(k)).get(l)+" "+Distributions[myj]+" ");						
				comb=new Combination(myi, data.getGraphs().get(k), data.getWinnersSizes().get(data.getLabel_graphs().get(k)).get(l), Distributions[myj],writer);
				writer.print(" | ");
				//						System.out.print(" | ");
				perm=new Permutation(myi, data.getGraphs().get(k), data.getWinnersSizes().get(data.getLabel_graphs().get(k)).get(l), Distributions[myj],writer);
				writer.print(" | ");
				//						System.out.print(" | ");
				sim=new Simulation(data.getGraphs().get(k),Distributions[myj],data.getWinnersSizes().get(data.getLabel_graphs().get(k)).get(l),data.getfR(),writer,randomNumbers);
				writer.print(" | ");
				//						System.out.print(" | ");
				col =new ColumnGeneration(data.getGraphs().get(k),Distributions[myj],data.getWinnersSizes().get(data.getLabel_graphs().get(k)).get(l),data.getfR(),writer,randomNumbers);

				//					}
			}
		}

		writer.close();
		//		}
	}
	public static String[] getDistributions() {
		return Distributions;
	}
	public static void setDistributions(String[] distributions) {
		Distributions = distributions;
	}
	public static String getPath() {
		return Path;
	}
	public static void setPath(String path) {
		Path = path;
	}
	public static String getLines1() {
		return Lines1;
	}
	public static void setLines1(String lines1) {
		Lines1 = lines1;
	}
	public static Random getRandomNumbers() {
		return randomNumbers;
	}
	public static void setRandomNumbers(Random randomNumbers) {
		Solution.randomNumbers = randomNumbers;
	}
	public static PrintWriter getWriter() {
		return writer;
	}
	public static void setWriter(PrintWriter writer) {
		Solution.writer = writer;
	}
	public static Combination getComb() {
		return comb;
	}
	public static void setComb(Combination comb) {
		Solution.comb = comb;
	}
	public static Permutation getPerm() {
		return perm;
	}
	public static void setPerm(Permutation perm) {
		Solution.perm = perm;
	}
	public static Simulation getSim() {
		return sim;
	}
	public static void setSim(Simulation sim) {
		Solution.sim = sim;
	}
	public static ColumnGeneration getCol() {
		return col;
	}
	public static void setCol(ColumnGeneration col) {
		Solution.col = col;
	}

}
