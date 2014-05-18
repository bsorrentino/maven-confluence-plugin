/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bsc.core;

import java.io.File;

import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.building.DefaultSettingsBuilderFactory;
import org.apache.maven.settings.building.DefaultSettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuilder;
import org.apache.maven.settings.building.SettingsBuildingException;
import org.apache.maven.settings.building.SettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuildingResult;
import org.jboss.forge.addon.maven.plugins.ConfigurationBuilder;
import org.jboss.forge.addon.maven.plugins.ConfigurationElementBuilder;
import org.jboss.forge.addon.maven.projects.MavenFacet;
import org.jboss.forge.addon.projects.Project;

/**
 *
 * @author softphone
 */
public class MavenHelper {
	
	private static final String M2_HOME = System.getenv().get("M2_HOME");
   
	/**
	 * 
	 * @return
	 */
	public static  Settings getSettings()
	   {
	      try
	      {
	    	 final String userHome = System.getProperty("user.home");
	    	 
	         SettingsBuilder settingsBuilder = new DefaultSettingsBuilderFactory().newInstance();
	         SettingsBuildingRequest settingsRequest = new DefaultSettingsBuildingRequest();
	         settingsRequest
	                  .setUserSettingsFile(new File( userHome.concat("/.m2/settings.xml")) );

	         if (M2_HOME != null)
	            settingsRequest.setGlobalSettingsFile(new File(M2_HOME + "/conf/settings.xml"));

	         SettingsBuildingResult settingsBuildingResult = settingsBuilder.build(settingsRequest);
	         Settings effectiveSettings = settingsBuildingResult.getEffectiveSettings();

	         if (effectiveSettings.getLocalRepository() == null)
	         {
	            effectiveSettings.setLocalRepository(userHome.concat("/.m2/repository"));
	         }

	         return effectiveSettings;
	      }
	      catch (SettingsBuildingException e)
	      {
	         throw new Error(e);
	      }
	   }    
    /**
     *
     * @param project
     * @param predicate
     * @return
     */
    public static Plugin findPluginManagement( Model model, F<Plugin,Boolean> predicate ) {
        
        java.util.List<Plugin> plugins = model.getBuild().getPluginManagement().getPlugins();
        
        for( Plugin p : plugins ) {
            
            if( predicate.f(p) ) {
                return p;
            }
        }
        
        return null;
    }
    
    /**
     * 
     * @param conf
     * @param id
     * @return
     */
    public static ConfigurationElementBuilder getOrCreateConfigurationElement( ConfigurationBuilder cb, String id ) {
    	
    	return (ConfigurationElementBuilder) (( cb.hasConfigurationElement(id) ) ? 
    			cb.getConfigurationElement(id) :
    			cb.createConfigurationElement(id));
    }
    
    /**
     * add property if it doesn't exist
     * 
     * @param project
     * @param key
     * @param value 
     */
    public static void addMavenProjectProperty( Project project, String key, String value ) 
    {
        final MavenFacet mcf = project.getFacet(MavenFacet.class);
        
        addMavenProjectProperty(mcf, key, value);
     
    }
    
    /**
     * add property if it doesn't exist
     * 
     * @param project
     * @param key
     * @param value 
     */
    private static void addMavenProjectProperty( final MavenFacet mcf, String key, String value ) 
    {
        final Model pom = mcf.getModel();

        java.util.Properties pp = pom.getProperties();

        if( !pp.containsKey(key)) {
            pp.setProperty(key, value);
            
            mcf.setModel(pom);

        }
        
        
    }
   
    /**
     * add or update property 
     * 
     * @param project
     * @param key
     * @param value 
     */
    public static void setMavenProjectProperty( Project project, String key, String value ) 
    {
        final MavenFacet mcf = project.getFacet(MavenFacet.class);
        
        setMavenProjectProperty(mcf, key, value);
     
    }
    
    /**
     * add or update property 
     * 
     * @param project
     * @param key
     * @param value 
     */
    private static void setMavenProjectProperty( MavenFacet mcf, String key, String value ) 
    {
        final Model pom = mcf.getModel();

        java.util.Properties pp = pom.getProperties();

        pp.setProperty(key, value);
            
        mcf.setModel(pom);
        
    }
    
}
