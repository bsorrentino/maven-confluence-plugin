package org.bsc.confluence.rest.scrollversions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ExportFormat;
import org.bsc.confluence.rest.RESTConfluenceServiceImpl;
import org.bsc.ssl.SSLCertificateInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;

public class SimpleScrollVersionsConfluenceService implements ConfluenceService  {

    final RESTConfluenceServiceImpl delegate;
    final String version;

    public SimpleScrollVersionsConfluenceService( String confluenceUrl,
                                                String version,
                                                Credentials credentials,
                                                SSLCertificateInfo sslCertificateInfo)
    {
        if (version == null) throw new java.lang.IllegalArgumentException("scrollVersionName is null!");
        if (version.isEmpty()) throw new java.lang.IllegalArgumentException("scrollVersionName is empty!");

        this.version = version;
        this.delegate = new RESTConfluenceServiceImpl(confluenceUrl, credentials, sslCertificateInfo);

    }

    private Model.Page proxyPage( final Model.Page source ) {
        return (Model.Page)Proxy.newProxyInstance(Model.Page.class.getClassLoader(),
                new Class<?>[]{Model.Page.class},
                ( Object proxy, Method method, Object[] args ) -> {
                    final Object result =  method.invoke(source, args);
                    if( "getTitle".equals(method.getName() )) {
                        return mapTitle( String.valueOf(result) );
                    }
                    return result;
                });

    }

    public String mapTitle( String title ) {
        return ( title.startsWith(".") )
                ? title
                : format( ".%s v%s", title, version)
                ;
    }

    @Override
    public Credentials getCredentials() {
        return delegate.getCredentials();
    }

    @Override
    public Model.PageSummary findPageByTitle(String parentPageId, String title) throws Exception {
        return delegate.findPageByTitle(parentPageId, title );
    }

    @Override
    public CompletableFuture<Model.Page> createPage(Model.Page parentPage, String title) {
        return delegate.createPage(parentPage, mapTitle(title));
    }

    /**
     *
     * @param pageId
     * @return
     * @throws Exception
     */
    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(String pageId) {
        return delegate.getPage(pageId);
    }

    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(String spaceKey, String title) {
        return delegate.getPage(spaceKey, mapTitle(title));
    }

    @Override
    public List<Model.PageSummary> getDescendents(String pageId) throws Exception {
        return delegate.getDescendents(pageId);
    }

    @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page, Storage content) {
        return delegate.storePage(proxyPage(page), content);
    }

    @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page) {
        return delegate.storePage(proxyPage(page));
    }

    @Override
    public boolean addLabelByName(String label, long id) throws Exception {
        return delegate.addLabelByName(label, id);
    }

    @Override
    public void exportPage(String url, String spaceKey, String title, ExportFormat exfmt, File outputFile) throws Exception {
        delegate.exportPage(url, spaceKey, mapTitle(title), exfmt, outputFile);
    }

    @Override
    public Model.Attachment createAttachment() {
        return delegate.createAttachment();
    }

    @Override
    public CompletableFuture<Optional<Model.Attachment>> getAttachment(String pageId, String name, String version) {
        return delegate.getAttachment(pageId, name, version);
    }

    @Override
    public CompletableFuture<Model.Attachment> addAttachment(Model.Page page, Model.Attachment attachment, InputStream source) {
        return delegate.addAttachment(page, attachment, source);
    }

    @Override
    public CompletableFuture<Boolean> removePage(Model.Page parentPage, String title) {
        return delegate.removePage(proxyPage(parentPage), title);
    }

    @Override
    public CompletableFuture<Boolean> removePageAsync(String pageId) {
        return delegate.removePageAsync(pageId);
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }

}
