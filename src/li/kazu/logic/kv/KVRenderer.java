package li.kazu.logic.kv;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class KVRenderer {
	
	final int cellS = 32;
	
	class KVCell {
		int x;
		int y;
		int nr;
		KVCell(int x, int y, int nr) {
			this.x = x; this.y = y; this.nr = nr;
		}
	}
	
	static enum Pos {FIRST, LAST};
	static enum Dir {HOR, VER_RIGHT, VER_LEFT};
	
	static class KVVariable {
		int varNr;
		int v1;
		int v2;
		int d;
		Dir dir;
		Pos pos;
		KVVariable(int varNr, int v1, int v2, final Pos pos, int d, final Dir dir) {
			this.varNr = varNr;
			this.v1 = v1;
			this.v2 = v2;
			this.d = d;
			this.pos = pos;
			this.dir = dir;
		}
	}
	
	class KVSetup {
		int w;
		int h;
		ArrayList<KVCell> cells = new ArrayList<>();
		ArrayList<KVVariable> variables = new ArrayList<>();
		KVSetup(int w, int h) {
			this.w = w; this.h = h;
		}
		void add(int x, int y, int nr) {
			this.cells.add(new KVCell(x,y,nr));
		}
		void addVariable(KVVariable var) {
			variables.add(var);
		}
	}
	
	KVSetup[] setups = new KVSetup[6];
	
	public KVRenderer() {
		
		KVSetup s2 = new KVSetup(2,2);	s2.add(0,0,0); s2.add(1,0,1); s2.add(0,1,2); s2.add(1,1,3);
										s2.addVariable(new KVVariable(0, 2,2,Pos.FIRST,1,Dir.HOR));
										s2.addVariable(new KVVariable(1, 2,2,Pos.FIRST,1,Dir.VER_LEFT));
		
		KVSetup s3 = new KVSetup(4,2);	s3.add(0,0,0); s3.add(1,0,1); s3.add(0,1,2); s3.add(1,1,3);		s3.add(2,0,5); s3.add(3,0,4); s3.add(2,1,7); s3.add(3,1,6);
										s3.addVariable(new KVVariable(0, 2,3,Pos.FIRST,1,Dir.HOR));
										s3.addVariable(new KVVariable(1, 2,2,Pos.FIRST,1,Dir.VER_LEFT));
										s3.addVariable(new KVVariable(2, 3,4,Pos.LAST,1,Dir.HOR));
										
		KVSetup s4 = new KVSetup(4,4);	s4.add(0,0,0); s4.add(1,0,1); s4.add(0,1,2); s4.add(1,1,3);		s4.add(2,0,5); s4.add(3,0,4); s4.add(2,1,7); s4.add(3,1,6);
										s4.add(0,2,10); s4.add(1,2,11); s4.add(0,3,8); s4.add(1,3,9);	s4.add(2,2,15); s4.add(3,2,14); s4.add(2,3,13); s4.add(3,3,12);
										s4.addVariable(new KVVariable(0, 2,3,Pos.FIRST,1,Dir.HOR));
										s4.addVariable(new KVVariable(1, 2,3,Pos.FIRST,1,Dir.VER_LEFT));
										s4.addVariable(new KVVariable(2, 3,4,Pos.LAST,1,Dir.HOR));
										s4.addVariable(new KVVariable(3, 3,4,Pos.LAST,1,Dir.VER_RIGHT));
										
		KVSetup s5 = new KVSetup(8,4);	s5.add(0,0,0); s5.add(1,0,1); s5.add(0,1,2); s5.add(1,1,3);		s5.add(2,0,5); s5.add(3,0,4); s5.add(2,1,7); s5.add(3,1,6);
										s5.add(0,2,10); s5.add(1,2,11); s5.add(0,3,8); s5.add(1,3,9);	s5.add(2,2,15); s5.add(3,2,14); s5.add(2,3,13); s5.add(3,3,12);
										s5.add(4,0,20); s5.add(5,0,21); s5.add(4,1,22); s5.add(5,1,23);	s5.add(6,0,17); s5.add(7,0,16); s5.add(6,1,19); s5.add(7,1,18);
										s5.add(4,2,30); s5.add(5,2,31); s5.add(4,3,28); s5.add(5,3,29);	s5.add(6,2,27); s5.add(7,2,26); s5.add(6,3,25); s5.add(7,3,24);
										s5.addVariable(new KVVariable(0, 2,3,Pos.FIRST,1,Dir.HOR));
										s5.addVariable(new KVVariable(0, 6,7,Pos.FIRST,1,Dir.HOR));
										s5.addVariable(new KVVariable(1, 2,3,Pos.FIRST,1,Dir.VER_LEFT));
										//s5.addVariable(new KVVariable(2, 3,4,Pos.LAST,1,Dir.HOR));
										//s5.addVariable(new KVVariable(2, 5,6,Pos.LAST,1,Dir.HOR));
										s5.addVariable(new KVVariable(2, 3,6,Pos.LAST,1,Dir.HOR));
										s5.addVariable(new KVVariable(3, 3,4,Pos.LAST,1,Dir.VER_RIGHT));
										s5.addVariable(new KVVariable(4, 5,8,Pos.FIRST,2,Dir.HOR));
										
		setups[2] = s2;
		setups[3] = s3;
		setups[4] = s4;
		setups[5] = s5;
	}

	public Dimension getPreferredSize(final KVData data) {
		if (data == null) {return null;}
		final KVSetup setup = setups[data.getNumVariables()];
		if (setup == null) {return null;}
		return new Dimension(setup.w*cellS, setup.h*cellS);
	}
	
	public void render(final KVData data, final Graphics2D g, final int w, final int h) {
		
		g.clearRect(0, 0, w, h);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (data == null) {return;}
		
		final KVSetup setup = setups[data.getNumVariables()];
		if (setup == null) {return;}
		
		//int ms = Math.min(w, h);

		
		final int neededW = cellS * setup.w;
		final int neededH = cellS * setup.h;
		final int ox = (w - neededW) / 2;
		final int oy = (h - neededH) / 2;
		
		final Font f1 = new Font("arial", Font.PLAIN, 9);
		final Font f2 = new Font("arial", Font.PLAIN, 16);
		final Font f2b = new Font("arial", Font.BOLD, 18);
		//g.drawLine(0, 0, ms, ms);
		
		// draw all KV cells
		for (final KVCell cell : setup.cells) {
			
			final int val = data.get(cell.nr);
			final String sval = (val < 0) ? ("-") : ("" + val);
			
			int x1 = ox + cell.x * cellS;
			int y1 = oy + cell.y * cellS;
			g.drawRect(x1,y1,cellS,cellS);							// frame
			
			g.setFont(f1);
			g.drawString(""+cell.nr, x1+1, cellS+y1-1);				// index
			
			final Font fVal = (data.isSelected(cell.nr)) ? f2b : f2;
			g.setFont(fVal);
			final Rectangle2D r = g.getFontMetrics().getStringBounds(sval, g);
			final int tw = (int)r.getWidth();
			final int th = (int)r.getHeight();
			g.drawString(sval, x1+1+cellS/2-tw/2, cellS+y1-1-cellS/2+th/2);	// value
			
		}
		
		g.setFont(f2);
		
		// draw all variables
		for (final KVVariable var : setup.variables) {
			
			final String varName = data.getVariableName(var.varNr);
			
			final Rectangle2D r = g.getFontMetrics().getStringBounds(varName, g);
			final int tw = (int)r.getWidth();
			final int th = (int)r.getHeight();
			
			if (var.dir == Dir.HOR) {
				
				final int ya = (var.pos == Pos.FIRST) ? (0 * cellS) : (setup.h * cellS);
				final int yb = (var.pos == Pos.FIRST) ? (-16) : (+16);
				
				final int x1 = ox + (var.v1-1) * cellS;
				final int x2 = ox + var.v2 * cellS;
				final int y = oy + ya + var.d * yb;
				final int xt = (x1+x2)/2 - 4;
				final int yt = (int) (oy + ya + (var.d+0.5f) * yb);
				g.drawLine(x1, y, x2, y);
				g.drawString(varName, xt, yt+6);
				
			} else {
				
				final int xa = (var.pos == Pos.FIRST) ? (0 * cellS) : (setup.w * cellS);
				final int xb = (var.pos == Pos.FIRST) ? (-16) : (+16);
						
				final int x = ox + xa + var.d * xb;
				final int y1 = oy + (var.v1-1) * cellS;
				final int y2 = oy + var.v2 * cellS;
				final int yt = (y1+y2)/2;
				final int xOff = (var.dir == Dir.VER_LEFT) ? (tw-4) : (4);
				final int xt = (int) (ox + xa + (var.d+0.5) * xb) - xOff;
				g.drawLine(x, y1, x, y2);
				g.drawString(varName, xt, yt+6);
				
			}
			
		}
		
		
		
	}
	
}
