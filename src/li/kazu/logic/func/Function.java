package li.kazu.logic.func;

import java.util.ArrayList;
import java.util.List;

/** describes one logic function (minterms, maxterms, dont-care) */
public class Function {

	private String[] varNames;
	private int numVariables;
	private int numFields;
	private int[] values = new int[0];
	
	
	private ArrayList<FunctionListener> listeners = new ArrayList<>();
	
	/** the variable names to use (2-5) varNames[0] = LSB! */
	public void setVariables(String[] varNames) {
		
		final int numVariables = varNames.length;
		if (numVariables < 2 || numVariables > 5) {throw new RuntimeException("out of bounds");}
		
		this.varNames = varNames;
		this.numVariables = numVariables;
		this.numFields = (int) Math.pow(2, numVariables);
		this.values = new int[this.numFields];
		clear();
		
	}
	
	//private final String[] varNames = new String[] {"a", "b", "c", "d", "e"};
	
	/** get the function's variable names. idx[0] = LSB! */
	public String getVariableName(final int idx) {
		return varNames[idx];
	}
	
	/** is the current model data valid? */
	public boolean isValid() {
		return numFields > 0 && numVariables > 0;
	}
	
	/** attach a new listener to the function model */
	public void addListener(final FunctionListener l) {
		this.listeners.add(l);
	}

	/** remove an existing listener from the function model */
	public void removeListener(final FunctionListener l) {
		this.listeners.remove(l);
	}
	
	/** clear all values */
	public void clear() {
		for (int idx = 0; idx < values.length; ++idx) {
			values[idx] = 0;
		}
		onFunctionChanged();
	}
	
	/** get a list of all minterms */
	public List<Integer> getMinterms() {
		final ArrayList<Integer> terms = new ArrayList<>();
		for (int i = 0; i < numFields; ++i) {
			if (get(i) == 1) {terms.add(i);}
		}
		return terms;
	}
	
		/** add a new minterm to the KV */
	public void addMinterm(int nr, boolean fireChanged) {
		checkTermNr(nr);
		values[nr] = 1;
		if (fireChanged) {onFunctionChanged();}
	}
	
	/** add new minterms to the KV */
	public void addMinterms(int[] nrs) {
		for (int nr : nrs) {addMinterm(nr, false);}
		onFunctionChanged();
	}
	
	/** add a new maxterm to the KV */
	public void addMaxterm(int nr, boolean fireChanged) {
		checkTermNr(nr);
		values[nr] = 0;
		if (fireChanged) {onFunctionChanged();}
	}
	
	/** add new maxterms to the KV */
	public void addMaxterms(int[] nrs) {
		for (int nr : nrs) {addMaxterm(nr, false);}
		onFunctionChanged();
	}
	
	/** get the kv-entry for the given term-nr */
	public int get(int nr) {
		checkTermNr(nr);
		return values[nr];
	}
	
	public static final int not(int val) {
		switch(val) {
		case 0: return 1;
		case 1: return 0;
		default: return val;
		}
	}
	
	public int getNumVariables() {
		return this.numVariables;
	}
	
	public int getNumFields() {
		return this.numFields;
	}
	
	public void fireChanged() {
		this.onFunctionChanged();
	}
	
	private void onFunctionChanged() {
		for (final FunctionListener l : listeners) {
			l.onFunctionChanged(this);
		}
	}
		
	private final void checkTermNr(int nr) {
		if (nr < 0 || nr >= numFields) {throw new RuntimeException("out of bounds");}
	}
	
}
