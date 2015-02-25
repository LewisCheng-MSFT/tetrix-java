package org.tetrix.v1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

@SuppressWarnings("serial")
public class TetrixFrame extends JFrame {

	private JLabel lblScore;
	private JMenuBar mainMenu;
	private JMenu mnuGame;
	private JMenuItem mnuGameNewGame;
	private JMenuItem mnuGamePause;
	private JMenuItem mnuGameExit;
	private JMenu mnuHelp;
	private JMenuItem mnuHelpIntro;
	private JMenuItem mnuHelpAbout;
	private Timer timer;
	private PanelBoard panBoard;
	private PanelNextShape panNextShape;
	private Shape shape;
	private Shape nextShape;
	private Random rnd;
	
	private int score;
	
	public TetrixFrame() {
		initializeComponents();
		newShape();
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
		mnuGameExit = new JMenuItem("�˳�");
		mnuGameExit.addActionListener(new mnuGameExitActionListener());
		mnuGame.add(mnuGameNewGame);
		mnuGame.add(mnuGamePause);
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
		timer = new Timer(Config.TIMER_INTERVAL, new timerActionListener());
		
		// panBoard and shape.
		panBoard = new PanelBoard();
		panBoard.setLocation(Config.LEFT_MARGIN, Config.TOP_MARGIN);
		shape = new Shape();
		nextShape = new Shape();
		panBoard.setShape(shape);
		
		// Random.
		rnd = new Random();
		
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
		setTitle("����˹���� v1.0");
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
	
	private class mnuHelpIntroActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			timer.stop();
			JOptionPane.showMessageDialog(TetrixFrame.this,
					"a���ƶ�    d���ƶ�    w��ת    s�����½�",
					"˵��",
					JOptionPane.INFORMATION_MESSAGE);
			timer.start();
		}

	}
	
	private class mnuHelpAboutActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			timer.stop();
			JOptionPane.showMessageDialog(TetrixFrame.this,
										"����˹���� v1.0  ���ߣ�����\n" +
										"2010/8/17 v1.0�깤~\n" +
										"2010/8/18 ��������bug",
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
				fallDown();
				break;
			}
		}
	}
	
	private class timerActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			fallDown();
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
	
	private void doLose() {
		timer.stop();
		JOptionPane.showMessageDialog(TetrixFrame.this,
				"ʤ���˱��ҳ��£�����������������~~",
				"�÷֣�" + score,
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void newShape() {
		nextShape.setKind(ShapeKind.values()[rnd.nextInt(ShapeKind.COUNT.ordinal())]);
		nextShape.setRotation(ShapeRotation.values()[rnd.nextInt(ShapeRotation.COUNT.ordinal())]);
		nextShape.setPosition(0, Config.COLUMN_COUNT / 2 - 1);
	}
	
	private void shiftShape() {
		shape.copyFrom(nextShape);
		newShape();
		panNextShape.repaint();
	}
	
	// Avoid timer and user execute this method at the same time.
	private void fallDown() {
		try {
			if (panBoard.touchBottom()) {
				panBoard.fixShape();
				int count = panBoard.eliminateBottomLines();
				score += 100 * (int)Math.pow(2.0, count - 1);
				lblScore.setText("�÷֣�" + score);
				shiftShape();
				return;
			}
			shape.moveDown();
			repaint();
		} catch (LosingNotifyException e) {
			doLose();
			newGame();
		}
	}
}