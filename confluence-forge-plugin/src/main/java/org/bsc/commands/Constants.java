package org.bsc.commands;

/**
 * 
 * @author softphone
 *
 */
public interface Constants {
	
	String PROP_CONFLUENCE_HOME = "confluence.home";
	String CFGELEM_SERVERID = "serverId";
	String CFGELEM_ENDPOINT = "endPoint";
	String CFGELEM_SPACEKEY = "spaceKey";
	String CFGELEM_PARENTPAGETITLE = "parentPageTitle";
	String MSG_SETUP_INTERRUPTED = "setup interrupted!";
	String MESG_FOLDER_CREATED = "folder created!";

	String PLUGIN_GROUPID = "org.bsc.maven";
	String PLUGIN_ARTIFACTID = "confluence-reporting-maven-plugin";
	String PLUGIN_VERSION = "4.1.0";

	String PLUGIN_KEY_2 = PLUGIN_GROUPID + ":"
			+ PLUGIN_ARTIFACTID;
	String PLUGIN_KEY_3 = PLUGIN_GROUPID + ":"
			+ PLUGIN_ARTIFACTID + ":" + PLUGIN_VERSION;

}
