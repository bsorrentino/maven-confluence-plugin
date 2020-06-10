package org.bsc.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
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

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 *
 * @author bsorrentino
 */
public abstract class AbstractBaseConfluenceMojo extends AbstractMojo {
    /**
     * Skip plugin execution
     *
     * @since 5.1
     */
    @Parameter(defaultValue = "false")
    protected boolean skip = false;
    /**
     * additional properties pass to template processor
     * Properties in the form of URI will be loaded and loaded value will be used instead, see processProperties
     */
    @Parameter()
    private java.util.Map<String, String> properties;
    /**
     * Confluence api endpoint url.
     * <ul>
     *  <li>To enable <b>xmlrpc api procotol</b> endpoint must end with <b>/rpc/xmlrpc</b></li>
     *  <li>To enable <b>rest api protocol</b> endpoint must end with <b>/rest/api</b></li>
  	 * </ul>
  	 * Example:
     * <pre>  
  	 * &lt;endPoint>http://your-confluence-site/rest/api</endPoint>
  	 * </pre>
     * <br>
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
     * &lt;sslCertificate>
     *  &lt;ignore>true|false</ignore>  &lt;!-- default: false -->
     *  &lt;hostNameVerifierClass>FQN</hostNameVerifierClass>  &lt;!-- default: null -->
     *  &lt;trustManagerClass>FQN</trustManagerClass> &lt;!-- default: null -->
     * &lt;/sslCertificate>
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
     * ScrollVersions addon configuration. Below the template
     * 
     * <pre>
     * &lt;scrollVersions>
     *  &lt;version>version name</version>  &lt;!-- mandatory -->
     * &lt;/scrollVersions>
     * </pre>
     * 
     * @since 6.5-beta1
     */
    @Parameter( name = "scrollVersions")
    private ScrollVersionsConfiguration scrollVersions;
    /**
     * 
     * @return
     */
    public Optional<ScrollVersionsConfiguration> getScrollVersions() {
        return ofNullable(scrollVersions);
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
            properties = new java.util.HashMap<>(5);
        }
        return properties;
    }

    /**
     * 
     * @param confluence
     * @return
     * @throws MojoExecutionException 
     */
    protected CompletableFuture<Model.Page> loadParentPage(ConfluenceService confluence, Optional<Site> site ) {

        return supplyAsync( () -> {

            final String _spaceKey =  site.flatMap( s -> s.optSpaceKey() ).orElse(spaceKey);
            final String _parentPageId = site.flatMap( s -> s.getHome().optParentPageId()).orElse( parentPageId );
            final String _parentPageTitle = site.flatMap( s -> s.getHome().optParentPageTitle()).orElse(parentPageTitle);

            Optional<Model.Page> result = Optional.empty();

            if( _parentPageId != null ) {
                result = confluence.getPage(  Model.ID.of(_parentPageId) ).join();

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

            if( result.isPresent() ) {
                getProperties().put("parentPageTitle", result.get().getTitle());
            }
            else {
                throwRTE("cannot get page with parentPageTitle [%s] in space [%s]!",parentPageTitle, spaceKey);
                //sgetLog().warn( format( "cannot get page with parentPageTitle [%s] in space [%s]!",parentPageTitle, spaceKey));
            }

            return result.get();

        });

    }
    
    /**
     * Issue 39
     *
     * Load username password from settings if user has not set them in JVM properties
     *
     * @throws MojoExecutionException
     */
    private void loadUserInfoFromSettings() throws MojoExecutionException {

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

    /**
     * Perform whatever build-process behavior this <code>Mojo</code> implements.
     * <br/>
     * This is the main trigger for the <code>Mojo</code> inside the <code>Maven</code> system, and allows
     * the <code>Mojo</code> to communicate errors.
     *
     * @throws MojoExecutionException if an unexpected problem occurs.
     *                                Throwing this exception causes a "BUILD ERROR" message to be displayed.
     * @throws MojoFailureException   if an expected problem (such as a compilation failure) occurs.
     *                                Throwing this exception causes a "BUILD FAILURE" message to be displayed.
     */
    @Override
    public final void execute() throws MojoExecutionException, MojoFailureException {

        if( getLog().isDebugEnabled())
            System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "debug");

        if( skip ) {
            getLog().info("plugin execution skipped");
            return;
        }

        loadUserInfoFromSettings();

        final Optional<ConfluenceProxy> proxyInfo = ofNullable(mavenSettings.getActiveProxy()).map( activeProxy ->
                ConfluenceProxy.of(
                        activeProxy.getHost(),
                        activeProxy.getPort(),
                        activeProxy.getUsername(),
                        activeProxy.getPassword(),
                        activeProxy.getNonProxyHosts()
                ));

        final ConfluenceService.Credentials credentials =
                new ConfluenceService.Credentials(getUsername(), getPassword());

        try ( ConfluenceService confluence  =
                      ConfluenceServiceFactory.createInstance(
                              getEndPoint(),
                              credentials,
                              proxyInfo.orElse(null),
                              sslCertificate,
                              getScrollVersions()))
        {

            execute( confluence );

        }
        catch( Exception e ) {
            final String msg = "error generating report";
            final Throwable cause = e.getCause();
            if( isFailOnError() ) {
                throw new MojoExecutionException(msg, (cause!=null) ? cause : e);
            }
            else {
                getLog().error( msg, (cause!=null) ? cause : e);
            }

        }

    }

    public abstract void execute( ConfluenceService confluenceService ) throws Exception ;
}
