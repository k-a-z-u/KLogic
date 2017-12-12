package li.kazu.logic;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class Helper {

	/** convert number to bit-string */
	public static final String toBits(final int nr, final int numVars) {
		String s = "";
		for (int i = numVars-1; i >= 0; --i) {
			s += ((nr & (1 << i)) != 0 ? "1" : "0");
		}
		return s;
	}
	
	public static void setClipboardContents(String aString){
	    StringSelection stringSelection = new StringSelection(aString);
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents(stringSelection, null);
	}
	
}
