/*
 * @(#)IndexedColorPickerPanel.java  1.0  2008-03-01
 *
 * Copyright (c) 2008 Jeremy Wood
 * E-mail: mickleness@gmail.com
 * All rights reserved.
 *
 * The copyright of this software is owned by Jeremy Wood.
 * You may not use, copy or modify this software, except in
 * accordance with the license agreement you entered into with
 * Jeremy Wood. For details see accompanying license terms.
 */

package com.bric.swing;

import java.util.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.image.IndexColorModel;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import com.bric.awt.*;

/** This is the large graphic element in the <code>IndexedColorPicker</code>
 * that depicts a range of colors.
 * <P>The user can click in this panel to select a new color.  The selected color is
 * highlighted with a circle drawn around it.  Also once this
 * component has the keyboard focus, the user can use the arrow keys to
 * traverse the available colors.
 * <P>The graphic will be based on either the width and height of this component.
 *
 * @version 1.5
 * @author Jeremy Wood
 * @author Kevin Walsh
 */
public class IndexedColorPickerPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/** The maximum size the graphic will be.  No matter
	 *  how big the panel becomes, the graphic will not exceed
	 *  this length.
	 *  <P>(This is enforced because only 1 BufferedImage is used
	 *  to render the graphic.  This image is created once at a fixed
	 *  size and is never replaced.)
	 */
	public static int MAX_COLS = 16; // MAX_ROWS = 16 too
	public static int MAX_CELLSIZE = 30; // pixels
	public static int PREF_CELLSIZE = 20; // pixels
	public static int MAX_SIZE = MAX_CELLSIZE*MAX_COLS;
	private ArrayList<ChangeListener> changeListeners = new ArrayList<>();

	private int colorIndex;
	private int rows, cols;
	private Color[] colors;

	private int width, height, cellSize;
	private void sizeToFit() {
		width = Math.min(MAX_SIZE, getWidth()-2*PAD);
		height = Math.min(MAX_SIZE, getHeight()-2*PAD);
		cellSize = Math.min(
				Math.min(width / cols, MAX_CELLSIZE),
				Math.min(height / rows, MAX_CELLSIZE));
		width = cellSize * cols;
		height = cellSize * rows;
	}

	MouseInputListener mouseListener = new MouseInputAdapter() {
		public void mousePressed(MouseEvent e) {
			requestFocus();
			Point p = e.getPoint();

			p.translate(-(getWidth()/2-width/2), -(getHeight()/2-height/2));
			int c = p.x/cellSize;
			int r = p.y/cellSize;
			if (c < 0 || c >= cols || r < 0 || r >= rows)
				return;
			setColorIndex(r*cols+c);
		}

		public void mouseDragged(MouseEvent e) {
			mousePressed(e);
		}
	};

	KeyListener keyListener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			int idx = colorIndex;
			if(e.getKeyCode()==KeyEvent.VK_LEFT) idx -= 1;
			else if(e.getKeyCode()==KeyEvent.VK_RIGHT) idx += 1;
			else if(e.getKeyCode()==KeyEvent.VK_UP) idx -= cols;
			else if(e.getKeyCode()==KeyEvent.VK_DOWN) idx += cols;
			else return;

			if (idx < 0 || idx >= colors.length)
				return;
			setColorIndex(idx);
		}
	};

	FocusListener focusListener = new FocusListener() {
		public void focusGained(FocusEvent e) { repaint(); }
		public void focusLost(FocusEvent e) { repaint(); }
	};

	ComponentListener componentListener = new ComponentAdapter() {
		public void componentResized(ComponentEvent e) {
			regenerateImage();
			repaint();
		}
	};

	BufferedImage image = new BufferedImage(MAX_SIZE, MAX_SIZE, BufferedImage.TYPE_INT_ARGB);
	IndexedColorPicker colorPicker;

	/** Creates a new <code>IndexedColorPickerPanel</code> */
	public IndexedColorPickerPanel(IndexedColorPicker cp) {
		colorPicker = cp;
		colors = colorPicker.getColors();

		int n = colors.length;

		// try a square
		cols = (int)(Math.sqrt(n) + 0.5);
		rows = (n + (cols - 1)) / cols;
		int blanks = rows*cols - n;
		// try a wider rectangle
		for (int nc = cols+1; nc < n && nc <= MAX_COLS; nc++) {
			int nr = (n + (nc - 1)) / nc;
			int nb = nr*nc - n;
			if (nc > 2*nr) // too wide
				break;
			if (nb >= blanks) // too many blanks
				continue;
			cols = nc;
			rows = nr;
			blanks = nb;
		}

		// setMaximumSize(new Dimension(cols*MAX_CELLSIZE+2*PAD, rows*MAX_CELLSIZE+2*PAD));
		setPreferredSize(new Dimension(cols*PREF_CELLSIZE+2*PAD, rows*PREF_CELLSIZE+2*PAD));

		setColorIndex(0);

		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);

		setFocusable(true);
		addKeyListener(keyListener);
		addFocusListener(focusListener);

		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		addComponentListener(componentListener);
	}

	/** This listener will be notified when the current color index
	 * changes.
	 */
	public void addChangeListener(ChangeListener l) {
		if(!changeListeners.contains(l))
			changeListeners.add(l);
	}

	/** Remove a <code>ChangeListener</code> so it is no longer
	 * notified when the selected color changes.
	 */
	public void removeChangeListener(ChangeListener l) {
		changeListeners.remove(l);
	}

	protected void fireChangeListeners() {
		for(ChangeListener l : changeListeners) {
			try {
				l.stateChanged(new ChangeEvent(this));
			} catch(RuntimeException e) {
				e.printStackTrace();
			}
		}
	}

	static final int PAD = 6;
	Insets imagePadding = new Insets(PAD, PAD, PAD, PAD);

	public void paint(Graphics g) {
		super.paint(g);

		int r = colorIndex / cols;
		int c = colorIndex % cols;
		int px = (2*c+1)*cellSize/2;
		int py = (2*r+1)*cellSize/2;

		Graphics2D g2 = (Graphics2D)g;

		g2.translate(getWidth()/2-width/2, getHeight()/2-height/2);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Rectangle shape = new Rectangle(0,0,width,height); // fixme: adjust borders

		if(hasFocus())
			PaintUtils.paintFocus(g2,shape,5);

		g2.drawImage(image, 0, 0, width, height, 0, 0, width, height, null);

		PaintUtils.drawBevel(g2, shape);

		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(1));
		g2.draw(new Ellipse2D.Float(px-3,py-3,6,6));
		g2.setColor(Color.black);
		g2.draw(new Ellipse2D.Float(px-4,py-4,8,8));

		// g.translate(-PAD, -PAD);
	}

	/** Sets the selected color of this panel.
	 * <P>This method may regenerate the graphic if necessary.
	 */
	public void setColorIndex(int idx) {
		if (idx == colorIndex)
			return;
		if (idx < 0 || idx >= colors.length)
			throw new IllegalArgumentException("The color index ("+idx+") must be between [0,"+colors.length+"].");
		colorIndex = idx;
		repaint();
		fireChangeListeners();
	}

	public int getColorIndex() { return colorIndex; }

	/** A row of pixel data we recycle every time we regenerate this image. */
	private int[] row = new int[MAX_SIZE];

	/** Regenerates the image. */
	private synchronized void regenerateImage() {
		sizeToFit();

		for (int r = 0; r < rows; r++) {
			for (int x = 0; x<width; x++)
				row[x] = colors[r*cols+x/cellSize].getRGB();
			for(int y = 0; y<cellSize; y++) {
				image.getRaster().setDataElements(0, r*cellSize+y, width, 1, row);
			}
		}
		repaint();
	}
}
