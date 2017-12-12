package li.kazu.logic.quine;

import java.util.ArrayList;
import java.util.List;



public class Domination {
	
	protected final int rows;
	protected final int cols;
	
	/** current content */
	protected final boolean[] tbl;
	
	/** all previous contents as well (allows stepping) */
	protected final List<boolean[]> steps = new ArrayList<boolean[]>();
	
	public Domination(final int numVars, final List<Entry> entries) {
		
		cols = 1 << numVars;
		rows = entries.size();
		
		tbl =  new boolean[cols*rows];
		
		for (int primeTermIdx = 0; primeTermIdx < entries.size(); ++primeTermIdx) {
			for (final int termNr : entries.get(primeTermIdx).usedTerms) {
				set(primeTermIdx, termNr);
			}
		}
		
	}
	
	public int numCols() {return cols;}
	
	public int numRows() {return rows;}
	
	private void set(final int primeTermIdx, final int termNr) {
		tbl[getIdx(primeTermIdx, termNr)] = true;
	}
	
	private void unset(final int primeTermIdx, final int termNr) {
		tbl[getIdx(primeTermIdx, termNr)] = false;
	}
	
	public boolean get(final int primeTermIdx, final int termNr) {
		return tbl[getIdx(primeTermIdx, termNr)];
	}
	
	public boolean getForStep(final int primeTermIdx, final int termNr, final int stepNr) {
		return steps.get(stepNr)[getIdx(primeTermIdx, termNr)];
	}
	
	private int getIdx(final int primeTermIdx, final int termNr) {
		return primeTermIdx * cols + termNr;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("\t");
		for (int col = 0; col < cols; ++col) {
			sb.append(col / 10);
		}
		sb.append("\r\n");
		sb.append("\t");
		for (int col = 0; col < cols; ++col) {
			sb.append(col % 10);
		}
		sb.append("\r\n");
		for (int row = 0; row < rows; ++row) {
			sb.append((char)('A' + row)).append("\t");
			for (int col = 0; col < cols; ++col) {
				final boolean isSet = get(row, col);
				sb.append(isSet ? '*' : ' ');
			}
			sb.append("\r\n");
		}
		return sb.toString();
	}
	
	private boolean colIsEmpty(final int col) {
		for (int row = 0; row < rows; ++row) {
			if (get(row,col)) {return false;}
		}
		return true;
	}
	
	private boolean rowIsEmpty(final int row) {		
		for (int col = 0; col < cols; ++col) {
			if (get(row,col)) {return false;}
		}
		return true;
	}
	
	/** column c1 contains all true-values of c2 */
	private boolean colContains(final int c1, final int c2) {
		for (int row = 0; row < rows; ++row) {
			final boolean v1 = get(row, c1);
			final boolean v2 = get(row, c2);
			if (v2 == true && v1 == false) {return false;}
		}
		return true;
	}
	
	/** row r1 contains all true-values of r2 */
	private boolean rowContains(final int r1, final int r2) {
		for (int col = 0; col < cols; ++col) {
			final boolean v1 = get(r1, col);
			final boolean v2 = get(r2, col);
			if (v2 == true && v1 == false) {return false;}
		}
		return true;
	}
	
	private void unsetRow(final int row) {
		for (int col = 0; col < cols; ++col) {
			unset(row, col);
		}
	}
	
	private void unsetCol(final int col) {
		for (int row = 0; row < rows; ++row) {
			unset(row, col);
		}
	}
	
	public boolean combineCols() {
		boolean did = false;
		for (int col1 = 0; col1 < cols; ++col1) {
			for (int col2 = 0; col2 < cols; ++col2) {
				if (col1 == col2) {continue;}
				if (colIsEmpty(col1)) {continue;}
				if (colIsEmpty(col2)) {continue;}
				if (colContains(col1, col2)) {
					unsetCol(col1);
					did = true;
				}
			}
		}
		return did;
	}
	
	public boolean combineRows() {
		boolean did = false;
		for (int row1 = 0; row1 < rows; ++row1) {
			for (int row2 = 0; row2 < rows; ++row2) {
				if (row1 == row2) {continue;}
				if (rowIsEmpty(row1)) {continue;}
				if (rowIsEmpty(row2)) {continue;}
				if (rowContains(row1, row2)) {
					unsetRow(row2);
					did = true;
				}
			}
		}
		return did;
	}
	
	public int getNumSteps() {
		return steps.size();
	}
	
	public void combine() {
		
		System.out.println(this);
		while(true) {
			
			steps.add(tbl.clone());
			if (!combineCols()) {break;}
			System.out.println(this);
			
			steps.add(tbl.clone());
			if (!combineRows()) {break;}
			System.out.println(this);
			
		}
		
		//combineCols();
		//combineRows();
		//combineCols();
		//combineRows();
		//System.out.println(this);
		
	}
	
	/** get the list of all required prime terms */
	public List<Integer> getFinalPrimeTerms() {
		final ArrayList<Integer> lst = new ArrayList<>();
		for (int row = 0; row < rows; ++row) {
			if (!rowIsEmpty(row)) {
				lst.add(row);
			}
		}
		return lst;
	}
	
	public String getTex() {
		
		final StringBuilder sb = new StringBuilder();
		
		// setup
		sb.append("\\begin{tabular}{|c");
		for (int i = 0; i < cols; ++i) {
			sb.append("|c");
		}
		sb.append("|}\\hline\r\n");
		sb.append("\t");
				
		// header
		sb.append("&");
		for (int i = 0; i < cols; ++i) {
			sb.append(i).append("&");
		}
		sb.setLength(sb.length()-1);
		sb.append("\\\\\\hline\r\n");
				
				
		// values
		for (int row = 0; row < rows; ++row) {
			sb.append("\t");
			sb.append((char)('A'+row)+"&");
			for (int col = 0; col < cols; ++col) {
				boolean c = get(row, col);
				if (c) {sb.append("x");}
				sb.append("&");
			}
			sb.setLength(sb.length()-1);
			sb.append("\\\\\\hline\r\n");
		}
				
		sb.append("\\end{tabular}\r\n");
				
		return sb.toString();
		
	}
	
}
