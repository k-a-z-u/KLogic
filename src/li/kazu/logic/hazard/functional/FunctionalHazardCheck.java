package li.kazu.logic.hazard.functional;

import java.util.ArrayList;
import java.util.List;

import li.kazu.logic.func.Function;

public class FunctionalHazardCheck {

	public enum Mode {
		STATIC,
		DYNAMIC,
	}
	
	public static FunctionalHazardCheckResult checkFromTermNr(final Function func, int startTerm, final Mode mode) {
	
		if (!func.isValid()) {
			return null;
		}
		
		final FunctionalHazardCheckResult res = new FunctionalHazardCheckResult(func.getNumVariables());
		
		// number of possible min/max terms
		final int numTerms = (int) Math.pow(2, func.getNumVariables());
		
		// are we searching from a "0" or a "1" ?
		final int startVal = func.get(startTerm);
		
		// dynamic:	perform all 0 -> 1 or 1 -> 0 checks (depending on startVal)
		// static:	perform all 0 -> 0 or 1 -> 1 checks (depending on startVal)
		
		for (int term = 0; term < numTerms; ++term) {
			
			// do not compare term with itself
			if (term == startTerm) {continue;}
			
			// get the output value of the term we are starting
			final int termVal = func.get(term);
			
			// is a dynamic change (0->1 or 1->0)
			// is a static change (1->1 or 1->1)
			if ( (mode == Mode.DYNAMIC && termVal != startVal) || (mode == Mode.STATIC && termVal == startVal) ) {
				
				System.out.println(startTerm + "->" + term);
				final List<TermWay> perms = getPossibleWaysAlongKV(numTerms, startTerm, term);
				
				final ArrayList<TermWayWithOutputChanges> resWays = new ArrayList<>();
				
				boolean allHom = true;
				for (TermWay way : perms) {
					OutputChanges outChanges = getOutputChanges(func, way);
					boolean isHom = outChanges.isHomogenous();
					if (!isHom) {allHom = false;}
					System.out.println(" - " + outChanges);
					System.out.println(" - " + isHom);
					final TermWayWithOutputChanges resWay = new TermWayWithOutputChanges(way, outChanges);
					resWays.add(resWay);
				}
				
				final FunctionalHazardCheckResult.Entry entry = new FunctionalHazardCheckResult.Entry(startTerm, term, !allHom, resWays);
				res.entries.add(entry);
				
			}
		}
		
		return res; 
	
	}
	
	/** get the output changes denoted by the path of terms, e,g from 1,2,3,4, how does the output (0/1) change? */
	private static final OutputChanges getOutputChanges(final Function func, List<Integer> terms) {
		final OutputChanges changes = new OutputChanges();
		for (final int termNr : terms) {
			changes.add(func.get(termNr));
		}
		return changes;
	}
	
	
	
	/**
	 * get all binary permutations from term1 to term2
	 * e.g. from 0 -> 7 = 000 -> 111
	 * is 000 001 011 111 or 000 001 101 111 or 000 100 101 111, .....
	 * is   0   1   3   7 or   0   1   5   7 or   0   4   5   7
	 * @param numTerms
	 * @param term1
	 * @param term2
	 * @return
	 */
	public static final List<TermWay> getPossibleWaysAlongKV(int numTerms, int term1, int term2) {
		
		// bitmask of all bits that are changed between term1 and term2
		final int changingBits = term1 ^ term2;
		
		final ArrayList<Integer> bits = new ArrayList<>();
		for (int i = 0; i < 31; ++i) {
			if ( (changingBits & (1 << i)) != 0 ) {
				bits.add(i);
			}
		}
		
		// all possible permutations of the changing bits
		final List<List<Integer>> bitPerms = new ArrayList<>();
		permute(bits, 0, bitPerms);
		System.out.println(" " + bitPerms);
	
		// convert bits back to term numbers -> all possible walks along the diagram
		final List<TermWay> termPerms = new ArrayList<>();
		for (final List<Integer> bitPerm : bitPerms) {
			termPerms.add(bitsToTermNumbers(term1, term2, bitPerm));
		}
		
		System.out.println(termPerms);
		
		return termPerms;
		
	}
	
	public static void permute(List<Integer> arr, int k, List<List<Integer>> res){
        for(int i = k; i < arr.size(); i++){
            java.util.Collections.swap(arr, i, k);
            permute(arr, k+1, res);
            java.util.Collections.swap(arr, k, i);
        }
        if (k == arr.size() -1){
        	final ArrayList<Integer> sub = new ArrayList<>();
        	sub.addAll(arr);
        	res.add(sub);
            //System.out.println(java.util.Arrays.toString(arr.toArray()));
        }
    }
	
	public static TermWay bitsToTermNumbers(int term1, int term2, final List<Integer> bits) {
		final TermWay terms = new TermWay();
		int sum = term1;
		terms.add(sum);					// starting value
		for (int bit : bits) {
			int bitVal = (1 << bit);
			if ( (term2 & bitVal) != 0 ) {
				sum += bitVal;			// set bit
			} else {
				sum -= bitVal;			// remove bit
			}
			terms.add(sum);
		}
		return terms;
	}
	
}
