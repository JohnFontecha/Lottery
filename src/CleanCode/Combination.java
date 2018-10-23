package CleanCode;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;


public class Combination {

	public static ArrayList<Group> grupos;
	public static Graph grafo=new Graph();
	private static double probt;

	public Combination(int instance, Graph g, int s,String dist,PrintWriter writer) { 
		
		
		
		grupos=new ArrayList<Group>();
		grafo=g;
		int adopters=grafo.getAdopters().size();
		
		if(binomial(adopters, s).intValue()<20000) {
			int arr[]=new int[adopters];

			for(int i=0;i<adopters;i++) {
				arr[i]=grafo.getAdopters().get(i).getId();
			}
			int r = s; 
			int n = arr.length;

			

			printCombination(arr, n, r,dist,writer); 
			double cost=0.0;
			double cost2=0.0;
			double totalp=0;
			for(int i=0;i<grupos.size();i++) {
				cost=cost+grupos.get(i).getCost();
				cost2=cost2+grupos.get(i).getCost()*grupos.get(i).getProb();
				totalp=totalp+grupos.get(i).getProb();
			}
			cost=cost/grupos.size();

//			writer.println("Expected value: "+cost);
//			System.out.print(cost2+" ");
			writer.print(cost2+" ");

			double [] totaldeviation=new double[grafo.getAdopters().size()];
			int [] sign=new int[grafo.getAdopters().size()];
			double totaldev=0;
			double max_plus=0.0;
			double min_plus=2.0;
			int maxid_plus=0;
			int minid_plus=0;
			double max_minus=0.0;
			double min_minus=2.0;
			int maxid_minus=0;
			int minid_minus=0;
			for(int i=0;i<grafo.getAdopters().size();i++) {
				double pi=0;
				for(int l=0;l<grupos.size();l++) {
					if(grupos.get(l).getNodes().contains(grafo.getAdopters().get(i).getId())) {
						pi=pi+grupos.get(l).getProb()/grupos.get(l).getNodes().size();
					}
				}

				if(dist=="PwLw") {
					totaldeviation[i]=Math.abs(pi-grafo.getAdopters().get(i).getpPwLw())/grafo.getAdopters().get(i).getpPwLw();		
					totaldev=totaldev+Math.abs(pi-grafo.getAdopters().get(i).getpPwLw())/grafo.getAdopters().get(i).getpPwLw();
					sign[i]=0;
					if(pi-grafo.getAdopters().get(i).getpPwLw()>0) {
						sign[i]=1;
					}
					if(pi-grafo.getAdopters().get(i).getpPwLw()<0) {
						sign[i]=-1;
					}
				} 
				if(dist=="CS") {
					totaldeviation[i]=Math.abs(pi-grafo.getAdopters().get(i).getpCS())/grafo.getAdopters().get(i).getpCS();		
					totaldev=totaldev+Math.abs(pi-grafo.getAdopters().get(i).getpCS())/grafo.getAdopters().get(i).getpCS();
					sign[i]=0;
					if(pi-grafo.getAdopters().get(i).getpCS()>0) {
						sign[i]=1;
					}
					if(pi-grafo.getAdopters().get(i).getpCS()<0) {
						sign[i]=-1;
					}
				}
				if(dist=="Norm") {
					totaldeviation[i]=Math.abs(pi-grafo.getAdopters().get(i).getpNor())/grafo.getAdopters().get(i).getpNor();		
					totaldev=totaldev+Math.abs(pi-grafo.getAdopters().get(i).getpNor())/grafo.getAdopters().get(i).getpNor();
					sign[i]=0;
					if(pi-grafo.getAdopters().get(i).getpNor()>0) {
						sign[i]=1;
					}
					if(pi-grafo.getAdopters().get(i).getpNor()<0) {
						sign[i]=-1;
					}
				}

				if(sign[i]>0) {
					if(totaldeviation[i]>max_plus) {
						max_plus=totaldeviation[i];
						maxid_plus=grafo.getAdopters().get(i).getId();
					}

					if(totaldeviation[i]<min_plus) {
						min_plus=totaldeviation[i];
						minid_plus=grafo.getAdopters().get(i).getId();
					}
				}
				if(sign[i]<0) {
					if(totaldeviation[i]>max_minus) {
						max_minus=totaldeviation[i];
						maxid_minus=grafo.getAdopters().get(i).getId();
					}

					if(totaldeviation[i]<min_minus) {
						min_minus=totaldeviation[i];
						minid_minus=grafo.getAdopters().get(i).getId();
					}
				}

			}

//			System.out.print((totaldev/grafo.getAdopters().size())+" "+min_plus+" "+minid_plus+" "+max_plus+" "+maxid_plus+" "+min_minus+" "+minid_minus+" "+max_minus+" "+maxid_minus+" ");
			writer.print((totaldev/grafo.getAdopters().size())+" "+min_plus+" "+minid_plus+" "+max_plus+" "+maxid_plus+" "+min_minus+" "+minid_minus+" "+max_minus+" "+maxid_minus+" ");
			
			
		} else {
//			System.out.print("NaN"+" "+"NaN"+" "+"NaN"+" "+"NaN"+" "+"NaN"+" "+"NaN"+" "+"NaN"+" "+"NaN"+" "+"NaN"+" "+"NaN"+" ");
			writer.print("NaN"+" "+"NaN"+" "+"NaN"+" "+"NaN"+" "+"NaN"+" "+"NaN"+" "+"NaN"+" "+"NaN"+" "+"NaN"+" "+"NaN"+" ");
		}
		
	} 

