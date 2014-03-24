/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.logisim.gui.start;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.cburch.logisim.Main;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.util.GraphicsUtil;

public class About {
	static final int IMAGE_BORDER = 10;
	static final int IMAGE_WIDTH = 380;
	static final int IMAGE_HEIGHT = 284;
	
	private static class PanelThread extends Thread {
		private MyPanel panel;
		private boolean running = true;
		
		PanelThread(MyPanel panel) {
			this.panel = panel;
		}
		
		@Override
		public void run() {
			long start = System.currentTimeMillis();
			while (running) {
				long elapse = System.currentTimeMillis() - start;
				int count = (int) (elapse / 500) % 4;
				panel.upper = (count == 2 || count == 3) ? Value.TRUE : Value.FALSE;
				panel.lower = (count == 1 || count == 2) ? Value.TRUE : Value.FALSE;
				panel.credits.setScroll((int) elapse);
				panel.repaint();
				try {
					Thread.sleep(20);
				} catch (InterruptedException ex) { }
			}
		}
	}
	
	private static class MyPanel extends JPanel implements AncestorListener {
		private final Color fadeColor = new Color(255, 255, 255, 128);
		private final Color headerColor = new Color(143, 0, 0);
		private final Color gateColor = Color.DARK_GRAY;
		private final Font headerFont = new Font("Monospaced", Font.BOLD, 72);
		private final Font versionFont = new Font("Serif", Font.PLAIN | Font.ITALIC, 32);
		private final Font copyrightFont = new Font("Serif", Font.ITALIC, 18);
		
		private Value upper = Value.FALSE;
		private Value lower = Value.TRUE;
		private AboutCredits credits;
		private PanelThread thread = null;

		public MyPanel() {
			setLayout(null);
			
			int prefWidth = IMAGE_WIDTH + 2 * IMAGE_BORDER;
			int prefHeight = IMAGE_HEIGHT + 2 * IMAGE_BORDER;
			setPreferredSize(new Dimension(prefWidth, prefHeight));
			setBackground(Color.WHITE);
			addAncestorListener(this);
			
			credits = new AboutCredits();
			credits.setBounds(0, prefHeight / 2, prefWidth, prefHeight / 2);
			add(credits);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			try {
				int x = IMAGE_BORDER;
				int y = IMAGE_BORDER;
				drawCircuit(g, x + 10, y + 55);
				g.setColor(fadeColor);
				g.fillRect(x, y, IMAGE_WIDTH, IMAGE_HEIGHT);
				drawText(g, x, y);
			} catch (Throwable t) { }
		}
		
