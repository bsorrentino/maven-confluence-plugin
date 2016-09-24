package org.codehaus.swizzle.confluence;

import java.awt.Label;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcClientException;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.URISyntaxException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.naming.directory.SearchResult;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.bsc.confluence.ConfluenceProxy;

/**
 * @version $Revision$ $Date$
 *
 *
 * extract from original version 1.2-SNAPSHOT on oct,15 2010 and patched to support attachment upload
 *
 * Issue: http://jira.codehaus.org/browse/SWIZZLE-57
 *
 */
class Confluence {
    protected static final String SERVICE_PREFIX_1 = "confluence1.";
    
    private final XmlRpcClient client;
    private String token;
    protected boolean sendRawData;
    
    private java.lang.ref.SoftReference<ServerInfo> serverInfoCache = null;
    
    private boolean isNullOrEmpty( String v ) {
        if( v == null ) return true;
        return ( v.trim().length() == 0 );        
    }
        
    private boolean isProxyEnabled( final ConfluenceProxy proxyInfo, final java.net.URI serviceURI ) {
        
        if( proxyInfo ==null || isNullOrEmpty(proxyInfo.host) ) return false;

        boolean result = true;
        
        if( !isNullOrEmpty(proxyInfo.nonProxyHosts)) {
            final String proxyHost           = System.setProperty("http.proxyHost", proxyInfo.host);
            final String proxyPort           = System.setProperty("http.proxyPort", String.valueOf(proxyInfo.port));
            final String nonProxyHosts  = System.setProperty("http.nonProxyHosts",proxyInfo.nonProxyHosts );
            
            final List<Proxy> proxyList = ProxySelector.getDefault().select(serviceURI);
            
            if( proxyList!=null && proxyList.size()==1 ) {
                
                final Proxy proxy = proxyList.get(0);
                
                result = (proxy.type()!=Type.DIRECT && proxy.address()!=null);

            }    
            
            if( proxyHost!=null ) System.setProperty("http.proxyHost", proxyHost);
            if( proxyPort!=null ) System.setProperty("http.proxyPort", proxyPort);
            if( nonProxyHosts!=null ) System.setProperty("http.nonProxyHosts",nonProxyHosts );
                 
        }
        
        return result;
    }
    
    protected Confluence(String endpoint, ConfluenceProxy proxyInfo ) throws URISyntaxException, MalformedURLException {
        this(new XmlRpcClient());
	if (endpoint.endsWith("/")) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }

        if (!endpoint.endsWith("/rpc/xmlrpc")) {
            endpoint += "/rpc/xmlrpc";
        }
    
        final java.net.URI serviceURI = new java.net.URI(endpoint);

        XmlRpcClientConfigImpl clientConfig = new XmlRpcClientConfigImpl();
        clientConfig.setServerURL(serviceURI.toURL() );

        clientConfig.setEnabledForExtensions(true); // add this to support attachment upload

        client.setConfig( clientConfig );

