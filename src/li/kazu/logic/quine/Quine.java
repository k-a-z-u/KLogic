package li.kazu.logic.quine;

import java.util.ArrayList;
import java.util.List;

import li.kazu.logic.func.Function;
import li.kazu.logic.func.FunctionListener;
import li.kazu.logic.func.GroupedMinterms;

public class Quine implements FunctionListener {

	
	public class Level {
		
		/** all entries for one level */
		public ArrayList<Entry> entries = new ArrayList<>(); 
		
	}
	
	private boolean showDuplicates = false;
	//private boolean aIsMSB = false;
	private int nextPrimeTerm = 0;
	Domination dom = null;
	private ArrayList<Entry> corePrimeTerms = new ArrayList<>();

	
	private ArrayList<QuineListener> listeners = new ArrayList<>();
	
	/** one entry per level */
	private final ArrayList<Level> levels = new ArrayList<>();
	
	/** the function to minimize */
	private final Function func;
	
	
	/** ctor */
	public Quine(final Function func) {
		this.func = func;
		this.func.addListener(this);
	}
	
	
	/** attach a new listener to the function model */
	public void addListener(final QuineListener l) {
		this.listeners.add(l);
	}

	/** remove an existing listener from the function model */
	public void removeListener(final QuineListener l) {
		this.listeners.remove(l);
	}
	
	
	
	/** function has changed. recalculate everything */
	public void onFunctionChanged(final Function f) {
		recalc();
	}
	
	/** recalculate everything */
	private void recalc() {
		
		// reset
		// 1st Level, contains all terms for 0th-Order (= minterms)
		this.nextPrimeTerm = 0;
		this.levels.clear();
		this.levels.add(new Level());	
		this.dom = null;
		
		if (func.isValid()) {
		
			// construct one entry for each minterm within the function
			for (final int nr : func.getMinterms()) {
				final Entry e = Entry.gen(nr, func.getNumVariables());
				levels.get(0).entries.add(e);
			}
			
			// minimize (1)
			while(_combine()) {;}
			final List<Entry> primeTerms = getPrimeTermsAll();
			
			// create domination table
			dom = new Domination(func.getNumVariables(), primeTerms);
			
			// before minimization: get all core prime terms
			final Integer[] corePrimeTermIndicies = dom.getCorePrimeTermIndices();
			corePrimeTerms.clear();
			for (final int i : corePrimeTermIndicies) {
				final Entry e = primeTerms.get(i);
				corePrimeTerms.add(e);
			}
			
			// minimize (2)
			dom.combine();
		
		}
		
		
		// inform listeners
		for (final QuineListener l : listeners) {
			l.onQuineChanged(this);
		}
		
	}
	
	public void setShowDuplicates(boolean show) {this.showDuplicates = show;}
	
	//public void setAMSB(boolean msb) {this.aIsMSB = msb;}
	
	public ArrayList<Level> getLevels() {return levels;}
	
	public List<Entry> getCorePrimeTerms() {
		return corePrimeTerms;
	}
	
//	public void combine() {
//		while(_combine()) {;}
//		final List<Entry> primeTerms = getPrimeTermsAll();
//		dom = new Domination(func.getNumVariables(), primeTerms);
//	}
	
	/**
	 * combine all terms within the current level
	 * returns true if there was at least one term that was combined
	 * @return
	 */
	private boolean _combine() {
		
		Log.add("combining all terms within level " + levels.size());
		
		boolean combined = false;
		
		final Level curLevel = levels.get(levels.size()-1);
		final int numEntries = curLevel.entries.size();
		
		final Level nextLevel = new Level();
		
		for (int i = 0; i < numEntries; ++i) {
			final Entry e1 = curLevel.entries.get(i);
			e1.isPrime = true;
		}
		
		for (int i = 0; i < numEntries; ++i) {
			final Entry e1 = curLevel.entries.get(i);
			
			for (int j = i+1; j < numEntries; ++j) {
				final Entry e2 = curLevel.entries.get(j);
				
				// only one parameter different between both entries? -> combine!
				if (e1.getDifference(e2) == 1) {
					
					e1.isPrime = false;
					e2.isPrime = false;
					final Entry e3 = Entry.combine(e1, e2);
					Log.add(" |- combining (" + e1 + ") and (" + e2 + ") to: (" + e3 + ")");
					
					// is this combination already covered by previous ones?
					e3.isDuplicate = coverExists(nextLevel, e3);
					
					if (showDuplicates || !e3.isDuplicate) {
						nextLevel.entries.add(e3);
						combined = true;
					} else {
						Log.add(" |--- combination (" + e3.usedTerms + ") already present. skipping.");	
					}
					
				}
								
			}
			
			// log and give each prime-term a unique number
			if (e1.prime()) {
				Log.add(" |- (" + e1 + ") is uncombineable -> prime!");
				e1.primeTermNr = nextPrimeTerm++;
			}
			
		}
		
		// found something to combine? -> append the newly created level
		if (combined) {
			levels.add(nextLevel);
		}
		
		// have we found something to combine?
		return combined;
		
	}
	
	private List<Entry> getPrimeTermsAll() {
		final ArrayList<Entry> lst = new ArrayList<>();
		for (final Level lvl : levels) {
			for (final Entry e : lvl.entries) {
				if (e.prime()) {
					lst.add(e);
				}
			}
		}
		return lst;
	}
	
