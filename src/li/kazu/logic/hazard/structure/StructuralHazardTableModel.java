package li.kazu.logic.hazard.structure;

import javax.swing.table.DefaultTableModel;


@SuppressWarnings("serial")
public class StructuralHazardTableModel extends DefaultTableModel {

	private StructuralHazardCheckResult res;
	
	/** set the data-source */
	public void setData(final StructuralHazardCheckResult res) {
		
		this.res = res;
		fireTableDataChanged();
		fireTableStructureChanged();
		
	}

	/** get the current data source */
	public StructuralHazardCheckResult getData() {
		return this.res;
	}
	
	@Override
	public int getRowCount() {
		return (res == null) ? (0) : (res.getEntries().size());
	}
	
	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int row, int column) {
		
		final StructuralHazardCheckResult.Entry e = this.res.getEntries().get(row);
		
		switch(column) {
			case 0: return e.fromTerm + " <-> " + e.toTerm;
			case 1: return e.getDesc();
			default: return "??";
		}
		
	}
	
	

	@Override
	public String getColumnName(int column) {
		switch(column) {
			case 0: return "Übergang";
			case 1: return "Hazard-Type";
			default: return "";
		}
	}
			
}
