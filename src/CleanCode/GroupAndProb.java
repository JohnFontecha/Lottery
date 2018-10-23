package CleanCode;

public class GroupAndProb {
	private Group g;
	private double p;
	public GroupAndProb(Group ng,double np) {
		this.g=ng;
		this.p=np;
	}
	public Group getG() {
		return g;
	}
	public void setG(Group g) {
		this.g = g;
	}
	public double getP() {
		return p;
	}
	public void setP(double p) {
		this.p = p;
	}
}
