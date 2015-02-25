package org.tetrix.v1;

import java.awt.*;

abstract class Config {
	
	// Control game speed.
	public static final int TIMER_INTERVAL = 1000;
	
	// Board related.	
	public static final int BLOCK_WIDTH = 20;
	public static final int BLOCK_HEIGHT = 20;
	
	public static final int ROW_COUNT = 20;
	public static final int COLUMN_COUNT = 10;
	
	public static final int TOTAL_WIDTH = COLUMN_COUNT * BLOCK_WIDTH;
	public static final int TOTAL_HEIGHT = ROW_COUNT * BLOCK_HEIGHT;
	
	// UI related.
	public static final int LEFT_MARGIN = 10;
	public static final int RIGHT_MARGIN = 180;
	public static final int TOP_MARGIN = 10;
	public static final int BOTTOM_MARGIN = 70;
	
	public static final int FRAME_WIDTH = LEFT_MARGIN + TOTAL_WIDTH + RIGHT_MARGIN;
	public static final int FRAME_HEIGHT = TOP_MARGIN + TOTAL_HEIGHT + BOTTOM_MARGIN;
	
	// lblScore.
	public static final int SCORE_MARGIN = 20;
	public static final int SCORE_WIDTH = 200;
	public static final int SCORE_HEIGHT = 30;
	public static final int SCORE_TOP = 50;
	public static final int SCORE_LEFT = LEFT_MARGIN + TOTAL_WIDTH + SCORE_MARGIN;
	public static final Font SCORE_FONT = new Font("ËÎÌו", Font.BOLD, 18);
	
	// Color theme.
	public static final Color BACKGROUND_COLOR = Color.BLACK;
	public static final Color[] BLOCK_COLORS = new Color[] {
		Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA,
		Color.ORANGE, Color.PINK, Color.YELLOW
	};
	
	// panNextShape.
	public static final int NEXTSHAPE_WIDTH = 120;
	public static final int NEXTSHAPE_HEIGHT = 120;
	public static final int NEXTSHAPE_LEFT = SCORE_LEFT;
	public static final int NEXTSHAPE_TOP = FRAME_HEIGHT - BOTTOM_MARGIN - NEXTSHAPE_HEIGHT;
	public static final Color NEXTSHAPE_COLOR = BACKGROUND_COLOR;

}
