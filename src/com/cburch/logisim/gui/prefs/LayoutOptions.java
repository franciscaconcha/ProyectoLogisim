/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.logisim.gui.prefs;

import javax.swing.JPanel;

import com.cburch.logisim.circuit.RadixOption;
import com.cburch.logisim.prefs.AppPreferences;
import com.cburch.logisim.util.TableLayout;

class LayoutOptions extends OptionsPanel {
	private PrefBoolean[] checks;
	private PrefOptionList afterAdd;
	private PrefOptionList radix1;
	private PrefOptionList radix2;

	public LayoutOptions(PreferencesFrame window) {
		super(window);
		
		checks = new PrefBoolean[] {
				new PrefBoolean(AppPreferences.PRINTER_VIEW,
						Strings.getter("layoutPrinterView")),
				new PrefBoolean(AppPreferences.ATTRIBUTE_HALO,
						Strings.getter("layoutAttributeHalo")),
				new PrefBoolean(AppPreferences.COMPONENT_TIPS,
						Strings.getter("layoutShowTips")),
				new PrefBoolean(AppPreferences.MOVE_KEEP_CONNECT,
						Strings.getter("layoutMoveKeepConnect")),
				new PrefBoolean(AppPreferences.ADD_SHOW_GHOSTS,
						Strings.getter("layoutAddShowGhosts")),
			};

		for (int i = 0; i < 2; i++) {
			RadixOption[] opts = RadixOption.OPTIONS;
			PrefOption[] items = new PrefOption[opts.length];
			for (int j = 0; j < RadixOption.OPTIONS.length; j++) {
				items[j] = new PrefOption(opts[j].getSaveString(), opts[j].getDisplayGetter());
			}
			if (i == 0) {
				radix1 = new PrefOptionList(AppPreferences.POKE_WIRE_RADIX1,
						Strings.getter("layoutRadix1"), items);
			} else {
				radix2 = new PrefOptionList(AppPreferences.POKE_WIRE_RADIX2,
						Strings.getter("layoutRadix2"), items);
			}
		}
		afterAdd = new PrefOptionList(AppPreferences.ADD_AFTER,
				Strings.getter("layoutAddAfter"),
				new PrefOption[] {
					new PrefOption(AppPreferences.ADD_AFTER_UNCHANGED,
							Strings.getter("layoutAddAfterUnchanged")),
					new PrefOption(AppPreferences.ADD_AFTER_EDIT,
							Strings.getter("layoutAddAfterEdit")) });
		
		JPanel panel = new JPanel(new TableLayout(2));
		panel.add(afterAdd.getJLabel());
		panel.add(afterAdd.getJComboBox());
		panel.add(radix1.getJLabel());
		panel.add(radix1.getJComboBox());
		panel.add(radix2.getJLabel());
		panel.add(radix2.getJComboBox());

		setLayout(new TableLayout(1));
		for (int i = 0; i < checks.length; i++) {
			add(checks[i]);
		}
		add(panel);
	}

	@Override
	public String getTitle() {
		return Strings.get("layoutTitle");
	}

	@Override
	public String getHelpText() {
		return Strings.get("layoutHelp");
	}
	
	@Override
	public void localeChanged() {
		for (int i = 0; i < checks.length; i++) {
			checks[i].localeChanged();
		}
		radix1.localeChanged();
		radix2.localeChanged();
	}
}
