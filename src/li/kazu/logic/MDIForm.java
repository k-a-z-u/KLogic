package li.kazu.logic;

import java.util.ArrayList;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;

import li.kazu.logic.func.Function;
import li.kazu.logic.hazard.functional.TermWayWithOutputChanges;
import li.kazu.logic.hazard.structure.StructuralHazardCheckResult;
import li.kazu.logic.kv.KVData;
import li.kazu.logic.quine.Quine;
import li.kazu.logic.quine.QuineTable1Listener;

@SuppressWarnings("serial")
public class MDIForm extends JFrame implements QuineTable1Listener, WinHazardFunction.Listener, WinHazardStruct.Listener {
	
	private final Function func = new Function();
	private final Quine quine = new Quine(func);
	private final KVData kvData = new KVData(func);

	private final JDesktopPane desktop;
	private final WinKV winKV = new WinKV(kvData);
	private final WinInput winInp = new WinInput(func);
	private final WinHazardFunction winHazFunc = new WinHazardFunction(func, this);
	private final WinHazardStruct winHazStruct = new WinHazardStruct(func, quine, this);
	private final WinQuine winQuine = new WinQuine(quine, this);

	public MDIForm() {
		
		setTitle("KLogic v1.4");
		
		desktop = new JDesktopPane();
	    //createFrame(); //Create first window
	    setContentPane(desktop);
	    setVisible(true);
	    setSize(1300, 800);
	    //...
	    //Make dragging a little faster but perhaps uglier.
	    desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
	    
		winInp.setVisible(true);
		winInp.setLocation(10, 10);
		desktop.add(winInp);
	    
		winKV.setVisible(true);
		winKV.setLocation(630, 10);
		desktop.add(winKV);
		
		winQuine.setVisible(true);
		winQuine.setLocation(10, 128);
		desktop.add(winQuine);

		winHazFunc.setVisible(true);
		winHazFunc.setLocation(630, 270);
		desktop.add(winHazFunc);
		
		winHazStruct.setVisible(true);
		winHazStruct.setLocation(980, 10);
		desktop.add(winHazStruct);
			
	}

	@Override
	public void onHoverTerms(final ArrayList<Integer> terms) {
		if (terms != null) {
			kvData.select(terms);
		} else {
			kvData.clearSelection();
		}
	}

	@Override
	public void onWayHover(TermWayWithOutputChanges w) {
		if (w != null && w.way != null) {
			kvData.select(w.way);
		} else {
			kvData.clearSelection();
		}
	}
	
	@Override
	public void onStructuralHazardHover(StructuralHazardCheckResult.Entry e) {
		if (e != null) {
			kvData.clearSelection();
			kvData.select(e.fromTerm);
			kvData.select(e.toTerm);
		} else {
			kvData.clearSelection();
		}
	}
	
}
