/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.mojo;

import biz.source_code.miniTemplator.MiniTemplator;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.ConfluenceService.Storage;
import org.bsc.confluence.DeployStateManager;
import org.bsc.confluence.model.ProcessUriException;
import org.bsc.confluence.model.Site;
import org.bsc.confluence.model.SiteFactory;
import org.bsc.confluence.model.SiteProcessor;
import org.bsc.mojo.configuration.DeployStateInfo;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.bsc.confluence.model.SiteProcessor.processPageUri;
import static org.bsc.confluence.model.SiteProcessor.processUri;


/**
 *
 * @author bsorrentino
 */
public abstract class AbstractConfluenceDeployMojo extends AbstractBaseConfluenceSiteMojo implements SiteFactory.Folder {

    /**
     * Home page template source. Template name will be used also as template source
     * for children
     */
    @Parameter(defaultValue = "${basedir}/src/site/confluence/template.wiki")
    protected java.io.File templateWiki;

    /**
     * During publish of documentation related to a new release, if it's true, the
     * pages related to SNAPSHOT will be removed
     */
    @Parameter(property = "confluence.removeSnapshots", required = false, defaultValue = "false")
    protected boolean removeSnapshots = false;

    /**
     * prefix child page with the title of the parent
     *
     * @since 4.9
     */
    @Parameter(property = "confluence.childrenTitlesPrefixed", required = false, defaultValue = "true")
    protected boolean childrenTitlesPrefixed = true;

    /**
     * Labels to add
     */
    @Parameter()
    java.util.List<String> labels;
    /**
     * Children files extension
     * 
     * @since 3.2.1
     */
    @Parameter(property = "wikiFilesExt", required = false, defaultValue = ".wiki")
    private String wikiFilesExt;

    /**
     * The file encoding of the source files.
     *
     */
    @Parameter(property = "encoding", defaultValue = "${project.build.sourceEncoding}")
    private String encoding;

    /**
     * <b>Experimental feature</b> - Store the last deployed state<br>
     * If declared, a local file will be generated that keeps the last update date
     * of all documents involved in publication.<br>
     * If such file is present the plugin will check the last update date of each
     * document, skipping it, if no update is detected.<br>
     * Example:
     * <pre>
     *   &lt;deployState>
     *     &lt;active> true|false &lt;/active> &lt;!-- default: true -->
     *     &lt;outdir> target dir &lt;/outdir> &lt;!-- default: project.build.directory -->
     *   &lt;/deployState>
     * </pre>
     *
     * @since 6.0.0
     */
    @Parameter
    protected DeployStateInfo deployState = new DeployStateInfo( false ) ;

    /**
     * Use this property to disable processing of properties that are in the form of URI.
     * If true all properties in the form of URI will be resolved, downloaded and the result will be used instead
     *
     * @since 6.6
     */
    @Parameter(defaultValue = "true")
    private boolean processProperties = true;

    /**
     *
     */
    protected Optional<DeployStateManager> deployStateManager = empty();

    /**
     *
     * @return
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     *
     * @param encoding
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     *
     * @return
     */
    protected final Charset getCharset() {

        if (encoding == null) {
            getLog().debug("encoding is null! default charset will be used");
            return Charset.defaultCharset();
        }

        try {
            Charset result = Charset.forName(encoding);
            return result;

        } catch (UnsupportedCharsetException e) {
            getLog().warn(format("encoding [%s] is not valid! default charset will be used", encoding));
            return Charset.defaultCharset();

        }
    }

    /**
     *
     * @return
     */
    public String getFileExt() {
        return (wikiFilesExt.charAt(0) == '.') ? wikiFilesExt : ".".concat(wikiFilesExt);
    }

    public MavenProject getProject() {
        return project;
    }

    public boolean isRemoveSnapshots() {
        return removeSnapshots;
    }

    public boolean isSnapshot() {
        final String version = project.getVersion();

        return (version != null && version.endsWith("-SNAPSHOT"));

    }

    public boolean isChildrenTitlesPrefixed() {
        return childrenTitlesPrefixed;
    }

    public List<String> getLabels() {

        if (labels == null) {
            return Collections.emptyList();
        }
        return labels;
    }

