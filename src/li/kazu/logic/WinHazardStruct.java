package li.kazu.logic;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import li.kazu.logic.func.Function;
import li.kazu.logic.hazard.structure.StructuralHazardCheck;
import li.kazu.logic.hazard.structure.StructuralHazardCheckResult;
import li.kazu.logic.hazard.structure.StructuralHazardTableModel;
import li.kazu.logic.quine.Quine;
import li.kazu.logic.quine.QuineListener;

@SuppressWarnings("serial")
public class WinHazardStruct extends JInternalFrame implements QuineListener {
	
	private final JTable tbl1 = new JTable();
	private final StructuralHazardTableModel mdl1 = new StructuralHazardTableModel();
		
	public interface Listener {
		void onStructuralHazardHover(StructuralHazardCheckResult.Entry e);
	}
		
	private final Quine quine;
	private final Function func;
	
	/** ctor */
	public WinHazardStruct(final Function func, final Quine quine, final Listener l) {
		
		setTitle("Statische Strukturhazards (BETA)");
		setLayout(new GridBagLayout());
		setSize(500, 200);
		
		this.func = func;
		this.quine = quine;
		this.quine.addListener(this);
		
		tbl1.setModel(mdl1);
		
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.BOTH;
		
		final JLabel lblDesc = new JLabel("Auf Basis der Kernprimimplikanten aus Quine/McCluskey");
		
		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.weighty = 0.0; gbc.gridwidth = 1; gbc.gridheight = 1; add(lblDesc, gbc);
		gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 1.0; gbc.weighty = 1.0; gbc.gridwidth = 1; gbc.gridheight = 1; add(new JScrollPane(tbl1), gbc);
		
		tbl1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override public void valueChanged(ListSelectionEvent evt) {
				final int idx = tbl1.getSelectedRow();
				if (idx != -1) {
					final StructuralHazardCheckResult.Entry e = mdl1.getData().getEntries().get(idx);
					if (l != null) {l.onStructuralHazardHover(e);}
				}
			}
		});
		
	}

	@Override
	public void onQuineChanged(final Quine q) {
		recalc();
	}
	
	private void recalc() {
		
		mdl1.setData(null);
		
		try {
			
			final StructuralHazardCheckResult res = StructuralHazardCheck.checkFromTermNr(func, quine.getPrimeTermsNeededNew());
			mdl1.setData(res);

		} catch (final Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
