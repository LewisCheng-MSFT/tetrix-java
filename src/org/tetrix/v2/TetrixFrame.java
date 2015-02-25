package org.tetrix.v2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

@SuppressWarnings("serial")
public class TetrixFrame extends JFrame {

	// Controls.
	private JLabel lblScore;
	private JMenuBar mainMenu;
	private JMenu mnuGame;
	private JMenuItem mnuGameNewGame;
	private JMenuItem mnuGamePause;
	private JMenuItem mnuGameBeginner;
	private JMenuItem mnuGameIntermediate;
	private JMenuItem mnuGameAdvanced;
	private JMenuItem mnuGameMaster;
	private JMenuItem mnuGameInstantDown;
	private JMenuItem mnuGameExit;
	private JMenu mnuHelp;
	private JMenuItem mnuHelpIntro;
	private JMenuItem mnuHelpAbout;
	private Timer timer;
	private PanelBoard panBoard;
	private PanelNextShape panNextShape;
	
	// Game data.
	private Shape shape;
	private Shape nextShape;
	private Random rnd;
	private boolean instantDown;
	private int score;
	
	public TetrixFrame() {
		initializeComponents();
		initializeGame();
		newGame();
	}
	
	public static void main(String[] args) {
		new TetrixFrame();
	}
	
	private void initializeComponents() {
		// Set the frame look to Windows style.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// There shall be no exceptions here.
		}
		
		// Menus.
		mainMenu = new JMenuBar();
		mnuGame = new JMenu("��Ϸ");
		mnuGameNewGame = new JMenuItem("����Ϸ");
		mnuGameNewGame.addActionListener(new mnuGameNewGameActionListener());
		mnuGamePause = new JMenuItem("��ͣ");
		mnuGamePause.addActionListener(new mnuGamePauseActionListener());
		mnuGameInstantDown = new JMenuItem("��������");
		mnuGameInstantDown.addActionListener(new mnuGameInstantDownActionListener());
		mnuGameBeginner = new JMenuItem("��ѧ��");
		mnuGameBeginner.addActionListener(new mnuGameBeginnerActionListener());
		mnuGameIntermediate = new JMenuItem("�м�");
		mnuGameIntermediate.addActionListener(new mnuGameIntermediateActionListener());
		mnuGameAdvanced = new JMenuItem("�߼�");
		mnuGameAdvanced.addActionListener(new mnuGameAdvancedActionListener());
		mnuGameMaster = new JMenuItem("��ʦ");
		mnuGameMaster.addActionListener(new mnuGameMasterActionListener());
		mnuGameExit = new JMenuItem("�˳�");
		mnuGameExit.addActionListener(new mnuGameExitActionListener());
		mnuGame.add(mnuGameNewGame);
		mnuGame.add(mnuGamePause);
		mnuGame.add(mnuGameInstantDown);
		mnuGame.addSeparator();
		mnuGame.add(mnuGameBeginner);
		mnuGame.add(mnuGameIntermediate);
		mnuGame.add(mnuGameAdvanced);
		mnuGame.add(mnuGameMaster);
		mnuGame.addSeparator();
		mnuGame.add(mnuGameExit);
		mnuHelp = new JMenu("����");
		mnuHelpIntro = new JMenuItem("˵��");
		mnuHelpIntro.addActionListener(new mnuHelpIntroActionListener());
		mnuHelpAbout = new JMenuItem("����");
		mnuHelpAbout.addActionListener(new mnuHelpAboutActionListener());
		mnuHelp.add(mnuHelpIntro);
		mnuHelp.add(mnuHelpAbout);
		mainMenu.add(mnuGame);
		mainMenu.add(mnuHelp);
		
		// Timer.
		timer = new Timer(Config.TIMER_INTERVAL[Config.DEFAULT_LEVEL.ordinal()],
						  new timerActionListener());
		
		// panBoard.
		panBoard = new PanelBoard();
		panBoard.setLocation(Config.LEFT_MARGIN, Config.TOP_MARGIN);
		
		// panNextShape.
		panNextShape = new PanelNextShape();
		panNextShape.setLocation(Config.NEXTSHAPE_LEFT, Config.NEXTSHAPE_TOP);
		panNextShape.setSize(Config.NEXTSHAPE_WIDTH, Config.NEXTSHAPE_HEIGHT);
		
		// lblSccore.
		lblScore = new JLabel();
		lblScore.setLocation(Config.SCORE_LEFT, Config.SCORE_TOP);
		lblScore.setSize(Config.SCORE_WIDTH, Config.SCORE_HEIGHT);
		lblScore.setFont(Config.SCORE_FONT);
		
