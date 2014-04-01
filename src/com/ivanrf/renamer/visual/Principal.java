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
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import com.ivanrf.renamer.utils.FileExtensionFilter;
import com.ivanrf.renamer.utils.Images;
import com.ivanrf.renamer.utils.LocaleButton;
import com.ivanrf.renamer.utils.LocaleChangeListener;
import com.ivanrf.renamer.utils.SubstanceBorder;
import com.ivanrf.renamer.utils.SwingWorkerExt;
import com.ivanrf.renamer.utils.Visual;
import com.ivanrf.renamer.utils.WrapLayout;

public class Principal extends JFrame implements ActionListener, LocaleChangeListener {

	private static final long serialVersionUID = 310314L;

	private JLabel findLabel;
	private JTextField findField;
	private JLabel replaceWithLabel;
	private JTextField replaceWithField;
	private JButton openButton;
	private JButton replaceButton;
	private JCheckBox caseSensitiveCheckBox;
	private JCheckBox regexCheckBox;
	private JButton syntaxButton;
	private JCheckBox replaceAllCheckBox;
	private JCheckBox showFileExtensionCheckBox;
	private JTable table;
	private LocaleButton localeButton;
	private JButton aboutButton;
	
	private File[] openFiles;
	private JFileChooser chooser;
	private boolean contentsNotSaved = false;
	
	private JLabel estadoLabel;
	private JProgressBar progressBar;
	private JLabel progressTimeElapsedLabel;
	private JLabel progressTimeRemainingLabel;
	private long startProgress;
	private long startPercentProgress;
	private Timer progressTimer;
	
	private static final int NAME_INDEX = 0;    
	private static final int NEW_NAME_INDEX = 1;
	
