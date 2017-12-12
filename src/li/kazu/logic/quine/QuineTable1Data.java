package li.kazu.logic.quine;

import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import li.kazu.logic.quine.Quine.Level;

@SuppressWarnings("serial")
public class QuineTable1Data extends DefaultTableModel {

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
	
	private Quine q;
	
	private final ArrayList<Row> rows = new ArrayList<>();
	
	/** ctor */
	public QuineTable1Data() {

	}
	
	/** set the data-source */
	public void setData(final Quine q) {
		
		this.q = q;
		
		final ArrayList<Level> levels = q.getLevels();
		rows.clear();
		//fireTableDataChanged();
		//fireTableStructureChanged();
		
		for (int row = 0; row < 1024; ++row) {
			final ArrayList<Cell> rowData = new ArrayList<Cell>();
			boolean isEmpty = true;
			for (int col = 0; col < levels.size(); ++col) {
				final Level lvl = levels.get(col);
				if (row < lvl.entries.size()) {
					final Entry e = lvl.entries.get(row);
					String txt1 = e.usedTerms.toString();
					String txt2 = e.mask;
					if (e.prime()) {txt2 += " " + (char)('A'+e.primeTermNr);}
					if (e.duplicate()) {txt2 += " xx";}
					rowData.add(new Cell(e, txt1));
					rowData.add(new Cell(e, txt2));
					isEmpty = false;
				} else {
					rowData.add(new Cell(null, ""));
					rowData.add(new Cell(null, ""));
				}
			}
			if (isEmpty) {break;}
			rows.add(new Row(rowData));
		}
		
		fireTableDataChanged();
		fireTableStructureChanged();
		
	}
	
	public String[] getColNames() {
		final ArrayList<String> colNames = new ArrayList<>();
		final ArrayList<Level> levels = q.getLevels();
		for (int i = 0; i < levels.size(); ++i) {
			colNames.add("T" + i);
			colNames.add("M" + i);
		}
		return colNames.toArray(new String[0]);
	}

	@Override
	public int getRowCount() {
		return (rows == null) ? (0) : (rows.size());
	}
	
	@Override
	public int getColumnCount() {
		return (rows == null || rows.isEmpty()) ? (0) : (rows.get(0).cells.size());
	}

	@Override
	public Object getValueAt(int row, int column) {
		return rows.get(row).cells.get(column).txt;
	}
	
	public final String toTeX() {
		
		if (q == null) {return "";}
		
		final StringBuilder sb = new StringBuilder();
		
		// setup
		sb.append("\\begin{tabular}{");
		for (int i = 0; i < q.getLevels().size(); ++i) {
			sb.append("|c|c|");
		}
		sb.append("}\\hline\r\n");
		sb.append("\t");
		
		// header
		int lvl = 0;
		for (int i = 0; i < q.getLevels().size(); ++i) {
			sb.append("T").append(lvl).append("&");
			sb.append("M").append(lvl).append("&");
			++lvl;
		}
		sb.setLength(sb.length()-1);
		sb.append("\\\\\\hline\r\n");
		
		
		// values
		for (final Row row : rows) {
			sb.append("\t");
			for (final Cell cell : row.cells) {
				if (cell.e != null && cell.e.duplicate()) {
					sb.append("\\sout{").append(cell.txt).append("}&");
				} else {
					sb.append(cell.txt).append("&");
				}
			}
			sb.setLength(sb.length()-1);
			sb.append("\\\\\\hline\r\n");
		}
		
		sb.append("\\end{tabular}\r\n");
		
		return sb.toString();
				
	}

	@Override
	public String getColumnName(int column) {
		return getColNames()[column];
	}
	
	protected Cell getRaw(int row, int col) {
		if (rows.size() <= row) {return null;}
		if (rows.get(row).cells.size() <= col) {return null;}
		return rows.get(row).cells.get(col);
	}
	
	public void applySizes(final JTable tbl) {
		
		int widthMul = 1;
		for (int i = 0; i < q.getLevels().size(); ++i) {
			int width = 10 + 24 * widthMul; widthMul *= 2;
			if (width < 40) {width = 40;}
			if (tbl.getColumnModel().getColumnCount() < i*2+1) {continue;}
			tbl.getColumnModel().getColumn(i*2+0).setPreferredWidth(width);
			tbl.getColumnModel().getColumn(i*2+1).setPreferredWidth(70);
		}
		
	}

	
	
//	public TableColumnModel getColumnModel() {
//		final DefaultTableColumnModel mdl = new DefaultTableColumnModel();
//		for (final String col : getColum
//	}


		
}