		// Frame properties.
		setLayout(null);
		setSize(Config.FRAME_WIDTH, Config.FRAME_HEIGHT);
		setResizable(false);
		GraphicsEnvironment lge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point cp = lge.getCenterPoint();
		setLocation(cp.x - getWidth() / 2, cp.y - getHeight() / 2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(new TetrixFrameKeyListener());
        
        // Add components.
		add(panBoard);
		add(panNextShape);
		add(lblScore);
        setJMenuBar(mainMenu);
        
        // Show the frame.
        setVisible(true);
	}
	
	private void setLevel(Level lv) {		
		mnuGameBeginner.setText(Config.STR_BEGINNER);
		mnuGameIntermediate.setText(Config.STR_INTERMEDIATE);
		mnuGameAdvanced.setText(Config.STR_ADVANCED);
		mnuGameMaster.setText(Config.STR_MASTER);
		
		switch (lv) {
		case Beginner:
			mnuGameBeginner.setText(Config.STR_BEGINNER + "[x]");
			setTitle(Config.VER_STRING + " [" + Config.STR_BEGINNER + "]");
			break;
		case Intermediate:
			mnuGameIntermediate.setText(Config.STR_INTERMEDIATE + "[x]");
			setTitle(Config.VER_STRING + " [" + Config.STR_INTERMEDIATE + "]");
			break;
		case Advanced:
			mnuGameAdvanced.setText(Config.STR_ADVANCED + "[x]");
			setTitle(Config.VER_STRING + " [" + Config.STR_ADVANCED + "]");
			break;
		case Master:
			mnuGameMaster.setText(Config.STR_MASTER + "[x]");
			setTitle(Config.VER_STRING + " [" + Config.STR_MASTER + "]");
			break;
		}
		timer.setDelay(Config.TIMER_INTERVAL[lv.ordinal()]);
	}
	
	private void initializeGame() {
		// Set initial level.
		setLevel(Config.DEFAULT_LEVEL);
		
		// Instant down is preferably disabled.
		instantDown = false;
		
		// Random.
		rnd = new Random();
		
		// Shapes.
		shape = new Shape();
		nextShape = new Shape();
		panBoard.setShape(shape);
		
		// Get the next shape.
		newShape();
	}

