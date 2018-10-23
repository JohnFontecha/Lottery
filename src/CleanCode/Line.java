package CleanCode;

public class Line {
	private double m;				//lines's slope
	private double b;				//lines's intercept
	/**
	 * This method constructs a line
	 * @param nm slope
	 * @param nb intercept
	 */
	public Line() {
		m=0.0;
		b=0.0;
	}
	
	/**
	 * This method constructs a line
	 * @param nm slope
	 * @param nb intercept
	 */
	public Line(double nm, double nb) {
		this.m=nm;
		this.b=nb;
	}
	/**
	 * This method returns the line's slope
	 * @return m
	 */
	public double getM() {
		return m;
	}
	/**
	 * This method sets the line's slope
	 * @param m
	 */
	public void setM(double m) {
		this.m = m;
	}
	/**
	 * This method returns the line's intercept
	 * @return b
	 */
	public double getB() {
		return b;
	}
	/**
	 * This method sets the line's intercept
	 * @param m
	 */
	public void setB(double b) {
		this.b = b;
	}
}
