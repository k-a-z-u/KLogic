package li.kazu.logic;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import li.kazu.logic.quine.Quine;
import li.kazu.logic.quine.QuineListener;
import li.kazu.logic.quine.QuineTable1;
import li.kazu.logic.quine.QuineTable1Data;
import li.kazu.logic.quine.QuineTable1Listener;
import li.kazu.logic.quine.QuineTable2Data;

@SuppressWarnings("serial")
public class WinQuine extends JInternalFrame implements QuineListener {

	
	private final QuineTable1Data mdl1 = new QuineTable1Data();
	private final QuineTable2Data mdl2 = new QuineTable2Data();

	private final QuineTable1 tblResult1;
	private final JTable tblResult2 = new JTable();
	
	private final JButton btnTable1Tex = new JButton("Table1 to TeX");
	private final JButton btnTable2Tex = new JButton("Table2 to TeX");
	
	private final JTextField txtPrime = new JTextField();
	private final JTextField txtCorePrime = new JTextField();

	private final Quine quine;
	
	/** ctor */
	public WinQuine(final Quine quine, final QuineTable1Listener listener) {
		
		setTitle("Quine McCluskey");
		setLayout(new GridBagLayout());
		setSize(600, 600);
		setResizable(true);
		
		this.quine = quine;
		this.quine.addListener(this);
		
		tblResult1 = new QuineTable1(listener, mdl1);
		tblResult2.setModel(mdl2);
		
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.weighty = 0.7; gbc.gridwidth = 1; add(new JScrollPane(tblResult1), gbc);
		gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 1.0; gbc.weighty = 0.3; gbc.gridwidth = 1; add(new JScrollPane(tblResult2), gbc);

		gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 1; gbc.gridwidth = 1; gbc.weighty = 0; add(new JLabel("Primimplikanten"), gbc);
		gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 1; gbc.gridwidth = 1; gbc.weighty = 0; add(txtPrime, gbc);
		gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 1; gbc.gridwidth = 1; gbc.weighty = 0; add(new JLabel("Kernprimimplikanten"), gbc);
		gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 1; gbc.gridwidth = 1; gbc.weighty = 0; add(txtCorePrime, gbc);
		
		final JPanel pnlOpts = new JPanel();
		pnlOpts.add(btnTable1Tex);
		pnlOpts.add(btnTable2Tex);
		
		gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 1; gbc.gridwidth = 1; gbc.weighty = 0; add(pnlOpts, gbc);
		
	}

	@Override
	public void onQuineChanged(final Quine q) {
		
		txtPrime.setText(q.getPrimeTermsAllStr());
		txtCorePrime.setText(q.getPrimeTermMinimumStr());
				
		mdl1.setData(q);
		mdl1.applySizes(tblResult1);
		
		mdl2.setData(q.getDomination());
		
//		final String[] colNames2 = new String[dCols+1];
//		colNames2[0] = "";
//		for (int col = 0; col < dCols; ++col) {colNames2[col+1] = col+"";}
//		final String[][] data2 = new String[dRows][];
//		for (int row = 0; row < dRows; ++row) {
//			data2[row] = new String[dCols+1];
//			data2[row][0] = ""+(char)('A'+row);
//			for (int col = 0; col < dCols; ++col) {
//				data2[row][col+1] = d.get(row,col) ? "x" : "";
//			}
//		}
//		
//		TableModel mdl2 = new DefaultTableModel(data2, colNames2);
//		tblResult2.setModel(mdl2);
		
//		q.dominate();
//		q.dump();
		
	}
	

	/*
	@Override
	public void onQuineChanged(Function f) {
		
		if (!f.isValid()) {return;}
		
		final List<Integer> minTerms = f.getMinterms();
		if (minTerms.isEmpty()) {return;}
		
		
		final Quine q = new Quine(f.getNumVariables());
		//q.setShowDuplicates(chkDuplicates.isSelected());
		//q.setAMSB(chkAMSB.isSelected());
		q.addTerms(minTerms);
				
		q.combine();
		

		final Domination d = q.getDomination();
		int dCols = d.numCols();
		int dRows = d.numRows();
		
//		final String[] colNames2 = new String[dCols+1];
//		colNames2[0] = "";
//		for (int col = 0; col < dCols; ++col) {colNames2[col+1] = col+"";}
//		final String[][] data2 = new String[dRows][];
//		for (int row = 0; row < dRows; ++row) {
//			data2[row] = new String[dCols+1];
//			data2[row][0] = ""+(char)('A'+row);
//			for (int col = 0; col < dCols; ++col) {
//				data2[row][col+1] = d.get(row,col) ? "x" : "";
//			}
//		}
//		
//		TableModel mdl2 = new DefaultTableModel(data2, colNames2);
//		tblResult2.setModel(mdl2);
		
//		q.dominate();
//		q.dump();
				
		txtPrime.setText(q.getPrimeTermsAllStr());
		txtCorePrime.setText(q.getPrimeTermMinimumStr());
				
		mdl1.setData(q);
		mdl1.applySizes(tblResult1);
		
	}
	*/
}
