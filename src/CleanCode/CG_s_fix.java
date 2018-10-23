package CleanCode;

import java.util.ArrayList;
import java.util.Collections;
import gurobi.GRB;
import gurobi.GRBColumn;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;


public class CG_s_fix {
	private Graph G;
	private ArrayList<Group> K; //groups
	ArrayList<GroupAndProb> sol;
	private int s;
	private GRBEnv env;
	private GRBModel model; 
	private ArrayList<GRBVar> x;
	private double [] beta;
	private int [] idbetas;
	private double objfunc;
	public static double TOL=0.00001;
	private PiecewiseFunction lines;
	private String distribution;
	int iteration;
	/**
	 * This method constructs the master problem
	 * @param gg
	 * @param kattr
	 */
	public CG_s_fix(Graph gg, ArrayList<Group> k, int s,PiecewiseFunction lines,String dist) {
		this.G=gg;
		this.K=k;
		this.s=s;
		this.lines=lines;
		this.distribution=dist;
		buildAndSolve();
	}
	private void buildAndSolve() {
		try {
			env= new GRBEnv();
			env.set(GRB.IntParam.OutputFlag, 0);
			model=new GRBModel(env);
			int k=K.size();
			int g=G.getAdopters().size();

			//Create variables
			x= new ArrayList<GRBVar>();
			for(int i=0;i<k;i++) {
				x.add(model.addVar(0, GRB.INFINITY, K.get(i).getCost(), GRB.CONTINUOUS, "x["+i+"]"));
			}
			model.update();

			//Create constraints
			GRBLinExpr [] cons=new GRBLinExpr[g];
			for(int i=0;i<g;i++) {
				cons[i]=new GRBLinExpr();
				//				System.out.println("-------------");
				for(int j=0; j<k;j++) {
					for(int l=0;l<K.get(j).getNodes().size();l++) {
						//						System.out.println(i+" "+j+" "+l+" "+G.getAdopters().get(i)+" "+K.get(j).getNodes().get(l));
						//						if(G.getAdopters().get(i)==K.get(j).getNodes().get(l)) {
						if(G.getAdopters().get(i).getId()==K.get(j).getNodes().get(l)) {
							//							System.out.println("Entra");
							//							cons[i].addTerm(1.0/s,x.get(j));
							cons[i].addTerm(K.get(j).getA().get(l),x.get(j));
							break;
						}
					}
				}
				int id=0;
				for(int j=0;j<G.getNodes().size();j++) {
					if(G.getAdopters().get(i).equals(G.getNodes().get(j))) {
						id=j;
					}
				}
				if(distribution=="PwLw") {
					model.addConstr(cons[i], GRB.EQUAL, G.getNodes().get(id).getpPwLw(), "node_"+G.getAdopters().get(i).getId());
				}
				if(distribution=="CS") {
					model.addConstr(cons[i], GRB.EQUAL, G.getNodes().get(id).getpCS(), "node_"+G.getAdopters().get(i).getId());
				}
				if(distribution=="Norm") {
					model.addConstr(cons[i], GRB.EQUAL, G.getNodes().get(id).getpNor(), "node_"+G.getAdopters().get(i).getId());
				}
				
				//				System.out.println(G.getNodes().get(id).getId()+"-->"+G.getAdopters().get(i)+" --> "+G.getNodes().get(id).getP());
			}
			model.set(GRB.IntAttr.ModelSense,GRB.MAXIMIZE);
			model.update();
//			model.write("master.lp");

			boolean seguir=true;
			iteration=0;
			//			System.out.println("----------------------------");
			//			ArrayList<Double> UB=new ArrayList<Double>();
			//			ArrayList<Double> LB=new ArrayList<Double>();
			//			System.out.println("group --> Reach --> Reduced cost");
			while(seguir) {
				//				System.out.println("Iteration: "+iteration);
				seguir=false;
				model.optimize();
				//				if(model.get(GRB.IntAttr.Status)==GRB.OPTIMAL) {
				objfunc=model.get(GRB.DoubleAttr.ObjVal);
				beta=new double[g];
				idbetas=new int[g];
				//					System.out.println("objective funciton "+objfunc);
				//					System.out.println("i \t beta \t betaid");
				for(int i=0;i<g;i++) {
					beta[i]=model.getConstrByName("node_"+G.getAdopters().get(i).getId()).get(GRB.DoubleAttr.Pi);
					idbetas[i]=G.getAdopters().get(i).getId();
					//						System.out.println(i+"\t "+beta[i]+"\t "+idbetas[i]);
				}
				sol=new ArrayList<GroupAndProb>();
				for(int i=0;i<K.size();i++) {
					if(x.get(i).get(GRB.DoubleAttr.X)>0) {
						GroupAndProb xval=new GroupAndProb(K.get(i), x.get(i).get(GRB.DoubleAttr.X));
						sol.add(xval);
					}
				}
				//				}			

				Sub_s_fix subp=new Sub_s_fix(G,lines,s,beta);
				//				System.out.println(subp.getMygroup().getNodes()+" --> "+subp.getMygroup().getCost()+" --> "+subp.getObjfunc());

				if(subp.getObjfunc()>TOL) {	
					K.add(subp.getMygroup());				
					GRBColumn newc=new GRBColumn();
					int size=K.size()-1;
					double []a=new double[g];
					for(int i=0;i<g;i++) {
						for(int j=0;j<subp.getMygroup().getNodes().size();j++) {
							if(subp.getMygroup().getNodes().get(j).equals(G.getAdopters().get(i).getId())) {
								//							a[i]=1.0/s;
								a[i]=subp.getMygroup().getA().get(j);
							}
						}
					}
					for(int i=0;i<g;i++) {
						newc.addTerm(a[i], model.getConstrByName("node_"+G.getAdopters().get(i).getId()));
					}
					x.add(model.addVar(0, 1, subp.getMygroup().getCost(), GRB.CONTINUOUS, newc,"x["+size+"]"));
					model.update();
					model.write("master.lp");
					seguir=true;
				}

				//				if(objfunc>0) {
				//					UB.add(Math.min(objfunc+subp.getObjfunc(),UB.get(minIndex(UB))));
				//				} else {
				//					double ub=G.getNodes().size()*1.0/1.0-G.getAdopters().size()*1.0/1.0+0.0;
				//					UB.add(ub);
				//				}
				//				LB.add(objfunc);
				iteration++;
				//				System.out.println("----------------------------");
			}

			//			model.addConstr(x.get(1), GRB.GREATER_EQUAL, 0.001, "extra");
			//			model.optimize();
			//
			//			System.out.println("--------------Final solution--------------");
			//			System.out.println("obj function: "+ model.get(GRB.DoubleAttr.ObjVal));
			//			System.out.println("group --> cost --> probability");
			//			for(int i=0;i< sol.size();i++) {
			//				System.out.println( sol.get(i).getG().getNodes()+" --> "+ sol.get(i).getG().getCost()+" --> "+ sol.get(i).getP());
			//			}

		} catch(GRBException e){
			e.printStackTrace();
		}
	}
	
