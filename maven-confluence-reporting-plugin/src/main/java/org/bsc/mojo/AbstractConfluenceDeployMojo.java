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

import static java.lang.String.format;
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
    protected DeployStateManager.Parameters deployState;

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
    protected DeployStateManager deployStateManager;

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
     * @param confluence
     * @param pageToUpdate
     * @param site
     * @param source
     * @param child
     * @param pageTitleToApply
     * @return
     */
    private <T extends Site.Page> CompletableFuture<Model.Page> updatePageContent(
            final ConfluenceService confluence,
            final Model.Page pageToUpdate, 
            final Site site, 
            final java.net.URI source, 
            final T child,
            final String pageTitleToApply)
    {
        final Optional<String> pagePrefixToApply = (isChildrenTitlesPrefixed()) ? ofNullable(this.getPageTitle()) : Optional.empty();

        return processPageUri(site, child, pageToUpdate, source, pagePrefixToApply, (err, content) -> {

            final CompletableFuture<Model.Page> result = new CompletableFuture<>();

            if (err.isPresent()) {
                result.completeExceptionally(RTE("error processing uri [%s]", source, err.get()));
                return result;
            }

            if (!content.isPresent()) {
                result.completeExceptionally(RTE("error content not present processing uri [%s]", source));
                return result;
            }

            try {

                final MiniTemplator t = new MiniTemplator.Builder()
                            .setSkipUndefinedVars(true)
                            .build(content.get().getInputStream(), getCharset());

                if (!child.isIgnoreVariables()) {

                    addStdProperties(t);

                    t.setVariableOpt("childTitle", pageTitleToApply); // DEPRECATED USE page.title
                    t.setVariableOpt("page.title", pageTitleToApply);
                }

                return confluence.storePage(pageToUpdate, new Storage(t.generateOutput(), content.get().getType()));

            } catch (Exception ex) {
                result.completeExceptionally(RTE("error storing page [%s]", pageToUpdate.getTitle(), ex));
            }

            return result;
        });

    }

    /**
     *
     * @param site
     * @param confluence
     * @param child
     * @param parentPage
     * @return
     */
    protected <T extends Site.Page> Model.Page generateChild(final ConfluenceService confluence, final Site site,
            final T child, final Model.Page parentPage) {

        final String homeTitle = site.getHome().getName();

        final java.net.URI source = child.getUri(getFileExt());

        getLog().debug(format("generateChild\n\tspacekey=[%s]\n\thome=[%s]\n\tparent=[%s]\n\tpage=[%s]\n\t%s",
                parentPage.getSpace(), homeTitle, parentPage.getTitle(), child.getName(),
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

        final Model.Page result = confluence.getPage(parentPage.getSpace(), pageTitleToApply)
                .thenCompose(page ->
                        (page.isPresent())
                                ? completedFuture(page.get())
                                : resetUpdateStatusForResource(source)
                                    .thenCompose(reset -> confluence.createPage(parentPage, pageTitleToApply)) )

                .thenCompose(p -> canProceedToUpdateResource(source)
                                    .thenCompose(update -> {
                                        if (update)
                                            return updatePageContent(confluence, p, site, source, child, pageTitleToApply);
                                        else {
                                            getLog().info(format("page [%s] has not been updated (deploy skipped)",
                                                    getPrintableStringForResource(source)));
                                            return /* confluence.storePage(p) */ completedFuture(p);
                                        }}))
                .join();

        child.setName(pageTitleToApply);

        child.getComputedLabels().forEach( label -> {

            try {
                confluence.addLabelByName(label, Long.parseLong(result.getId()));
            } catch (Exception e) {
                getLog().warn( format( "error adding label [%s]", label), e );
            }

        });

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
                        format("error processing value of property [%s]\n%s", e.getKey(), ex.getMessage()));
                if (ex.getCause() != null)
                    getLog().debug(ex.getCause());

            } catch (URISyntaxException ex) {

                // DO Nothing
                getLog().debug(format("property [%s] is not a valid uri", e.getKey()));
            }

        }
    }

    protected CompletableFuture<Void> resetUpdateStatusForResource(java.net.URI uri) {
        if (uri != null && deployStateManager != null)
            deployStateManager.resetState(uri);
        return CompletableFuture.completedFuture(null);
    }

    /**
     *
     * @param uri
     * @return
     */
    protected CompletableFuture<Boolean> canProceedToUpdateResource(java.net.URI uri) {
        if (uri == null)
            return CompletableFuture.completedFuture(false);
        if (deployStateManager == null)
            return CompletableFuture.completedFuture(true);

        return CompletableFuture.completedFuture(deployStateManager.isUpdated(uri));
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

        final Optional<String> pagePrefixToApply = (isChildrenTitlesPrefixed()) ? ofNullable(this.getPageTitle()) : Optional.empty();

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
     * @param confluence
     * @param confluencePage
     * @param attachment
     */
    private CompletableFuture<Model.Attachment> generateAttachment(final ConfluenceService confluence, final Site site,
            final Model.Page confluencePage, final Site.Attachment attachment) {

        getLog().debug(format("generateAttachment\n\tpageId:[%s]\n\ttitle:[%s]\n\tfile:[%s]", confluencePage.getId(),
                confluencePage.getTitle(), getPrintableStringForResource(attachment.getUri())));

        final java.net.URI uri = attachment.getUri();

        return confluence.getAttachment(confluencePage.getId(), attachment.getName(), attachment.getVersion())
                .exceptionally(e -> {
                    getLog().debug(format("Error getting attachment [%s] from confluence: [%s]", attachment.getName(),
                            e.getMessage()));
                    return Optional.empty();
                }).thenCompose(att -> {

                    if (!att.isPresent()) {
                        getLog().debug(format("Creating new attachment for [%s]", attachment.getName()));
                        Model.Attachment result = confluence.createAttachment();
                        result.setFileName(attachment.getName());
                        result.setContentType(attachment.getContentType());
                        result.setComment(attachment.getComment());
                        return resetUpdateStatusForResource(uri).thenCompose(reset -> completedFuture(result));
                    }

                    Model.Attachment result = att.get();
                    result.setContentType(attachment.getContentType());
                    result.setComment(attachment.getComment());
                    return completedFuture(result);

                }).thenCompose(finalAttachment -> canProceedToUpdateResource(uri).thenCompose(updated -> {
                    if (updated) {
                        return updateAttachmentData(confluence, site, uri, confluencePage, finalAttachment);
                    } else {
                        getLog().info(format("attachment [%s] has not been updated (deploy skipped)",
                                getPrintableStringForResource(uri)));
                        return completedFuture(finalAttachment);
                    }
                }));

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