        if( isProxyEnabled(proxyInfo, serviceURI) ) {
            
            final XmlRpcCommonsTransportFactory transportFactory = new XmlRpcCommonsTransportFactory( client );

            final HttpClient httpClient = new HttpClient();
            final HostConfiguration hostConfiguration = httpClient.getHostConfiguration();
            hostConfiguration.setProxy( proxyInfo.host, proxyInfo.port );
            hostConfiguration.setHost(serviceURI.getHost(), serviceURI.getPort(), serviceURI.toURL().getProtocol());

            if( !isNullOrEmpty(proxyInfo.userName) && !isNullOrEmpty(proxyInfo.password) ) {
                Credentials cred = new UsernamePasswordCredentials(proxyInfo.userName,proxyInfo.password);
                httpClient.getState().setProxyCredentials(AuthScope.ANY, cred);
            }

            transportFactory.setHttpClient( httpClient );
            client.setTransportFactory( transportFactory );
        }
    }
    // Would have been nicer to have a constructor with clientConfig and optionally a transport
    // but there's a circular dependency between an XmlRpcClient and TransportFactory
    protected Confluence(XmlRpcClient client) {
        this.client = client;
        token = ""; // empty token allows anonymous access
    }

    protected Confluence(Confluence c) {
        this.client = c.client;
        token = c.token; // empty token allows anonymous access
    }


    protected String getServicePrefix() {
        return SERVICE_PREFIX_1;
    }
    
    public boolean willSendRawData() {
        return sendRawData;
    }

    public void sendRawData(boolean sendRawData) {
        this.sendRawData = sendRawData;
    }

    public void login(String username, String password) throws SwizzleException, ConfluenceException {
        token = (String) call("login", username, password);
    }

    /**
     * remove this token from the list of logged in tokens. Returns true if the user was logged out, false if they were not logged in in the first place (we don't really need this return, but void
     * seems to kill XML-RPC for me)
     */
    public boolean logout() throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("logout");
        return value.booleanValue();
    }

    /**
     * exports a Confluence instance and returns a String holding the URL for the download. The boolean argument indicates whether or not attachments ought to be included in the export.
     */
    public String exportSite(boolean exportAttachments) throws SwizzleException, ConfluenceException {
        return (String) call("exportSite", new Boolean(exportAttachments));
    }

    /**
     * retrieve some basic information about the server being connected to. Useful for clients that need to turn certain features on or off depending on the version of the server. (Since 1.0.3)
     */
    public ServerInfo getServerInfo() throws SwizzleException, ConfluenceException {
        
        
        if( serverInfoCache == null || serverInfoCache.get()==null ) {
            Map data = (Map) call("getServerInfo");
            serverInfoCache = new java.lang.ref.SoftReference<ServerInfo>( new ServerInfo(data) );
        }

        return serverInfoCache.get();
    }
    /**
     * returns all the {@link SpaceSummary} instances that the current user can see.
     */
    public List getSpaces() throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getSpaces");
        return toList(vector, SpaceSummary.class);
    }

    /**
     * returns a single Space.
     */
    public Space getSpace(String spaceKey) throws SwizzleException, ConfluenceException {
        Map data = (Map) call("getSpace", spaceKey);
        return new Space(data);
    }

    /**
     * exports a space and returns a String holding the URL for the download. The export type argument indicates whether or not to export in XML, PDF, or HTML format - use "TYPE_XML", "TYPE_PDF", or
     * "TYPE_HTML" respectively. Also, using "all" will select TYPE_XML.
     */
    public String exportSpace(String spaceKey, String exportType) throws SwizzleException, ConfluenceException {
        return (String) call("exportSpace", spaceKey, exportType);
    }

    /**
     * create a new space, passing in name, key and description.
     */
    public Space addSpace(Space space) throws SwizzleException, ConfluenceException {
        Map data = (Map) call("addSpace", space);
        return new Space(data);
    }

    /**
     * remove a space completely.
     */
    public Boolean removeSpace(String spaceKey) throws SwizzleException, ConfluenceException {
        return (Boolean) call("removeSpace", spaceKey);
    }

    /**
     * returns all the {@link PageSummary} instances in the space. Doesn't include pages which are in the Trash. Equivalent to calling {{Space.getCurrentPages()}}.
     */
    public List getPages(String spaceKey) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getPages", spaceKey);
        return toList(vector, PageSummary.class);
    }

    /**
     * returns a single Page
     */
    public Page getPage(PageSummary summary) throws SwizzleException, ConfluenceException {
        return getPage(summary.getId());
    }

    public Page getPage(String pageId) throws SwizzleException, ConfluenceException {
        Map data = (Map) call("getPage", pageId);
        return new Page(data);
    }

    /**
     * returns a single Page
     */
    public Page getPage(String spaceKey, String pageTitle) throws SwizzleException, ConfluenceException {
        Map data = (Map) call("getPage", spaceKey, pageTitle);
        return new Page(data);
    }

    /**
     * returns all the {@link PageHistorySummary} instances - useful for looking up the previous versions of a page, and who changed them.
     */
    public List getPageHistory(String pageId) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getPageHistory", pageId);
        return toList(vector, PageHistorySummary.class);
    }

    /**
     * returns all the {@link Attachment}s for this page (useful to point users to download them with the full file download URL returned).
     */
    public List getAttachments(String pageId) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getAttachments", pageId);
        return toList(vector, Attachment.class);
    }

    /**
     * returns all the ancestors (as {@link PageSummary} instances) of this page (parent, parent's parent etc).
     */
    public List getAncestors(String pageId) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getAncestors", pageId);
        return toList(vector, PageSummary.class);
    }

    /**
     * returns all the direct children (as {@link PageSummary} instances) of this page.
     */
    public List getChildren(String pageId) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getChildren", pageId);
        return toList(vector, PageSummary.class);
    }

    /**
     * returns all the descendents (as {@link PageSummary} instances) of this page (children, children's children etc).
     */
    public List getDescendents(String pageId) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getDescendents", pageId);
        return toList(vector, PageSummary.class);
    }

    /**
     * returns all the {@link Comment}s for this page.
     */
    public List getComments(String pageId) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getComments", pageId);
        return toList(vector, Comment.class);
    }

    /**
     * returns an individual comment.
     */
    public Comment getComment(String commentId) throws SwizzleException, ConfluenceException {
        Map data = (Map) call("getComment", commentId);
        return new Comment(data);
    }

    /**
     * adds a comment to the page.
     */
    public Comment addComment(Comment comment) throws SwizzleException, ConfluenceException {
        Map data = (Map) call("addComment", comment);
        return new Comment(data);
    }

    /**
     * removes a comment from the page.
     */
    public boolean removeComment(String commentId) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("removeComment", commentId);
        return value.booleanValue();
    }

    /**
     * add or update a page. For adding, the Page given as an argument should have space, title and content fields at a minimum. For updating, the Page given should have id, space, title, content and
     * version fields at a minimum. The parentId field is always optional. All other fields will be ignored.
     */
    public Page storePage(Page page) throws SwizzleException, ConfluenceException {
        Map data = (Map) call(SERVICE_PREFIX_1, "storePage", new Object[] { page });
        return new Page(data);
    }

    /**
     * returns the HTML rendered content for this page. If 'content' is provided, then that is rendered as if it were the body of the page (useful for a 'preview page' function). If it's not provided,
     * then the existing content of the page is used instead (ie useful for 'view page' function).
     */
    public String renderContent(String spaceKey, String pageId, String content) throws SwizzleException, ConfluenceException {
        return (String) call("renderContent", spaceKey, pageId, content);
    }

    public String renderContent(String spaceKey, String pageId) throws SwizzleException, ConfluenceException {
        return renderContent(spaceKey, pageId, "");
    }

    public String renderContent(PageSummary page) throws SwizzleException, ConfluenceException {
        return renderContent(page.getSpace(), page.getId());
    }

    /**
     * Like the above renderContent(), but you can supply an optional hash (map, dictionary, etc) containing additional instructions for the renderer. Currently, only one such parameter is supported:
     */
    public String renderContent(String spaceKey, String pageId, String content, Map parameters) throws SwizzleException, ConfluenceException {
        return (String) call("renderContent", spaceKey, pageId, content, parameters);
    }

    /**
     * remove a page
     */
    public void removePage(String pageId) throws SwizzleException, ConfluenceException {
        call("removePage", pageId);
    }

    /**
     * get information about an attachment.
     */
    public Attachment getAttachment(String pageId, String fileName, String versionNumber) throws SwizzleException, ConfluenceException {
        Map data = (Map) call("getAttachment", pageId, fileName, versionNumber);
        return new Attachment(data);
    }

    /**
     * get the contents of an attachment.
     */
    public byte[] getAttachmentData(String pageId, String fileName, String versionNumber) throws SwizzleException, ConfluenceException {
        return (byte[]) call("getAttachmentData", pageId, fileName, versionNumber);
    }

    /**
     * add a new attachment to a content entity object. *Note that this uses a lot of memory -- about 4 times the size of the attachment.*
     */
    public Attachment addAttachment(long contentId, Attachment attachment, byte[] attachmentData) throws SwizzleException, ConfluenceException {
        Map data = (Map) call("addAttachment", new Long(contentId), attachment, attachmentData);
        return new Attachment(data);
    }

    /**
     * remove an attachment from a content entity object.
     */
    public boolean removeAttachment(String contentId, String fileName) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("removeAttachment", contentId, fileName);
        return value.booleanValue();
    }

    /**
     * move an attachment to a different content entity object and/or give it a new name.
     */
    public boolean moveAttachment(String originalContentId, String originalName, String newContentEntityId, String newName) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("moveAttachment", originalContentId, originalName, newContentEntityId, newName);
        return value.booleanValue();
    }

    /**
     * returns all the {@link BlogEntrySummary} instances in the space.
     */
    public List getBlogEntries(String spaceKey) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getBlogEntries", spaceKey);
        return toList(vector, BlogEntrySummary.class);
    }

    /**
     * returns a single BlogEntry.
     */
    public BlogEntry getBlogEntry(String pageId) throws SwizzleException, ConfluenceException {
        Map data = (Map) call("getBlogEntry", pageId);
        return new BlogEntry(data);
    }

    /**
     * add or update a blog entry. For adding, the BlogEntry given as an argument should have space, title and content fields at a minimum. For updating, the BlogEntry given should have id, space,
     * title, content and version fields at a minimum. All other fields will be ignored.
     */
    public BlogEntry storeBlogEntry(BlogEntry entry) throws SwizzleException, ConfluenceException {
        Map data = (Map) call("storeBlogEntry", entry);
        return new BlogEntry(data);
    }

    /**
     * Retrieves a blog post in the Space with the given spaceKey, with the title 'postTitle' and posted on the day 'dayOfMonth'.
     */
    public BlogEntry getBlogEntryByDayAndTitle(String spaceKey, int dayOfMonth, String postTitle) throws SwizzleException, ConfluenceException {
        Map data = (Map) call("getBlogEntryByDayAndTitle", spaceKey, new Integer(dayOfMonth), postTitle);
        return new BlogEntry(data);
    }

    /**
     * return a list of {@link SearchResult}s which match a given search query (including pages and other content types). This is the same as a performing a parameterised search (see below) with an
     * empty parameter map.
     */
    public List search(String query, int maxResults) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("search", query, new Integer(maxResults));
        return toList(vector, SearchResult.class);
    }

    /**
     * Returns a list of {@link SearchResult}s like the previous search, but you can optionally limit your search by adding parameters to the parameter map. If you do not include a parameter, the
     * default is used instead.
     */
    public List search(String query, Map parameters, int maxResults) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("search", query, parameters, new Integer(maxResults));
        return toList(vector, SearchResult.class);
    }

    /**
     * Returns a List of {@link Permission}s representing the permissions the current user has for this space (a list of "view", "modify", "comment" and / or "admin").
     */
    public List getPermissions(String spaceKey) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getPermissions", spaceKey);
        return Arrays.asList(vector);
    }

    /**
     * Returns a List of {@link Permission}s representing the permissions the given user has for this space. (since 2.1.4)
     */
    public List getPermissionsForUser(String spaceKey, String userName) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getPermissionsForUser", spaceKey, userName);
        return toList(vector, Permission.class);
    }

    /**
     * Returns a List of {@link Permission}s representing the permissions set on the given page.
     */
    public List getPagePermissions(String pageId) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getPagePermissions", pageId);
        return toList(vector, Permission.class);
    }

    /**
     * returns List of the space level {@link Permission}s which may be granted. This is a list of possible permissions to use with {{addPermissionToSpace}}, below, not a list of current permissions
     * on a Space.
     */
    public List getSpaceLevelPermissions() throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getSpaceLevelPermissions");
        return toList(vector, Permission.class);
    }

    /**
     * Give the entity named {{remoteEntityName}} (either a group or a user) the permission {{permission}} on the space with the key {{spaceKey}}.
     */
    public boolean addPermissionToSpace(String permission, String remoteEntityName, String spaceKey) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("addPermissionToSpace", permission, remoteEntityName, spaceKey);
        return value.booleanValue();
    }

    /**
     * Give the entity named {{remoteEntityName}} (either a group or a user) the permissions {{permissions}} on the space with the key {{spaceKey}}.
     */
    public boolean addPermissionsToSpace(List permissions, String remoteEntityName, String spaceKey) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("addPermissionsToSpace", permissions.toArray(), remoteEntityName, spaceKey);
        return value.booleanValue();
    }

    /**
     * Remove the permission {{permission} from the entity named {{remoteEntityName}} (either a group or a user) on the space with the key {{spaceKey}}.
     */
    public boolean removePermissionFromSpace(String permission, String remoteEntityName, String spaceKey) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("removePermissionFromSpace", permission, remoteEntityName, spaceKey);
        return value.booleanValue();
    }

    /**
     * Give anonymous users the permission {{permission}} on the space with the key {{spaceKey}}. (since 2.0)
     */
    public boolean addAnonymousPermissionToSpace(String permission, String spaceKey) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("addAnonymousPermissionToSpace", permission, spaceKey);
        return value.booleanValue();
    }

    /**
     * Give anonymous users the permissions {{permissions}} on the space with the key {{spaceKey}}. (since 2.0)
     */
    public boolean addAnonymousPermissionsToSpace(List permissions, String spaceKey) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("addAnonymousPermissionsToSpace", permissions.toArray(), spaceKey);
        return value.booleanValue();
    }

    /**
     * Remove the permission {{permission} from anonymous users on the space with the key {{spaceKey}}. (since 2.0)
     */
    public boolean removeAnonymousPermissionFromSpace(String permission, String spaceKey) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("removeAnonymousPermissionFromSpace", permission, spaceKey);
        return value.booleanValue();
    }

    /**
     * Remove all the global and space level permissions for {{groupname}}.
     */
    public boolean removeAllPermissionsForGroup(String groupname) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("removeAllPermissionsForGroup", groupname);
        return value.booleanValue();
    }

    /**
     * get a single user
     */
    public User getUser(String username) throws SwizzleException, ConfluenceException {
        Map data = (Map) call("getUser", username);
        return new User(data);
    }

    /**
     * add a new user with the given password
     */
    public void addUser(User user, String password) throws SwizzleException, ConfluenceException {
        call("addUser", user, password);
    }

    /**
     * add a new group
     */
    public void addGroup(String group) throws SwizzleException, ConfluenceException {
        call("addGroup", group);
    }

    /**
     * get a user's current groups as a list of {@link String}s
     */
    public List getUserGroups(String username) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getUserGroups", username);
        return Arrays.asList(vector);
    }

    /**
     * add a user to a particular group
     */
    public void addUserToGroup(String username, String groupname) throws SwizzleException, ConfluenceException {
        call("addUserToGroup", username, groupname);
    }

    /**
     * remove a user from a group.
     */
    public boolean removeUserFromGroup(String username, String groupname) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("removeUserFromGroup", username, groupname);
        return value.booleanValue();
    }

    /**
     * delete a user.
     */
    public boolean removeUser(String username) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("removeUser", username);
        return value.booleanValue();
    }

    /**
     * remove a group. If {{defaultGroupName}} is specified, users belonging to {{groupname}} will be added to {{defaultGroupName}}.
     */
    public boolean removeGroup(String groupname, String defaultGroupName) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("removeGroup", groupname, defaultGroupName);
        return value.booleanValue();
    }

    /**
     * gets all groups as a list of {@link String}s
     */
    public List getGroups() throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getGroups");
        return Arrays.asList(vector);
    }

    /**
     * checks if a user exists
     */
    public boolean hasUser(String username) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("hasUser", username);
        return value.booleanValue();
    }

    /**
     * checks if a group exists
     */
    public boolean hasGroup(String groupname) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("hasGroup", groupname);
        return value.booleanValue();
    }

    /**
     * edits the details of a user
     */
    public boolean editUser(User remoteUser) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("editUser", remoteUser);
        return value.booleanValue();
    }

    /**
     * deactivates the specified user
     */
    public boolean deactivateUser(String username) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("deactivateUser", username);
        return value.booleanValue();
    }

    /**
     * reactivates the specified user
     */
    public boolean reactivateUser(String username) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("reactivateUser", username);
        return value.booleanValue();
    }

    /**
     * returns all registered users as Strings
     */
    public List getActiveUsers(boolean viewAll) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getActiveUsers", new Boolean(viewAll));
        return Arrays.asList(vector);
    }

    /**
     * updates user information
     */
    public boolean setUserInformation(UserInformation userInfo) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("setUserInformation", userInfo);
        return value.booleanValue();
    }

    /**
     * Retrieves user information
     */
    public UserInformation getUserInformation(String username) throws SwizzleException, ConfluenceException {
        Map data = (Map) call("getUserInformation", username);
        return new UserInformation(data);
    }

    /**
     * changes the current user's password
     */
    public boolean changeMyPassword(String oldPass, String newPass) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("changeMyPassword", oldPass, newPass);
        return value.booleanValue();
    }

    /**
     * changes the specified user's password
     */
    public boolean changeUserPassword(String username, String newPass) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("changeUserPassword", username, newPass);
        return value.booleanValue();
    }

    /**
     * Returns all {@link Label}s for the given ContentEntityObject ID
     */
    public List getLabelsById(long objectId) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getLabelsById", new Long(objectId));
        return toList(vector, Label.class);
    }

    /**
     * Returns the most popular {@link Label}s for the Confluence instance, with a specified maximum number.
     */
    public List getMostPopularLabels(int maxCount) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getMostPopularLabels", new Integer(maxCount));
        return toList(vector, Label.class);
    }

    /**
     * Returns the most popular {@link Label}s for the given {{spaceKey}}, with a specified maximum number of results.
     */
    public List getMostPopularLabelsInSpace(String spaceKey, int maxCount) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getMostPopularLabelsInSpace", spaceKey, new Integer(maxCount));
        return toList(vector, Label.class);
    }

    /**
     * Returns the recently used {@link Label}s for the Confluence instance, with a specified maximum number of results.
     */
    public List getRecentlyUsedLabels(int maxResults) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getRecentlyUsedLabels", new Integer(maxResults));
        return toList(vector, Label.class);
    }

    /**
     * Returns the recently used {@link Label}s for the given {{spaceKey}}, with a specified maximum number of results.
     */
    public List getRecentlyUsedLabelsInSpace(String spaceKey, int maxResults) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getRecentlyUsedLabelsInSpace", spaceKey, new Integer(maxResults));
        return toList(vector, Label.class);
    }

    /**
     * Returns an array of {@link Space}s that have been labelled with {{labelName}}.
     */
    public List getSpacesWithLabel(String labelName) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getSpacesWithLabel", labelName);
        return toList(vector, Space.class);
    }

    /**
     * Returns the {@link Label}s related to the given label name, with a specified maximum number of results.
     */
    public List getRelatedLabels(String labelName, int maxResults) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getRelatedLabels", labelName, new Integer(maxResults));
        return toList(vector, Label.class);
    }

    /**
     * Returns the {@link Label}s related to the given label name for the given {{spaceKey}}, with a specified maximum number of results.
     */
    public List getRelatedLabelsInSpace(String labelName, String spaceKey, int maxResults) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getRelatedLabelsInSpace", labelName, spaceKey, new Integer(maxResults));
        return toList(vector, Label.class);
    }

    /**
     * Retrieves the {@link Label}s matching the given {{labelName}}, {{namespace}}, {{spaceKey}} or {{owner}}.
     */
    public List getLabelsByDetail(String labelName, String namespace, String spaceKey, String owner) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getLabelsByDetail", labelName, namespace, spaceKey, owner);
        return toList(vector, Label.class);
    }

    /**
     * Returns the content for a given label ID
     */
    public List getLabelContentById(long labelId) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getLabelContentById", new Long(labelId));
        return Arrays.asList(vector);
    }

    /**
     * Returns the content for a given label name.
     */
    public List getLabelContentByName(String labelName) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getLabelContentByName", labelName);
        return Arrays.asList(vector);
    }

    /**
     * Returns the content for a given Label object.
     */
    public List getLabelContentByObject(Label labelObject) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getLabelContentByObject", labelObject);
        return toList(vector, Label.class);
    }

    /**
     * Returns all Spaces that have content labelled with {{labelName}}.
     */
    public List getSpacesContainingContentWithLabel(String labelName) throws SwizzleException, ConfluenceException {
        Object[] vector = (Object[]) call("getSpacesContainingContentWithLabel", labelName);
        return toList(vector, Space.class);
    }

    /**
     * Adds a label to the object with the given ContentEntityObject ID.
     */
    public boolean addLabelByName(String labelName, long objectId) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("addLabelByName", labelName, new Long(objectId));
        return value.booleanValue();
    }

    /**
     * Adds a label with the given ID to the object with the given ContentEntityObject ID.
     */
    public boolean addLabelById(long labelId, long objectId) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("addLabelById", new Long(labelId), new Long(objectId));
        return value.booleanValue();
    }

    /**
     * Adds the given label object to the object with the given ContentEntityObject ID.
     */
    public boolean addLabelByObject(Label labelObject, long objectId) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("addLabelByObject", labelObject, new Long(objectId));
        return value.booleanValue();
    }

    /**
     * Adds a label to the object with the given ContentEntityObject ID.
     */
    public boolean addLabelByNameToSpace(String labelName, String spaceKey) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("addLabelByNameToSpace", labelName, spaceKey);
        return value.booleanValue();
    }

    /**
     * Removes the given label from the object with the given ContentEntityObject ID.
     */
    public boolean removeLabelByName(String labelName, long objectId) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("removeLabelByName", labelName, new Long(objectId));
        return value.booleanValue();
    }

    /**
     * Removes the label with the given ID from the object with the given ContentEntityObject ID.
     */
    public boolean removeLabelById(long labelId, long objectId) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("removeLabelById", new Long(labelId), new Long(objectId));
        return value.booleanValue();
    }

    /**
     * Removes the given label object from the object with the given ContentEntityObject ID.
     */
    public boolean removeLabelByObject(Label labelObject, long objectId) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("removeLabelByObject", labelObject, new Long(objectId));
        return value.booleanValue();
    }

    /**
     * Removes the given label from the given {{spaceKey}}.
     */
    public boolean removeLabelByNameFromSpace(String labelName, String spaceKey) throws SwizzleException, ConfluenceException {
        Boolean value = (Boolean) call("removeLabelByNameFromSpace", labelName, spaceKey);
        return value.booleanValue();
    }

    private List toList(Object[] vector, Class type) throws SwizzleException {
        try {
            List list = new ArrayList(vector.length);

            Constructor constructor = type.getConstructor(new Class[] { Map.class });
            for (int i = 0; i < vector.length; i++) {
                Map data = (Map) vector[i];
                Object object = constructor.newInstance(new Object[] { data });
                list.add(object);
            }

            return list;

        } catch (Exception e) {
            throw new SwizzleException(e);
        }
    }

    private Object call(String command) throws SwizzleException, ConfluenceException {
        Object[] args = {};
        return call(command, args);
    }

    private Object call(String command, Object arg1) throws SwizzleException, ConfluenceException {
        Object[] args = { arg1 };
        return call(command, args);
    }

    private Object call(String command, Object arg1, Object arg2) throws SwizzleException, ConfluenceException {
        Object[] args = { arg1, arg2 };
        return call(command, args);
    }

    private Object call(String command, Object arg1, Object arg2, Object arg3) throws SwizzleException, ConfluenceException {
        Object[] args = { arg1, arg2, arg3 };
        return call(command, args);
    }

    private Object call(String command, Object arg1, Object arg2, Object arg3, Object arg4) throws SwizzleException, ConfluenceException {
        Object[] args = { arg1, arg2, arg3, arg4 };
        return call(command, args);
    }

    private Object call(String command, Object[] args) throws SwizzleException {
        return call( getServicePrefix(), command, args );
    }
    
    /**
     * Force use of service prefix
     * Need for fix issue 29
     * 
     * @param servicePrefix
     * @param command
     * @param args
     * @return
     * @throws SwizzleException 
     */
    private Object call(String servicePrefix , String command, Object[] args) throws SwizzleException {
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof MapObject) {
                MapObject map = (MapObject) arg;
                if (sendRawData) {
                    throw new UnsupportedOperationException("send raw data is unsupported!");
                } else {
                    args[i] = map.toMap();
                }
            }
        }
        Object[] vector;
        if (!command.equals("login")) {
            vector = new Object[args.length + 1];
            vector[0] = token;
            System.arraycopy(args, 0, vector, 1, args.length);
        } else {
            vector = args;
        }
        try {
            return client.execute(servicePrefix + command, vector);
        } catch (XmlRpcClientException e) {
            throw new SwizzleException(e.getMessage(), e.linkedException);
        } catch (XmlRpcException e) {
            throw new ConfluenceException(e.getMessage(), e.linkedException);
        }
    }
}