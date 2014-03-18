/*
 * Copyright (C) 2014 Ivan Ridao Freitas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivanrf.renamer.utils;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class LocaleButton extends JButton {
	
	private static final long serialVersionUID = 170314L;
	
	private LocaleChangeListener localeChangeListener;

	public LocaleButton(Vector<Locale> locales, Locale defaultLocale) {
		super();
		
		final JPopupMenu popup = new JPopupMenu();
		for (int i = 0; i < locales.size(); i++) {
			final Locale locale = locales.get(i);
			String text = locale.getDisplayName();
			final Icon icon = getLocaleIcon(locale);
			popup.add(new JMenuItem(new AbstractAction(text, icon) {
				private static final long serialVersionUID = 170314L;
	            public void actionPerformed(ActionEvent e) {
	            	setIcon(icon);
	            	if(localeChangeListener != null)
	            		localeChangeListener.localeChanged(locale);
	            }
	        }));
		}
		
		this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            	popup.show(e.getComponent(), 0, (int) -popup.getPreferredSize().getHeight() - 5);
            }
        });
		
		setInitialIcon(locales, defaultLocale);
		setMargin(new Insets(5,5,5,5));
	}
	
	private Icon getLocaleIcon(Locale locale) {
		String localeSt = locale.getLanguage() + ((locale.getCountry().length() != 0) ? "_" + locale.getCountry() : "");
		return Images.getFlagIcon(localeSt);
	}
	
	private void setInitialIcon(Vector<Locale> locales, Locale defaultLocale) {
		this.setIcon(getLocaleIcon(defaultLocale));
		String defLanguage = Locale.getDefault().getLanguage();
		String defCountry = Locale.getDefault().getCountry();
		for (int i = 0; i < locales.size(); i++) {
			Locale locale = locales.get(i);
			if (locale.getLanguage().equals(defLanguage) && locale.getCountry().equals(defCountry)) {
				this.setIcon(getLocaleIcon(locale));
				break;
			} else if (locale.getLanguage().equals(defLanguage))
				this.setIcon(getLocaleIcon(locale));
		}
	}
	
	public void setLocaleChangeListener(LocaleChangeListener localeChangeListener) {
		this.localeChangeListener = localeChangeListener;
	}
	
}
