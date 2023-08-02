/**
 * PHP CodeSniffer Maven Plugin (v0.0.2)
 * http://umakant.com/phpcodesniffer-maven-plugin
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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

public abstract class PHPCodeSnifferMojo extends AbstractMojo {

    /**
     * Path to store PHP CodeSniffer when we download it.
     */
    @Parameter( property = "check.phpCodeSnifferDownloadPath")
    protected String phpCodeSnifferDownloadPath;

    /**
     * Version of the PHP CodeSniffer to download.
     */
    @Parameter( property = "check.phpCodeSnifferVersion")
    protected String phpCodeSnifferVersion;

    /**
     * Path to PHP CodeSniffer if user has it already.
     */
    @Parameter( property = "check.phpCodeSnifferPath")
    protected String phpCodeSnifferPath;

    /**
     * Path to set if sniffer is downloaded.
     */
    protected static String phpCodeSnifferPathDownloaded;

    /**
     * Path to PHP Binary file to run checks.
     */
    @Parameter( property = "check.phpBinary")
    protected String phpBinary;

    /**
     * Path to code check has to run against.
     */
    @Parameter ( property = "check.pathToCode")
    protected String pathToCode;

    /**
     * Path to rules file.
     */
    @Parameter ( property = "check.standard" )
    protected String standard;

    /**
     * Memory limit to set for PHP while running tests.
     */
    @Parameter ( property = "check.memoryLimit", defaultValue = "250M")
    protected String memoryLimit;

	@Override
	abstract public void execute() throws MojoExecutionException, MojoFailureException;
}
