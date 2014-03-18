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

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import sun.awt.image.ToolkitImage;

public class Images {

	private static String IMG_DIR = "com/ivanrf/renamer/images/";
	
	public static ImageIcon OPEN = getImageIcon(IMG_DIR + "open.png");
	public static ImageIcon SAVE = getImageIcon(IMG_DIR + "save.png");
	public static ImageIcon INFO = getImageIcon(IMG_DIR + "info.png");
	
	public static String LOGO = IMG_DIR + "Logo.png";
	private static Image LOGO_16x16 = getImage(IMG_DIR + "Logo16.png");
	private static Image LOGO_32x32 = getImage(IMG_DIR + "Logo32.png");
	private static Image LOGO_64x64 = getImage(IMG_DIR + "Logo64.png");
	
	private static Images instance; 
	
	private Images() {}
	
	public static Images getInstance() {
		if(instance==null)
			instance = new Images();
		return instance;
	}
	
	public static Image getImage(String image){
		return getImageIcon(image).getImage();
	}
	
	public static ImageIcon getImageIcon(String image){
		return new ImageIcon(getInstance().getClass().getClassLoader().getResource(image));
	}
	
	public static List<Image> getAppIconImages() {
		//Genero una lista de iconos para la aplicacion
		List<Image> images = new ArrayList<Image>();
		images.add(LOGO_16x16);
		images.add(LOGO_32x32);
		images.add(LOGO_64x64);
		return images;
	}
	
	public static ImageIcon getFlagIcon(String locale){
		return getImageIcon(IMG_DIR + "flags/" + locale + ".png");
	}
	
	public static ImageIcon getScaledImage(String image, int size){
		return getScaledImage(getImageIcon(image), size, size);
	}
	
	public static ImageIcon getScaledImage(ImageIcon imageIcon, int newWidth, int newHeight){
		if(imageIcon.getIconWidth()!=newWidth || imageIcon.getIconHeight()!=newHeight){
			int width = -1, height = -1;
			if(imageIcon.getIconWidth() > imageIcon.getIconHeight()) //Ajusto el ancho
				width = newWidth;
			else //Ajusto el alto
				height = newHeight;
			Image image = imageIcon.getImage();
			ToolkitImage scaledImage = (ToolkitImage) image.getScaledInstance(width, height, Image.SCALE_SMOOTH);		
			if(scaledImage.getWidth()>newWidth)
				scaledImage = (ToolkitImage) image.getScaledInstance(newWidth, -1, Image.SCALE_SMOOTH);
			else if(scaledImage.getHeight()>newHeight)
				scaledImage = (ToolkitImage) image.getScaledInstance(-1, newHeight, Image.SCALE_SMOOTH);
			imageIcon = new ImageIcon(scaledImage);
		}
		return imageIcon;
	}
}