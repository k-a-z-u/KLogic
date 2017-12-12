package li.kazu.logic.func;

import java.util.ArrayList;
import java.util.List;

public class GroupedMinterms {

	private final ArrayList<List<Integer>> groups = new ArrayList<>();
	
	private ArrayList<GroupedMintermsListener> listeners = new ArrayList<>();

	
	public ArrayList<List<Integer>> getGroups() {
		return groups;
	}
	
	/** attach a new listener to the function model */
	public void addListener(final GroupedMintermsListener l) {
		this.listeners.add(l);
	}

	/** remove an existing listener from the function model */
	public void removeListener(final GroupedMintermsListener l) {
		this.listeners.remove(l);
	}
	
	/** clear all values */
	public void clear() {
		groups.clear();
		onChanged();
	}
	
	/** add a new group, that spans the given minterms (e.g. primterm, core-primeterm) */
	public void addGroup(final List<Integer> mintermGroup) {
		groups.add(mintermGroup);
		onChanged();
	}
	
	private void onChanged() {
		for (final GroupedMintermsListener l : listeners) {
			l.onGroupChanged(this);
		}
	}
	
}
