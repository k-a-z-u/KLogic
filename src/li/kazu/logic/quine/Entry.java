package li.kazu.logic.quine;

import java.util.ArrayList;

public final class Entry {

	public ArrayList<Integer> usedTerms = new ArrayList<>();
	
	public String mask;
	
	/** ordered prime-term nr (if isPrime) */
	public int primeTermNr = -1;
	
	public boolean isPrime;
	
	/** this entry's combination is already covered by another entry (this is just for learning purpose!) */
	public boolean isDuplicate;
	
	public Entry() {
		
	}

	/** copy ctor */
	public Entry(final Entry o) {
		this.mask = o.mask;
		this.usedTerms.addAll(o.usedTerms);
		this.primeTermNr = o.primeTermNr;
		this.isDuplicate = o.isDuplicate;
		this.isPrime = o.isPrime;
	}
	
	public boolean prime() {
		return isPrime && !isDuplicate;
	}
	
	public boolean duplicate() {
		return isDuplicate;
	}
	
	/** generate a new entry. (0. ORdnung) */
	public static final Entry gen(final int nr, final int numTerms) {
		final Entry e = new Entry();
		e.usedTerms.add(nr);
		e.mask = padLeft(Integer.toString(nr, 2), numTerms);
		if (e.mask.length() > numTerms) {throw new RuntimeException("something fishy. mask is longer than given number-of-terms!");}
		return e;
	}
	
	/** does this entry use exactly the same terms as e ? */
	public boolean usesSameTerms(final Entry e) {
		return usedTerms.containsAll(e.usedTerms);
	}
	
	private static String padLeft(String s, final int n) {
		while (s.length() < n) {
			s = "0" + s;
		}
		return s;
	}
	
	public static final Entry combine(final Entry e1, final Entry e2) {
		final Entry e = new Entry();
		e.usedTerms.addAll(e1.usedTerms);
		e.usedTerms.addAll(e2.usedTerms);
		e.mask = combineMask(e1.mask, e2.mask);
		return e;
	}
	
	private static final String combineMask(final String s1, final String s2) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s1.length(); ++i) {
			final char c1 = s1.charAt(i);
			final char c2 = s2.charAt(i);
			if (c1 == c2) {sb.append(c1);} else {sb.append('-');}
		}
		return sb.toString();
	}

	
	/** get the number of different fields within the mask of both entries */
	public final int getDifference(final Entry o) {
				
		int diff = 0;
		for (int i = 0; i < mask.length(); ++i) {
			if (mask.charAt(i) != o.mask.charAt(i)) {++diff;}
		}
		return diff;
		
	}
	
	@Override
	public String toString() {
		String s = mask + " " + usedTerms.toString();
		if (prime())		{s += " IS_PRIME";}
		if (duplicate())	{s += " IS_DUPLICATE";}
		return s;
	}
	
}
