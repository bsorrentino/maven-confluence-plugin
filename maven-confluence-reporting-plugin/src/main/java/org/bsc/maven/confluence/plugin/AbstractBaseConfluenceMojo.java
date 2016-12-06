package org.bsc.maven.confluence.plugin;

import org.bsc.ssl.SSLCertificateInfo;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Server;
import org.bsc.functional.P1;
import org.bsc.confluence.ConfluenceProxy;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceServiceFactory;
import org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcherException;
import static java.lang.String.format;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bsc.confluence.ConfluenceService.Model;
import rx.functions.Action1;


/**
 *
 * @author bsorrentino
 */
public abstract class AbstractBaseConfluenceMojo extends AbstractMojo {

    /**
     * additional properties pass to template processor
     */
    @Parameter()
    private java.util.Map properties;    
    /**
     * Confluence end point url
     */
    @Parameter(property = "confluence.endPoint", defaultValue = "http://localhost:8080/rpc/xmlrpc")
    private String endPoint;
    /**
     * Confluence target confluence spaceKey
     */
    @Parameter(property = "confluence.spaceKey", required = false)
    private String spaceKey;
    
    /**
     * Confluence parent page title
     */
    @Parameter(property = "confluence.parentPage", defaultValue = "Home")
    private String parentPageTitle;
    /**
     * Confluence parent page id. 
     * If set it is possible to avoid specifying parameters spaceKey and parentPageTitle
     * 
     * @since 4.10
     */
    @Parameter(property = "confluence.parentPageId")
    private String parentPageId;
    
    /**
     * Confluence username
     */
    @Parameter(property = "confluence.userName", required = false)
    private String username;
    /**
     * Confluence password
     */
    @Parameter(property = "confluence.password", required = false)
    private String password;

    /**
     * @parameter expression="${settings}"
     * @readonly
     * @since 3.1.1
     */
    @Parameter(readonly = true, property = "settings")
    protected org.apache.maven.settings.Settings mavenSettings;

    /**
     * Issue 39
     *
     * Server's <code>id</code> in <code>settings.xml</code> to look up username and password.
     * Defaults to <code>${url}</code> if not given.
     *
     * @since 3.1.1
     */
    @Parameter(property = "confluence.serverId")
    private String serverId;

    /**
     * Issue 39
     *
     * MNG-4384
     *
     * @since 1.5
     */
    @Component(role = org.sonatype.plexus.components.sec.dispatcher.SecDispatcher.class, hint = "default")
    private SecDispatcher securityDispatcher;

    /**
     * if using a https url, configure if the plugin accepts every certifactes or
     * respects hostnameVerifierClass and trustManagerClass (if set).
     *
     * Below the Template
     *
     * <pre>
     *
     * < sslCertificate>
     *  < ignore>true|false</ignore>  // default false
     *  < hostNameVerifierClass>FQN</hostNameVerifierClass> //default null
     *  < trustManagerClass>FQN</trustManagerClass> // default null
     * < /sslCertificate>
     *
     * </pre>
     * @since 4.1.0
     */
    @Parameter
    protected SSLCertificateInfo sslCertificate = new SSLCertificateInfo();

    /**
     *
     * Indicates whether the build will continue even if there are clean errors.     
     *
     * @since 5.0-rc1
     */
    @Parameter(property = "confluence.failOnError",defaultValue = "true")
    private boolean failOnError = true;

    /**
     * 
     * Indicates whether the build will continue even if there are clean errors.     
     * 
     * @return true if build have to fail on error, otherwise false
     */
    public boolean isFailOnError() {
        return failOnError;
    }
  
    /**
     *
     */
    public AbstractBaseConfluenceMojo() {
    }

    public final String getEndPoint() {
        return endPoint;
    }

    public final String _getSpaceKey() {
        return spaceKey;
    }

    public final String getUsername() {
        return username;
    }

    public final String getPassword() {
        return password;
    }

    @SuppressWarnings("unchecked")
    public final java.util.Map<String, String> getProperties() {
        if (null == properties) {
            properties = new java.util.HashMap<String, String>(5);
        }
        return properties;
    }
    
