/*
 * @(#)IndexedColorPicker.java  1.0  2008-03-01
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

import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.image.IndexColorModel;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.*;

/** This is a panel that offers a set of controls to pick a color from an
 * IndexColorModel, with up to 256 indexed colors.
 * To use this class to create a color choosing dialog, simply call:
 * <BR><code>IndexedColorPicker.showDialog(frame, originalColor, colorModel);</code>
 * 
 * @version 1.4
 * @author Jeremy Wood
 * @author Kevin Walsh
 */
public class IndexedColorPicker extends JPanel {
	private static final long serialVersionUID = 3L;

	/** The localized strings used in this (and related) panel(s). */
	protected static ResourceBundle strings = ResourceBundle.getBundle("resources.bric.ColorPicker");

	/** Example indexed color models. */
	public static final IndexColorModel GRAY4 = new IndexColorModel(4, 16,
			new int[] {
				0x000000, 0x111111, 0x222222, 0x333333, 0x444444, 0x555555, 0x666666, 0x777777,
				0x888888, 0x999999, 0xaaaaaa, 0xbbbbbb, 0xcccccc, 0xdddddd, 0xeeeeee, 0xffffff,
			}, 0, 0, null);
	public static final IndexColorModel ATARI = new IndexColorModel(7, 128,
			new int[] {
				0x000000, 0x0a0a0a, 0x373737, 0x5f5f5f, 0x7a7a7a, 0xa1a1a1, 0xc5c5c5, 0xededed,
				0x000000, 0x352100, 0x5a4500, 0x816c00, 0x9c8700, 0xc3af01, 0xe8d326, 0xfffa4d,
				0x310000, 0x590700, 0x7d2b00, 0xa45200, 0xbf6d04, 0xe7952b, 0xffb950, 0xffe077,
				0x470000, 0x6e0000, 0x931302, 0xba3b2a, 0xd55545, 0xfc7d6c, 0xffa190, 0xffc9b8,
				0x4b0002, 0x720029, 0x96034e, 0xbe2a75, 0xd94590, 0xff6cb7, 0xff91dc, 0xffb8ff,
				0x3c0049, 0x640070, 0x880094, 0xaf24bc, 0xca3fd7, 0xf266fe, 0xff8aff, 0xffb2ff,
				0x1e007d, 0x4500a5, 0x6902c9, 0x9129f1, 0xac44ff, 0xd36bff, 0xf790ff, 0xffb7ff,
				0x000096, 0x1d00bd, 0x4111e1, 0x6939ff, 0x8453ff, 0xab7bff, 0xcf9fff, 0xf7c7ff,
				0x00008d, 0x0004b4, 0x1728d9, 0x3f50ff, 0x5a6bff, 0x8192ff, 0xa5b6ff, 0xcddeff,
				0x000065, 0x001e8c, 0x0042b0, 0x1b6ad8, 0x3685f3, 0x5dacff, 0x82d0ff, 0xa9f8ff,
				0x000f25, 0x00364c, 0x005a70, 0x048298, 0x1f9db3, 0x47c4da, 0x6be8fe, 0x92ffff,
				0x002000, 0x004701, 0x006b25, 0x00934d, 0x1aae68, 0x42d58f, 0x66f9b4, 0x8dffdb,
				0x002700, 0x004e00, 0x007200, 0x0d9a06, 0x28b520, 0x4fdc48, 0x74ff6c, 0x9bff94,
				0x002200, 0x004a00, 0x036e00, 0x2b9500, 0x45b000, 0x6dd812, 0x91fc36, 0xb9ff5d,
				0x000a00, 0x073a00, 0x2b5f00, 0x528600, 0x6da100, 0x95c800, 0xb9ed1c, 0xe0ff43,
				0x000000, 0x352100, 0x5a4500, 0x816c00, 0x9c8700, 0xc3af01, 0xe8d326, 0xfffa4d,
			}, 0, 0, null);
	public static final IndexColorModel XTERM16 = new IndexColorModel(4, 16,
			new int[] {
				0x000000, 0x800000, 0x008000, 0x808000, 0x000080, 0x800080, 0x008080, 0xc0c0c0,
				0x808080, 0xff0000, 0x00ff00, 0xffff00, 0x0000ff, 0xff00ff, 0x00ffff, 0xffffff,
			}, 0, 0, null);
	public static final IndexColorModel XTERM256 = new IndexColorModel(8, 256,
			new int[] {
				0x000000, 0x800000, 0x008000, 0x808000, 0x000080, 0x800080, 0x008080, 0xc0c0c0,
				0x808080, 0xff0000, 0x00ff00, 0xffff00, 0x0000ff, 0xff00ff, 0x00ffff, 0xffffff,
				0x000000, 0x00005f, 0x000087, 0x0000af, 0x0000d7, 0x0000ff, 0x005f00, 0x005f5f,
				0x005f87, 0x005faf, 0x005fd7, 0x005fff, 0x008700, 0x00875f, 0x008787, 0x0087af,
				0x0087d7, 0x0087ff, 0x00af00, 0x00af5f, 0x00af87, 0x00afaf, 0x00afd7, 0x00afff,
				0x00d700, 0x00d75f, 0x00d787, 0x00d7af, 0x00d7d7, 0x00d7ff, 0x00ff00, 0x00ff5f,
				0x00ff87, 0x00ffaf, 0x00ffd7, 0x00ffff, 0x5f0000, 0x5f005f, 0x5f0087, 0x5f00af,
				0x5f00d7, 0x5f00ff, 0x5f5f00, 0x5f5f5f, 0x5f5f87, 0x5f5faf, 0x5f5fd7, 0x5f5fff,
				0x5f8700, 0x5f875f, 0x5f8787, 0x5f87af, 0x5f87d7, 0x5f87ff, 0x5faf00, 0x5faf5f,
				0x5faf87, 0x5fafaf, 0x5fafd7, 0x5fafff, 0x5fd700, 0x5fd75f, 0x5fd787, 0x5fd7af,
				0x5fd7d7, 0x5fd7ff, 0x5fff00, 0x5fff5f, 0x5fff87, 0x5fffaf, 0x5fffd7, 0x5fffff,
				0x870000, 0x87005f, 0x870087, 0x8700af, 0x8700d7, 0x8700ff, 0x875f00, 0x875f5f,
				0x875f87, 0x875faf, 0x875fd7, 0x875fff, 0x878700, 0x87875f, 0x878787, 0x8787af,
				0x8787d7, 0x8787ff, 0x87af00, 0x87af5f, 0x87af87, 0x87afaf, 0x87afd7, 0x87afff,
				0x87d700, 0x87d75f, 0x87d787, 0x87d7af, 0x87d7d7, 0x87d7ff, 0x87ff00, 0x87ff5f,
				0x87ff87, 0x87ffaf, 0x87ffd7, 0x87ffff, 0xaf0000, 0xaf005f, 0xaf0087, 0xaf00af,
				0xaf00d7, 0xaf00ff, 0xaf5f00, 0xaf5f5f, 0xaf5f87, 0xaf5faf, 0xaf5fd7, 0xaf5fff,
				0xaf8700, 0xaf875f, 0xaf8787, 0xaf87af, 0xaf87d7, 0xaf87ff, 0xafaf00, 0xafaf5f,
				0xafaf87, 0xafafaf, 0xafafd7, 0xafafff, 0xafd700, 0xafd75f, 0xafd787, 0xafd7af,
				0xafd7d7, 0xafd7ff, 0xafff00, 0xafff5f, 0xafff87, 0xafffaf, 0xafffd7, 0xafffff,
				0xd70000, 0xd7005f, 0xd70087, 0xd700af, 0xd700d7, 0xd700ff, 0xd75f00, 0xd75f5f,
				0xd75f87, 0xd75faf, 0xd75fd7, 0xd75fff, 0xd78700, 0xd7875f, 0xd78787, 0xd787af,
				0xd787d7, 0xd787ff, 0xdfaf00, 0xdfaf5f, 0xdfaf87, 0xdfafaf, 0xdfafdf, 0xdfafff,
				0xdfdf00, 0xdfdf5f, 0xdfdf87, 0xdfdfaf, 0xdfdfdf, 0xdfdfff, 0xdfff00, 0xdfff5f,
				0xdfff87, 0xdfffaf, 0xdfffdf, 0xdfffff, 0xff0000, 0xff005f, 0xff0087, 0xff00af,
				0xff00df, 0xff00ff, 0xff5f00, 0xff5f5f, 0xff5f87, 0xff5faf, 0xff5fdf, 0xff5fff,
				0xff8700, 0xff875f, 0xff8787, 0xff87af, 0xff87df, 0xff87ff, 0xffaf00, 0xffaf5f,
				0xffaf87, 0xffafaf, 0xffafdf, 0xffafff, 0xffdf00, 0xffdf5f, 0xffdf87, 0xffdfaf,
				0xffdfdf, 0xffdfff, 0xffff00, 0xffff5f, 0xffff87, 0xffffaf, 0xffffdf, 0xffffff,
				0x080808, 0x121212, 0x1c1c1c, 0x262626, 0x303030, 0x3a3a3a, 0x444444, 0x4e4e4e,
				0x585858, 0x626262, 0x6c6c6c, 0x767676, 0x808080, 0x8a8a8a, 0x949494, 0x9e9e9e,
				0xa8a8a8, 0xb2b2b2, 0xbcbcbc, 0xc6c6c6, 0xd0d0d0, 0xdadada, 0xe4e4e4, 0xeeeeee,
			}, 0, 0, null);

