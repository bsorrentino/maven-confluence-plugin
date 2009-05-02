package org.bsc.maven.reporting.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.reporting.AbstractMavenReportRenderer;
import org.bsc.maven.reporting.ReportingResolutionListener;
import org.codehaus.plexus.i18n.I18N;


/**
 * 
 * @author Sorrentino
 *
 */
public class DependenciesRenderer extends AbstractMavenReportRenderer {

    private final Locale locale;
    private final ReportingResolutionListener listener;
    private final MavenProject project;
    private final MavenProjectBuilder mavenProjectBuilder;
	private final ArtifactRepository localRepository;
	private final ArtifactFactory factory;
    private final I18N i18n;
    private final Log log;

    /**
     * 
     * @param sink
     * @param project
     * @param mavenProjectBuilder
     * @param locale
     * @param listener
     */
    public DependenciesRenderer(	Sink sink, 
    								MavenProject project, 
    								MavenProjectBuilder mavenProjectBuilder,
    								ArtifactRepository localRepository,
    								ArtifactFactory factory,
    								I18N i18n,
    								Locale locale, 
    								ReportingResolutionListener listener,
    								Log log )
    {
        super( sink );

        this.project = project;
        this.locale = locale;
        this.listener = listener;
        this.mavenProjectBuilder = mavenProjectBuilder;
        this.localRepository = localRepository;
        this.i18n 	= i18n;
        this.factory = factory;
        this.log = log;
    }

    public String getTitle()
    {
        return getReportString( "report.dependencies.title" );
    }

    public void renderBody()
    {
        // Dependencies report
    	
        List<?> dependencies = listener.getRootNode().getChildren();

        if ( dependencies.isEmpty() )
        {
            startSection( getTitle() );

            // TODO: should the report just be excluded?
            paragraph( getReportString( "report.dependencies.nolist" ) );

            endSection();

            return;
        }

        startSection( getTitle() );
        
        String groupId = getReportString( "report.dependencies.column.groupId" );
        String artifactId = getReportString( "report.dependencies.column.artifactId" );
        String version = getReportString( "report.dependencies.column.version" );
        String classifier = getReportString( "report.dependencies.column.classifier" );
        String type = getReportString( "report.dependencies.column.type" );
        String optional = getReportString( "report.dependencies.column.optional" );
        String[] tableHeader = new String[]{groupId, artifactId, version, classifier, type, optional};

        // collect dependencies by scope
        Map dependenciesByScope = getDependenciesByScope( dependencies );

        renderDependenciesForScope( Artifact.SCOPE_COMPILE,
                                    (List) dependenciesByScope.get( Artifact.SCOPE_COMPILE ), tableHeader );
        renderDependenciesForScope( Artifact.SCOPE_RUNTIME,
                                    (List) dependenciesByScope.get( Artifact.SCOPE_RUNTIME ), tableHeader );
        renderDependenciesForScope( Artifact.SCOPE_TEST, (List) dependenciesByScope.get( Artifact.SCOPE_TEST ),
                                    tableHeader );
        renderDependenciesForScope( Artifact.SCOPE_PROVIDED,
                                    (List) dependenciesByScope.get( Artifact.SCOPE_PROVIDED ), tableHeader );
        renderDependenciesForScope( Artifact.SCOPE_SYSTEM, (List) dependenciesByScope.get( Artifact.SCOPE_SYSTEM ),
                                    tableHeader );

        endSection();

        // Transitive dependencies
        List artifacts = new ArrayList( listener.getArtifacts() );
        artifacts.removeAll( dependencies );

        startSection( getReportString( "report.transitivedependencies.title" ) );

        if ( artifacts.isEmpty() )
        {
            paragraph( getReportString( "report.transitivedependencies.nolist" ) );
        }
        else
        {
            paragraph( getReportString( "report.transitivedependencies.intro" ) );

            dependenciesByScope = getDependenciesByScope( artifacts );

            renderDependenciesForScope( Artifact.SCOPE_COMPILE,
                                        (List) dependenciesByScope.get( Artifact.SCOPE_COMPILE ), tableHeader );
            renderDependenciesForScope( Artifact.SCOPE_RUNTIME,
                                        (List) dependenciesByScope.get( Artifact.SCOPE_RUNTIME ), tableHeader );
            renderDependenciesForScope( Artifact.SCOPE_TEST, (List) dependenciesByScope.get( Artifact.SCOPE_TEST ),
                                        tableHeader );
            renderDependenciesForScope( Artifact.SCOPE_PROVIDED,
                                        (List) dependenciesByScope.get( Artifact.SCOPE_PROVIDED ), tableHeader );
            renderDependenciesForScope( Artifact.SCOPE_SYSTEM,
                                        (List) dependenciesByScope.get( Artifact.SCOPE_SYSTEM ), tableHeader );
        }

        endSection();

        //for Dependencies Graph
        startSection( getReportString( "report.dependencies.graph.title" ) );

        //for Dependencies Graph Tree
        startSection( getReportString( "report.dependencies.graph.tree.title" ) );

        sink.lineBreak();

        sink.paragraph();
        //sink.list();
        printDependencyListing( listener.getRootNode(), false );
        //sink.list_();
        sink.paragraph_();
        endSection();

        //for Artifact Descriptions / URLs
        startSection( getReportString( "report.dependencies.graph.tables.title" ) );
        printDescriptionsAndURLs( listener.getRootNode() );
        endSection();

        endSection();
    }

