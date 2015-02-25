package org.tetrix.v2;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
class PanelBoard extends JPanel {
	private Block heap[][];
	private Shape shape;
	
	public PanelBoard() {
		setSize(Config.TOTAL_WIDTH, Config.TOTAL_HEIGHT);
		heap = new Block[Config.ROW_COUNT][Config.COLUMN_COUNT];
		clearHeap();
	}
	
	// Clear the board and its data.
	public void clearHeap() {
		for (int i = 0; i < heap.length; ++i)
			for (int j = 0; j < heap[i].length; ++j) {
				if (heap[i][j] == null)
					heap[i][j] = new Block();
				
				heap[i][j].color = Config.BACKGROUND_COLOR;
				heap[i][j].occupied = false;
			}
	}
	
	// Is the shape touch any thing on the left side?
	public boolean touchLeft() {
		int n = shape.getEdgeLength();
		
		for (int j = 0; j < n; ++j) {
			for (int i = 0; i < n; ++i) {
				if (shape.getMap()[i][j] == 1) {
					int ei = shape.getRow(i);
					int ej = shape.getCol(j) - 1;
					
					// Touch the left wall or other shapes staying there.
					if (ej < 0 || (ei >= 0 && heap[ei][ej].occupied))
						return true;
				}
			}
		}
		return false;
	}
	
	// Is the shape touch any thing on the right side?
	public boolean touchRight() {
		int n = shape.getEdgeLength();
		
		for (int j = n - 1; j >= 0; --j) {
			for (int i = 0; i < n; ++i) {
				if (shape.getMap()[i][j] == 1) {
					int ei = shape.getRow(i);
					int ej = shape.getCol(j) + 1;

					// Touch the right wall or other shapes staying there.
					if (ej >= Config.COLUMN_COUNT || (ei >= 0 && heap[ei][ej].occupied))
						return true;
				}
			}
		}
		return false;
	}
	
	// Is the shape touch any thing below it?
	public boolean touchBottom() {
		int n = shape.getEdgeLength();
		
		for (int i = n - 1; i >= 0; --i) {
			for (int j = 0; j < n; ++j) {
				if (shape.getMap()[i][j] == 1) {
					int ei = shape.getRow(i) + 1;
					int ej = shape.getCol(j);

					// Touch the bottom or other shapes staying there.
					if (ei >= Config.ROW_COUNT || (ei >= 0 && heap[ei][ej].occupied))
						return true;
				}
			}
		}
		return false;
	}
	
	// Set the moving shape on the board.
	public void setShape(Shape s) {
		this.shape = s;
	}
	
	// Make the shape fixed into the heap.
	public void fixShape() throws LosingNotifyException {
		int n = shape.getEdgeLength();
		
		for (int i = 0; i < n; ++i)
			for (int j = 0; j < n; ++j) {
				if (shape.getMap()[i][j] == 0)
					continue;
				
				int ei = shape.getRow(i);
				int ej = shape.getCol(j);
				
				// Lose.
				if (ei <= 0)
					throw new LosingNotifyException();
				
				// Ignore the block out of the board.
				if (ei >= Config.ROW_COUNT || ej < 0 || ej >= Config.COLUMN_COUNT)
					continue;
				
				heap[ei][ej].color = Config.BLOCK_COLORS[shape.getKind().ordinal()];
				heap[ei][ej].occupied = true;
			}
	}
	
	public void eliminateBottomLine(int row, int cnt) {
		// Move the board down for one line.
		for (int k = row; k >= cnt; --k)
			for (int j = 0; j < Config.COLUMN_COUNT; ++j) {
				heap[k][j].color = heap[k - cnt][j].color;
				heap[k][j].occupied = heap[k - cnt][j].occupied;
			}
		
		// Fix the top lines.
		for (int i = 0; i < cnt; ++i) {
			for (int j = 0; j < Config.COLUMN_COUNT; ++j) {
				heap[i][j].color = Config.BACKGROUND_COLOR;
				heap[i][j].occupied = false;
			}
		}
	}
	
	public int eliminateBottomLines() {
		// How many lines being eliminated.
		int count = 0;
		
		for (int i = Config.ROW_COUNT - 1; i >= 0; --i) {
			// See if line i is all occupied.
			boolean allOccupied = true;
			for (int j = 0; j < Config.COLUMN_COUNT; ++j)
				if (heap[i][j].occupied == false) {
					allOccupied = false;
					break;
				}
			
			// Eliminate the line.
			if (allOccupied) {
				eliminateBottomLine(i, 1);
				
				++count;
				
				// The original next line has been the current line,
				// so it's vital to fix the index!
				++i;
			}
		}
		
		repaint();
		return count;
	}
	
	@Override
	public void paint(Graphics g) {
		// Clear background first.
		Painter.paintRect(g, this.getWidth(), this.getHeight(), Config.BACKGROUND_COLOR);
		
		// Print the static heap.
		for (int i = 0; i < heap.length; ++i)
			for (int j = 0; j < heap[i].length; ++j) {
				if (heap[i][j].occupied) {
					Painter.paintBlock(g, i, j, heap[i][j].color);
				}
			}
		
		// Print the falling shape.
		Painter.paintShape(g, shape);
	}
}