	static class DemoFrame extends JFrame {
		IndexedColorPicker picker;
		JButton button;

		DemoFrame(IndexColorModel cm) {
			super("Demo");
			getContentPane().setLayout(new GridBagLayout());
			setColorModel(cm);
		}

		void setColorModel(IndexColorModel cm) {
			if (picker != null)
				getContentPane().remove(picker);

			picker = new IndexedColorPicker(cm, false);

			GridBagConstraints c = new GridBagConstraints();

			c.gridx = 0; c.gridy = 0;
			c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
			getContentPane().add(picker,c);

			// picker.setMinimumSize(new Dimension(165,165));
			// picker.setPreferredSize(new Dimension(325,325));
			picker.setValueControlsVisible(false);
			picker.setPreviewSwatchVisible(false);
			picker.setColorIndex(0);

			if (button == null) {
				button = new JButton("Show Dialog");
				c.gridx = 0; c.gridy = 1;
				c.weightx = 0; c.weighty = 0; c.fill = GridBagConstraints.NONE;
				c.anchor = GridBagConstraints.CENTER;
				c.insets = new Insets(10,10,10,10);
				getContentPane().add(button,c);

				button.addActionListener(e -> {
					int idx = IndexedColorPicker.showDialog(DemoFrame.this,
							picker.getColor(), picker.getColorModel());
					if (idx >= 0)
						picker.setColorIndex(idx);
				});
			}
		}

	}

