package li.kazu.logic;


import javax.swing.UIManager;

import li.kazu.logic.func.Function;
import li.kazu.logic.hazard.functional.FunctionalHazardCheck;
import li.kazu.logic.hazard.functional.FunctionalHazardCheckResult;

public class Main {

	public static void main(String[] args) throws Exception {
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		//testHazard();
		
//		final JFrame frm = new JFrame();
//		final KVView kv = new KVView();
//		final KVData data = new KVData();
//		kv.setData(data);
//		data.setNumVariables(4);
//		data.addMinterms(new int[] {1, 2, 3, 5, 6, 7, 9, 12, 13, 14});
//		
//		frm.add(kv);
//		frm.setSize(300,300);
//		frm.setVisible(true);
		
//		new UI();
		new MDIForm();
		
//		final int numTerms = 4;
//		final Quine q = new Quine(numTerms);
//		q.addTerms( new int[] {0,1,4,5,6,7,8,9,11,15} );
//		while(q.combine()) {;}
//		q.dominate();
//		q.dump();
		
		
	}
	
	@SuppressWarnings("unused")
	private static void testHazard() {
		
		Function func = new Function();
		func.setNumVariables(4);
		func.addMinterm(1);
		func.addMinterm(9);
		func.addMinterm(7);
		func.addMinterm(15);
		func.addMinterm(14);
		func.addMinterm(13);
		func.addMinterm(12);
		
		final FunctionalHazardCheckResult res = FunctionalHazardCheck.checkFromTermNr(func, 0, FunctionalHazardCheck.Mode.DYNAMIC);
		for (FunctionalHazardCheckResult.Entry e : res.entries) {
			System.out.println(e);;
		}
		System.out.println(res.toTeX(4));
		
		
		/*
		HazardCheck.getPermutations(4, 0, 3);
		HazardCheck.getPermutations(4, 0, 7);
		HazardCheck.getPermutations(4, 7, 0);
		*/
		
	}

}
