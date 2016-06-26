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


/**
 *
 * @author bsorrentino
 */
public abstract class AbstractBaseConfluenceMojo extends AbstractMojo {

    /**
     * Confluence end point url
     */
    @Parameter(property = "confluence.endPoint", defaultValue = "http://localhost:8080/rpc/xmlrpc")
    private String endPoint;
    /**
     * Confluence target confluence's spaceKey
     */
    @Parameter(property = "confluence.spaceKey", required = true)
    private String spaceKey;
    /**
     * Confluence target confluence's spaceKey
     */
    @Parameter(property = "confluence.parentPage", defaultValue = "Home")
    private String parentPageTitle;
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
     *  < ignore>true|false</ignore>  // default true
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
     */
    public AbstractBaseConfluenceMojo() {
    }

    public final String getEndPoint() {
        return endPoint;
    }

    public final String getSpaceKey() {
        return spaceKey;
    }

    public final String getParentPageTitle() {
        return parentPageTitle;
    }

    public final String getUsername() {
        return username;
    }

    public final String getPassword() {
        return password;
    }

    /**
     *
     * @param task
     * @throws MojoExecutionException
     */
    protected void confluenceExecute(P1<ConfluenceService> task) throws MojoExecutionException {

        if (sslCertificate != null) {
            getLog().debug(String.valueOf(sslCertificate));

            sslCertificate.setup(this.getEndPoint());
        }

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

            confluence = ConfluenceServiceFactory.createInstance(getEndPoint(), proxyInfo, getUsername(), getPassword());

            getLog().info(confluence.getVersion());

            confluence.call(task);
            
        } catch (Exception e) {

            getLog().error("has been imposssible connect to confluence due exception", e);

            throw new MojoExecutionException("has been imposssible connect to confluence due exception", e);
        }

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