	public int getS() {
		return s;
	}
	public void setS(int s) {
		this.s = s;
	}
	public double [] getBeta() {
		return beta;
	}
	public void setBeta(double [] beta) {
		this.beta = beta;
	}
	public double getObjfunc() {
		return objfunc;
	}
	public void setObjfunc(double objfunc) {
		this.objfunc = objfunc;
	}
	public int[] getIdbetas() {
		return idbetas;
	}
	public void setIdbetas(int[] idbetas) {
		this.idbetas = idbetas;
	}

	public static int minIndex (ArrayList<Double> list) {
		return list.indexOf (Collections.min(list)); 
	}
	public GRBEnv getEnv() {
		return env;
	}
	public void setEnv(GRBEnv env) {
		this.env = env;
	}
	public GRBModel getModel() {
		return model;
	}
	public void setModel(GRBModel model) {
		this.model = model;
	}
	public ArrayList<GRBVar> getX() {
		return x;
	}
	public void setX(ArrayList<GRBVar> x) {
		this.x = x;
	}
	public static double getTOL() {
		return TOL;
	}
	public static void setTOL(double tOL) {
		TOL = tOL;
	}
	public PiecewiseFunction getLines() {
		return lines;
	}
	public void setLines(PiecewiseFunction lines) {
		this.lines = lines;
	}
	public String getDistribution() {
		return distribution;
	}
	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}
	public void setG(Graph g) {
		G = g;
	}
	public void setK(ArrayList<Group> k) {
		K = k;
	}
	public void setSol(ArrayList<GroupAndProb> sol) {
		this.sol = sol;
	}
	public Graph getG() {
		return G;
	}
	public ArrayList<Group> getK() {
		return K;
	}
	public ArrayList<GroupAndProb> getSol() {
		return sol;
	}
	public int getIteration() {
		return iteration;
	}
	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

}
