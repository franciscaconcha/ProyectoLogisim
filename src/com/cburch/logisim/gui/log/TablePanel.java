/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.logisim.gui.log;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JScrollBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.cburch.logisim.data.Value;
import com.cburch.logisim.util.GraphicsUtil;

class TablePanel extends LogPanel {
	private static final Font HEAD_FONT = new Font("Serif", Font.BOLD, 14);
	private static final Font BODY_FONT = new Font("Serif", Font.PLAIN, 14);
	private static final int COLUMN_SEP = 8;
	private static final int HEADER_SEP = 4;
	
	private class MyListener implements ModelListener {
		public void selectionChanged(ModelEvent event) {
			computeRowCount();
		}

		public void entryAdded(ModelEvent event, Value[] values) {
			int oldCount = rowCount; 
			computeRowCount();
			if (oldCount == rowCount) {
				int value = vsb.getValue();
				if (value > vsb.getMinimum()
						&& value < vsb.getMaximum() - vsb.getVisibleAmount()) {
					vsb.setValue(vsb.getValue() - vsb.getUnitIncrement(-1));
				} else {
					repaint();
				}
			}
		}
		
		public void filePropertyChanged(ModelEvent event) { }
		
		private void computeRowCount() {
			Model model = getModel();
			Selection sel = model.getSelection();
			int rows = 0;
			for (int i = sel.size() - 1; i >= 0; i--) {
				int x = model.getValueLog(sel.get(i)).size();
				if (x > rows) rows = x;
			}
			if (rowCount != rows) {
				rowCount = rows;
				computePreferredSize();
			}
		}
	}
	
	private class VerticalScrollBar extends JScrollBar
			implements ChangeListener {
		private int oldMaximum = -1;
		private int oldExtent = -1;

		public VerticalScrollBar() {
			getModel().addChangeListener(this);
		}
		
		@Override
		public int getUnitIncrement(int direction) {
			int curY = getValue();
			if (direction > 0) {
				return curY > 0 ? cellHeight : cellHeight + HEADER_SEP;
			} else {
				return curY > cellHeight + HEADER_SEP ? cellHeight
						: cellHeight + HEADER_SEP;
			}
		}
	
		@Override
		public int getBlockIncrement(int direction) {
			int curY = getValue();
			int curHeight = getVisibleAmount();
			int numCells = curHeight / cellHeight - 1;
			if (numCells <= 0) numCells = 1;
			if (direction > 0) {
				return curY > 0
					? numCells * cellHeight
					: numCells * cellHeight + HEADER_SEP;
			} else {
				return curY > cellHeight + HEADER_SEP
					? numCells * cellHeight
					: numCells * cellHeight + HEADER_SEP;
			}
		}

		public void stateChanged(ChangeEvent event) {
			int newMaximum = getMaximum();
			int newExtent = getVisibleAmount();
			if (oldMaximum != newMaximum || oldExtent != newExtent) {
				if (getValue() + oldExtent >= oldMaximum) {
					setValue(newMaximum - newExtent);
				}
				oldMaximum = newMaximum;
				oldExtent = newExtent;
			}
		}
	}
	
	private MyListener myListener = new MyListener();
	private int cellWidth = 25; // reasonable start values
	private int cellHeight = 15;
	private int rowCount = 0;
	private int tableWidth;
	private int tableHeight;
	private VerticalScrollBar vsb;
	
	public TablePanel(LogFrame frame) {
		super(frame);
		vsb = new VerticalScrollBar();
		modelChanged(null, getModel());
	}
	
	@Override
	public String getTitle() {
		return Strings.get("tableTab");
	}
	
	@Override
	public String getHelpText() {
		return Strings.get("tableHelp");
	}
	
	@Override
	public void localeChanged() {
		computePreferredSize();
		repaint();
	}
	
	@Override
	public void modelChanged(Model oldModel, Model newModel) {
		if (oldModel != null) oldModel.removeModelListener(myListener);
		if (newModel != null) newModel.addModelListener(myListener);
	}
	
