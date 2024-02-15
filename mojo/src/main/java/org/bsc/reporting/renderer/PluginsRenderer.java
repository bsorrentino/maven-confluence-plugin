/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bsc.reporting.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.report.projectinfo.ProjectInfoReportUtils;
import org.apache.maven.reporting.AbstractMavenReportRenderer;
import org.codehaus.plexus.i18n.I18N;
import org.codehaus.plexus.util.StringUtils;

/**
 *
 * @author bsorrentino
 */
public class PluginsRenderer extends AbstractMavenReportRenderer
    {
        private final Log log;

        private final List<Artifact> plugins;

        private final List<Artifact> reports;

        private final Locale locale;

        private final I18N i18n;

        private final MavenProject project;

        private final MavenProjectBuilder mavenProjectBuilder;

        private final ArtifactFactory artifactFactory;

        private final ArtifactRepository localRepository;

        /**
         * @param log
         * @param sink
         * @param locale
         * @param i18n
         * @param plugins
         * @param reports
         * @param project
         * @param mavenProjectBuilder
         * @param artifactFactory
         * @param localRepository
         */
        public PluginsRenderer( 
                Log log, 
                Sink sink, 
                Locale locale, 
                I18N i18n, 
                Set<Artifact> plugins, 
                Set<Artifact> reports,
                MavenProject project, 
                MavenProjectBuilder mavenProjectBuilder,
                ArtifactFactory artifactFactory, 
                ArtifactRepository localRepository )
        {
            super( sink );

            this.log = log;

            this.locale = locale;

            this.plugins = new ArrayList<>( plugins );

            this.reports = new ArrayList<>( reports );

            this.i18n = i18n;

            this.project = project;

            this.mavenProjectBuilder = mavenProjectBuilder;

            this.artifactFactory = artifactFactory;

            this.localRepository = localRepository;
        }

        /** {@inheritDoc} */
        public String getTitle()
        {
            return getReportString( "report.plugins.title" );
        }

        /** {@inheritDoc} */
        public void renderBody()
        {
            // === Section: Project Plugins.
            renderSectionPlugins( true );

            // === Section: Project Reports.
            renderSectionPlugins( false );
        }

        /**
         * @param isPlugins <code>true</code> to use <code>plugins</code> variable, <code>false</code> to use
         * <code>reports</code> variable.
         */
        private void renderSectionPlugins( boolean isPlugins )
        {
            List<Artifact> list = ( isPlugins ? plugins : reports );
            String[] tableHeader = getPluginTableHeader();

            startSection( ( isPlugins ? getReportString( "report.plugins.title" )
                                     : getReportString( "report.plugins.report.title" ) ) );

            if ( list == null || list.isEmpty() )
            {

                paragraph(  ( isPlugins ? getReportString( "report.plugins.nolist" )
                                        : getReportString( "report.plugins.report.nolist" ) ) );

                endSection();

                return;
            }

            Collections.sort( list, getArtifactComparator() );

            startTable();
            tableHeader( tableHeader );

            for ( Iterator<Artifact> iterator = list.iterator(); iterator.hasNext(); )
            {
                Artifact artifact = (Artifact) iterator.next();

                VersionRange versionRange;
                if ( StringUtils.isEmpty( artifact.getVersion() ) )
                {
                    versionRange = VersionRange.createFromVersion( Artifact.RELEASE_VERSION );
                }
                else
                {
                    versionRange = VersionRange.createFromVersion( artifact.getVersion() );
                }

                Artifact pluginArtifact = artifactFactory.createParentArtifact( artifact.getGroupId(), artifact
                    .getArtifactId(), versionRange.toString() );
                List<?> artifactRepositories = project.getPluginArtifactRepositories();
                if ( artifactRepositories == null )
                {
                    artifactRepositories = new ArrayList<>();
                }
                try
                {
                    MavenProject pluginProject = mavenProjectBuilder.buildFromRepository( pluginArtifact,
                                                                                          artifactRepositories,
                                                                                          localRepository );
                    tableRow( getPluginRow( pluginProject.getGroupId(), pluginProject.getArtifactId(), pluginProject
                                            .getVersion(), pluginProject.getUrl() ) );
                }
                catch ( ProjectBuildingException e )
                {
                    log.info( "Could not build project for: " + artifact.getArtifactId() + ":" + e.getMessage(), e );
                    tableRow( getPluginRow( artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(),
                                            null ) );
                }

            }
            endTable();

            endSection();
        }

        // ----------------------------------------------------------------------
        // Private methods
        // ----------------------------------------------------------------------

        private String[] getPluginTableHeader()
        {
            // reused key...
            String groupId = getReportString( "report.dependencyManagement.column.groupId" );
            String artifactId = getReportString( "report.dependencyManagement.column.artifactId" );
            String version = getReportString( "report.dependencyManagement.column.version" );
            return new String[] { groupId, artifactId, version };
        }

        private String[] getPluginRow( String groupId, String artifactId, String version, String link )
        {
            artifactId = ProjectInfoReportUtils.getArtifactIdCell( artifactId, link );
            return new String[] { groupId, artifactId, version };
        }

        private Comparator<Artifact> getArtifactComparator()
        {
            return new Comparator<Artifact>()
            {
                /** {@inheritDoc} */
                public int compare( Artifact a1, Artifact a2 )
                {
                    int result = a1.getGroupId().compareTo( a2.getGroupId() );
                    if ( result == 0 )
                    {
                        result = a1.getArtifactId().compareTo( a2.getArtifactId() );
                    }
                    return result;
                }
            };
        }

        private String getReportString( String key )
        {
            return i18n.getString( "project-info-report", locale, key );
        }

}
