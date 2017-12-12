package li.kazu.logic;
//package de.fhws.gti;
//
//import java.awt.Dimension;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
//import java.awt.Insets;
//import java.awt.Toolkit;
//import java.awt.datatransfer.Clipboard;
//import java.awt.datatransfer.StringSelection;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JFrame;
//import javax.swing.JInternalFrame;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.JTextField;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableModel;
//
//import de.fhws.gti.kv.KVData;
//import de.fhws.gti.kv.KVView;
//import de.fhws.gti.quine.Domination;
//import de.fhws.gti.quine.Quine;
//import de.fhws.gti.quine.QuineTable1;
//import de.fhws.gti.quine.QuineTable1Data;
//import de.fhws.gti.quine.QuineTable1Listener;
//
//public class UI extends JFrame implements QuineTable1Listener {
//	
//	// 3,6,7,8,9,12				(a * b * !d) + (b * c * !d) + (!b * !c * d) + (!a * !b * d)
//	// 2,4,6,8,9,12,14
//	// 0,1,4,5,6,7,8,9,11,15
//
//	private static final long serialVersionUID = 1L;
//	private final JTextField txtTerms = new JTextField("0,1,4,5,6,7,8,9,11,15");	
//	private final JTextField txtVars = new JTextField("4");	
//
//	private final QuineTable1Data mdl1 = new QuineTable1Data();
//	private final QuineTable1 tblResult1 = new QuineTable1(this, mdl1);
//	
//	private final JTable tblResult2 = new JTable();
//
//	private final JButton btnRun = new JButton("go!");
//	
//	private final JButton btnTable1Tex = new JButton("Table1 to TeX");
//	private final JButton btnTable2Tex = new JButton("Table2 to TeX");
//
//	private final JCheckBox chkDuplicates = new JCheckBox("show duplicates");
//	private final JCheckBox chkAMSB = new JCheckBox("A = MSB");
//
//	private final JTextField txtPrimeTerms = new JTextField();
//
//	private final JTextField txtResult = new JTextField();
//	
//	private final KVData kvData = new KVData();
//	private final KVView kv = new KVView(kvData);
//
//	public UI() {
//		
//
//		
//		setTitle("Quine McCluskey Minimierung v1.2 © Frank Ebner");
//		
//		tblResult1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		
//		final GridBagConstraints gbc = new GridBagConstraints();
//		gbc.fill = GridBagConstraints.BOTH;
//
//		
//		final JPanel pnlCfg = new JPanel(new GridBagLayout());
//		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1; gbc.gridwidth = 1; pnlCfg.add(new JLabel("Minterme"), gbc);
//		gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.gridwidth = 1; pnlCfg.add(txtTerms, gbc);
//		gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1; gbc.gridwidth = 1; pnlCfg.add(new JLabel("Variablen"), gbc);
//		gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.gridwidth = 1; pnlCfg.add(txtVars, gbc);
//		
//		final JPanel pnlOpt = new JPanel();
//		pnlOpt.add(chkDuplicates);
//		pnlOpt.add(chkAMSB);
//		
//		final JPanel pnlTbl = new JPanel(new GridBagLayout());
//		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.gridwidth = 1; gbc.weighty = 0.66; pnlTbl.add(new JScrollPane(tblResult1), gbc);
//		gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 1.0; gbc.gridwidth = 1; gbc.weighty = 0.33; pnlTbl.add(new JScrollPane(tblResult2), gbc);
//		
//		final JPanel pnlRes = new JPanel(new GridBagLayout());
//		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1; gbc.gridwidth = 1; gbc.weighty = 0; pnlRes.add(new JLabel("Primimplikanten"), gbc);
//		gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 1; gbc.gridwidth = 1; gbc.weighty = 0; pnlRes.add(txtPrimeTerms, gbc);
//		gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 1; gbc.gridwidth = 1; gbc.weighty = 0; pnlRes.add(new JLabel("Kernprimimplikanten"), gbc);
//		gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 1; gbc.gridwidth = 1; gbc.weighty = 0; pnlRes.add(txtResult, gbc);
//		
//		final JPanel pnlAct = new JPanel();
//		pnlAct.add(btnTable1Tex);
//		pnlAct.add(btnTable2Tex);
//		pnlAct.add(btnRun);
//
//
//		setLayout(new GridBagLayout());
//		
//		gbc.insets = new Insets(5, 5, 5, 5);
//		
//		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5; gbc.gridwidth = 1; gbc.gridheight = 1; gbc.weighty = 0; add(pnlCfg, gbc);
//		gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.5; gbc.gridwidth = 1; gbc.gridheight = 1; gbc.weighty = 0; add(pnlOpt, gbc);
//		gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.5; gbc.gridwidth = 1; gbc.gridheight = 1; gbc.weighty = 0; add(pnlAct, gbc);
//		gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.5; gbc.gridwidth = 1; gbc.gridheight = 3; gbc.weighty = 0; add(kv, gbc);
//		gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 1.0; gbc.gridwidth = 2; gbc.gridheight = 1; gbc.weighty = 1; add(pnlTbl, gbc);
//		gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 1.0; gbc.gridwidth = 2; gbc.gridheight = 1; gbc.weighty = 0; add(pnlRes, gbc);
//
//
//		setSize(new Dimension(800, 800));
//		setVisible(true);
//		
//	
//		
//		btnRun.addActionListener(new ActionListener() {
//			@Override public void actionPerformed(ActionEvent e) {
//				
//				try {
//				recalc();
//				} catch (final Exception ex) {
//					ex.printStackTrace();
//					JOptionPane.showMessageDialog(null, "Error\r\n" + ex.getMessage());
//				}
//				
//			}
//		});
//		
//		btnTable1Tex.addActionListener(new ActionListener() {
//			@Override public void actionPerformed(ActionEvent e) {
//				final String tex = mdl1.toTeX();
//				setClipboardContents(tex);
//				System.out.println(tex);		
//			}
//		});
//		
//		btnTable2Tex.addActionListener(new ActionListener() {
//			@Override public void actionPerformed(ActionEvent e) {
//				exportTable2();
//			}
//		});
//		
//	}
//	
//	public void setClipboardContents(String aString){
//	    StringSelection stringSelection = new StringSelection(aString);
//	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//	    clipboard.setContents(stringSelection, null);
//	}
//	
//	private Quine q = null;
//	
//	
//	
//	private void exportTable2() {
//		
//		final Quine q = getQuine();
//		q.combine();
//		final Domination d = q.getDomination();
//		final String s = d.getTex();
//		
//		setClipboardContents(s);
//		System.out.println(s);			
//		
//	}
//	
////	class Cell {
////		final String txt;
////		final Entry e;
////		Cell(Entry e, final String txt) {this.e = e; this.txt = txt;}
////		@Override public String toString() {return txt;}
////	}
////	
////	private ArrayList<Cell[]> getTable1Data(final Quine q) {
////		
////		final ArrayList<Level> levels = q.getLevels();
////		final ArrayList<Cell[]> data = new ArrayList<>();
////				
////		for (int row = 0; row < 1024; ++row) {
////			final ArrayList<Cell> rowData = new ArrayList<Cell>();
////			boolean isEmpty = true;
////			for (int col = 0; col < levels.size(); ++col) {
////				final Level lvl = levels.get(col);
////				if (row < lvl.entries.size()) {
////					final Entry e = lvl.entries.get(row);
////					String txt1 = e.usedTerms.toString();
////					String txt2 = e.mask;
////					if (lvl.entries.get(row).prime()) {txt2 += " " + (char)('A'+e.primeTermNr);}
////					if (lvl.entries.get(row).duplicate()) {txt2 += " xx";}
////					rowData.add(new Cell(e, txt1));
////					rowData.add(new Cell(e, txt2));
////					isEmpty = false;
////				} else {
////					rowData.add(new Cell(null, ""));
////					rowData.add(new Cell(null, ""));
////				}
////			}
////			if (isEmpty) {break;}
////			data.add(rowData.toArray(new Cell[0]));
////		}
////		
////		return data;
////		
////	}
//	
//	private Quine getQuine() {
//		
//		final int numVariables = Integer.parseInt(txtVars.getText());
//		final Quine q = new Quine(numVariables);
//		q.setShowDuplicates(chkDuplicates.isSelected());
//		q.setAMSB(chkAMSB.isSelected());
//		final String terms[] = txtTerms.getText().split(",");
//		for (final String term : terms) {
//			q.addTerm(Integer.parseInt(term.trim()));
//		}
//		return q;
//		
//	}
//	
//	private void updateKV() {
//		
////		kvData.clear();
////		
////		final int numVariables = Integer.parseInt(txtVars.getText());
////		if (numVariables < 2 || numVariables > 5) {
////			return;
////		}
////
////		kvData.setNumVariables(numVariables);
////		
////		final String terms[] = txtTerms.getText().split(",");
////		for (final String term : terms) {
////			kvData.addMinterm(Integer.parseInt(term.trim()));
////		}
//		
//	}
//	
//	private final void recalc() {
//				
//		
//		q = getQuine();
//		
//		q.combine();
//		
//
//		final Domination d = q.getDomination();
//		int dCols = d.numCols();
//		int dRows = d.numRows();
//		
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
//		TableModel mdl2 = new DefaultTableModel(data2, colNames2);
//		tblResult2.setModel(mdl2);
//		
//		q.dominate();
//		q.dump();
//		
//		//final ArrayList<Cell[]> data = getTable1Data(q);
//		//final ArrayList<String> colNames = new ArrayList<>();
//		
//		//final ArrayList<Level> levels = q.getLevels();
//		//for (int i = 0; i < levels.size(); ++i) {
//		//	colNames.add("T" + i);
//		//	colNames.add("M" + i);
//		//}
//		
//		
//		txtPrimeTerms.setText(q.getPrimeTermsAllStr());
//		txtResult.setText(q.getPrimeTermMinimumStr());
//		
//		updateKV();
//		
//		mdl1.setData(q);
//		mdl1.applySizes(tblResult1);
//		
//	}
//
//	@Override
//	public void onHoverTerms(final ArrayList<Integer> terms) {
//		if (terms != null) {
//			kvData.select(terms);
//		} else {
//			kvData.clearSelection();
//		}
//	}
//	
//	
//}
