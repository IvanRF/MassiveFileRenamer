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

import java.io.File;
import java.util.Date;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Version {

	private static final String IMPLEMENTATION_VERSION = "Implementation-Version";
	private static final String IMPLEMENTATION_BUILDSTAMP = "Implementation-BuildStamp";
	
	private static final String JAR_NAME = Visual.getLocalDir() + File.separator + "MassiveFileRenamer.jar";
	
	public static String getVersion(){
		//Saco la version del Manifest que esta adentro del JAR
		String version = getJarManifestAttribute(JAR_NAME, IMPLEMENTATION_VERSION);
		return version;
	}
	
	public static Date getVersionDate(){
		//Saco la fecha del Manifest que esta adentro del JAR
		String buildStamp = getJarManifestAttribute(JAR_NAME, IMPLEMENTATION_BUILDSTAMP);
		if(buildStamp!=null)
			return Visual.getDateFromSQL(buildStamp);
		else
			return new Date(new File(JAR_NAME).lastModified()); //Saco la fecha de la última fecha de modificación del JAR
	}
	
	public static String getJarManifestAttribute(String jar, String attribute) {
		try {
			JarFile jarfile = new JarFile(jar);
			Manifest manifest = jarfile.getManifest();
			Attributes att = manifest.getMainAttributes();
			return att.getValue(attribute);
		} catch (Exception e) {
			return null;
		}
	}
}
