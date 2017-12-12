package li.kazu.logic;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JInternalFrame;

import li.kazu.logic.kv.KVData;
import li.kazu.logic.kv.KVView;

@SuppressWarnings("serial")
public class WinKV extends JInternalFrame {

	private final KVView kv = new KVView();
	
	/** ctor */
	public WinKV(final KVData data) {
		
		setSize(340,250);
		setMinimumSize(new Dimension(100,100));
		setTitle("KV Diagramm");
		setLayout(new GridLayout(1,1));
		
		add(kv);
		kv.setData(data);
		setResizable(true);
		
	}
	
	@Override
	public Dimension getPreferredSize() {
		return kv.getPreferredSize();
	}
	
}