    /**
     * initialize properties shared with template
     */
    protected void initTemplateProperties(Site site) {

        processProperties(site);

        getProperties().put("pageTitle", getPageTitle()); // DEPRECATED USE home.title
        getProperties().put("home.title", getPageTitle());
        getProperties().put("page.title", getPageTitle());

        getProperties().put("artifactId", project.getArtifactId());
        getProperties().put("version", project.getVersion());
        getProperties().put("groupId", project.getGroupId());
        getProperties().put("name", project.getName());
        getProperties().put("description", project.getDescription());

        final java.util.Properties projectProps = project.getProperties();

        if (projectProps != null) {

            for (Map.Entry<Object, Object> e : projectProps.entrySet()) {
                getProperties().put(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
            }
        }

    }

    protected String getPrintableStringForResource(java.net.URI uri) {

        try {
            Path p = Paths.get(uri);
            return getProject().getBasedir().toPath().relativize(p).toString();

        } catch (Exception e) {
            return uri.toString();
        }

    }

    /**
     *
     * @param <T>
     * @param pageToUpdate
     * @param site
     * @param source
     * @param child
     * @param pageTitleToApply
     * @return
     */
    private <T extends Site.Page> CompletableFuture<Storage> getPageContent(
            final Optional<Model.Page> pageToUpdate,
            final Site site,
            final java.net.URI source,
            final T child,
            final String pageTitleToApply)
    {
        final Optional<String> pagePrefixToApply = (isChildrenTitlesPrefixed()) ? ofNullable(this.getPageTitle()) : empty();

        return processPageUri(site, child, pageToUpdate, source, pagePrefixToApply)
                .thenCompose( content -> {

                    try {

                        final MiniTemplator t = new MiniTemplator.Builder()
                                .setSkipUndefinedVars(true)
                                .build(content.getInputStream(), getCharset());

                        if (!child.isIgnoreVariables()) {

                            addStdProperties(t);

                            t.setVariableOpt("childTitle", pageTitleToApply); // DEPRECATED USE page.title
                            t.setVariableOpt("page.title", pageTitleToApply);
                        }

                        return completedFuture(Storage.of(t.generateOutput(), content.getType()));

                    } catch (Exception ex) {
                        final CompletableFuture<Storage> result = new CompletableFuture<>();
                        result.completeExceptionally(ex);
                        return result;
                    }
                });

    }

    /**
     *
     * @param uri
     * @param defaultResult
     * @param performUpdate
     * @return
     */
    protected CompletableFuture<Model.Page> updatePageIfNeeded(java.net.URI uri,
                                                              Model.Page defaultResult,
                                                              Supplier<CompletableFuture<Model.Page>> performUpdate )
    {

        return canProceedToUpdateResource(uri,
                () -> performUpdate.get()
                        .thenApply( p -> {
                            getLog().info(format("defaultResult [%s] has been updated!",
                                    getPrintableStringForResource(uri)));
                            return p;
                        }),
                () -> completedFuture(defaultResult)
                        .thenApply( p -> {
                            getLog().info(format("defaultResult [%s] has not been updated! (deploy skipped)",
                                getPrintableStringForResource(uri)));
                            return p;
                        })
                );
    }

    /**
     *
     * @param site
     * @param confluence
     * @param child
     * @param parentPage
     * @return
     */
    protected <T extends Site.Page> Model.Page generateChild(final ConfluenceService confluence,
                                                             final Site site,
                                                             final T child,
                                                             final Model.Page parentPage)
    {

        final String homeTitle = site.getHome().getName();

        final java.net.URI source = child.getUri();

        getLog().debug(
                format("generateChild\n\tspacekey=[%s]\n\thome=[%s]\n\tparent=[%s]\n\tpage=[%s]\n\t%s",
                    parentPage.getSpace(),
                    homeTitle,
                    parentPage.getTitle(),
                    child.getName(),
                    getPrintableStringForResource(source)));

        final String pageTitleToApply = isChildrenTitlesPrefixed()
                ? format("%s - %s", homeTitle, child.getName())
                : child.getName();

        if (!isSnapshot() && isRemoveSnapshots()) {
            final String snapshot = pageTitleToApply.concat("-SNAPSHOT");

            confluence.removePage(parentPage, snapshot)
                    .thenAccept(deleted -> getLog().info(format("Page [%s] has been removed!", snapshot)))
                    .exceptionally(ex -> throwRTE("page [%s] not found!", snapshot, ex));
        }

        final Function<Model.Page, CompletableFuture<Model.Page>> updatePage = p ->
                updatePageIfNeeded(source,p,
                        () -> getPageContent( ofNullable(p), site, source, child, pageTitleToApply )
                                .thenCompose( storage -> confluence.storePage(p, storage)));

        final Supplier<CompletableFuture<Model.Page>> createPage = () ->
                    resetUpdateStatusForResource(source)
                        .thenCompose( reset -> getPageContent( Optional.empty(), site, source, child, pageTitleToApply ) )
                        .thenCompose( storage -> confluence.createPage(parentPage, pageTitleToApply, storage));

        final Model.Page result =
                confluence.getPage(parentPage.getSpace(), pageTitleToApply)
                .thenCompose(page ->
                        (page.isPresent())
                                ? updatePage.apply(page.get())
                                : createPage.get())
                .thenCompose( p -> confluence.addLabelsByName( p.getId(), child.getComputedLabels() ).thenApply( (v) -> p) )
                .join();

        child.setName(pageTitleToApply);

        return result;

    }

    /**
     * Issue 46
     *
     **/
    private void processProperties(Site site) {
        if (!processProperties) return;
        for (Map.Entry<String, String> e : this.getProperties().entrySet()) {

            try {

                String v = e.getValue();
                if (v == null) {
                    getLog().warn(format("property [%s] has null value!", e.getKey()));
                    continue;
                }
                final java.net.URI uri = new java.net.URI(v);

                if (uri.getScheme() == null) {
                    continue;
                }

                getProperties().put(e.getKey(), processUriContent(site, site.getHome(), uri, getCharset()));

            } catch (ProcessUriException ex) {
                getLog().warn(
                        format("error processing value of property [%s] - %s", e.getKey(), ex.getMessage()));
                if (ex.getCause() != null)
                    getLog().debug(ex.getCause());

            } catch (URISyntaxException ex) {

                // DO Nothing
                getLog().debug(format("property [%s] is not a valid uri", e.getKey()));
            }

        }
    }

    protected CompletableFuture<Boolean> resetUpdateStatusForResource(java.net.URI uri) {
        return completedFuture( deployStateManager.map( dsm -> dsm.resetState(uri) ).orElse(false) );
    }

    /**
     *
     * @param uri
     * @return
     */
    private <U> CompletableFuture<U> canProceedToUpdateResource(java.net.URI uri,
                                                                  Supplier<CompletableFuture<U>> yes,
                                                                  Supplier<CompletableFuture<U>> no )
    {
        return deployStateManager
                .map( dsm -> dsm.isUpdated(uri, yes, no) )
                .orElseGet( () -> yes.get());
    }

    /**
     *
     * @param site
     * @param child
     * @param uri
     * @param charset
     * @return
     * @throws ProcessUriException
     */
    private String processUriContent(Site site, Site.Page child, java.net.URI uri, final Charset charset) throws ProcessUriException {

        final Optional<String> pagePrefixToApply = (isChildrenTitlesPrefixed()) ? ofNullable(this.getPageTitle()) : empty();

        try {
            return SiteProcessor.processUriContent(site, child, uri, pagePrefixToApply, content -> content.getContent(charset) );
        } catch (Exception ex) {
            throw new ProcessUriException("error reading content!", ex);
        }
    }

    /**
     * 
     * @param page
     * @param source
     */
    private void setPageUriFormFile(Site.Page page, java.io.File source) {
        if (page == null) {
            throw new IllegalArgumentException("page is null!");
        }

        if (source != null && source.exists() && source.isFile() && source.canRead()) {
            page.setUri(source.toURI());
        } else {
            try {
                java.net.URL sourceUrl = getClass().getClassLoader().getResource("defaultTemplate.confluence");
                page.setUri(sourceUrl.toURI());
            } catch (URISyntaxException ex) {
                // TODO log
            }
        }

    }

    private DirectoryStream<Path> newDirectoryStream(Path attachmentPath, Site.Attachment attachment)
            throws IOException {

        if (StringUtils.isNotBlank(attachment.getName())) {
            return Files.newDirectoryStream(attachmentPath, attachment.getName());
        }

        final DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path entry) throws IOException {

                return !(Files.isDirectory(entry) || Files.isHidden(entry) || Files.isSymbolicLink(entry)
                        || (!Files.isReadable(entry)));
            }
        };
        return Files.newDirectoryStream(attachmentPath, filter);
    }

    /**
     * 
     * @param page
     * @param confluence
     * @param confluencePage
     */
    private void generateAttachments(final ConfluenceService confluence, Site site, Site.Page page,
            final Model.Page confluencePage) /* throws MavenReportException */ {

        getLog().debug(format("generateAttachments pageId [%s] title [%s]", confluencePage.getId(),
                confluencePage.getTitle()));

        for (final Site.Attachment attachment : page.getAttachments()) {

            final Path attachmentPath = Paths.get(attachment.getUri());

            try {

                if (!Files.isDirectory(attachmentPath)) {
                    val result = generateAttachment(confluence, site, confluencePage, attachment).get();

                    getLog().debug(format("generated attachment: %s", result.toString()));
                } else {
                    try (final DirectoryStream<Path> dirStream = newDirectoryStream(attachmentPath, attachment)) {

                        for (Path p : dirStream) {

                            final Site.Attachment fileAttachment = new Site.Attachment();

                            fileAttachment.setName(p.getFileName().toString());
                            fileAttachment.setUri(p.toUri());

                            fileAttachment.setComment(attachment.getComment());
                            fileAttachment.setVersion(attachment.getVersion());

                            if (StringUtils.isNotEmpty(attachment.getContentType())) {
                                fileAttachment.setContentType(attachment.getContentType());
                            }

                            val result = generateAttachment(confluence, site, confluencePage, fileAttachment).get();
                            getLog().debug(format("generated attachment: %s", result.toString()));

                        }

                    } catch (IOException ex) {
                        getLog().warn(format("error reading directory [%s]", attachmentPath), ex);
                    }

                }
            } catch (InterruptedException | ExecutionException ex) {
                getLog().warn(format("error generating remote attachment from [%s]", attachment.getName()), ex);

            }
        }
    }

    private CompletableFuture<Model.Attachment> updateAttachmentData(final ConfluenceService confluence,
            final Site site, final java.net.URI uri, final Model.Page confluencePage,
            final Model.Attachment attachment) {

        return processUri(uri, (err, is) -> {

            if (err.isPresent()) {
                CompletableFuture<Model.Attachment> result = new CompletableFuture<>();
                result.completeExceptionally(err.get());
                return result;
            }

            if (!is.isPresent()) {
                getLog().warn(format("getting problem to read local attacchment file at [%s]", uri));
                return completedFuture(attachment);
            }

            return confluence.addAttachment(confluencePage, attachment, is.get());

        });

    }

    /**
     *
     * @param uri
     * @param defaultResult
     * @param performUpdate
     * @return
     */
    protected CompletableFuture<Model.Attachment> updateAttachmentIfNeeded(
                                        java.net.URI uri,
                                        Model.Attachment defaultResult,
                                        Supplier<CompletableFuture<Model.Attachment>> performUpdate )
    {

        return canProceedToUpdateResource(uri,
                () -> performUpdate.get()
                        .thenApply( p -> {
                            getLog().info(format("defaultResult [%s] has been updated!",
                                    getPrintableStringForResource(uri)));
                            return p;
                        }),
                () -> completedFuture(defaultResult)
                        .thenApply( p -> {
                            getLog().info(format("defaultResult [%s] has not been updated! (deploy skipped)",
                                    getPrintableStringForResource(uri)));
                            return p;
                        })
        );
    }

    /**
     *
     * @param confluence
     * @param confluencePage
     * @param attachment
     */
    private CompletableFuture<Model.Attachment> generateAttachment(final ConfluenceService confluence,
                                                                   final Site site,
                                                                   final Model.Page confluencePage,
                                                                   final Site.Attachment attachment)
    {

        getLog().debug(format("generateAttachment\n\tpageId:[%s]\n\ttitle:[%s]\n\tfile:[%s]",
                confluencePage.getId(),
                confluencePage.getTitle(),
                getPrintableStringForResource(attachment.getUri())));

        final java.net.URI uri = attachment.getUri();

        final Model.Attachment defaultAttachment = confluence.createAttachment();
        defaultAttachment.setFileName(attachment.getName());
        defaultAttachment.setContentType(attachment.getContentType());
        defaultAttachment.setComment(attachment.getComment());

        return updateAttachmentIfNeeded(uri,defaultAttachment, () ->
            confluence.getAttachment(confluencePage.getId(), attachment.getName(), attachment.getVersion())
                    .exceptionally(e -> {
                        getLog().debug(format("Error getting attachment [%s] from confluence: [%s]", attachment.getName(),
                                e.getMessage()));
                        return empty();
                    })
                    .thenCompose(optAttachment ->
                        optAttachment.map( result -> {
                            result.setContentType(attachment.getContentType());
                            result.setComment(attachment.getComment());
                            return completedFuture(result);

                        }).orElseGet( () ->
                            resetUpdateStatusForResource(uri)
                                    .thenCompose(reset -> completedFuture(defaultAttachment))
                        )
                    )
                    .thenCompose(finalAttachment ->
                            updateAttachmentIfNeeded(uri, finalAttachment,
                                    () -> updateAttachmentData(confluence, site, uri, confluencePage, finalAttachment)))
        );

    }

    /**
     * 
     * @param confluence
     * @param parentPage
     * @param confluenceParentPage
     * @param confluenceParentPage
     */
    protected void generateChildren(final ConfluenceService confluence, final Site site, final Site.Page parentPage,
            final Model.Page confluenceParentPage, final Map<String, Model.Page> varsToParentPageMap) {

        getLog().debug(format("generateChildren # [%d]", parentPage.getChildren().size()));

        generateAttachments(confluence, site, parentPage, confluenceParentPage);

        for (Site.Page child : parentPage.getChildren()) {

            final Model.Page confluencePage = generateChild(confluence, site, child, confluenceParentPage);

            for (Site.Page.Generated generated : child.getGenerateds()) {
                varsToParentPageMap.put(generated.getRef(), confluencePage);
            }

            if (confluencePage != null) {

                generateChildren(confluence, site, child, confluencePage, varsToParentPageMap);
            }

        }

    }

    /**
     * 
     * @param folder
     * @param page
     * @return
     */
    protected boolean navigateAttachments(java.io.File folder, Site.Page page) /* throws MavenReportException */ {

        if (folder.exists() && folder.isDirectory()) {

            java.io.File[] files = folder.listFiles();

            if (files != null && files.length > 0) {

                for (java.io.File f : files) {

                    if (f.isDirectory() || f.isHidden()) {
                        continue;
                    }

                    Site.Attachment a = new Site.Attachment();

                    a.setName(f.getName());
                    a.setUri(f.toURI());

                    page.getAttachments().add(a);
                }
            }

            return true;
        }

        return false;
    }

    /**
     * 
     * @param level
     * @param folder
     * @param parentChild
     */
    protected void navigateChild(final int level, final java.io.File folder,
            final Site.Page parentChild) /* throws MavenReportException */ {

        if (folder.exists() && folder.isDirectory()) {

            folder.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {

                    if (file.isHidden() || file.getName().charAt(0) == '.') {
                        return false;
                    }

                    if (file.isDirectory()) {

                        if (navigateAttachments(file, parentChild)) {
                            return false;
                        }

                        Site.Page child = new Site.Page();

                        child.setName(file.getName());
                        setPageUriFormFile(child, new java.io.File(file, templateWiki.getName()));

                        parentChild.getChildren().add(child);

                        navigateChild(level + 1, file, child);

                        return true;
                    }

                    final String fileName = file.getName();

                    if (!file.isFile() || !file.canRead() || !fileName.endsWith(getFileExt())
                            || fileName.equals(templateWiki.getName())) {
                        return false;
                    }

                    Site.Page child = new Site.Page();
                    final int extensionLen = getFileExt().length();

                    child.setName(fileName.substring(0, fileName.length() - extensionLen));
                    setPageUriFormFile(child, file);

                    parentChild.getChildren().add(child);

                    return false;

                }
            });
        }

    }

    @Override
    public Site createSiteFromFolder() {

        final Site result = new Site();

        // result.setBasedir( project.getBasedir() );

        result.getLabels().addAll(getLabels());

        final Site.Home home = new Site.Home();

        home.setName(getPageTitle());

        setPageUriFormFile(home, templateWiki);

        result.setHome(home);

        navigateAttachments(getAttachmentFolder(), home);

        if (getChildrenFolder().exists() && getChildrenFolder().isDirectory()) {

            getChildrenFolder().listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {

                    if (file.isHidden() || file.getName().charAt(0) == '.')
                        return false;

                    if (file.isDirectory()) {

                        Site.Page parentChild = new Site.Page();

                        parentChild.setName(file.getName());
                        setPageUriFormFile(parentChild, new java.io.File(file, templateWiki.getName()));

                        result.getHome().getChildren().add(parentChild);

                        navigateChild(1, file, parentChild);

                        return false;
                    }

                    final String fileName = file.getName();

                    if (!file.isFile() || !file.canRead() || !fileName.endsWith(getFileExt())
                            || fileName.equals(templateWiki.getName())) {
                        return false;
                    }

                    Site.Page child = new Site.Page();

                    final int extensionLen = getFileExt().length();

                    child.setName(fileName.substring(0, fileName.length() - extensionLen));
                    setPageUriFormFile(child, file);

                    result.getHome().getChildren().add(child);

                    return false;

                }
            });
        }

        return result;
    }

}
