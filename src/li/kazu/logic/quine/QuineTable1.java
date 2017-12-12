package li.kazu.logic.quine;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JTable;

import li.kazu.logic.quine.QuineTable1Data.Cell;

@SuppressWarnings("serial")
public class QuineTable1 extends JTable implements MouseMotionListener {

	private final QuineTable1Listener l;
	private final QuineTable1Data data;
	
	/** ctor */
	public QuineTable1(final QuineTable1Listener l, final QuineTable1Data data) {
		super.addMouseMotionListener(this);
		setModel(data);
		this.data = data;
		this.l = l;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		final int row = rowAtPoint(e.getPoint());
		final int col = columnAtPoint(e.getPoint());
        final Cell cell = data.getRaw(row, col);
        if (cell != null && cell.e != null) {
        	l.onHoverTerms(cell.e.usedTerms);
        } else {
        	l.onHoverTerms(null);
        }
	}

	
	
}
