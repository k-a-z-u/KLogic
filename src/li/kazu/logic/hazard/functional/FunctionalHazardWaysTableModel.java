package li.kazu.logic.hazard.functional;

import javax.swing.table.DefaultTableModel;

/** display all possible ways from start to all other relevant terms */
@SuppressWarnings("serial")
public class FunctionalHazardWaysTableModel extends DefaultTableModel {

	private FunctionalHazardCheckResult.Entry entry;
	
	/** set the data-source */
	public void setData(final FunctionalHazardCheckResult.Entry entry) {
		
		this.entry = entry;
		fireTableDataChanged();
		fireTableStructureChanged();
		
	}

	/** get the current data source */
	public FunctionalHazardCheckResult.Entry getData() {
		return this.entry;
	}
	
	@Override
	public int getRowCount() {
		return (entry == null) ? (0) : (entry.ways.size());
	}
	
	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public Object getValueAt(int row, int column) {
		
		final TermWayWithOutputChanges way = entry.ways.get(row);
		
		switch(column) {
			case 0: return way.way;
			case 1: return way.changes;
			case 2: return way.isMonotonic() ? "nein" : "ja";
			default: return "??";
		}
		
	}
	
	

	@Override
	public String getColumnName(int column) {
		switch(column) {
			case 0: return "Weg";
			case 1: return "Ausgangsänderung";
			case 2: return "Hazard?";
			default: return "";
		}
	}
			
}
