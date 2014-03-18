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

import java.io.File;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ivanrf.renamer.utils.SwingWorkerExt;

public class FileRenamer {

	public static File[] renameFiles(File[] files, File[] newFiles, SwingWorkerExt<?> swingWorker) throws Exception {
		File[] renamedFiles = new File[files.length];
		swingWorker.setMaximum(files.length);
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			File newFile = newFiles[i];
			boolean succeeded = file.renameTo(newFile);
			renamedFiles[i] = (succeeded) ? newFile : file;
			swingWorker.addProgressValue();
		}
		return renamedFiles;
	}
	
	public static File[] getNewFiles(File[] files, String[] newNames, boolean showExtension) throws Exception {
		File[] newFiles = new File[files.length];
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			String newName = newNames[i];
			File newFile = getNewFile(file, newName, showExtension);
			newFiles[i] = newFile;
		}
		return newFiles;
	}

	public static File getNewFile(File file, String newName, boolean showExtension) {
		if (!showExtension) {
			String extension = getFileExtension(file);
			newName = newName + extension;
		}
		newName = file.getParent() + File.separator + newName;
		return new File(newName);
	}

	public static String[][] getNewFileNames(String find, String replace, File[] files, boolean replaceAll, boolean caseSensitive,
			boolean regEx, boolean showExtension, SwingWorkerExt<?> swingWorker) throws Exception {
		Vector<String> filesNames = new Vector<String>();
		for (int i = 0; i < files.length; i++) {
			String name = files[i].getName();
			if (!showExtension)
				name = removeFileExtension(name);
			filesNames.add(name);
		}

		String[][] rows = new String[filesNames.size()][2];
		Pattern pattern = Pattern.compile(find, getFlags(caseSensitive, regEx));
		swingWorker.setMaximum(filesNames.size());
		for (int i = 0; i < filesNames.size(); i++) {
			String name = filesNames.get(i);
			Matcher matcher = pattern.matcher(name);
			rows[i][0] = name;
			rows[i][1] = (!replaceAll) ? matcher.replaceFirst(replace)
					: matcher.replaceAll(replace);
			swingWorker.addProgressValue();
		}
		return rows;
	}

	private static String removeFileExtension(String name) {
		int dot = name.lastIndexOf('.');
		if (dot != -1)
			name = name.substring(0, dot);
		return name;
	}

	private static String getFileExtension(File file) {
		String ext = "";
		String name = file.getName();
		int dot = name.lastIndexOf('.');
		if (dot != -1)
			ext = name.substring(dot); // includes dot
		return ext;
	}

	private static int getFlags(boolean caseSensitive, boolean regEx) {
		if (!caseSensitive && !regEx)
			return Pattern.CASE_INSENSITIVE | Pattern.LITERAL;
		else if (caseSensitive && regEx)
			return 0;
		else if (caseSensitive && !regEx)
			return Pattern.LITERAL;
		else
			// if(!caseSensitive && regEx)
			return Pattern.CASE_INSENSITIVE;
	}

	/**
	 * Checks if there are duplicate files in the array.
	 * @param newFiles new filenames array
	 * @return index of the first duplicated file, <code>-1</code> otherwise
	 */
	public static int hasDuplicateFiles(File[] newFiles) {
		for (int i = 1; i < newFiles.length; i++) {
			File newFile = newFiles[i];
			for (int j = 0; j < i; j++) {
				File newFilePrev = newFiles[j];
				if(newFile.equals(newFilePrev))
					return i;
			}
		}
		return -1;
	}
	
	/**
	 * Checks if a new file already exists in the system, if the filename was changed.
	 * @param files working files
	 * @param newFiles new files
	 * @return index of the first existing file, <code>-1</code> otherwise
	 */
	public static int fileExists(File[] files, File[] newFiles) {
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			File newFile = newFiles[i];
			if (!file.equals(newFile) && newFile.exists())
				return i;
		}
		return -1;
	}
}
