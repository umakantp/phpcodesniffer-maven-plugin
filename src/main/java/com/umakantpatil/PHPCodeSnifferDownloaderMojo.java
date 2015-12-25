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
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.codehaus.plexus.util.FileUtils;

/**
 * Goal to download PHP Code Sniffer.
 */
@Mojo( name = "download",
       threadSafe = true,
       defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class PHPCodeSnifferDownloaderMojo extends PHPCodeSnifferMojo  {

    /**
     * Execute i.e. download PHP CodeSniffer checks.
     *
     * Below is the sample URL to download PHP CodeSniffer.
	 * http://download.pear.php.net/package/PHP_CodeSniffer-2.4.0.tgz
     *
     * @since 0.0.1
     * @see org.apache.maven.plugin.Mojo#execute()
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		downloadCodeSnifferIfNecessary();
	}

	/**
     * Download PHP CodeSniffer from its PHP Pear website if only user has asked.
     *
     * After downloading it also sets path in "phpCodeSnifferPath".
     *
     * @since 0.0.1
     * @throws MojoExecutionException
     */
    protected void downloadCodeSnifferIfNecessary() throws MojoExecutionException {
    	if (phpCodeSnifferDownloadPath != null) {
    		if (phpCodeSnifferVersion == null || phpCodeSnifferVersion.equals("")) {
    			throw new MojoExecutionException("Please define which version of PHP CodeSniffer you want to download.");
    		}
    		Log logger = getLog();
    		String path;
    		if (checkIfPhpCodeSnifferExists()) {
    			path = pathToPHPCodeSniffer();
    		} else {
    			String pathToSniff = pathToPHPCodeSniffer();
    			try {
    				// Possibly older version still exists.
					FileUtils.deleteDirectory(new File(pathToSniff));
				} catch (IOException e) {

				}
    			FileDownloader downloader = new FileDownloader();
    			path = downloader.get(pathToSniff, phpCodeSnifferVersion, logger);
    		}
    		phpCodeSnifferPath = path;
    		phpCodeSnifferPathDownloaded = path;
    	}
    }

    /**
     * Checks if PHP CodeSniffer version exists at the path.
     *
     * @since 0.0.2
     * @return Boolean If PHP CodeSniffer Exists.
     */
    protected boolean checkIfPhpCodeSnifferExists() {
    	String phpSniffPath = pathToPHPCodeSniffer();
    	File f = new File(phpSniffPath + File.separator + phpCodeSnifferVersion);
    	Log logger = getLog();
    	logger.debug("Checking if file exists at " + f.getAbsolutePath());
    	if (f.exists()) {
    		logger.info("PHP CodeSniffer "+phpCodeSnifferVersion+ " already exists.");
    		return true;
    	}
    	return false;
    }

    /**
     * Returns path PHP CodeSniffer download.
     *
     * @since 0.0.2
     * @return String Path to PHP CodeSniffer.
     */
    public String pathToPHPCodeSniffer() {
    	return phpCodeSnifferDownloadPath + File.separator +"PHPCodeSniffer";
    }
}
