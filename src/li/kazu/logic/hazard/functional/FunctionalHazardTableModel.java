package li.kazu.logic.hazard.functional;


import javax.swing.table.DefaultTableModel;

import li.kazu.logic.Helper;

@SuppressWarnings("serial")
public class FunctionalHazardTableModel extends DefaultTableModel {

	private FunctionalHazardCheckResult res;
	
	/** set the data-source */
	public void setData(final FunctionalHazardCheckResult res) {
		
		this.res = res;
		fireTableDataChanged();
		fireTableStructureChanged();
		
	}
	
	/** get the current data source */
	public FunctionalHazardCheckResult getData() {
		return this.res;
	}

	@Override
	public int getRowCount() {
		return (res == null) ? (0) : (res.entries.size());
	}
	
	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public Object getValueAt(int row, int column) {
		
		final int numVars = res.numVars;
		final FunctionalHazardCheckResult.Entry entry = res.entries.get(row);
		
		switch(column) {
			case 0: return entry.fromTermNr + " -> " + entry.toTermNr;
			case 1: return Helper.toBits(entry.fromTermNr, numVars) + " -> " + Helper.toBits(entry.toTermNr, numVars);
			case 2: return entry.hazard ? "ja" : "nein";
			default: return "??";
		}
		
	}
	
	

	@Override
	public String getColumnName(int column) {
		switch(column) {
			case 0: return "Übergang (Term -> Term)";
			case 1: return "Übergang (Wert -> Wert)";
			case 2: return "Hazard?";
			default: return "";
		}
	}
			
}