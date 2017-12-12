package li.kazu.logic.kv;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class KVView extends JPanel implements KVDataListener {

	private KVData data = null;
	private KVRenderer renderer = new KVRenderer();
	
	/** ctor */
	public KVView() {
		;
	}
	
	/** ctor */
	public KVView(final KVData data) {
		setData(data);
	}
	
	/** set the underlying data model */
	public void setData(final KVData data) {
		if (this.data != null) {
			this.data.removeListener(this);
		}
		this.data = data;
		if (this.data != null) {
			this.data.addListener(this);
			onDataChanged(this.data);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		final Dimension dim = renderer.getPreferredSize(data);
		return (dim != null) ? (dim) : (super.getPreferredSize());
	}
	
	@Override
	public void onDataChanged(final KVData data) {
		invalidate();
		repaint();
	}

	@Override
	public void paint(final Graphics arg0) {
		
		renderer.render(data, (Graphics2D)arg0, this.getWidth(), this.getHeight());
		
	}
	

	
	
}