	/** This demonstrates how to customize a small <code>ColorPicker</code> component.
	*/
	public static void main(String[] args) {
		final IndexColorModel cm = args.length == 0 ? XTERM16 :
				args[0].equals("atari") ? ATARI :
				args[0].equals("gray4") ? GRAY4 :
				args[0].equals("xterm16") ? XTERM16 :
				args[0].equals("xterm256") ? XTERM256 :
				null;
		if (cm == null)
			throw new IllegalArgumentException("Unrecognized color model ("+args[0]+")");

		final DemoFrame demo = new DemoFrame(cm);
		final JWindow palette = new JWindow();
		final JComboBox comboBox = new JComboBox();

		palette.getContentPane().setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.insets = new Insets(5,5,5,5);
		palette.getContentPane().add(comboBox,c);

		comboBox.addItem("atari");
		comboBox.addItem("gray4");
		comboBox.addItem("xterm16");
		comboBox.addItem("xterm256");

		palette.pack();
		palette.setLocationRelativeTo(null);

		comboBox.addActionListener(e -> {
			int i = ((JComboBox)e.getSource()).getSelectedIndex();
			if (i == 0)
				demo.setColorModel(ATARI);
			else if (i == 1)
				demo.setColorModel(GRAY4);
			else if (i == 2)
				demo.setColorModel(XTERM16);
			else if (i == 3)
				demo.setColorModel(XTERM256);
		});
		comboBox.setSelectedIndex(0);

		demo.addComponentListener(new ComponentAdapter() {
			public void componentMoved(ComponentEvent e) {
				Point p = demo.getLocation();
				palette.setLocation(new Point(p.x-palette.getWidth()-10,p.y));
			}
		});
		demo.pack();
		demo.setLocationRelativeTo(null);
		demo.setVisible(true);
		palette.setVisible(true);

		demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static int showDialog (Container owner, Color originalColor) {
		if (owner instanceof Window) {
			return showDialog ((Window) owner, originalColor);
		} else {
			Logger.getLogger(ColorPicker.class.getName()).log (Level.SEVERE,
					"Not a Window subclass: " + owner);
			Toolkit.getDefaultToolkit().beep();
		}
		return -1;
	}

	/** This creates a modal dialog prompting the user to select a color.
	 * <P>This uses a generic dialog title: "Choose a Color".
	 * 
	 * @param owner the dialog this new dialog belongs to.  This must be a Frame or a Dialog.
	 * Java 1.6 supports Windows here, but this package is designed/compiled to work in Java 1.4,
	 * so an <code>IllegalArgumentException</code> will be thrown if this component is a <code>Window</code>.
	 * @param originalColor the color the <code>ColorPicker</code> initially points to.
	 * @param cm the index color model to select from, up to 256 colors.
	 * @return the <code>Color</code> the user chooses, or <code>null</code> if the user cancels the dialog.
	 */
	public static int showDialog(Window owner,Color originalColor,IndexColorModel cm) {
		return showDialog(owner, null, originalColor, cm);
	}

	/** This creates a modal dialog prompting the user to select a color.
	 * 
	 * @param owner the dialog this new dialog belongs to.  This must be a Frame or a Dialog.
	 * Java 1.6 supports Windows here, but this package is designed/compiled to work in Java 1.4,
	 * so an <code>IllegalArgumentException</code> will be thrown if this component is a <code>Window</code>.
	 * @param title the title for the dialog.
	 * @param originalColor the color the <code>ColorPicker</code> initially points to.
	 * @param cm the index color model to select from, up to 256 colors.
	 * @return the <code>Color</code> the user chooses, or <code>null</code> if the user cancels the dialog.
	 */
	public static int showDialog(Window owner, String title, Color originalColor, IndexColorModel cm) {
		IndexedColorPickerDialog d;
		if(owner instanceof Frame || owner==null) {
			d = new IndexedColorPickerDialog( (Frame)owner, originalColor, cm);
		} else if(owner instanceof Dialog){
			d = new IndexedColorPickerDialog( (Dialog)owner, originalColor, cm);
		} else {
			throw new IllegalArgumentException("the owner ("+owner.getClass().getName()+") must be a java.awt.Frame or a java.awt.Dialog");
		}

		d.setTitle(title == null ? 
				strings.getObject("ColorPickerDialogTitle").toString() : 
				title);
		d.pack();
		d.setVisible(true);
		return d.getColorIndex();
	}

	/** <code>PropertyChangeEvents</code> will be triggered for this property when the selected color
	 * changes.
	 * 
	 */
	public static final String SELECTED_COLOR_PROPERTY = "selected color";

	ChangeListener changeListener = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			Object src = e.getSource();

			if(src==colorPanel) {
				if(adjustingColorPanel>0)
					return;
				int idx = colorPanel.getColorIndex();
				setColorIndex(idx);
			} else if (src==valueSpinner) {
				if(adjustingValueSpinner>0)
					return;
				int idx =((Number)valueSpinner.getValue()).intValue();
				setColorIndex(idx);
			}
		}
	};

	private ColorSwatch preview = new ColorSwatch(50);
	private JLabel valueLabel = new JLabel(strings.getObject("valueLabel").toString());
	private JSpinner valueSpinner;

	/** Used to indicate when we're internally adjusting the selected color of the ColorPanel.
	 * If this equals zero, then incoming events are triggered by the user and must be processed.
	 * If this is not equal to zero, then incoming events are triggered by another method
	 * that's already responding to the user's actions.
	 */
	private int adjustingColorPanel = 0;

	/** Used to indicate when we're internally adjusting the value of the value field.
	 * If this equals zero, then incoming events are triggered by the user and must be processed.
	 * If this is not equal to zero, then incoming events are triggered by another method
	 * that's already responding to the user's actions.
	 */
	private int adjustingValueSpinner = 0;

	private IndexedColorPickerPanel colorPanel;

	private IndexColorModel colorModel;
	private int[] modelRGB;
	private Color[] colors;

	private int colorIndex;

	public IndexColorModel getColorModel() { return colorModel; }
	public Color[] getColors() { return colors; }

	/** Create a new <code>ColorPicker</code>.
	 * 
	 * <code>ColorPicker</code> are optional.  This boolean will control whether they
	 * are shown or not.
	 * <P>It may be that your users will never need or want numeric control when
	 * they choose their colors, so hiding this may simplify your interface.
	 * @param cm the index color model to select from, up to 256 colors.
	 */
	public IndexedColorPicker(IndexColorModel cm, boolean showExpertControls) {
		super(new GridBagLayout());

		this.colorModel = cm;

		int n = colorModel.getMapSize();
		if (n < 2 || n > 256)
			throw new IllegalArgumentException("The color model must have between 2 and 256 colors.");

		modelRGB = new int[n];
		colorModel.getRGBs(modelRGB);
		colors = new Color[n];
		for (int i = 0; i < n; i++)
			colors[i] = new Color(modelRGB[i]);

		colorPanel = new IndexedColorPickerPanel(this);

		valueSpinner = new JSpinner(new SpinnerNumberModel(0,0,n,1));

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0; c.gridy = 0; c.weightx = 1; c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER; c.insets = new Insets(3,3,3,3);
		add(colorPanel,c);

		JPanel options = new JPanel(new GridBagLayout());
		c.gridx = 0; c.gridy = 0; c.weightx = 0; c.weighty = 1;
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.CENTER;
		options.add(preview,c);
		preview.setOpaque(true);

		if (showExpertControls) {
			c.weightx = 0; c.weighty = 0;
			c.anchor = GridBagConstraints.WEST; c.fill = GridBagConstraints.NONE;
			c.gridy++;
			options.add(valueLabel,c);
			c.gridy++;
			options.add(valueSpinner,c);
		}

		c.gridx = 1; c.gridy = 0; c.weightx = 0; c.weighty = 1;
		c.fill = GridBagConstraints.VERTICAL;
		add(options, c);

		// colorPanel.setPreferredSize(new Dimension(500, 500)); // preview.getPreferredSize().height, 
		// preview.getPreferredSize().height));

		colorPanel.addChangeListener(changeListener);
		valueSpinner.addChangeListener(changeListener);
	}

	/** This controls whether the value spinner (and label) are visible or not.
	*/
	public void setValueControlsVisible(boolean b) {
		valueLabel.setVisible(b);
		valueSpinner.setVisible(b);
	}

	/** This controls whether the preview swatch visible or not.
	 * <P>Note this lives inside the "expert controls", so if <code>setExpertControlsVisible(false)</code>
	 * has been called, then calling this method makes no difference: the swatch will be hidden.
	 */
	public void setPreviewSwatchVisible(boolean b) {
		preview.setVisible(b);
	}

	/** @return the current RGB coordinates of this <code>ColorPicker</code>.
	 * Each value is between [0,255].
	 * 
	 */
	public int[] getRGB() {
		Color c = colors[colorIndex];
		return new int[] { c.getRed(), c.getGreen(), c.getBlue() };
	}

	public int getColorIndex() {
		return colorIndex;
	}

	/** Sets the current color of this <code>ColorPicker</code>, or if the color
	 * is not found, sets the color index to 0.
	 */
	public void setColor(Color c) {
		setRGB(c.getRed(),c.getGreen(),c.getBlue());
	}

	private int findColor(int r, int g, int b) {
		int rgb = (r << 16) | (g << 8) | b;
		for (int i = 0; i < modelRGB.length; i++)
			if ((modelRGB[i]&0xffffff) == rgb)
				return i;
		return 0;
	}

	/** Sets the current color of this <code>ColorPicker</code>
	 * 
	 * @param r the red value.  Must be between [0,255].
	 * @param g the green value.  Must be between [0,255].
	 * @param b the blue value.  Must be between [0,255].
	 */
	public void setRGB(int r,int g,int b) {
		if(r<0 || r>255)
			throw new IllegalArgumentException("The red value ("+r+") must be between [0,255].");
		if(g<0 || g>255)
			throw new IllegalArgumentException("The green value ("+g+") must be between [0,255].");
		if(b<0 || b>255)
			throw new IllegalArgumentException("The blue value ("+b+") must be between [0,255].");

		int idx = findColor(r, g, b);
		setColorIndex(idx);
	}

	public void setColorIndex(int idx) {
		if (idx < 0 || idx >= modelRGB.length)
			throw new IllegalArgumentException("The color index ("+idx+") must be between [0,"+(modelRGB.length-1)+"].");

		int lastIndex = colorIndex;

		adjustingColorPanel++;
		try {
			colorIndex = idx;
			preview.setForeground(colors[colorIndex]);
			colorPanel.setColorIndex(colorIndex);
			updateValueSpinner();
		} finally {
			adjustingColorPanel--;
		}
		if (lastIndex != colorIndex)
			firePropertyChange(SELECTED_COLOR_PROPERTY,colors[lastIndex],colors[colorIndex]);
	}

	/** @return the current <code>Color</code> this <code>ColorPicker</code> has selected.
	*/
	public Color getColor() {
		return colors[colorIndex];
	}

	/** @return the <code>ColorPickerPanel</code> this <code>ColorPicker</code> displays. */
	public IndexedColorPickerPanel getColorPanel() {
		return colorPanel;
	}

	private void updateValueSpinner() {
		adjustingValueSpinner++;
		try {
			valueSpinner.setValue(Integer.valueOf(colorIndex));
		} finally {
			adjustingValueSpinner--;
		}
	}

}