		private void drawCircuit(Graphics g, int x0, int y0) {
			if (g instanceof Graphics2D) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(5.0f));
			}
			drawWires(g, x0, y0);
			g.setColor(gateColor);
			drawNot(g, x0, y0, 70, 10);
			drawNot(g, x0, y0, 70, 110);
			drawAnd(g, x0, y0, 130, 30);
			drawAnd(g, x0, y0, 130, 90);
			drawOr(g, x0, y0, 220, 60);
		}

		private void drawWires(Graphics g, int x0, int y0) {
			Value upperNot = upper.not();
			Value lowerNot = lower.not();
			Value upperAnd = upperNot.and(lower);
			Value lowerAnd = lowerNot.and(upper);
			Value out = upperAnd.or(lowerAnd);
			int x;
			int y;
			
			g.setColor(upper.getColor());
			x = toX(x0, 20);
			y = toY(y0, 10);
			g.fillOval(x - 7, y - 7, 14, 14);
			g.drawLine(toX(x0, 0), y, toX(x0, 40), y);
			g.drawLine(x, y, x, toY(y0, 70));
			y = toY(y0, 70);
			g.drawLine(x, y, toX(x0, 80), y);
			g.setColor(upperNot.getColor());
			y = toY(y0, 10);
			g.drawLine(toX(x0, 70), y, toX(x0, 80), y);
			
			g.setColor(lower.getColor());
			x = toX(x0, 30);
			y = toY(y0, 110);
			g.fillOval(x - 7, y - 7, 14, 14);
			g.drawLine(toX(x0, 0), y, toX(x0, 40), y);
			g.drawLine(x, y, x, toY(y0, 50));
			y = toY(y0, 50);
			g.drawLine(x, y, toX(x0, 80), y);
			g.setColor(lowerNot.getColor());
			y = toY(y0, 110);
			g.drawLine(toX(x0, 70), y, toX(x0, 80), y);
			
			g.setColor(upperAnd.getColor());
			x = toX(x0, 150);
			y = toY(y0, 30);
			g.drawLine(toX(x0, 130), y, x, y);
			g.drawLine(x, y, x, toY(y0, 45));
			y = toY(y0, 45);
			g.drawLine(x, y, toX(x0, 174), y);
			g.setColor(lowerAnd.getColor());
			y = toY(y0, 90);
			g.drawLine(toX(x0, 130), y, x, y);
			g.drawLine(x, y, x, toY(y0, 75));
			y = toY(y0, 75);
			g.drawLine(x, y, toX(x0, 174), y);
			
			g.setColor(out.getColor());
			y = toY(y0, 60);
			g.drawLine(toX(x0, 220), y, toX(x0, 240), y);
		}
		
		private void drawNot(Graphics g, int x0, int y0, int x, int y) {
			int[] xp = new int[4];
			int[] yp = new int[4];
			xp[0] = toX(x0, x - 10); yp[0] = toY(y0, y);
			xp[1] = toX(x0, x - 29); yp[1] = toY(y0, y - 7);
			xp[2] = xp[1]; yp[2] = toY(y0, y + 7);
			xp[3] = xp[0]; yp[3] = yp[0];
			g.drawPolyline(xp, yp, 4);
			int diam = toDim(10);
			g.drawOval(xp[0], yp[0] - diam / 2, diam, diam);
		}
		
		private void drawAnd(Graphics g, int x0, int y0, int x, int y) {
			int[] xp = new int[4];
			int[] yp = new int[4];
			xp[0] = toX(x0, x - 25); yp[0] = toY(y0, y - 25);
			xp[1] = toX(x0, x - 50); yp[1] = yp[0];
			xp[2] = xp[1]; yp[2] = toY(y0, y + 25);
			xp[3] = xp[0]; yp[3] = yp[2];
			int diam = toDim(50);
			g.drawArc(xp[1], yp[1], diam, diam, -90, 180);
			g.drawPolyline(xp, yp, 4);
		}
		
		private void drawOr(Graphics g, int x0, int y0, int x, int y) {
			int cx = toX(x0, x - 50);
			int cd = toDim(62);
			GraphicsUtil.drawCenteredArc(g, cx, toY(y0, y - 37), cd, -90, 53);
			GraphicsUtil.drawCenteredArc(g, cx, toY(y0, y + 37), cd, 90, -53);
			GraphicsUtil.drawCenteredArc(g, toX(x0, x - 93), toY(y0, y), toDim(50), -30, 60);
		}

		private static int toX(int x0, int offs) {
			return x0 + offs * 3 / 2;
		}
		
		private static int toY(int y0, int offs) {
			return y0 + offs * 3 / 2;
		}
		
		private static int toDim(int offs) {
			return offs * 3 / 2;
		}
			
		private void drawText(Graphics g, int x, int y) {
			FontMetrics fm;
			String str;
			
			g.setColor(headerColor);
			g.setFont(headerFont);
			g.drawString("Logisim", x, y + 45);
			g.setFont(copyrightFont); fm = g.getFontMetrics();
			str = "\u00a9 " + Main.COPYRIGHT_YEAR;
			g.drawString(str, x + IMAGE_WIDTH - fm.stringWidth(str), y + 16);
			g.setFont(versionFont); fm = g.getFontMetrics();
			str = "Version " + Main.VERSION_NAME;
			g.drawString(str, x + IMAGE_WIDTH - fm.stringWidth(str), y + 75);
		}

		public void ancestorAdded(AncestorEvent arg0) {
			if (thread == null) {
				thread = new PanelThread(this);
				thread.start();
			}
		}

		public void ancestorRemoved(AncestorEvent arg0) {
			if (thread != null) {
				thread.running = false;
			}
		}

		public void ancestorMoved(AncestorEvent arg0) { }
	}

	private About() { }

	public static MyPanel getImagePanel() {
		return new MyPanel();
	}

	public static void showAboutDialog(JFrame owner) {
		MyPanel imgPanel = getImagePanel();
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(imgPanel);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

		JOptionPane.showMessageDialog(owner, panel,
				"Logisim " + Main.VERSION_NAME, JOptionPane.PLAIN_MESSAGE);
	}
}