	private static Principal instance;
	
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Visual.setSkin();
				Principal.getInstance();
			}
		});
	}
	
	public static Principal getInstance(){
		if(instance==null)
			instance = new Principal();
		return instance;
	}
	
	private Principal() {
		super();
		initGUI();
		setMinimumSize(new Dimension(600, 300));
		setSize(800, 600);
		Visual.locateOnScreenCenter(this);
		setTitle("Massive File Renamer");
		setIconImages(Images.getAppIconImages());
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				thisWindowClosing();
			}
		});
		
		setVisible(true);
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			setLayout(new GridBagLayout());
			
			int gridx=0;
			int gridy=0;
			GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridy = gridy++;
            constraints.insets = new Insets(5,5,5,5);
            constraints.anchor = GridBagConstraints.EAST;
			{
				findLabel = new JLabel();
				constraints.gridx = gridx++;
				this.add(findLabel, constraints);

				findField = new JTextField(Visual.FIND_DEFAULT);
				findField.putClientProperty(LafWidget.TEXT_EDIT_CONTEXT_MENU, Boolean.TRUE);
				findField.getDocument().addDocumentListener(new TextDocumentListener());
				constraints.gridx = gridx++;
				constraints.weightx = 1.0;
	            constraints.fill = GridBagConstraints.HORIZONTAL;
				this.add(findField, constraints);
				constraints.weightx = 0;
				
				openButton = new JButton(Images.OPEN);
				constraints.gridx = gridx++;
				this.add(openButton, constraints);
				openButton.addActionListener(new ActionListener() {				
		            public void actionPerformed(ActionEvent ae) {
		            	openButtonActionPerformed();
					}
				});
			}
			gridx=0;
			constraints.gridy = gridy++;
			{
				replaceWithLabel = new JLabel();
				constraints.gridx = gridx++;
				this.add(replaceWithLabel, constraints);

				replaceWithField = new JTextField(Visual.REPLACE_DEFAULT);
				replaceWithField.putClientProperty(LafWidget.TEXT_EDIT_CONTEXT_MENU, Boolean.TRUE);
				replaceWithField.getDocument().addDocumentListener(new TextDocumentListener());
				constraints.gridx = gridx++;
				constraints.weightx = 1.0;
	            constraints.fill = GridBagConstraints.HORIZONTAL;
				this.add(replaceWithField, constraints);
				constraints.weightx = 0;

				replaceButton = new JButton(Images.SAVE);
				constraints.gridx = gridx++;
				this.add(replaceButton, constraints);
				replaceButton.addActionListener(new ActionListener() {				
		            public void actionPerformed(ActionEvent ae) {
		            	replaceButtonActionPerformed();
					}
				});
			}
			gridx=0;
			constraints.gridy = gridy++;
			constraints.anchor = GridBagConstraints.WEST;
			constraints.insets = new Insets(0,0,0,0);
			{
				JPanel panel = new JPanel(new WrapLayout(FlowLayout.LEFT,5,0));
				constraints.gridx = gridx++;
				constraints.gridwidth = 3;
				this.add(panel, constraints);
				
				ActionListener updateAction = new ActionListener() {
		            public void actionPerformed(ActionEvent ae) {
		            	updateTable();
		            }
		        };

				replaceAllCheckBox = new JCheckBox();
				panel.add(replaceAllCheckBox);
				replaceAllCheckBox.addActionListener(updateAction);
				
				caseSensitiveCheckBox = new JCheckBox();
				panel.add(caseSensitiveCheckBox);
				caseSensitiveCheckBox.addActionListener(updateAction);
				
				JPanel regexPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
				panel.add(regexPanel);
				
				regexCheckBox = new JCheckBox();
				regexPanel.add(regexCheckBox);
				
				regexPanel.add(Visual.getAreaVacia(5, 0));
				
				syntaxButton = new JButton();
				regexPanel.add(syntaxButton);
				syntaxButton.addActionListener(new ActionListener() {				
		            public void actionPerformed(ActionEvent ae) {
		            	syntaxButtonActionPerformed();
					}
				});
				syntaxButton.setEnabled(false);
				
				regexCheckBox.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent ae) {
		            	syntaxButton.setEnabled(regexCheckBox.isSelected());
		            	updateTable();
		            }
		        });
				
				showFileExtensionCheckBox = new JCheckBox();
				panel.add(showFileExtensionCheckBox);
				showFileExtensionCheckBox.addActionListener(updateAction);
			}
			gridx=0;
			constraints.gridy = gridy++;
			constraints.insets = new Insets(5,5,5,5);
			{
				table = new JTable();
				setTableModel(new String[22][2]);
				constraints.gridx = gridx++;
				constraints.gridwidth = 3;
				constraints.weightx = 1.0;
	            constraints.weighty = 1.0;
	            constraints.fill = GridBagConstraints.BOTH;
				this.add(new JScrollPane(table), constraints);
				table.getTableHeader().setReorderingAllowed(false);
			}
			gridx=0;
			constraints.gridy = gridy++;
			constraints.insets = new Insets(0,0,0,0);
			constraints.weighty = 0;
			{
				JPanel barraDeEstadoPanel = new JPanel(new BorderLayout());
				barraDeEstadoPanel.setBorder(new SubstanceBorder(new Insets(0,0,0,0)));
				SubstanceLookAndFeel.setDecorationType(barraDeEstadoPanel, DecorationAreaType.FOOTER);
				constraints.gridx = gridx++;
				constraints.fill = GridBagConstraints.HORIZONTAL;
				this.add(barraDeEstadoPanel, constraints);
				{
					barraDeEstadoPanel.add(Visual.getAreaVacia(5,0), BorderLayout.WEST);
					
					estadoLabel = new JLabel();	
					barraDeEstadoPanel.add(estadoLabel, BorderLayout.CENTER);
					estadoLabel.setVerticalAlignment(SwingConstants.CENTER);			
				}
				{
					JPanel barraDeEstadoDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT,5,0));
					barraDeEstadoDerecha.setOpaque(false);
					barraDeEstadoPanel.add(barraDeEstadoDerecha, BorderLayout.EAST);

					{
						JPanel progressPanel = new JPanel(new GridBagLayout());
						progressPanel.setOpaque(false);
						barraDeEstadoDerecha.add(progressPanel);
						
						constraints = new GridBagConstraints();
						
						progressBar = new JProgressBar(0, 100);
						progressBar.setStringPainted(true);
						progressBar.setString("");
						constraints.gridwidth = 2;
						constraints.insets = new Insets(2,5,0,5);
						progressPanel.add(progressBar, constraints);
						
						constraints = new GridBagConstraints();
						constraints.gridy = 1;
						
						progressTimeElapsedLabel = new JLabel(" ");
						constraints.insets = new Insets(0,5,0,0);
						constraints.anchor = GridBagConstraints.WEST;
						progressPanel.add(progressTimeElapsedLabel, constraints);
						
						progressTimeRemainingLabel = new JLabel(" ");
						constraints.gridx = 1;
						constraints.insets = new Insets(0,0,0,5);
						constraints.anchor = GridBagConstraints.EAST;
						progressPanel.add(progressTimeRemainingLabel, constraints);
						progressTimeRemainingLabel.setHorizontalAlignment(SwingConstants.RIGHT);
						
						progressTimer = new Timer(1000, this);
						progressTimer.setInitialDelay(0);
					}
					{
						JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
						sep.setPreferredSize(new Dimension(5, 30));
						barraDeEstadoDerecha.add(sep);
						
						localeButton = new LocaleButton(getAppLocales(), Locale.ENGLISH);
						barraDeEstadoDerecha.add(localeButton);
						Visual.setFlatProperty(localeButton);
						localeButton.setLocaleChangeListener(this);
					}
					{
						JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
						sep.setPreferredSize(new Dimension(5, 30));
						barraDeEstadoDerecha.add(sep);
						
						aboutButton = new JButton(Images.INFO);
						barraDeEstadoDerecha.add(aboutButton);
						Visual.setFlatProperty(aboutButton);
						aboutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						aboutButton.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent e) {
								aboutButtonActionPerformed();
							}
						});
					}
				}
			}
			setLabelsText();
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Vector<Locale> getAppLocales() {
		Vector<Locale> locales = new Vector<Locale>();
		locales.add(Locale.ENGLISH);			//English
		locales.add(new Locale("es"));			//Spanish
		locales.add(new Locale("es", "AR"));	//Spanish (Argentina)
		return locales;
	}
	
	@Override
	public void localeChanged(final Locale newLocale) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Locale.setDefault(newLocale);
				setLabelsText();
				applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
				
				//Updating the LookAndFeel causes a saveConfig(). Thus, language is updated in the config file
				LookAndFeel currLaf = UIManager.getLookAndFeel();
				if (currLaf instanceof SubstanceLookAndFeel) {
					SubstanceLookAndFeel.resetLabelBundle();
					SubstanceLookAndFeel.setFontPolicy(null);
				}
				try {
					UIManager.setLookAndFeel(currLaf.getClass().getName());
				} catch (Exception exc) {
				}
				SwingUtilities.updateComponentTreeUI(Principal.this);
			}
		});
	}
	
	private void setLabelsText() {
		findLabel.setText(Visual.getString("Find"));
		replaceWithLabel.setText(Visual.getString("ReplaceWith"));
		openButton.setText(Visual.getString("Open"));
		replaceButton.setText(Visual.getString("Replace"));
		replaceAllCheckBox.setText(Visual.getString("ReplaceAll"));
		caseSensitiveCheckBox.setText(Visual.getString("MatchCase"));
		regexCheckBox.setText(Visual.getString("RegularExpressions"));
		syntaxButton.setText(Visual.getString("RegExSyntax"));
		showFileExtensionCheckBox.setText(Visual.getString("ShowExtension"));
		
		progressTimeElapsedLabel.setToolTipText(Visual.getString("ElapsedTime"));
		progressTimeRemainingLabel.setToolTipText(Visual.getString("TimeLeft"));
		localeButton.setToolTipText(Visual.getString("Language"));
		aboutButton.setToolTipText(Visual.getString("About"));
		
		table.getColumnModel().getColumn(NAME_INDEX).setHeaderValue(Visual.getString("Name"));
		table.getColumnModel().getColumn(NEW_NAME_INDEX).setHeaderValue(Visual.getString("NewName"));
		
		chooser = null;
	}
	
	private void setTableModel(String[][] rows) {
		table.setModel(new FilenamesTableModel(rows, new String[]{ Visual.getString("Name"), Visual.getString("NewName") }));
		table.getColumnModel().getColumn(NAME_INDEX).setCellRenderer(new FilenamesTableRenderer());
		table.getColumnModel().getColumn(NEW_NAME_INDEX).setCellRenderer(new FilenamesTableRenderer());
		table.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				//A cell was edited
				checkContentNotSaved();
			}
		});
	}
	
	private void updateTable() {
		if (openFiles != null) {
			setEstado(Visual.getString("UpdatingTable"));
			startIndeterminateProgressBar();
			
			new SwingWorkerExt<String[][]>(){
				@Override
				protected String[][] doInBackground() throws Exception {
					String find = findField.getText();
					String replace = replaceWithField.getText();
					boolean replaceAll = replaceAllCheckBox.isSelected();
					boolean caseSensitive = caseSensitiveCheckBox.isSelected();
					boolean regEx = regexCheckBox.isSelected();
					boolean showExtension = showFileExtensionCheckBox.isSelected();
					return FileRenamer.getNewFileNames(find, replace, openFiles, replaceAll, caseSensitive, regEx, showExtension, this);
				}
				@Override
				protected void done() {
					try{
						String[][] rows = get();
						setTableModel(rows);
						checkContentNotSaved();
					} catch(Exception e){
					}
					setEstado("");
					stopProgressBar();
				}
			}.execute();
		}
	}
	
	private void checkContentNotSaved() {
		boolean rowModified = false;
		for (int i = 0; i < table.getRowCount() && !rowModified; i++) {
			if (!table.getValueAt(i, NAME_INDEX).equals(table.getValueAt(i, NEW_NAME_INDEX)))
				rowModified = true;
		}
		if (this.contentsNotSaved != rowModified) {
			this.contentsNotSaved = rowModified;
			getRootPane().putClientProperty(SubstanceLookAndFeel.WINDOW_MODIFIED, rowModified);
		}
	}
	
	private void replaceButtonActionPerformed() {
		if (openFiles != null) {
			setEstado(Visual.getString("CheckingNames"));
			startIndeterminateProgressBar();
			
			new SwingWorkerExt<Void>(){
				@Override
				protected Void doInBackground() throws Exception {
					//Get new names from table
					String[] newNames = new String[openFiles.length];
					for (int i = 0; i < openFiles.length; i++)
						newNames[i] = (String) table.getModel().getValueAt(i, NEW_NAME_INDEX); 
					//Generate new File objects
					boolean showExtension = showFileExtensionCheckBox.isSelected();
					File[] newFiles = FileRenamer.getNewFiles(openFiles, newNames, showExtension);
					//Check files
					int indexDuplicated = FileRenamer.hasDuplicateFiles(newFiles);
					if (indexDuplicated != -1) {
						showMessage(Visual.getString("DuplicateFilesMsg"), indexDuplicated);
						throw new Exception("Duplicated files");
					}
					int indexExists = FileRenamer.fileExists(openFiles, newFiles);
					if (indexExists != -1) {
						showMessage(Visual.getString("FileExistsMsg"), indexExists);
						throw new Exception("File already exists");
					}
					//Rename files
					publishEstado(Visual.getString("ReplacingNames"));
					openFiles = FileRenamer.renameFiles(openFiles, newFiles, this);
					return null;
				}
				@Override
				protected void done() {
					try {
						get();
						updateTable();
					} catch(Exception e) {
						setEstado("");
						stopProgressBar();
					}
				}
			}.execute();
		}
	}
	
	private void showMessage(final String message, final int row) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				selectTableCell(row, NEW_NAME_INDEX);
				Visual.showErrorMessage(message);
			}
		});
	}
	
	private void selectTableCell(final int row, final int column) {
    	super.requestFocus();
    	table.requestFocus();
    	SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(row < table.getModel().getRowCount() && column < table.getModel().getColumnCount()) {
					table.setRowSelectionInterval(row,row);
			    	table.setColumnSelectionInterval(column, column);
			    	showRow(row);
				}
			}
		});
    }
	
	private void showRow(int row) {
		table.scrollRectToVisible(table.getCellRect(row, 0, true));
//		table.getParent().getParent().getParent().repaint();
	}

	private void openButtonActionPerformed() {
		if (chooser == null) {
			chooser = new JFileChooser();
			chooser.setMultiSelectionEnabled(true);	
			FileExtensionFilter mp3Filter = new FileExtensionFilter("mp3", "MPEG Audio Files");
			chooser.addChoosableFileFilter(mp3Filter);
			chooser.setFileFilter(chooser.getChoosableFileFilters()[0]);
		}
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			openFiles = chooser.getSelectedFiles();
			updateTable();
		}
	}
	
	private void syntaxButtonActionPerformed() {
		Visual.newDialog(new SyntaxPanel(), Visual.getString("RegExSyntax"));
	}
	
	private void aboutButtonActionPerformed() {
		Visual.newDialog(new AboutPanel(), Visual.getString("About"));
	}
	
	public void setEstado(String text) {
		estadoLabel.setText("<html><b>"+text+"</b></html>");
	}
	
	public void stopProgressBar() {
		progressBar.setIndeterminate(false);
		progressBar.setString("");
		progressBar.setValue(0);
		progressTimer.stop();
		progressTimeElapsedLabel.setText(" ");
		progressTimeRemainingLabel.setText(" ");
	}
	
	public void startIndeterminateProgressBar() {
		progressBar.setIndeterminate(true);
		progressBar.setString("");
		
		startProgressTimer();
	}
	
	public void startPercentProgressBar() {
		progressBar.setIndeterminate(false);
		progressBar.setString(null);
		
		startPercentProgress = System.currentTimeMillis();
		startProgressTimer();
	}
	
	private void startProgressTimer() {
		if (!progressTimer.isRunning()) {
			startProgress = System.currentTimeMillis();
			progressTimer.start();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		progressTimeElapsedLabel.setText(Visual.getString("ElapsedTimeAbbr")+": "+Visual.getDateHsMinSegString(System.currentTimeMillis()-startProgress));
	}
	
	public void setProgressBarValue(int currentValue) {
		progressBar.setValue(currentValue);
		
		if(currentValue>0){
			long elapsedTime = System.currentTimeMillis() - startPercentProgress;
			long timePerIteration = elapsedTime / currentValue;
			long estimatedTimeRemaining = timePerIteration * (progressBar.getMaximum() - currentValue);
			progressTimeRemainingLabel.setText(Visual.getString("TimeLeftAbbr")+": "+Visual.getDateHsMinSegString(estimatedTimeRemaining));
		}
	}
	
	private void thisWindowClosing() {
		setState(NORMAL); //If minimized, maximizes to show a centered dialog
		
		if (!contentsNotSaved || 
			 contentsNotSaved && Visual.showConfirmDialog(Visual.getString("UnsavedChangesMsg"), Visual.getString("Exit")))
			System.exit(0);
	}
	
	class FilenamesTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 210810L;
		
		public FilenamesTableModel(Object[][] data, Object[] columnNames) {
			super(data, columnNames);
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			return column==NEW_NAME_INDEX && getValueAt(row, column)!=null;
		}
	}
	
	class FilenamesTableRenderer extends SubstanceDefaultTableCellRenderer {
		private static final long serialVersionUID = 140314L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {			
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
			
			if(openFiles != null && row < openFiles.length) {
				String tooltip = null;
				if (col == NAME_INDEX)
					tooltip = openFiles[row].getAbsolutePath();
				else if (col == NEW_NAME_INDEX) {
					boolean showExtension = showFileExtensionCheckBox.isSelected();
					File file = openFiles[row];
					File newFile = FileRenamer.getNewFile(file, (String) value, showExtension);
					tooltip = newFile.getAbsolutePath();
					
					//Checks if newFile already exists in the table
					boolean duplicated = false;
					for (int i = 0; i < row && !duplicated; i++) {
						File newFilePrev = FileRenamer.getNewFile(openFiles[i], (String) table.getValueAt(i, col), showExtension);
						if (newFile.equals(newFilePrev))
							duplicated = true;
					}
					if (duplicated) {
						this.setForeground(Color.RED);
						tooltip = "<html><p>" + tooltip + "</p>"
								+ "<b><font color=#ff0000>" +  Visual.getString("DuplicateFilesMsg") + "</font></b></html>";
					} else {
						//Highlight rows with changes
						if (!newFile.equals(file))
							this.setForeground(new Color(0, 0, 153)); //Dark blue
					}
				}
				this.setToolTipText(tooltip);
			}
			return this;
		}
	}
	
	class TextDocumentListener implements DocumentListener {
		@Override
		public void changedUpdate(DocumentEvent e) {
			updateTable();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			updateTable();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			updateTable();
		}
	}
}