	// The main function that prints all combinations of size r 
	// in arr[] of size n. This function mainly uses combinationUtil() 
	static void printCombination(int arr[], int n, int r,String dist,PrintWriter writer) 
	{ 
		// A temporary array to store all combination one by one 
		int data[]=new int[r]; 

		// Print all combination using temprary array 'data[]' 
		combinationUtil(arr, data, 0, n-1, 0, r,dist,writer); 
	} 


	public static void combinationUtil(int arr[], int data[], int start, int end, int index, int r, String dist,PrintWriter writer){ 
		if (index == r) 
		{ 
			Group grupo =new Group();
			for (int j=0; j<r; j++) {
				grupo.getNodes().add(data[j]);
			}
			int nn = data.length; 
			probt=0.0;
			permute(data, 0, nn-1,dist);
			double probability=probt;
			grupo.setProb(probability);

			//Aqui mirar el costo
			double cost=0.0;
			int cont=0;
			double este=0;
			for(int i=0;i<grafo.getNodes().size();i++) {
				if(grafo.getAdopters().contains(grafo.getNodes().get(i))) {

				}else {					
					cont=0;
					este=0.0;
					for(int j=0;j<grafo.getNodes().get(i).getN().size();j++) {
						for (int k=0; k<r; k++) {
							if(data[k]==grafo.getNodes().get(i).getN().get(j)) {
								cont++;
							}
						}
					}
					switch (cont) {
					case 0: 
						este=0;
						break;
					case 1:
						este=1.0/6.0;
						break;
					case 2:
						este=0.3;
						break;
					case 3:
						este=0.4;
						break;
					case 4:
						este=0.466666667;
						break;
					default:
						este=0.5;
						break;
					}
					cost=cost+este;
				}
			}
			grupo.setCost(cost);

//			for (int j=0; j<r; j++) {
//				writer.print(data[j]+" ");	
//			}
//			writer.print(cost);
//			writer.println(""); 
			//			System.out.println("");
			grupos.add(grupo);
			return; 
		} 

		// replace index with all possible elements. The condition 
		// "end-i+1 >= r-index" makes sure that including one element 
		// at index will make a combination with remaining elements 
		// at remaining positions 
		for (int i=start; i<=end && end-i+1 >= r-index; i++) 
		{ 
			data[index] = arr[i]; 
			combinationUtil(arr, data, i+1, end, index+1, r,dist,writer); 
		} 
	} 



	/** 
	 * permutation function 
	 * @param str string to calculate permutation for 
	 * @param l starting index 
	 * @param r end index 
	 */
	private static void permute(int[]  str, int l, int r,String dist) 
	{ 
		//		double probt=0.0;
		if (l == r) {
			double probg=1;
			for(int i=str.length-1;i>=0;i--) {
				double sum=0.0;
				for(int j=0;j<i;j++) {
					for(int k=0;k<grafo.getAdopters().size();k++) {					
						int k2 = str[j];
						if(grafo.getAdopters().get(k).getId()==k2) {

							if(dist=="PwLw") {
								sum=sum+grafo.getAdopters().get(k).getpPwLw();
								break;
							}
							if(dist=="CS") {
								sum=sum+grafo.getAdopters().get(k).getpCS();
								break;
							}
							if(dist=="Norm") {
								sum=sum+grafo.getAdopters().get(k).getpNor();
								break;
							}

						}
					}

				}
				double probi=0;
				int k2 = str[i];
				for(int k=0;k<grafo.getAdopters().size();k++) {					
					if(grafo.getAdopters().get(k).getId()==k2) {

						if(dist=="PwLw") {
							probi=grafo.getAdopters().get(k).getpPwLw();			
						}
						if(dist=="CS") {
							probi=grafo.getAdopters().get(k).getpCS();			
						}
						if(dist=="Norm") {
							probi=grafo.getAdopters().get(k).getpNor();		
						}

						break;
					}
				}			
				probg=probg*probi/(1-sum);
			}			
			probt=probt+probg;
		}
		else
		{ 
			for (int i = l; i <= r; i++) 
			{ 
				str = swap(str,l,i); 
				permute(str, l+1, r,dist); 
				str = swap(str,l,i); 
			} 
		} 
	} 

	/** 
	 * Swap Characters at position 
	 * @param a string value 
	 * @param i position 1 
	 * @param j position 2 
	 * @return swapped string 
	 */
	public static int [] swap(int[] a, int i, int j) 
	{ 
		int temp; 
		int [] charArray =  a;
		temp = charArray[i] ; 
		charArray[i] = charArray[j]; 
		charArray[j] = temp; 
		return charArray; 
	} 

	static BigInteger binomial(final int N, final int K) {
		BigInteger ret = BigInteger.ONE;
		for (int k = 0; k < K; k++) {
			ret = ret.multiply(BigInteger.valueOf(N-k))
					.divide(BigInteger.valueOf(k+1));
		}
		return ret;
	}
}
