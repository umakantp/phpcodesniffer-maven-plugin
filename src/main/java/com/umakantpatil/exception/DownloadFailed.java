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
package com.umakantpatil.exception;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Exception to throw when download has failed.
 */
public class DownloadFailed extends MojoExecutionException {

	public DownloadFailed(String message) {
		super(message);
	}
}
