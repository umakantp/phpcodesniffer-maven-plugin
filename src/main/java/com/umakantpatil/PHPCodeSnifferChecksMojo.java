/**
 * PHP CodeSniffer Maven Plugin (v0.0.1)
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Goal to run PHP CodeSniffer checks.
 */
@Mojo( name = "check",
       threadSafe = true,
       defaultPhase = LifecyclePhase.TEST)
public class PHPCodeSnifferChecksMojo extends PHPCodeSnifferMojo {

    /**
     * Execute the PHP CodeSniffer checks.
     * 
     * Below is the sample command to run PHP CodeSniffer.
     * <php-binary> -d memory_limit=<512M> -f <phpcs>/scripts/phpcs -- <-sp> --standard=<src/test/checks/general-ruleset.xml> <src/main>
     * 
     * @see org.apache.maven.plugin.Mojo#execute()
     * @throws MojoExecutionException If configuration is not provided.
     * @throws MojoFailureException If tests are not passed.
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
    	String ds = File.separator;
    	Log logger = getLog();
        if (phpCodeSnifferPath == null && phpCodeSnifferPathDownloaded == null) {
        	throw new MojoExecutionException("Please provide path to installed PHP CodeSniffer or path to download PHP CodeSniffer.");
        }
        if (phpBinary == null) {
        	throw new MojoExecutionException("Please provide binary php path.");
        }
        if (pathToCode == null) {
        	throw new MojoExecutionException("Please provide path to code.");
        }
        if (phpCodeSnifferPathDownloaded != null) {
        	phpCodeSnifferPath = phpCodeSnifferPathDownloaded;
        }
        logger.info("PHP CodeSniffer is stored at "+phpCodeSnifferPath);
        logger.info("Running checks against "+pathToCode);
        if (standard != null) {
        	logger.info("Selected standard it "+standard);
        }
        logger.info("Memory limit is " + memoryLimit);
        String command = "";
        command += phpBinary + " ";
        command += "-d " + memoryLimit + " ";
        command += "-f " + phpCodeSnifferPath + ds + "scripts" + ds + "phpcs -- -sp ";
        if (standard != null) {
        	command += "--standard="+standard+" ";
        }
        command += pathToCode;
        logger.info("Final command to run " + command);
        try {
        	Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        	BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        	String s = null;
        	while ((s = stdInput.readLine()) != null) {
        	    logger.info(s);
        	}
        	int exitValue = proc.waitFor();
        	if (exitValue == 1) {
        		throw new MojoFailureException("Fix the above errors.");
        	}
		} catch (InterruptedException e) {
			throw new MojoFailureException("Error while executing the sniff checks.");
		} catch (IOException e) {
			throw new MojoFailureException("Error while executing the sniff checks.");
		}
    }
}
