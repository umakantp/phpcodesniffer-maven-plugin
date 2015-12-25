/**
 * PHP CodeSniffer Maven Plugin (v0.0.2)
 * http://umakantpatil.com/phpcodesniffer-maven-plugin
 * Copyright 2015 Umakant Patil
 *
 * PHP CodeSniffer is copyright of Squiz Pty Ltd.
 *
 * Author licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.umakantpatil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.maven.plugin.logging.Log;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import com.umakantpatil.exception.DownloadFailed;

/**
 * Helper class to download, untar & copy PHP CodeSniffer.
 */
public class FileDownloader {

	/**
	 * Downloads, copies and untars PHP Code Sniffer.
	 *
	 * Example URL of a download package is
	 * http://download.pear.php.net/package/PHP_CodeSniffer-2.4.0.tgz
	 *
	 * @param phpCodeSnifferDownloadPath Path where to download PHP CodeSniffer
	 * @param phpCodeSnifferVersion      Which version to download.
	 * @param logger					 Logger class to log the details for debugging.
	 *
	 * @since 0.0.1
	 * @throws DownloadFailed If version provided is not available on the website.
	 *                        Or any IO Exception is thrown.
	 */
	public String get(String phpCodeSnifferDownloadPath, String phpCodeSnifferVersion, Log logger) throws DownloadFailed {
		InputStream is = null;
		FileOutputStream fos = null;
		String ds = File.separator;
		// System's temp directory.
		String tempDir = System.getProperty("java.io.tmpdir");
		// Where to download sniffer temporary.
		String tempSniff = tempDir + ds +"PHPCodeSniffer" + phpCodeSnifferVersion;
		// Where to untar the downloaded sniffer.
		String tempUntaredSniff = tempDir  + ds + "PHPCodeSniffer-untar-" + phpCodeSnifferVersion;

		String downloadPath = "http://download.pear.php.net/package/PHP_CodeSniffer-"+phpCodeSnifferVersion+".tgz";
		logger.info("Downloading PHP CodeSniffer from "+downloadPath);

		try {
			logger.debug("Dowmloading PHP CodeSniffer at " + tempSniff);
			URL url;
			url = new URL(downloadPath);
			URLConnection urlConn = url.openConnection();
			is = urlConn.getInputStream();
			fos = new FileOutputStream(tempSniff);
			byte[] buffer = new byte[4096];
			int len;

			while ((len = is.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}

			if (is != null) {
				is.close();
	        }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw new DownloadFailed("Provided version of PHP CodeSniffer is wrong or check your internet connection. details: " + e.getMessage());
		} finally {
			try {
		        if (fos != null) {
		            fos.close();
		        }
			} catch (Exception e) {
				// TODO:: Catch right exceptions, not all.
			}
		}

		try {
			Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");
			archiver.extract(new File(tempSniff), new File(tempUntaredSniff));
			logger.debug("Temporary untar directory is " + tempUntaredSniff);
			String tempSniffFolder = tempUntaredSniff + ds + "PHP_CodeSniffer-"+phpCodeSnifferVersion;
			File srcDir = new File(tempSniffFolder);
			File destDir = new File(phpCodeSnifferDownloadPath);
			recursiveCopy(srcDir, destDir);
			logger.debug("Copying PHP CodeSniffer from " + srcDir.getAbsolutePath());
			logger.debug("Copying PHP CodeSniffer to " + destDir.getAbsolutePath());
			File f = new File(phpCodeSnifferDownloadPath + ds + phpCodeSnifferVersion);
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return phpCodeSnifferDownloadPath;
	}

	/**
	 * Recursively copy folder from source to destination.
	 *
	 * Taken from https://erangatennakoon.wordpress.com/2012/05/08/recursive-file-and-folder-copy-in-java/
	 *
	 * @param fSource Source folder to copy.
	 * @param fDest   Destination where has to be copied.
	 *
	 * @since 0.0.1
	 */
	private void recursiveCopy(File fSource, File fDest) {
		try {
			if (fSource.isDirectory()) {
				// A simple validation, if the destination is not exist then create it
				if (!fDest.exists()) {
					fDest.mkdirs();
				}

				// Create list of files and directories on the current source
				// Note: with the recursion 'fSource' changed accordingly
				String[] fList = fSource.list();

				for (int index = 0; index < fList.length; index++) {
					File dest = new File(fDest, fList[index]);
					File source = new File(fSource, fList[index]);

                    // Recursion call take place here
                    recursiveCopy(source, dest);
               }
			} else {
				// Found a file. Copy it into the destination, which is already created in 'if' condition above

				// Open a file for read and write (copy)
				FileInputStream fInStream = new FileInputStream(fSource);
				FileOutputStream fOutStream = new FileOutputStream(fDest);

				// Read 2K at a time from the file
				byte[] buffer = new byte[2048];
				int iBytesReads;

				// In each successful read, write back to the source
				while ((iBytesReads = fInStream.read(buffer)) >= 0) {
					fOutStream.write(buffer, 0, iBytesReads);
				}

				// Safe exit
				if (fInStream != null) {
					fInStream.close();
				}

				if (fOutStream != null) {
					fOutStream.close();
				}
			}
		} catch (Exception ex) {
			// TODO:: Please handle all the relevant exceptions here.
		}
	}
}