	private List<Entry> getPrimeTermsCore() {
		final ArrayList<Entry> lst = new ArrayList<>();
		for (final Level lvl : levels) {
			for (final Entry e : lvl.entries) {
				if (e.prime()) {
					lst.add(e);
				}
			}
		}
		return lst;
	}
	
	public boolean wasSuccessful() {
		return dom != null && dom.wasSuccessful();
	}
	
	/** prime-terms after domination */
	private List<Entry> getPrimeTermsNeeded() {
		
		if (dom == null) {return new ArrayList<>();}
		if (!dom.wasSuccessful()) {return new ArrayList<>();}
		
		final List<Integer> indices = dom.getFinalPrimeTerms();
		final List<Entry> allTerms = getPrimeTermsAll();
		final ArrayList<Entry> res = new ArrayList<>();
		for (final int idx : indices) {
			res.add(allTerms.get(idx));
		}
		return res;
		
	}
	
	/** prime-terms after domination */
	public final GroupedMinterms getPrimeTermsNeededNew() {
		
		final GroupedMinterms res = new GroupedMinterms();
		if (dom == null) {return res;}
		
		final List<Integer> indices = dom.getFinalPrimeTerms();
		final List<Entry> allTerms = getPrimeTermsAll();
		for (final int idx : indices) {
			res.addGroup(allTerms.get(idx).usedTerms);
		}
		
		return res;
		
	}


	
//	public void dominate() {
//		dom.combine();		
//	}
	
	/** does the given level already contain an entry that covers the given terms? */
	public boolean coverExists(final Level l, final Entry e) {
		for (final Entry e2 : l.entries) {
			if (e2.usesSameTerms(e)) {return true;}
		}
		return false;
	}
	
//	/** add a new term (0. Ordnung) */
//	public void addTerm(final int nr) {
//		final Entry e = Entry.gen(nr, func.getNumVariables());
//		levels.get(0).entries.add(e);
//	}
//	
//	/** add new terms (0. Ordnung) */
//	public void addTerms(final int[] arr) {
//		for (int i : arr) {addTerm(i);}
//	}
//	
//	/** add new terms (0. Ordnung) */
//	public void addTerms(final List<Integer> lst) {
//		for (int i : lst) {addTerm(i);}
//	}
	
	public void dump() {
		
		for (int i = 0; i < levels.size(); ++i) {
			final Level l = levels.get(i);
			System.out.println("Level " + i);
			for (final Entry e : l.entries) {
				System.out.println("  " + e);
			}
			System.out.println("\r\n\r\n");
		}
		
		System.out.println("PRIME TERMS (all)");
		System.out.println("  " + getPrimeTermsAll());
		
		System.out.println("PRIME TERMS (minimum)");
		System.out.println("  " + getPrimeTermsNeeded());
		
	}
	
	private String termsToString(final List<Entry> terms) {
		
//		final int numVars = func.getNumVariables();
		
//		final char chars[] = new char[numVars];
//		for (int i = 0; i < numVars; ++i) {
//			chars[i] = aIsMSB ? (char)('a'+numVars-i-1) : (char)('a'+i);
//		}
		
		final StringBuilder sb = new StringBuilder();
		
		for (Entry e : terms) {
			sb.append('(');
			boolean empty = true;
			//for (int i = 0; i < e.mask.length(); ++i) {
			for (int i = e.mask.length()-1; i >= 0; --i) {
				int j = e.mask.length() - i - 1;
				char mask = e.mask.charAt(j);
				//char c = chars[i];
				final String var = func.getVariableName(i);
				if (mask == '0') {
					sb.append("!").append(var).append(" ");
					empty = false;
				} else if (mask == '1') {
					sb.append(var).append(" ");
					empty = false;
				}
			}
			if (empty) {sb.append("1");}
			if (sb.length() >= 1) {sb.setLength(sb.length()-1);}			// remove trailing " "
			sb.append(") + ");
		}
	
		if (sb.length() > 3) {sb.setLength(sb.length()-3);}		// remove trailing " + "
		
		return sb.toString();
				
		
	}
	
	public String getPrimeTermsAllStr() {
		return termsToString(getPrimeTermsAll());
	}
	
	public String getPrimeTermMinimumStr() {
		return termsToString(getPrimeTermsNeeded());
	}
	
	public String getCorePrimeTermsStr() {
		return termsToString(getCorePrimeTerms());
	}
	
	public Domination getDomination() {
		return dom;
	}
	
	
//		final char chars[] = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
//		
//		final StringBuilder sb = new StringBuilder();
//		
//		for (Level l : levels) {
//			for (Entry e : l.entries) {
//				if (e.isPrime) {
//					sb.append('(');
//					boolean empty = true;
//					for (int i = 0; i < e.mask.length(); ++i) {
//						int j = e.mask.length() - i - 1;
//						char mask = e.mask.charAt(j);
//						char c = chars[i];
//						if (mask == '0') {
//							sb.append("!").append(c).append(" * ");
//							empty = false;
//						} else if (mask == '1') {
//							sb.append(c).append(" * ");
//							empty = false;
//						}
//					}
//					if (empty) {sb.append("1");}
//					if (sb.length() >= 3) {sb.setLength(sb.length()-3);}			// remove trailing " * "
//					sb.append(") + ");
//				}
//			}	
//		}
//		
//		if (sb.length() > 3) {sb.setLength(sb.length()-3);}		// remove trailing " + "
//		
//		return sb.toString();
//				
//		// http://gym-neu.dyndns.org/~hws/hardware/Verfahren_nach_Quine_und_McCluskey.html
//		
//	}
	
}
