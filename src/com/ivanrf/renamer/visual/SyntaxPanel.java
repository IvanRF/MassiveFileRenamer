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
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class SyntaxPanel extends JPanel {

	private static final long serialVersionUID = 160710L;
	
	public SyntaxPanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setMargin(new Insets(5,5,5,5));
		textArea.setText(
				"Add \\ before this characters (  [  {  \\  ^  $  |  )  ?  *  +  . if you want to replace them."+"\n\n"+
				"Character Classes"+"\n"+		   
				"[abc]	a, b, or c (simple class)"+"\n"+ 	   
				"[^abc] 	Any character except a, b, or c (negation)"+"\n"+ 	   
				"[a-zA-Z] 	a through z, or A through Z, inclusive (range)"+"\n"+ 	   
				"[a-d[m-p]]	a through d, or m through p: [a-dm-p] (union)"+"\n"+ 	   
				"[a-z&&[def]] 	d, e, or f (intersection)"+"\n"+ 	   
				"[a-z&&[^bc]] 	a through z, except for b and c: [ad-z] (subtraction)"+"\n"+ 	   
				"[a-z&&[^m-p]]	a through z, and not m through p: [a-lq-z] (subtraction)"+"\n"+ 	 
				"\n"+  
				"Predefined Character Classes"+"\n"+		   
				". 	Any character (may or may not match line terminators)"+"\n"+ 	   
				"\\d 	A digit: [0-9]"+"\n"+ 	   
				"\\D 	A non-digit: [^0-9]"+"\n"+ 	   
				"\\s 	A whitespace character: ["+"\\ t \\ n \\x0B \\f\\r]"+"\n"+ 	   
				"\\S 	A non-whitespace character: [^\\s]"+"\n"+ 	   
				"\\w 	A word character: [a-zA-Z_0-9]"+"\n"+	   
				"\\W 	A non-word character: [^\\w]"+"\n"+ 	 
				"\n"+
				"Quantifiers			Meaning"+"\n"+	   
				"Greedy	Reluctant	Possessive"+"\n"+	 	   
				"X?	X??	X?+	X, once or not at all"+"\n"+	   
				"X*	X*?	X*+	X, zero or more times"+"\n"+	   
				"X+	X+?	X++	X, one or more times"+"\n"+	   
				"X{n} 	X{n}? 	X{n}+ 	X, exactly n times"+"\n"+	   
				"X{n,}	X{n,}?	X{n,}+	X, at least n times"+"\n"+   
				"X{n,m}	X{n,m}?	X{n,m}+	X, at least n but not more than m times"
		);
		this.add(textArea, BorderLayout.CENTER);
	}
}
