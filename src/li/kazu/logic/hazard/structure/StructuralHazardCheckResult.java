package li.kazu.logic.hazard.structure;

import java.util.ArrayList;


public class StructuralHazardCheckResult {

	public class Entry {
		
		public final int fromTerm;
		public final int toTerm;
		public final int fromVal;
		public final int toVal;
		public final StructuralHazardType type;
		
		Entry(final int fromTerm, final int toTerm, final int fromVal, final int toVal, final StructuralHazardType type) {
			this.fromTerm = fromTerm;
			this.toTerm = toTerm;
			this.fromVal = fromVal;
			this.toVal = toVal;
			this.type = type;
		}
		
		@Override
		public boolean equals(final Object o) {
			if ( !(o instanceof Entry) ) {return false;}
			final Entry e2 = (Entry) o;
			return (e2.fromTerm == fromTerm && e2.toTerm == toTerm) || (e2.fromTerm == toTerm && e2.toTerm == fromTerm);	// bi-directional!
		}
		
		public final String getDesc() {
			switch(type) {
			case DYNAMIC: return "dynamischer " + fromVal + "-" + toVal + " Hazard";
			case STATIC: return "statischer " +fromVal + " Hazard";
			default: throw new RuntimeException("not yet supported");
			}
		}
		
	}
	
	private ArrayList<Entry> entries = new ArrayList<>();
	
	/** add detected hazard when changing from term t1 to term t2 (with value change from v1 to v2) */
	public void add(final int t1, final int t2, final int v1, final int v2, final StructuralHazardType type) {
		final Entry e = new Entry(t1, t2, v1, v2, type);
		if (!entries.contains(e)) {
			entries.add(e);			// only add one direction
		}
	}
	
	public ArrayList<Entry> getEntries() {
		return entries;
	}
	
}