    /**
     *
     * @param task
     * @throws MojoExecutionException
     */
    protected <T extends Action1<ConfluenceService>> void confluenceExecute(T task) throws MojoExecutionException {

        ConfluenceService confluence = null;
        
        try {

            ConfluenceProxy proxyInfo = null;

            final Proxy activeProxy = mavenSettings.getActiveProxy();

            if (activeProxy != null) {

                proxyInfo =
                        new ConfluenceProxy(
                                activeProxy.getHost(),
                                activeProxy.getPort(),
                                activeProxy.getUsername(),
                                activeProxy.getPassword(),
                                activeProxy.getNonProxyHosts()
                        );
            }

            final ConfluenceService.Credentials credentials = 
                new ConfluenceService.Credentials(getUsername(), getPassword());

            confluence = 
                    ConfluenceServiceFactory.createInstance(
                            getEndPoint(), 
                            credentials, 
                            proxyInfo, 
                            sslCertificate
                    );

            getLog().info( String.valueOf(confluence) );

            confluence.call(task);
            
        } catch( RuntimeException re ) {
            
            throw re;
            
        } catch (Exception e) {

            final String msg = "has been impossible connect to confluence due exception";
            //getLog().error(msg, e);

            throw new MojoExecutionException(msg, e);
        }

    }

    /**
     * 
     * @param confluence
     * @return
     * @throws MojoExecutionException 
     */
    protected Model.Page loadParentPage( ConfluenceService confluence) throws MojoExecutionException {
        
        Model.Page result = null;
        if( parentPageId != null ) {
            
            try {
                result = confluence.getPage( parentPageId );
                
                if( result==null ) {
                    getLog().warn( format( "parentPageId [%s] not found! Try with parentPageTitle [%s] in space [%s]", 
                                                parentPageId, parentPageTitle, spaceKey));
                }
            } catch (Exception ex) {
                getLog().warn( format( "cannot get page with parentPageId [%s]! Try with parentPageTitle [%s] in space [%s]\n%s", 
                                                parentPageId, parentPageTitle, spaceKey, ExceptionUtils.getRootCauseMessage(ex)) );
                
            }
        }
        
        if( result == null  ) {
            if( spaceKey == null ) {
                throw new MojoExecutionException( "spaceKey is not set!");                
            }
            try {
                result = confluence.getPage(spaceKey, parentPageTitle);

                if( result==null ) {
                    throw new MojoExecutionException( format( "parentPageTitle [%s] not found in space [%s]!", 
                                                      parentPageTitle, spaceKey));
                }
            } catch (Exception ex) {
                throw new MojoExecutionException( format( "cannot get page with parentPageTitle [%s] in space [%s]!", 
                                                      parentPageTitle, spaceKey), ex);
            }
        }
        getProperties().put("parentPageTitle", result.getTitle());
        
        return result;

    }
    
    /**
     * Issue 39
     *
     * Load username password from settings if user has not set them in JVM properties
     *
     * @throws MojoExecutionException
     */
    protected void loadUserInfoFromSettings() throws MojoExecutionException {

        if ((getUsername() == null || getPassword() == null) && (mavenSettings != null)) {
            if (this.serverId == null)
                throw new MojoExecutionException("SettingKey must be set! (username and/or password are not provided)");

            Server server = this.mavenSettings.getServer(this.serverId);

            if (server == null)
                throw new MojoExecutionException(String.format("server with id [%s] not found in settings!", this.serverId));

            if (getUsername() == null && server.getUsername() != null) username = server.getUsername();

            if (getPassword() == null && server.getPassword() != null) {
                try {
                    //
                    // FIX to resolve
                    // org.sonatype.plexus.components.sec.dispatcher.SecDispatcherException:
                    // java.io.FileNotFoundException: ~/.settings-security.xml (No such file or directory)
                    //
                    if (securityDispatcher instanceof DefaultSecDispatcher) {


                        //System.setProperty( DefaultSecDispatcher.SYSTEM_PROPERTY_SEC_LOCATION, sb.toString() );

                        ((DefaultSecDispatcher) securityDispatcher).setConfigurationFile("~/.m2/settings-security.xml");
                    }

                    password = securityDispatcher.decrypt(server.getPassword());
                } catch (SecDispatcherException e) {
                    throw new MojoExecutionException(e.getMessage());
                }
            }
        }
    }


}
