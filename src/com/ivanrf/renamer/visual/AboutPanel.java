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

package com.ivanrf.renamer.visual;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.pushingpixels.substance.internal.animation.IconGlowTracker;
import org.pushingpixels.substance.internal.utils.icon.GlowingIcon;

import com.ivanrf.renamer.utils.Images;
import com.ivanrf.renamer.utils.Version;
import com.ivanrf.renamer.utils.Visual;

public class AboutPanel extends JPanel {

	private static final long serialVersionUID = 140314L;
	
	public AboutPanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setLayout(new BorderLayout());
			setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
			
			{
				JLabel logoLabel = new JLabel();
				setGlowingIcon(logoLabel, Images.getScaledImage(Images.LOGO, 300));
				this.add(logoLabel, BorderLayout.CENTER);
			}
			{
				JPanel textPanel = new JPanel(new BorderLayout(5, 10));
				textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
				this.add(textPanel, BorderLayout.EAST);
				
				JLabel label = new JLabel("Massive File Renamer");
				label.setFont(label.getFont().deriveFont(Font.BOLD, 20));
				textPanel.add(label, BorderLayout.NORTH);
				
				String text = "<html>" + 
					"<p>" + Visual.getString("Version") + ": <b>" + Version.getVersion() + "</b></p>" +			
					"<p>" + Visual.getString("LastUpdate") + ": <b>" + Visual.getDateHoursString(Version.getVersionDate()) + "</b></p><br/>" +
					"</html>";
				
				label = new JLabel(text);
				Font textFont = label.getFont().deriveFont((float) 14);
				label.setFont(textFont);
				label.setVerticalAlignment(SwingConstants.TOP);
				textPanel.add(label, BorderLayout.CENTER);
				
				JPanel rightsPanel = new JPanel();
				rightsPanel.setLayout(new BoxLayout(rightsPanel, BoxLayout.Y_AXIS));
				textPanel.add(rightsPanel, BorderLayout.SOUTH);
				
				text = "<html><p>" + Visual.getString("DevelopedBy") + " <b>Iván Ridao Freitas</b>.</p></html>";
				
				label = new JLabel(text);
				label.setFont(textFont);
				rightsPanel.add(label);
				
				text = "<html><p><a href='#'>http://www.ivanrf.com</a></p></html>";
				
				label = new JLabel(text);
				label.setFont(textFont);
				rightsPanel.add(label);
				label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				label.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						Visual.openWebpage("http://www.ivanrf.com");
					}
				});
				
				text = "<html><br/><p>Copyright © 2014 Iván Ridao Freitas.</p></html>";

				label = new JLabel(text);
				label.setFont(textFont);
				rightsPanel.add(label);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setGlowingIcon(JLabel label, Icon icon){
		IconGlowTracker igt = new IconGlowTracker(label);
		label.setIcon(new GlowingIcon(icon, igt));
		igt.play();
		label.setHorizontalAlignment(SwingConstants.CENTER);
	}
}
