package org.tetrix.v2;

import java.awt.*;

abstract class Painter {
	public static void paintRect(Graphics g, int width, int height, Color color) {
		g.setColor(Config.BACKGROUND_COLOR);
		g.fillRect(0, 0, width, height);
	}
	
	public static void paintBlock(Graphics g, int row, int col, Color color) {
		g.setColor(color);
		
		g.fillRect(col * Config.BLOCK_WIDTH,
				   row * Config.BLOCK_HEIGHT,
				   Config.BLOCK_WIDTH,
				   Config.BLOCK_HEIGHT);
		g.setColor(Color.DARK_GRAY);
		g.drawRect(col * Config.BLOCK_WIDTH,
				   row * Config.BLOCK_HEIGHT,
				   Config.BLOCK_WIDTH,
				   Config.BLOCK_HEIGHT);
	}
	
	public static void paintShape(Graphics g, Shape s, int row, int col) {
		Color color = Config.BLOCK_COLORS[s.getKind().ordinal()];
		
		int n = s.getEdgeLength();
		
		for (int i = 0; i < n; ++i)
			for (int j = 0; j < n; ++j) {
				if (s.getMap()[i][j] == 0)
					continue;
				
				int ei = row + i;
				int ej = col + j;
				
				// Ignore the block out of the board.
				if (ei < 0 || ei >= Config.ROW_COUNT || ej < 0 || ej >= Config.COLUMN_COUNT)
					continue;
				
				paintBlock(g, ei, ej, color);
			}
	}
	
	public static void paintShape(Graphics g, Shape s) {
		// Paint from the position of the element[0, 0] of the shape.
		paintShape(g, s, s.getRow(0), s.getCol(0));
	}
}
