package CleanCode;

import java.util.ArrayList;

public class PiecewiseFunction {
		private ArrayList<Line> lines;		//piecewise-linear-function's lines
		/**
		 * This method constructs the piecewise-linear-function
		 * @param nlines lines
		 */
		public PiecewiseFunction(){
			lines=new ArrayList<Line>(); 
		}
		/**
		 * This method constructs the piecewise-linear-function
		 * @param nlines lines
		 */
		public PiecewiseFunction(ArrayList<Line> nlines){
			this.lines=nlines;
		}
		/**
		 * This method add a line to the set of lines of the piecewise-linear-function
		 * @param l
		 */
		public void addLine(Line l) {
			lines.add(l);
		}
		/**
		 * This method returns the set of lines of the piecewise-linear-function  
		 * @return lines
		 */
		public ArrayList<Line> getLines() {
			return lines;
		}
		/**
		 * This method sets the set of lines of the piecewise-linear-function
		 * @param lines
		 */
		public void setLines(ArrayList<Line> lines) {
			this.lines = lines;
		}
		
}
