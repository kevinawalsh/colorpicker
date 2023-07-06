/*
 * @(#)IndexedColorPickerDialog.java  1.0  2008-03-01
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dialog;
import java.awt.image.IndexColorModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/** This wraps an <code>IndexedColorPicker</code> in a simple dialog with "OK" and "Cancel" options.
 * <P>(This object is used by the static calls in <code>IndexedColorPicker</code> to show a dialog.)
 *
 */
class IndexedColorPickerDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	IndexedColorPicker cp;
	JButton ok = new JButton(ColorPicker.strings.getObject("OK").toString());
	JButton cancel = new JButton(ColorPicker.strings.getObject("Cancel").toString());
	int returnValue = -1;

	public IndexedColorPickerDialog(Frame owner,Color color,IndexColorModel cm) {
		super(owner);
		initialize(owner,color,cm);
	}

	public IndexedColorPickerDialog(Dialog owner,Color color,IndexColorModel cm) {
		super(owner);
		initialize(owner,color,cm);
	}

	private void initialize(Component owner,Color color,IndexColorModel cm) {
		cp = new IndexedColorPicker(cm,true);
		setModal(true);
		setResizable(false);
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0;
		c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10,10,10,10);
		getContentPane().add(cp,c);
		c.gridy++; c.gridwidth = 1;
		getContentPane().add(new JPanel(),c);
		c.gridx++; c.weightx = 0;
		getContentPane().add(cancel,c);
		c.gridx++; c.weightx = 0;
		getContentPane().add(ok,c);
		cp.setColor(color);
		pack();
		setLocationRelativeTo(owner);

		ok.addActionListener(e -> {
			returnValue = cp.getColorIndex();
			setVisible(false);
		});
		cancel.addActionListener(e -> setVisible(false));

		getRootPane().setDefaultButton(ok);
	}

	/** @return the color committed when the user clicked 'OK'.  Note this returns <code>null</code>
	 * if the user canceled this dialog, or exited via the close decoration.
	 */
	public int getColorIndex() {
		return returnValue;
	}
}

