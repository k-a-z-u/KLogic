package li.kazu.logic.hazard.functional;

import java.util.ArrayList;
import java.util.List;

import li.kazu.logic.Helper;

/** result of function hazard check */
public class FunctionalHazardCheckResult {
		
	public static class Entry {
		
		public final int fromTermNr;
		public final int toTermNr;
		public final boolean hazard;
		public final List<TermWayWithOutputChanges> ways;
		
		Entry(int fromTerm, int toTerm, boolean hazard, List<TermWayWithOutputChanges> ways) {
			this.fromTermNr = fromTerm;
			this.toTermNr = toTerm;
			this.hazard = hazard;
			this.ways = ways;
		}
		
		@Override
		public String toString() {
			return fromTermNr + " -> " + toTermNr + " dyn func hazard? " + hazard + "\r\n" + ways;
		}
		
	}
	
	
	public final int numVars;
	public final List<Entry> entries = new ArrayList<>();
	
	/** ctor */
	public FunctionalHazardCheckResult(final int numVars) {
		this.numVars = numVars;
	}
	
	public String toTeX(int numVariables) {
		final StringBuilder sb = new StringBuilder();
		sb.append("\\begin{tabular}{|c|c|p{4.8cm}|p{4.2cm}|c|}\\hline\r\n");
		sb.append("Wechsel&&Moegliche Wege&Ausgangsaenderung je Weg&Hazard?\\\\\\hline\r\n");
		for (final Entry e : entries) {
			sb.append("$").append(e.fromTermNr).append(" \\rightarrow ").append(e.toTermNr).append("$").append(" & ");
			sb.append("$").append(Helper.toBits(e.fromTermNr, numVariables)).append(" \\rightarrow ").append(Helper.toBits(e.toTermNr, numVariables)).append("$").append(" & ");
			for (int i = 0; i < e.ways.size(); ++i) {
				if (i % 2 == 0 && i != 0) {sb.append("\\newline");}
				sb.append("\\mbox{").append(e.ways.get(i).way).append("}");
			} sb.append("&");
			for (int i = 0; i < e.ways.size(); ++i) {
				if (i % 2 == 0 && i != 0) {sb.append("\\newline");}
				final boolean isHom = e.ways.get(i).changes.isHomogenous();
				sb.append("\\mbox{").append(!isHom ? "\\bf" : "").append(e.ways.get(i).changes).append("} ");
			} sb.append("&");
			sb.append( e.hazard ? "ja" : "nein");
			sb.append("\\\\\\hline\r\n");
		}
		sb.append("\\end{tabular}\r\n");
		return sb.toString();
	}
			
}
