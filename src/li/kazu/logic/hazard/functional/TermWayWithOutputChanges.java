package li.kazu.logic.hazard.functional;



/** way along several,adjacent minterms including corresponding output changes */
public class TermWayWithOutputChanges {
		
	public final TermWay way;
	public final OutputChanges changes;
	
	public TermWayWithOutputChanges(final TermWay way, final OutputChanges changes) {
		this.way = way;
		this.changes = changes;
	}
	
	@Override
	public String toString() {
		return way + " -> " + changes;
	}
	
	/** is the output-change homogenous? (max) 1 change from 0->1 or 1->0 */
	public boolean isMonotonic() {
		return this.changes.isHomogenous();
	}
	
	
}
