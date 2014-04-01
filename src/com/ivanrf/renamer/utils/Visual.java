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

import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.FocusManager;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.GeminiSkin;
import org.pushingpixels.substance.api.skin.SkinChangeListener;

import com.ivanrf.renamer.visual.Principal;

public class Visual {
	
	private static String CONFIG_FILE = getLocalDir() + File.separator + "config.ini";
	private static String LABELS_BUNDLE = "com.ivanrf.renamer.resources.Labels";
	
	public static String SKIN_DEFAULT = "org.pushingpixels.substance.api.skin.GeminiSkin";
	public static String FIND_DEFAULT = "";
	public static String REPLACE_DEFAULT = "";
	public static Locale SYSTEM_LOCALE = Locale.getDefault(); //JVM locale, before I change it

	public static void setSkin() {
		SubstanceLookAndFeel.registerSkinChangeListener(new SkinChangeListener(){
			@Override
			public void skinChanged() {
				saveConfig();
			}
		});
		
		loadConfig();
		
		boolean set = SubstanceLookAndFeel.setSkin(SKIN_DEFAULT);
		if(!set)
			SubstanceLookAndFeel.setSkin(new GeminiSkin());
		UIManager.put(SubstanceLookAndFeel.SHOW_EXTRA_WIDGETS, Boolean.TRUE);
	}
	
	public static String getLocalDir() {
		try {
			File jarFile = new File(Visual.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			String jarDir = jarFile.getParentFile().getPath();
			return jarDir;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	private static void loadConfig() {
		try {
			Properties props = new Properties();
			FileInputStream configFile = new FileInputStream(CONFIG_FILE);
			props.load(configFile);
			
			SKIN_DEFAULT = props.getProperty("Skin");
			FIND_DEFAULT = props.getProperty("Field.DefaultFind", "");
			REPLACE_DEFAULT = props.getProperty("Field.DefaultReplace", "");
			//Load locale
			String language = props.getProperty("Locale.Language");
			String country = props.getProperty("Locale.Country");
			if (language.length() > 0
			&& (!language.equals(SYSTEM_LOCALE.getLanguage()) || !country.equals(SYSTEM_LOCALE.getCountry())))
				Locale.setDefault(new Locale(language, country));
			
			configFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void saveConfig() {
		try {
			Properties props = new Properties();
			
			String skin = SubstanceLookAndFeel.getCurrentSkin().getClass().getCanonicalName();
			props.setProperty("Skin", skin);
			props.setProperty("Field.DefaultFind", FIND_DEFAULT);
			props.setProperty("Field.DefaultReplace", REPLACE_DEFAULT);
			//Save locale, only if it was modified
			String language = Locale.getDefault().getLanguage();
			String country = Locale.getDefault().getCountry();
			boolean saveLocale = !language.equals(SYSTEM_LOCALE.getLanguage()) || !country.equals(SYSTEM_LOCALE.getCountry());
			props.setProperty("Locale.Language", (saveLocale) ? language : "");
			props.setProperty("Locale.Country", (saveLocale) ? country : "");
			
			FileOutputStream configFile = new FileOutputStream(CONFIG_FILE);
			props.store(configFile, null);
			configFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static ResourceBundle getLabelBundle() {
		Locale locale = Locale.getDefault();
		return ResourceBundle.getBundle(LABELS_BUNDLE, locale);
	}
	
	public static String getString(String key) {
		return getLabelBundle().getString(key);
	}
	
	public static void locateOnScreenCenter(Component component) {
		Dimension paneSize = component.getSize();
	    Dimension screenSize = component.getToolkit().getScreenSize();
	    component.setLocation((screenSize.width - paneSize.width) / 2, (screenSize.height - paneSize.height) / 2);
	}
	
	public static JDialog newDialog(JComponent panel, String titulo){
		return newDialog(panel, titulo, WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	public static JDialog newDialog(JComponent panel, String titulo, int operation){
		final JDialog dialog = new JDialog(Principal.getInstance(), titulo, true);
		SubstanceLookAndFeel.setDecorationType(panel, DecorationAreaType.GENERAL);
		dialog.getContentPane().add(panel);
		dialog.setDefaultCloseOperation(operation);
		dialog.getRootPane().registerKeyboardAction(new ActionListener() {
			  public void actionPerformed(ActionEvent actionEvent) {				  
				  dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
			  }
			}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		dialog.pack();
		locateOnScreenCenter(dialog);
		dialog.setResizable(false);
		setVisibleDialog(dialog);
		return dialog;
	}
	
	public static void setVisibleDialog(final JDialog dialog){		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				dialog.setVisible(true);
			}
		});
	}
	
	public static String getDateHsMinSegString(long timeInMillis){
		int timeInSeconds = (int) (timeInMillis/1000);
		int hours, minutes, seconds;
		hours = timeInSeconds / 3600;
		timeInSeconds -= hours * 3600;
		minutes = timeInSeconds / 60;
		timeInSeconds -= minutes * 60;
		seconds = timeInSeconds;
		
		String ret="";
		if(hours!=0)
			ret += hours + "h";
		if(minutes!=0)
			ret += minutes + "m";
		ret += seconds + "s";
		return ret;
	}
	
	public static void setFlatProperty(JComponent comp){
		comp.putClientProperty(SubstanceLookAndFeel.FLAT_PROPERTY, Boolean.TRUE);
		if(comp instanceof AbstractButton)
			((AbstractButton)comp).setFocusPainted(false);
	}
	
	public static Component getAreaVacia(int width, int height){
		return Box.createRigidArea(new Dimension(width, height));
	}
	
	public static String getDateHoursString(Date date){
		return new SimpleDateFormat("dd/MM/yyyy  HH:mm").format(date)+" hs.";
	}
	
	public static Date getDateFromSQL(String date){
	    try {
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void openWebpage(String url) {
		try {
			Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
		    	desktop.browse(new URL(url).toURI());
		    else //Try for any X-server system
	            Runtime.getRuntime().exec("xdg-open " + url);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	//************************** DIALOGS ****************************//
	
	public static Container getCurrentFocusRoot(){
		return FocusManager.getCurrentManager().getCurrentFocusCycleRoot(); //FocusManager.getCurrentManager().getPermanentFocusOwner()
	}
	
	public static boolean showConfirmDialog(Object message, String title) {
		return JOptionPane.showConfirmDialog(getCurrentFocusRoot(), message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION;
	}
	
	private static void showMessageDialog(Object message, String title, int messageType) {
		JOptionPane.showMessageDialog(getCurrentFocusRoot(), message, title, messageType);
	}
	
	public static boolean showErrorMessage(String message) {
		return showErrorMessage(message, null);
	}
	
	public static boolean showErrorMessage(String message, Component compFocus) {		
		showMessageDialog(message, getString("Error"), JOptionPane.ERROR_MESSAGE);
		if(compFocus!=null)
			compFocus.requestFocus();
		return false;
	}
}
