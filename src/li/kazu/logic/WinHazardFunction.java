package li.kazu.logic;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import li.kazu.logic.func.Function;
import li.kazu.logic.func.FunctionListener;
import li.kazu.logic.hazard.functional.FunctionalHazardCheck;
import li.kazu.logic.hazard.functional.FunctionalHazardCheckResult;
import li.kazu.logic.hazard.functional.FunctionalHazardTableModel;
import li.kazu.logic.hazard.functional.FunctionalHazardWaysTableModel;
import li.kazu.logic.hazard.functional.TermWayWithOutputChanges;

@SuppressWarnings("serial")
public class WinHazardFunction extends JInternalFrame implements FunctionListener {

	private final Function func;
	
	private final JRadioButton radDynamic = new JRadioButton("dynamisch");
	private final JRadioButton radStatic = new JRadioButton("statisch");
	private final JTextField txtFrom = new JTextField("0");
	private final JButton btnGo = new JButton("GO");
	private final JButton btnTeX =  new JButton("-> TeX");
	
	private final JLabel lblDesc = new JLabel("");
	
	private final JTable tbl1 = new JTable();
	private final FunctionalHazardTableModel mdl1 = new FunctionalHazardTableModel();
	
	private final JTable tbl2 = new JTable();
	private final FunctionalHazardWaysTableModel mdl2 = new FunctionalHazardWaysTableModel();
		
	private FunctionalHazardCheckResult curResult;
	
	public interface Listener {
		void onWayHover(TermWayWithOutputChanges w);
	}
		
	/** ctor */
	public WinHazardFunction(final Function func, final Listener l) {
		
		setTitle("Funktionshazards (BETA)");
		setLayout(new GridBagLayout());
		setSize(500, 450);
				
		tbl1.setModel(mdl1);
		tbl2.setModel(mdl2);

		this.func = func;
		func.addListener(this);
		
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.BOTH;
		
		final ButtonGroup grp = new ButtonGroup();
		grp.add(radDynamic);
		grp.add(radStatic);
		final JPanel pnlModes = new JPanel();
		pnlModes.add(radDynamic);	radDynamic.setSelected(true);
		pnlModes.add(radStatic);
		
		final JPanel pnlActions = new JPanel();
		pnlActions.add(btnTeX);
		
		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1; gbc.gridwidth = 1; gbc.gridheight = 1; add(new JLabel("Modus"), gbc);
		gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.gridwidth = 1; gbc.gridheight = 1; add(pnlModes, gbc);
		
		gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1; gbc.gridwidth = 1; gbc.gridheight = 1; add(new JLabel("Von Term"), gbc);
		gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.gridwidth = 1; gbc.gridheight = 1; add(txtFrom, gbc);
		
		gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.1; gbc.gridwidth = 1; gbc.gridheight = 2; add(btnGo, gbc);

		gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.1; gbc.gridwidth = 3; gbc.gridheight = 1; add(lblDesc, gbc);
		
		gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 1.0; gbc.weighty = 0.4; gbc.gridwidth = 3; gbc.gridheight = 1; add(new JScrollPane(tbl1), gbc);
		gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 1.0; gbc.weighty = 0.6; gbc.gridwidth = 3; gbc.gridheight = 1; add(new JScrollPane(tbl2), gbc);

		gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 1.0; gbc.weighty = 0.6; gbc.gridwidth = 3; gbc.gridheight = 1; add(pnlActions, gbc);


		
		btnGo.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				if (!func.isValid()) {
					JOptionPane.showMessageDialog(null, "Erst Funktion eingeben");
				} else {
					recalc();
				}
			}
		});
		
		tbl1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override public void valueChanged(ListSelectionEvent e) {
				final int idx = tbl1.getSelectedRow();
				if (idx == -1) {
					mdl2.setData(null);
				} else {
					mdl2.setData(mdl1.getData().entries.get(idx));
				}
			}
		});
		
		tbl2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override public void valueChanged(ListSelectionEvent e) {
				final int idx = tbl2.getSelectedRow();
				if (idx != -1) {
					final TermWayWithOutputChanges w = mdl2.getData().ways.get(idx);
					if (l != null) {l.onWayHover(w);}
				}
			}
		});
		
		btnTeX.addActionListener(new ActionListener() {
			@Override public void actionPerformed(final ActionEvent e) {
				final String tex = curResult.toTeX(func.getNumVariables());
				Helper.setClipboardContents(tex);
				System.out.println(tex);
			}
		});
		
	}

	@Override
	public void onFunctionChanged(final Function f) {
		recalc();
	}
	
	private void recalc() {
		
		mdl1.setData(null);
		mdl2.setData(null);
		
		try {
			
			FunctionalHazardCheck.Mode mode = null;
			if (radDynamic.isSelected())	{mode = FunctionalHazardCheck.Mode.DYNAMIC;}
			if (radStatic.isSelected())		{mode = FunctionalHazardCheck.Mode.STATIC;}
			
			if (mode == null) {
				JOptionPane.showMessageDialog(null, "kein Modus gewählt");
				return;
			}
			
			final int startTerm = Integer.parseInt(txtFrom.getText());
			final int startVal = func.get(startTerm);
			final int endVals = Function.not(startVal);
			curResult = FunctionalHazardCheck.checkFromTermNr(func, startTerm, mode);
			
			mdl1.setData(curResult);
			
			if (mode == FunctionalHazardCheck.Mode.DYNAMIC) {	
				lblDesc.setText("Dynamische " + startVal + "-" + endVals + " Hazards von Minterm " + startTerm + " zu allen " + endVals + "-Termen");
			} else if (mode == FunctionalHazardCheck.Mode.STATIC) {
				lblDesc.setText("Statische " + startVal + "-" + startVal + " Hazards von Minterm " + startTerm + " zu allen anderen " + startVal + "-Termen");
			}
		
		} catch (NumberFormatException e) {
			
		} catch (final Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
