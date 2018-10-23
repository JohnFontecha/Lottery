package CleanCode;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Permutation {


	public static ArrayList<Group> grupos;
	public static ArrayList<Set<Integer>> sets;
	public static Graph grafo;

	static void permute(Object[] a, int k, PermuteCallback callback) {
		int n = a.length;

		int[] indexes = new int[k];
		int total = (int) Math.pow(n, k);

		Object[] snapshot = new Object[k];
		while (total-- > 0) {
			for (int i = 0; i < k; i++){
				snapshot[i] = a[indexes[i]];
			}
			callback.handle(snapshot);

			for (int i = 0; i < k; i++) {
				if (indexes[i] >= n - 1) {
					indexes[i] = 0;
				} else {
					indexes[i]++;
					break;
				}
			}
		}
	}

	public static interface PermuteCallback{
		public void handle(Object[] snapshot);
	};

	public Permutation(int instance, Graph g, int s,String dist,PrintWriter writer) {
		grupos=new ArrayList<Group>();
		sets=new ArrayList<Set<Integer>>();
		grafo=new Graph();		
		grafo=g;
		int adopters=g.getAdopters().size();
		Object[] chars=new Object[adopters];

		if(Math.pow(adopters, s)<20000) {
			for(int i=0;i<adopters;i++) {
				chars[i]=g.getAdopters().get(i).getId();
			}

			//		Object[] chars = { 1, 2, 3, 4 };
			PermuteCallback callback = new PermuteCallback() {

				public void handle(Object[] snapshot) {
					Group grupo =new Group();
					Set<Integer> set=new HashSet<Integer>();
					for(int i = 0; i < snapshot.length; i ++){
						grupo.addNode((Integer) snapshot[i]);
						set.add((Integer) snapshot[i]);
						//					System.out.print(snapshot[i]+"-");
					}


					//				System.out.println("kakak");
					sets.add(set);
					grupos.add(grupo);
				}
			};
			permute(chars, s, callback);

			double totalcost=0.0;
			double totalcost2=0.0;
			for(int l=0;l<sets.size();l++) {
				double cost=0.0;
				int cont=0;
				double este=0;
				for(int i=0;i<grafo.getNodes().size();i++) {
					if(grafo.getAdopters().contains(grafo.getNodes().get(i))) {

					}else {					
						cont=0;
						este=0.0;
						for(int j=0;j<grafo.getNodes().get(i).getN().size();j++) {			
							for (int element : sets.get(l)) {
								//							System.out.println(element+" "+grafo.getNodes().get(i).getN().get(j));
								if(element==grafo.getNodes().get(i).getN().get(j)) {
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
				double proba=1;
				for (int element : grupos.get(l).getNodes()) {
					//				System.out.println(element+" "+grafo.getNodes().get(i).getN().get(j));
					for(int k=0;k<grafo.getAdopters().size();k++) {
						if(element==grafo.getAdopters().get(k).getId()) {
							//						System.out.println(grafo.getAdopters().get(k).getId()+" "+grafo.getAdopters().get(k).getP());
							if(dist=="PwLw") {
								proba=proba*grafo.getAdopters().get(k).getpPwLw();
							}
							if(dist=="CS") {
								proba=proba*grafo.getAdopters().get(k).getpCS();
							}
							if(dist=="Norm") {
								proba=proba*grafo.getAdopters().get(k).getpNor();
							}


						}
					}
				}

				totalcost=totalcost+cost;
				totalcost2=totalcost2+cost*proba;
				grupos.get(l).setCost(cost);
				grupos.get(l).setProb(proba);
				//			System.out.println(grupos.get(l).getNodes()+" "+grupos.get(l).getCost());
//				writer.println(grupos.get(l).getNodes()+" "+sets.get(l)+" "+grupos.get(l).getCost()+" "+proba);
			}
//			writer.println("Expected value: "+(totalcost/sets.size()));
//			writer.println("Expected value2: "+(totalcost2));
			//		System.out.println("Expected value: "+(totalcost/sets.size()));
			//		System.out.println("Expected value2: "+(totalcost2));
//			System.out.print(totalcost2+" ");
			writer.print(totalcost2+" ");

//			writer.close();
			
			
		} else {
			writer.print("NaN"+" ");
//			System.out.print("NaN"+" ");
		}
		
		

	}

}