	public int getColumn(MouseEvent event) {
		int x = event.getX() - (getWidth() - tableWidth) / 2;
		if (x < 0) return -1;
		Selection sel = getModel().getSelection();
		int ret = (x + COLUMN_SEP / 2) / (cellWidth + COLUMN_SEP);
		return ret >= 0 && ret < sel.size() ? ret : -1;
	}
	
	public int getRow(MouseEvent event) {
		int y = event.getY() - (getHeight() - tableHeight) / 2;
		if (y < cellHeight + HEADER_SEP) return -1;
		int ret = (y - cellHeight - HEADER_SEP) / cellHeight;
		return ret >= 0 && ret < rowCount ? ret : -1;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Dimension sz = getSize();
		int top = Math.max(0, (sz.height - tableHeight) / 2);
		int left = Math.max(0, (sz.width - tableWidth) / 2);
		Model model = getModel();
		if (model == null) return;
		Selection sel = model.getSelection();
		int columns = sel.size();
		if (columns == 0) {
			g.setFont(BODY_FONT);
			GraphicsUtil.drawCenteredText(g, Strings.get("tableEmptyMessage"), sz.width / 2, sz.height / 2);
			return;
		}
		
		g.setColor(Color.GRAY);
		int lineY = top + cellHeight + HEADER_SEP / 2;
		g.drawLine(left, lineY, left + tableWidth, lineY);
		
		g.setColor(Color.BLACK);
		g.setFont(HEAD_FONT);
		FontMetrics headerMetric = g.getFontMetrics();
		int x = left;
		int y = top + headerMetric.getAscent() + 1;
		for (int i = 0; i < columns; i++) {
			x = paintHeader(sel.get(i).toShortString(), x, y, g, headerMetric);
		}
		
		g.setFont(BODY_FONT);
		FontMetrics bodyMetric = g.getFontMetrics();
		Rectangle clip = g.getClipBounds();
		int firstRow = Math.max(0, (clip.y - y) / cellHeight - 1);
		int lastRow = Math.min(rowCount, 2 + (clip.y + clip.height - y) / cellHeight);
		int y0 = top + cellHeight + HEADER_SEP;
		x = left;
		for (int col = 0; col < columns; col++) {
			SelectionItem item = sel.get(col);
			ValueLog log = model.getValueLog(item);
			int radix = item.getRadix();
			int offs = rowCount - log.size();
			y = y0 + Math.max(offs, firstRow) * cellHeight;
			for (int row = Math.max(offs, firstRow); row < lastRow; row++) {
				Value val = log.get(row - offs);
				String label = val.toDisplayString(radix);
				int width = bodyMetric.stringWidth(label);
				g.drawString(label, x + (cellWidth - width) / 2,
						y + bodyMetric.getAscent());
				y += cellHeight;
			}
			x += cellWidth + COLUMN_SEP;
		}
	}

	private int paintHeader(String header, int x, int y,
			Graphics g, FontMetrics fm) {
		int width = fm.stringWidth(header);
		g.drawString(header, x + (cellWidth - width) / 2, y);
		return x + cellWidth + COLUMN_SEP;
	}
	
	private void computePreferredSize() {
		Model model = getModel();
		Selection sel = model.getSelection();
		int columns = sel.size();
		if (columns == 0) {
			setPreferredSize(new Dimension(0, 0));
			return;
		}
		
		Graphics g = getGraphics();
		if (g == null) {
			cellHeight = 16;
			cellWidth = 24;
		} else {
			FontMetrics fm = g.getFontMetrics(HEAD_FONT);
			cellHeight = fm.getHeight();
			cellWidth = 24;
			for (int i = 0; i < columns; i++) {
				String header = sel.get(i).toShortString();
				cellWidth = Math.max(cellWidth, fm.stringWidth(header));
			}
		}
		
		tableWidth = (cellWidth + COLUMN_SEP) * columns - COLUMN_SEP;
		tableHeight = cellHeight * (1 + rowCount) + HEADER_SEP;
		setPreferredSize(new Dimension(tableWidth, tableHeight));
		revalidate();
		repaint();
	}

	JScrollBar getVerticalScrollBar() {
		return vsb;
	}
}