    private Map getDependenciesByScope( List dependencies )
    {
        Map dependenciesByScope = new HashMap();
        for ( Iterator i = dependencies.iterator(); i.hasNext(); )
        {
            ReportingResolutionListener.Node node = (ReportingResolutionListener.Node) i.next();
            Artifact artifact = node.getArtifact();

            List multiValue = (List) dependenciesByScope.get( artifact.getScope() );
            if ( multiValue == null )
            {
                multiValue = new ArrayList();
            }
            multiValue.add( artifact );
            dependenciesByScope.put( artifact.getScope(), multiValue );
        }
        return dependenciesByScope;
    }

    private void renderDependenciesForScope( String scope, List artifacts, String[] tableHeader )
    {
        if ( artifacts != null )
        {
            // can't use straight artifact comparison because we want optional last
            Collections.sort( artifacts, getArtifactComparator() );

            startSection( scope );

            paragraph( getReportString( "report.dependencies.intro." + scope ) );
            startTable();
            tableHeader( tableHeader );

            for ( Iterator iterator = artifacts.iterator(); iterator.hasNext(); )
            {
                Artifact artifact = (Artifact) iterator.next();
                tableRow( getArtifactRow( artifact ) );
            }
            endTable();

            endSection();
        }
    }

    private Comparator getArtifactComparator()
    {
        return new Comparator()
        {
            public int compare( Object o1, Object o2 )
            {
                Artifact a1 = (Artifact) o1;
                Artifact a2 = (Artifact) o2;

                // put optional last
                if ( a1.isOptional() && !a2.isOptional() )
                {
                    return +1;
                }
                else if ( !a1.isOptional() && a2.isOptional() )
                {
                    return -1;
                }
                else
                {
                    return a1.compareTo( a2 );
                }
            }
        };
    }

    private String[] getArtifactRow( Artifact artifact )
    {
        return new String[]{artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(),
            artifact.getClassifier(), artifact.getType(), artifact.isOptional() ? "(optional)" : " "};
    }

    private void printDependencyListing( ReportingResolutionListener.Node node, boolean printRoot )
    {
        Artifact artifact = node.getArtifact();
        String id = artifact.getDependencyConflictId();

        sink.listItem();
        sink.paragraph();

        if( printRoot ) {
	        sink.link( "#" + id );
	        sink.text( id );
	        sink.link_();
        }

        if ( !node.getChildren().isEmpty() )
        {
            sink.list();
            for ( Iterator deps = node.getChildren().iterator(); deps.hasNext(); )
            {
                ReportingResolutionListener.Node dep = (ReportingResolutionListener.Node) deps.next();
                printDependencyListing( dep, true );
            }
            sink.list_();
        }

        sink.paragraph_();
        sink.listItem_();
    }

    private void printDescriptionsAndURLs( ReportingResolutionListener.Node node )
    {
        Artifact artifact = node.getArtifact();
        String id = artifact.getDependencyConflictId();

        if ( !Artifact.SCOPE_SYSTEM.equals( artifact.getScope() ) )
        {
            try
            {
                MavenProject artifactProject = getMavenProjectFromRepository( artifact, localRepository );
                String artifactDescription = artifactProject.getDescription();
                String artifactUrl = artifactProject.getUrl();
                String artifactName = artifactProject.getName();

                sink.paragraph();
                sink.anchor( id );
                sink.bold();
                sink.text( artifactName );
                sink.bold_();
                sink.anchor_();
                sink.paragraph_();

                if ( artifactDescription != null )
                {
                    sink.paragraph();
                    sink.text( artifactDescription );
                    sink.paragraph_();
                }

                if ( artifactUrl != null )
                {
                    sink.paragraph();
                    sink.link( artifactUrl );
                    sink.text( artifactUrl );
                    sink.link_();
                    sink.paragraph_();
                }
            }
            catch ( ProjectBuildingException e )
            {
                log.debug( e );
            }

            for ( Iterator deps = node.getChildren().iterator(); deps.hasNext(); )
            {
                ReportingResolutionListener.Node dep = (ReportingResolutionListener.Node) deps.next();
                printDescriptionsAndURLs( dep );
            }
        }
        else
        {
            sink.paragraph();
            sink.anchor( id );
            sink.bold();
            sink.text( id );
            sink.bold_();
            sink.anchor_();
            sink.paragraph_();

            sink.paragraph();
            sink.text( artifact.getFile().toString() );
            sink.paragraph_();
        }
    }

    /**
     * Get the <code>Maven project</code> from the repository depending
     * the <code>Artifact</code> given.
     *
     * @param artifact an artifact
     * @return the Maven project for the given artifact
     * @throws org.apache.maven.project.ProjectBuildingException
     *          if any
     */
    private MavenProject getMavenProjectFromRepository( Artifact artifact, ArtifactRepository localRepository )
        throws ProjectBuildingException
    {
        Artifact projectArtifact = artifact;

        boolean allowStubModel = false;
        if ( !"pom".equals( artifact.getType() ) )
        {
            projectArtifact = factory.createProjectArtifact( artifact.getGroupId(), artifact.getArtifactId(),
                                                             artifact.getVersion(), artifact.getScope() );
            allowStubModel = true;
        }

        // TODO: we should use the MavenMetadataSource instead
        return mavenProjectBuilder.buildFromRepository( projectArtifact, project.getRemoteArtifactRepositories(),
                                                        localRepository, allowStubModel );
    }

    private String getReportString( String key )
    {
        return i18n.getString( "project-info-report", locale, key );
    }

}
