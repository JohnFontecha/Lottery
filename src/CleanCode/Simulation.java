package CleanCode;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Simulation {
	public static final int replicas = 10000;
	public static ArrayList<Group> grupos;
	public static ArrayList<Double> costpereplica;
	public static ArrayList<Set<Integer>> sets;
	ArrayList<GroupAndProb> sol;
	public static double TOL=0.00001;


	public Simulation(Graph gg, String dist, int s,PiecewiseFunction lines, PrintWriter writer, Random rnd) {
		grupos=new ArrayList<Group>();
		costpereplica=new ArrayList<Double>();
		sets=new ArrayList<Set<Integer>>();

		int numremovidos=s;		
		ArrayList<Integer> removidos;
		ArrayList<ArrayList<Integer>> pool=new ArrayList<ArrayList<Integer>>();

		ArrayList<Double> lower=new ArrayList<Double>();
		ArrayList<Double> upper=new ArrayList<Double>();

		lower.add(0.0);

		for(int i=0;i<gg.getNodes().size();i++) {
			if(gg.getAdopters().get(0).getId()==gg.getNodes().get(i).getId()) {
				//				System.out.println(gg.getNodes().get(i).getId()+" "+gg.getAdopters().get(0)+" "+gg.getNodes().get(i).getP());
				
				if(dist=="PwLw") {
					upper.add(gg.getNodes().get(i).getpPwLw());
				}
				if(dist=="CS") {
					upper.add(gg.getNodes().get(i).getpCS());
				}
				if(dist=="Norm") {
					upper.add(gg.getNodes().get(i).getpNor());
				}
				
				break;
			}
		}

		for(int i=1;i<gg.getAdopters().size();i++) {
			double t=upper.get(i-1);
			lower.add(t);
			for(int j=0;j<gg.getNodes().size();j++) {
				if(gg.getAdopters().get(i).getId()==gg.getNodes().get(j).getId()) {
					//					System.out.println(gg.getNodes().get(j).getId()+" "+gg.getAdopters().get(i)+" "+gg.getNodes().get(j).getP()+" "+(upper.get(i-1)+gg.getNodes().get(j).getP()));
					if(dist=="PwLw") {
						upper.add(t+gg.getNodes().get(j).getpPwLw());
					}
					if(dist=="CS") {
						upper.add(t+gg.getNodes().get(j).getpCS());
					}
					if(dist=="Norm") {
						upper.add(t+gg.getNodes().get(j).getpNor());
					}
					break;
				}
			} 
		}

		double totalcost=0.0;
		for(int j=0;j<replicas;j++) {
			removidos=new ArrayList<Integer>();
			Set<Integer> set=new HashSet<Integer>();
			//			System.out.println("--------------");
			for(int i=0;i<numremovidos;i++) {
				double prob= rnd.nextDouble();
				//				System.out.println(prob);
				int pos=0;
				for(int k=0;k<lower.size();k++) {
					if(prob>=lower.get(k) && prob<upper.get(k)) {
						pos=k;
						break;
					}
				}
				int removed=gg.getAdopters().get(pos).getId();
				removidos.add(removed);
				set.add(removed);
			}

//			if(sets.contains(set)==false) {
				pool.add(removidos);
				sets.add(set);


				Group grupo =new Group();
				for (int jj=0; jj<s; jj++) {
					grupo.getNodes().add(removidos.get(jj));
				}

				double cost=0.0;
				int cont=0;
				double este=0;
				for(int i=0;i<gg.getNodes().size();i++) {
					//						System.out.println("node: "+gg.getNodes().get(i).getId());
					if(gg.getAdopters().contains(gg.getNodes().get(i))) {//.getId()

					}else {					
						cont=0;
						este=0.0;
						//							System.out.println("---");
						//							System.out.println(removidos);
						//							System.out.println(gg.getNodes().get(i).getN());
						for(int jj=0;jj<gg.getNodes().get(i).getN().size();jj++) {
							for (int element : set) {
								//							System.out.println(element+" "+grafo.getNodes().get(i).getN().get(j));
								if(element==gg.getNodes().get(i).getN().get(jj)) {
									cont++;
								}
							}
						}
						switch (cont) {
						case 1:
							este=1.0/6.0;
							break;
						case 2:
							este=0.3;
							break;
						case 3:
							este=0.4;
							break;
						default:
							este=0;
							break;
						}
						cost=cost+este;
					}
				}
				totalcost=totalcost+cost;
				grupo.setCost(cost);
				grupos.add(grupo);
//				writer.println(removidos+" "+set+" "+cost);
//				System.out.println(set+" "+removidos+" "+cost);
//			}
		
			
		}
//		writer.println("Expected value: "+(totalcost/grupos.size()));
//		System.out.println("Expected value: "+(totalcost/sets.size())+" "+sets.size());
//		System.out.println("Expected value: "+(totalcost/grupos.size()));
//		System.out.print(totalcost/grupos.size()+" ");
		writer.print(totalcost/grupos.size()+" ");
		double sum=0;
		for(int i=0;i<grupos.size();i++) {
			sum=sum+Math.pow(grupos.get(i).getCost()-(totalcost/grupos.size()), 2);
		}
		double stddev=Math.sqrt(sum/replicas);
//		System.out.print(stddev+" ");
		writer.print(stddev+" ");
		
//		writer.close();
		//		try {
		//			env= new GRBEnv();
		//			env.set(GRB.IntParam.OutputFlag, 0);
		//			model=new GRBModel(env);
		//			int k=grupito.size();
		//			int g=gg.getAdopters().size();
		//
		//			//Create variables
		//			//			x= new GRBVar[k];
		//			x= new ArrayList<GRBVar>();
		//			for(int i=0;i<k;i++) {
		//				//				x[i]=model.addVar(0, 1, K.get(i).getCost(), GRB.CONTINUOUS, "x["+i+"]");
		//				x.add(model.addVar(0, 1, 0, GRB.CONTINUOUS, "x["+i+"]"));
		//			}
		//			model.update();
		//
		//			//Create constraints
		//			GRBLinExpr [] cons=new GRBLinExpr[g];
		//			for(int i=0;i<g;i++) {
		//				cons[i]=new GRBLinExpr();
		//				for(int jj=0; jj<k;jj++) {
		//					for(int l=0;l<grupito.get(jj).getNodes().size();l++) {
		//						if(gg.getAdopters().get(i)==grupito.get(jj).getNodes().get(l)) {
		//							cons[i].addTerm(1.0/s,x.get(jj));
		//							break;
		//						}
		//					}
		//				}
		//				model.addConstr(cons[i], GRB.EQUAL, gg.getNodes().get(i).getP(), "node_"+gg.getAdopters().get(i));
		//			}
		//			model.set(GRB.IntAttr.ModelSense,GRB.MAXIMIZE);
		//			model.update();
		//			model.write("master2.lp");
		//			model.optimize();
		//			objfunc=model.get(GRB.DoubleAttr.ObjVal);
		//			double suma=0.0;
		//			//				sol=new ArrayList<groupAndProb>();
		//			for(int i=0;i<grupito.size();i++) {
		//				if(x.get(i).get(GRB.DoubleAttr.X)>0) {
		//					//						groupAndProb xval=new groupAndProb(grupito.get(i), x.get(i).get(GRB.DoubleAttr.X));
		//					//						sol.add(xval);
		//					suma=suma+grupito.get(i).getCost()*x.get(i).get(GRB.DoubleAttr.X);
		//				}
		//			}
		//
		//			costpereplica.add(suma);
		//
		//		} catch(GRBException e){
		//			e.printStackTrace();
		//		}
//		System.out.println("Check");
//		writer.close();
	}
}

