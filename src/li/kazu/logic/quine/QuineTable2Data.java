package li.kazu.logic.quine;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;


@SuppressWarnings("serial")
public class QuineTable2Data extends DefaultTableModel {

	class Cell {
		final String txt;
		final Entry e;
		Cell(Entry e, final String txt) {this.e = e; this.txt = txt;}
		@Override public String toString() {return txt;}
	}
	
	class Row {
		ArrayList<Cell> cells;
		Row(final ArrayList<Cell> cells) {
			this.cells = cells;
		}
	}
	


	private String[] colNames = new String[0];
	private String[][] data = new String[0][];
	
	/** ctor */
	public QuineTable2Data() {

	}
	
	/** set the data-source */
	public void setData(final Domination dom) {
				
		if (dom == null) {
			colNames = new String[0];
			data = new String[0][];
			return;
		}
		
		final int dCols = dom.numCols();
		final int dRows = dom.numRows();
		final int stepNr = 0;
		
		this.colNames = new String[dCols+1];
		colNames[0] = "";
		for (int col = 0; col < dCols; ++col) {colNames[col+1] = col+"";}
		data = new String[dRows][];
		for (int row = 0; row < dRows; ++row) {
			data[row] = new String[dCols+1];
			data[row][0] = ""+(char)('A'+row);
			for (int col = 0; col < dCols; ++col) {
				data[row][col+1] = dom.getForStep(row,col, stepNr) ? "x" : "";
			}
		}
		
		fireTableDataChanged();
		fireTableStructureChanged();
		
	}


	@Override
	public int getRowCount() {
		return (data == null) ? (0) : (data.length);
	}
	
	@Override
	public int getColumnCount() {
		return (colNames == null) ? (0) : (colNames.length);
	}

	@Override
	public Object getValueAt(int row, int column) {
		return data[row][column];
	}
	
	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}
		
}