	private class mnuGameExitActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
		
	}
	
	private class mnuGameNewGameActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			newGame();
		}
		
	}
	
	private class mnuGameMasterActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			setLevel(Level.Master);
			newGame();
		}

	}

	private class mnuGameAdvancedActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			setLevel(Level.Advanced);
			newGame();
		}

	}

	private class mnuGameIntermediateActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			setLevel(Level.Intermediate);
			newGame();
		}

	}
	
	private class mnuGameBeginnerActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			setLevel(Level.Beginner);
			newGame();
		}

	}

	private class mnuGameInstantDownActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			instantDown = !instantDown;
			if (instantDown)
				mnuGameInstantDown.setText("��������[x]");
			else
				mnuGameInstantDown.setText("��������");
		}

	}
	
	private class mnuHelpIntroActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			timer.stop();
			JOptionPane.showMessageDialog(TetrixFrame.this,
					"a���ƶ�    d���ƶ�    w��ת    s�����½�    ctrl-s����",
					"˵��",
					JOptionPane.INFORMATION_MESSAGE);
			timer.start();
		}

	}
	
	private class mnuHelpAboutActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			timer.stop();
			JOptionPane.showMessageDialog(
				TetrixFrame.this,
				Config.VER_STRING +
				"\n���ߣ�����\n" +
				"2010/8/17 v1.0�깤��\n" +
				"          ʵ�ֶ���˹���������Ϸ���ܣ�\n" +
				"2010/8/18 ��������bug��\n" +
				"2010/8/18 v2.0�깤��\n" +
				"          ��Ӽ����������䡢���׵ȹ��ܡ�\n",
				"����  ����˹����",
				JOptionPane.INFORMATION_MESSAGE);
			timer.start();
		}

	}

	private class mnuGamePauseActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (timer.isRunning()) {
				timer.stop();
				mnuGamePause.setText("����");
			} else {
				timer.start();
				mnuGamePause.setText("��ͣ");
			}
		}

	}
	
	private class TetrixFrameKeyListener extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			if (!timer.isRunning())
				return;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				moveLeft();
				break;
			case KeyEvent.VK_D:
				moveRight();
				break;
			case KeyEvent.VK_W:
				rotate();
				break;
			case KeyEvent.VK_S:
				if (e.isControlDown())
					cheat();
				else
					fallDown(instantDown);
				break;
			}
		}
	}
	
	private class timerActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			fallDown(false);
		}

	}
	
	private class PanelNextShape extends JPanel {

		@Override
		public void paint(Graphics g) {
			// Clear background.
			Painter.paintRect(g, Config.NEXTSHAPE_WIDTH, Config.NEXTSHAPE_HEIGHT, Config.NEXTSHAPE_COLOR);
			
			// Draw next shape.
			Painter.paintShape(g, nextShape, 1, 1);
		}

	}
	
	private void moveLeft() {
		if (panBoard.touchLeft())
			return;
		
		shape.moveLeft();
		repaint();
	}
	
	private void moveRight() {
		if (panBoard.touchRight())
			return;
		
		shape.moveRight();
		repaint();
	}
	
	private void cheat() {
		panBoard.eliminateBottomLine(Config.ROW_COUNT - 1,
				 					 Config.LINE_ELIMINATIONS);
		repaint();
	}
	
	private void rotate() {
		// Eliminate all the case where rotation cannot be done.
		if (panBoard.touchLeft()) {
			if (shape.getKind() == ShapeKind.I) {
				if (shape.getRotation() == ShapeRotation.R90 ||
					shape.getRotation() == ShapeRotation.R270)
					return;
			} else if (shape.getKind() == ShapeKind.J) {
				if (shape.getRotation() == ShapeRotation.R180)
					return;
			} else if (shape.getKind() == ShapeKind.L) {
				if (shape.getRotation() == ShapeRotation.R0)
					return;
			} else if (shape.getKind() == ShapeKind.S) {
				if (shape.getRotation() == ShapeRotation.R90)
					return;
			} else if (shape.getKind() == ShapeKind.T) {
				if (shape.getRotation() == ShapeRotation.R270)
					return;
			} else if (shape.getKind() == ShapeKind.Z) {
				if (shape.getRotation() == ShapeRotation.R90)
					return;
			}
		} else if (panBoard.touchRight()) {
			if (shape.getKind() == ShapeKind.I) {
				if (shape.getRotation() == ShapeRotation.R90 ||
					shape.getRotation() == ShapeRotation.R270)
					return;
			} else if (shape.getKind() == ShapeKind.J) {
				if (shape.getRotation() == ShapeRotation.R0)
					return;
			} else if (shape.getKind() == ShapeKind.L) {
				if (shape.getRotation() == ShapeRotation.R180)
					return;
			} else if (shape.getKind() == ShapeKind.S) {
				if (shape.getRotation() == ShapeRotation.R270)
					return;
			} else if (shape.getKind() == ShapeKind.T) {
				if (shape.getRotation() == ShapeRotation.R90)
					return;
			} else if (shape.getKind() == ShapeKind.Z) {
				if (shape.getRotation() == ShapeRotation.R270)
					return;
			}
		}
		
		// Now we can safely do rotation.
		shape.rotate();
		repaint();
	}
	
	private void newGame() {
		score = 0;
		lblScore.setText("�÷֣�0");
		mnuGamePause.setText("��ͣ");
		panBoard.clearHeap();
		shiftShape();
		timer.start();
	}
	
	private void lose() {
		timer.stop();
		JOptionPane.showMessageDialog(TetrixFrame.this,
				"ʤ���˱��ҳ��£�����������������~~",
				"�÷֣�" + score,
				JOptionPane.INFORMATION_MESSAGE);
		newGame();
	}
	
	private void newShape() {
		nextShape.setKind(ShapeKind.values()[rnd.nextInt(ShapeKind.COUNT.ordinal())]);
		nextShape.setRotation(ShapeRotation.values()[rnd.nextInt(ShapeRotation.COUNT.ordinal())]);
		nextShape.setPosition(0, Config.COLUMN_COUNT / 2 - 1);
	}
	
	// Shift current shape to the next shape, and generate new next shape.
	private void shiftShape() {
		shape.copyFrom(nextShape);
		newShape();
		panNextShape.repaint();
	}
	
	// Avoid timer and user execute this method at the same time.
	private void fallDown(boolean id) {
		try {
			while (true) {
				if (panBoard.touchBottom()) {
					// Touch bottom, fix it onto the board.
					panBoard.fixShape();
					
					// Count line eliminations and calculate score.
					int count = panBoard.eliminateBottomLines();
					score += 100 * (int)Math.pow(2.0, count - 1);
					lblScore.setText("�÷֣�" + score);
					
					shiftShape();
					return;
				}
				
				// Move down one line.
				shape.moveDown();
				repaint();
				
				// Not instant down, just exit loop.
				if (!id)
					break;
			}
		} catch (LosingNotifyException e) {
			lose();
		}
	}
	
}