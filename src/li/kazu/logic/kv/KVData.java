package li.kazu.logic.kv;

import java.util.ArrayList;

import li.kazu.logic.func.Function;
import li.kazu.logic.func.FunctionListener;

public class KVData implements FunctionListener {

	private Function func;
	private ArrayList<Integer> selection = new ArrayList<>();
	private ArrayList<KVDataListener> listeners = new ArrayList<>();
	
	public KVData(final Function func) {
		setFunction(func);
	}
	
	/** the function to display */
	public void setFunction(final Function func) {
		if (this.func != null) {
			this.func.removeListener(this);
		}
		this.func = func;
		if (this.func != null) {
			this.func.addListener(this);
		}
		onDataChanged();
	}
	
	public String getVariableName(final int idx) {
		return func.getVariableName(idx);
	}
	
	/** attach a new listener to the model */
	public void addListener(final KVDataListener l) {
		this.listeners.add(l);
	}

	/** remove an existing listener from the model */
	public void removeListener(final KVDataListener l) {
		this.listeners.remove(l);
	}
	
	/** visibly select the given term */
	public void select(int nr) {
		selection.add(nr);
		onDataChanged();
	}
	
	/** visibly select the given term */
	public void select(final ArrayList<Integer> nrs) {
		selection.clear();
		selection.addAll(nrs);
		onDataChanged();
	}
	
	/** remove term selection */
	public void clearSelection() {
		selection.clear();
		onDataChanged();
	}
		
	/** get the kv-entry for the given term-nr */
	public int get(int nr) {
		return this.func.get(nr);
	}
	
	public int getNumVariables() {
		return this.func.getNumVariables();
	}
	
	public int getNumFields() {
		return this.func.getNumFields();
	}
	
	private void onDataChanged() {
		for (final KVDataListener l : listeners) {
			l.onDataChanged(this);
		}
	}
	
	/** is the given term currently selected? */
	public boolean isSelected(int nr) {
		return selection.contains(nr);
	}

	@Override
	public void onFunctionChanged(Function f) {
		onDataChanged(); // inform listeners
	}
	
}
