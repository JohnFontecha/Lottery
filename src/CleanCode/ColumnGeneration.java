package CleanCode;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;


public class ColumnGeneration {
	private CG_s_fix masterp;
	private Master masterpp;
	public ColumnGeneration(Graph gg, String dist, int s,PiecewiseFunction lines, PrintWriter writer, Random rnd) {
		
		int adopters=gg.getAdopters().size();
		ArrayList<Group> K=new ArrayList<Group>();
		for(int i=0;i<adopters;i++) {//nodes.size()
			Group firstgroup=new Group();
			firstgroup.addNode(gg.getAdopters().get(i).getId());
			firstgroup.addA(1.0);
			firstgroup.setCost(-0.1);
			K.add(firstgroup);
		}
//		System.out.println(K.size());
		int totaliterations=0;
		for(int i=0;i<s;i++) {
			 masterp=new CG_s_fix(gg,K,s-i,lines,dist);
			 totaliterations=totaliterations+masterp.getIteration();
		}
		 masterpp=new Master(gg,K,s,dist,writer);
		 
//			System.out.print(totaliterations+" ");
			writer.print(totaliterations+" ");
		 
	}
	public CG_s_fix getMasterp() {
		return masterp;
	}
	public void setMasterp(CG_s_fix masterp) {
		this.masterp = masterp;
	}
	public Master getMasterpp() {
		return masterpp;
	}
	public void setMasterpp(Master masterpp) {
		this.masterpp = masterpp;
	}
}
