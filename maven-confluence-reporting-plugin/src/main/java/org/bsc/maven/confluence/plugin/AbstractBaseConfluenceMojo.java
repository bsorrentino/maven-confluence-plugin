package org.bsc.maven.confluence.plugin;

import static java.lang.String.format;

import java.util.Optional;
import java.util.function.Consumer;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Server;
import org.bsc.confluence.ConfluenceProxy;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.ConfluenceServiceFactory;
import org.bsc.confluence.model.Site;
import org.bsc.confluence.rest.scrollversions.ScrollVersionsConfiguration;
import org.bsc.ssl.SSLCertificateInfo;
import org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcherException;


/**
 *
 * @author bsorrentino
 */
public abstract class AbstractBaseConfluenceMojo extends AbstractMojo {
    /**
     * additional properties pass to template processor
     */
    @Parameter()
    private java.util.Map<String, String> properties;    
    /**
     * Confluence api endpoint url
     * 
     * <ul>
     *  <li>To enable <b>xmlrpc api procotol</b> endpoint must end with <b>/rpc/xmlrpc</b></li>
     *  <li>To enable <b>rest api protocol</b> endpoint must end with <b>/rest/api</b></li>
  	 * </ul>
  	 * 
  	 * Example:
     * <pre>  
  	 * < endPoint>http://your_confluence-site/rest/api</endPoint>
  	 * </pre>
  	 *  
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
     * ScrollVersions addon configuration object
     */
    @Parameter( name = "scrollVersions")
    private ScrollVersionsConfiguration scrollVersions;
    
    /**
     * 
     * @return
     */
    public Optional<ScrollVersionsConfiguration> getScrollVersions() {
        return Optional.ofNullable(scrollVersions);
    }

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
    protected <T extends Consumer<ConfluenceService>> void confluenceExecute(T task)  {

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

        try ( ConfluenceService confluence  = 
                ConfluenceServiceFactory.createInstance(
                        getEndPoint(), 
                        credentials, 
                        proxyInfo, 
                        sslCertificate,
                        getScrollVersions())) 
        {

                    task.accept(confluence);
        
        } 
        catch( Throwable re ) {     
            throw new RuntimeException(re);      
        }
               
    }

    /**
     * 
     * @param confluence
     * @return
     * @throws MojoExecutionException 
     */
    protected Model.Page loadParentPage( ConfluenceService confluence, Optional<Site> site ) {
        
        
        final String _spaceKey =  site.flatMap( s -> s.optSpaceKey() ).orElse(spaceKey);
        final String _parentPageId = site.flatMap( s -> s.getHome().optParentPageId()).orElse(parentPageId);
        final String _parentPageTitle = site.flatMap( s -> s.getHome().optParentPageTitle()).orElse(parentPageTitle);
        
        Optional<Model.Page> result = Optional.empty();
        
        if( _parentPageId != null ) {
            
            result = confluence.getPage( _parentPageId ).join();
            
            if( !result.isPresent() ) {
                getLog().warn( format( "parentPageId [%s] not found! Try with parentPageTitle [%s] in space [%s]", 
                                            _parentPageId, _parentPageTitle, spaceKey));
            }
        }
        
        if( !result.isPresent()  ) {
            if( _spaceKey == null ) {
                throw new IllegalStateException( "spaceKey is not set!");                
            }
            result = confluence.getPage(_spaceKey, _parentPageTitle).join();
            
        }
        
        if( !result.isPresent() ) {
            throwRTE("cannot get page with parentPageTitle [%s] in space [%s]!",parentPageTitle, spaceKey);                
        }
        getProperties().put("parentPageTitle", result.get().getTitle());
        
        return result.get();

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
                throw new MojoExecutionException("'serverId' must be set! (username and/or password are not provided)");

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

    protected RuntimeException RTE( String message, Object...args  ) {
        Object[] arguments = (Object[])args;
        
        final String m = String.format( message, arguments);
        
        if( arguments.length > 0 && arguments[arguments.length -1] instanceof Throwable) {
           return new RuntimeException(m, (Throwable)arguments[arguments.length -1]);
        }
        return new RuntimeException(m) ;
    }
    
    protected <T> T throwRTE( String message, Object...args   ) {
        throw RTE(message, args);
    }
}
