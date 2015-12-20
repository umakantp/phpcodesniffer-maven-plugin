package com.umakantpatil;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

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
     * @throws MojoExecutionException
     */
    protected void downloadCodeSnifferIfNecessary() throws MojoExecutionException {
    	if (phpCodeSnifferDownloadPath != null) {
    		if (phpCodeSnifferVersion == null || phpCodeSnifferVersion.equals("")) {
    			throw new MojoExecutionException("Please define which version of PHP CodeSniffer you want to download.");
    		}
    		Log logger = getLog();
    		FileDownloader downloader = new FileDownloader();
    		String path;
    		path = downloader.get(phpCodeSnifferDownloadPath, phpCodeSnifferVersion, logger);
    		phpCodeSnifferPath = path;
    		phpCodeSnifferPathDownloaded = path;
    	}
    } 
}
