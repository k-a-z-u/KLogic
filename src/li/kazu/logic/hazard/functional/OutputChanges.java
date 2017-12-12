package li.kazu.logic.hazard.functional;

import java.util.ArrayList;

/** output changes of the kv-diagram, alogn a certain way */
@SuppressWarnings("serial")
public class OutputChanges extends ArrayList<Integer> {
	
	public boolean isHomogenous() {
		return OutputChanges.homogenous(this);
	}
	
	/** is the given list of output values homogenous? (only one change by comparing each neighbor pair) */
	private static final boolean homogenous(final OutputChanges outValues) {
		int numChanges = 0;
		for (int i = 0; i < outValues.size()-1; ++i) {
			if (outValues.get(i) != outValues.get(i+1)) {++numChanges;}
		}
		return numChanges <= 1;
	}
	
}