package li.kazu.logic;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import li.kazu.logic.func.Function;

@SuppressWarnings("serial")
public class WinInput extends JInternalFrame {

	
	private final JTextField txtTerms = new JTextField("0,1,4,5,6,7,8,9,11,15");	
	private final JTextField txtVars = new JTextField("4");	
	private final JButton btnOk = new JButton("uebernehmen");
	
	public WinInput(final Function func) {
		
		setTitle("Funktionseingabe");
		setSize(500, 100);
		
		final GridBagConstraints gbc = new GridBagConstraints();
		setLayout(new GridBagLayout());
		
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.BOTH;
		
		//final JPanel pnlCfg = new JPanel(new GridBagLayout());
		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1; gbc.gridwidth = 1; add(new JLabel("Minterme"), gbc);
		gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.gridwidth = 1; add(txtTerms, gbc);
		gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1; gbc.gridwidth = 1; add(new JLabel("Variablen"), gbc);
		gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.gridwidth = 1; add(txtVars, gbc);
		
		gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.0; gbc.gridwidth = 1; gbc.gridheight = 2; add(btnOk, gbc);
	
		btnOk.addActionListener(new ActionListener() {
			@Override public void actionPerformed(final ActionEvent e) {
				
				func.clear();
				
				final int numVariables = Integer.parseInt(txtVars.getText());
				func.setNumVariables(numVariables);
				
				final String terms[] = txtTerms.getText().split(",");
				for (final String term : terms) {
					func.addMinterm(Integer.parseInt(term.trim()));
				}
				
			}
		});
		
	}
	
	
}
