package CleanCode;

import gurobi.*;

public class Sub_s_fix {
	private Graph G;
	private PiecewiseFunction lines;
	private int s;
	private GRBModel model;
	private GRBEnv env;
	private GRBVar [] z;
	private GRBVar [] y;
	private GRBVar [] w;
	private double objfunc;
	private double groupcost;
	private Group mygroup;
	private double [] beta;

	public Sub_s_fix(Graph g, PiecewiseFunction lines, int s, double [] betas) {
		this.G=g;
		this.lines=lines;
		this.s=s;
		this.beta=betas;
		buildAndSolve();
	}

	public void buildAndSolve() {
		try {
			env= new GRBEnv();
			env.set(GRB.IntParam.OutputFlag, 0);
			model=new GRBModel(env);
			int N=G.getNodes().size();
			int Adopt=G.getAdopters().size();
			int nonAdopt=N-Adopt;

			//Create variables
			z= new GRBVar[nonAdopt];
			y= new GRBVar[Adopt];
			w= new GRBVar[nonAdopt];
			int cont=0;
			for(int i=0;i<N;i++) {
				if(G.getAdopters().contains(G.getNodes().get(i))) {

				}else {
					z[cont]=model.addVar(0, GRB.INFINITY, 1, GRB.CONTINUOUS, "z["+G.getNodes().get(i).getId()+"]");
					w[cont]=model.addVar(0, GRB.INFINITY, 0, GRB.CONTINUOUS, "w["+G.getNodes().get(i).getId()+"]");
					cont++;
				}
			} 
			for(int i=0;i<Adopt;i++) {
				y[i]=model.addVar(0, 1, -beta[i]/s, GRB.BINARY, "y["+G.getAdopters().get(i).getId()+"]");
			}
			model.update();

			//Create constraints
			cont=0;
//			int cont2=0;
			for(int i=0;i<N;i++) {
				if(G.getAdopters().contains(G.getNodes().get(i))) {

				}else {
					GRBLinExpr lhs_c1=new GRBLinExpr();
					GRBLinExpr rhs_c1=new GRBLinExpr();
					lhs_c1.addTerm(1, w[cont]);
					for(int j=0;j<Adopt;j++) {
						if(G.getNodes().get(i).getN().contains(G.getAdopters().get(j).getId())) {
							rhs_c1.addTerm(1, y[j]);
						}
					}
					model.addConstr(lhs_c1, GRB.EQUAL, rhs_c1, "cons1["+G.getNodes().get(i).getId()+"]");
					
					
					for(int t=0;t<lines.getLines().size();t++) {
						GRBLinExpr lhs_c2=new GRBLinExpr();
						lhs_c2.addTerm(1, z[cont]);
						lhs_c2.addTerm(-lines.getLines().get(t).getM(), w[cont]);
						model.addConstr(lhs_c2, GRB.LESS_EQUAL,lines.getLines().get(t).getB() , "cons2["+G.getNodes().get(i).getId()+","+t+"]");						
//						cont2++;
					}
					cont++;
				}
			} 
			GRBLinExpr lhs_cons3=new GRBLinExpr();
			for(int i=0; i<Adopt;i++) {
				lhs_cons3.addTerm(1, y[i]);
			}
			model.addConstr(lhs_cons3, GRB.EQUAL, s, "cons3");
			model.update();	
			model.set(GRB.IntAttr.ModelSense,GRB.MAXIMIZE);
			model.update();
//			model.write("example.lp");
			model.optimize();

			if(model.get(GRB.IntAttr.Status)==GRB.OPTIMAL) {
				mygroup=new Group();				
				objfunc=model.get(GRB.DoubleAttr.ObjVal);
//				System.out.println("reduced cost: "+objfunc);
				for(int i=0;i<Adopt;i++) {
					if(y[i].get(GRB.DoubleAttr.X)>=0.9) {
						mygroup.addNode(G.getAdopters().get(i).getId());
						mygroup.addA(1.0/s);
					}
				}
				double sum=0;
				cont=0;
				for(int i=0;i<N;i++) {
					if(G.getAdopters().contains(G.getNodes().get(i))) {

					}else {
						sum=sum+z[cont].get(GRB.DoubleAttr.X);
						cont++;
					}
				} 
				mygroup.setCost(sum); //(double)Math.round(sum * 100000d) / 100000d
			}
		} catch(GRBException e){
			e.printStackTrace();
		}
	}

	public Graph getG() {
		return G;
	}

	public void setG(Graph g) {
		this.G = g;
	}

	public PiecewiseFunction getLines() {
		return lines;
	}

	public void setLines(PiecewiseFunction lines) {
		this.lines = lines;
	}

	public int getS() {
		return s;
	}

	public void setS(int s) {
		this.s = s;
	}

	public double getObjfunc() {
		return objfunc;
	}

	public void setObjfunc(double objfunc) {
		this.objfunc = objfunc;
	}

	public double getGroupcost() {
		return groupcost;
	}

	public void setGroupcost(double groupcost) {
		this.groupcost = groupcost;
	}

	public Group getMygroup() {
		return mygroup;
	}

	public void setMygroup(Group mygroup) {
		this.mygroup = mygroup;
	}

	public GRBModel getModel() {
		return model;
	}

	public void setModel(GRBModel model) {
		this.model = model;
	}

	public GRBEnv getEnv() {
		return env;
	}

	public void setEnv(GRBEnv env) {
		this.env = env;
	}

	public GRBVar[] getZ() {
		return z;
	}

	public void setZ(GRBVar[] z) {
		this.z = z;
	}

	public GRBVar[] getY() {
		return y;
	}

	public void setY(GRBVar[] y) {
		this.y = y;
	}

	public GRBVar[] getW() {
		return w;
	}

	public void setW(GRBVar[] w) {
		this.w = w;
	}

	public double[] getBeta() {
		return beta;
	}

	public void setBeta(double[] beta) {
		this.beta = beta;
	}
}
