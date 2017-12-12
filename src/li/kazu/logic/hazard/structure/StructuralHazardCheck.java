package li.kazu.logic.hazard.structure;

import java.util.List;

import li.kazu.logic.func.Function;
import li.kazu.logic.func.GroupedMinterms;


public class StructuralHazardCheck {

	
	/** check for structural hazards */
	public static StructuralHazardCheckResult checkFromTermNr(final Function func, final GroupedMinterms groups) {
		
		final StructuralHazardCheckResult res = new StructuralHazardCheckResult();
		
		for (final List<Integer> grp1 : groups.getGroups()) {
			for (final List<Integer> grp2 : groups.getGroups()) {
				
				if (grp1 == grp2) {continue;}
				
				for (int t1 : grp1) {
					for (int t2 : grp2) {
						
						if (t1 == t2) {continue;}
						if (!isAdjacent(t1,t2)) {continue;}
						if (grp2.contains(t1)) {continue;}
						if (grp1.contains(t2)) {continue;}
						
						// detected hazard
						final int v1 = func.get(t1);
						final int v2 = func.get(t2);
						if (v1 == v2) {
							res.add(t1, t2, v1, v2, StructuralHazardType.STATIC);
						} else {
							res.add(t1, t2, v1, v2, StructuralHazardType.DYNAMIC);
						}
						
					}
				}
				
			}
		}
		
		return res;
		
	}
	
	/** are the two given minterms adjacent? (only one bit changed) */
	private static boolean isAdjacent(final int i, final int j) {
		final int xor = i^j;
		if (xor > 64) {throw new RuntimeException("not yet supported");}
		return (xor == 1 || xor == 2 || xor == 4 || xor == 8 || xor == 16 || xor == 32 || xor == 64);
	}

	

	
}
